package ro.sebastianrapa.atmapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.sebastianrapa.atmapp.config.CardAuthenticationConfig;
import ro.sebastianrapa.atmapp.form.CardLoginForm;
import ro.sebastianrapa.atmapp.form.DepositForm;
import ro.sebastianrapa.atmapp.form.WithdrawForm;
import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.Log;
import ro.sebastianrapa.atmapp.model.exception.runtime.BankAccountNotFoundException;
import ro.sebastianrapa.atmapp.model.exception.runtime.CardNotFoundException;
import ro.sebastianrapa.atmapp.model.exception.runtime.NotSufficientFundsException;
import ro.sebastianrapa.atmapp.security.CardAuthentication;
import ro.sebastianrapa.atmapp.service.*;

@Controller
@RequestMapping("customer/atm")
public class ATMController {

    private static final boolean REQUIRES_AUTH = true;
    private static final String NOT_AUTH_CARD_MESSAGE = "Card is not authenticated";

    private transient final ATMService atmService;
    private transient final BankAccountService bankAccountService;
    private transient final CardService cardService;
    private transient final CardAuthenticationConfig cardAuthConfig;
    private transient final ValidationService validationService;
    private transient final LogService logService;

    public ATMController(final ATMService atmService,
                         final BankAccountService bankAccountService,
                         final CardService cardService,
                         final CardAuthenticationConfig cardAuthConfig,
                         final ValidationService validationService,
                         final LogService logService) {
        this.atmService = atmService;
        this.bankAccountService = bankAccountService;
        this.cardService = cardService;
        this.cardAuthConfig = cardAuthConfig;
        this.validationService = validationService;
        this.logService = logService;
    }

    @GetMapping("/index")
    public ModelAndView index() {

        final ModelAndView modelAndView = new ModelAndView("customer/atm/index");
        modelAndView.addObject("cards", cardService.fetchAllCards());

        return modelAndView;
    }

    @GetMapping(value = "/introduce-card", params = "cardNumber")
    public ModelAndView introduceCard(@RequestParam final String cardNumber,
                                      final CardLoginForm form) {
        return getIntroducedCardPage(cardNumber, form);
    }

    @PostMapping("/authenticate-card")
    public ModelAndView modelAndView(@ModelAttribute("form") final CardLoginForm form,
                                     final BindingResult bindingResult) {

        final CardAuthentication cardAuth = cardAuthConfig.getCardAuthentication();

        final String introducedPin = form.getPinCode();
        final Card introducedCard = cardAuth.getIntroducedCard();
        final String cardNumber = introducedCard.getCardNumber();

        // Validate the pin code format
        validationService.validatePinCodeFormat(introducedPin, bindingResult);

        if (bindingResult.hasErrors()) {
            // If we have ping code format error return to the introduced card page
            return getIntroducedCardPage(cardNumber, form);
        }

        // Check if the pin code is correct
        if (!introducedCard.checkPinCode(introducedPin)) {
            // Count the wrong input
            cardAuth.wrongPinAttempt();
            // Add an error message
            bindingResult.rejectValue("pinCode", "wrong.pin.code.error", "Wrong ping code introduced. Attempts left: " + cardAuth.getAttemptsLeft());
            // Check if there were to many wrong attempts
            if (cardAuth.tooManyWrongAttempts()) {
                // Block Card
                cardService.blockCard(introducedCard);
                // Add Log to inform about blocking card
                logService.addNewLog(new Log("Blocked card for to many wrong attempts"));
                // Redirect to index page
                return redirectToIndexPage();
            }
            // Return to the introduced-card page
            return getIntroducedCardPage(cardNumber, form);
        }

        // Successfully authenticate this card
        cardAuth.authenticationRequired(!REQUIRES_AUTH);

        // Get the bank account associated with this card
        final BankAccount associatedAccount;
        try {
            associatedAccount = bankAccountService.getBankAccountByIban(introducedCard.getBankAccountIban());
        } catch (BankAccountNotFoundException e) {
            // Add log
            logService.addNewLog(new Log(e.getMessage()));

            return redirectToIndexPage();
        }
        // Load the authenticated HTML file
        final ModelAndView modelAndView = new ModelAndView("/customer/atm/authenticated");
        modelAndView.addObject("bankAccount", associatedAccount);

        return modelAndView;
    }

    @GetMapping("/deposit")
    public ModelAndView deposit(@ModelAttribute("form") final DepositForm form) {
        // Get deposit form
        return getDepositForm(form);
    }

    @PostMapping("/deposit")
    public ModelAndView depositConfirmed(@ModelAttribute("form") final DepositForm form,
                                         final BindingResult bindingResult) {
        final String depositAmount = form.getDepositAmount();

        // Validate deposit amount
        validationService.validateDepositAmount(depositAmount, bindingResult);

        if (bindingResult.hasErrors()) {
            return getDepositForm(form);
        }
        // Get the bank account iban of the authenticated card
        final String bankAccountIban = cardAuthConfig.getCardAuthentication()
                                               .getIntroducedCard()
                                               .getBankAccountIban();
        // Get the bank account associated with the card
        final BankAccount accountToDepositOn;
        try {
            accountToDepositOn = bankAccountService.getBankAccountByIban(bankAccountIban);
        } catch (BankAccountNotFoundException e) {
            // Add log
            logService.addNewLog(new Log(e.getMessage()));
            return redirectToIndexPage();
        }
        // Make the deposit
        atmService.deposit(accountToDepositOn, Integer.parseInt(depositAmount));

        // Log out the card
        cardAuthConfig.getCardAuthentication()
                      .authenticationRequired(REQUIRES_AUTH);

        return redirectToIndexPage();
    }
    
    @GetMapping("/withdraw")
    public ModelAndView withdraw(@ModelAttribute("form") final WithdrawForm form) {
        // Get withdraw form
        return getWithdrawForm(form);
    }

    @PostMapping("/withdraw")
    public ModelAndView withdrawConfirmed(@ModelAttribute("form") final WithdrawForm form,
                                         final BindingResult bindingResult) {
        final String withdrawAmount = form.getWithdrawAmount();

        // Validate withdraw amount
        validationService.validateWithdrawAmount(withdrawAmount, bindingResult);

        if (bindingResult.hasErrors()) {
            return getWithdrawForm(form);
        }
        // Get the bank account iban of the authenticated card
        final String bankAccountIban = cardAuthConfig.getCardAuthentication()
                                               .getIntroducedCard()
                                               .getBankAccountIban();
        // Get the bank account associated with the card
        final BankAccount accountToWithdrawFrom;
        try {
            accountToWithdrawFrom = bankAccountService.getBankAccountByIban(bankAccountIban);
        } catch (BankAccountNotFoundException e) {
            // Add log
            logService.addNewLog(new Log(e.getMessage()));
            return redirectToIndexPage();
        }
        // Make the withdraw
        try {
            atmService.withdraw(accountToWithdrawFrom, Integer.parseInt(withdrawAmount));
        } catch (NotSufficientFundsException e) {
            // Add log
            logService.addNewLog(new Log(e.getMessage()));
            bindingResult.rejectValue("withdrawAmount", "withdraw.too.large.error", "Not sufficient funds");
            return getWithdrawForm(form);
        }

        // Log out the card
        cardAuthConfig.getCardAuthentication()
                      .authenticationRequired(REQUIRES_AUTH);

        return redirectToIndexPage();
    }
    
    

    private ModelAndView getDepositForm(DepositForm form) {
        CardAuthentication cardAuth = cardAuthConfig.getCardAuthentication();

        if (!cardAuth.isAuthenticated()) {
            // Add log
            logService.addNewLog(new Log(NOT_AUTH_CARD_MESSAGE));
            return redirectToIndexPage();
        }

        ModelAndView modelAndView = new ModelAndView("customer/atm/deposit");
        modelAndView.addObject("form", form);
        return modelAndView;
    }

    private ModelAndView getWithdrawForm(WithdrawForm form) {
        CardAuthentication cardAuth = cardAuthConfig.getCardAuthentication();

        if (!cardAuth.isAuthenticated()) {
            // Add log
            logService.addNewLog(new Log(NOT_AUTH_CARD_MESSAGE));
            return redirectToIndexPage();
        }

        ModelAndView modelAndView = new ModelAndView("customer/atm/withdraw");
        modelAndView.addObject("form", form);
        return modelAndView;
    }

    private ModelAndView getIntroducedCardPage(final String cardNumber,
                                               final CardLoginForm form) {

        // Get the Card Authentication Bean
        CardAuthentication cardAuth = cardAuthConfig.getCardAuthentication();

        Card introducedCard;
        try {
            // Get the introducedCard from the service
            introducedCard = cardService.getCardByCardNumber(cardNumber);
        } catch (CardNotFoundException e) {
            // Add log
            logService.addNewLog(new Log(e.getMessage()));
            return redirectToIndexPage();
        }

        // If in the Card Authentication Bean we don't have any introduced card
        // Or the introduced card is not equal to the one in the Bean
        if (!cardAuth.hasIntroducedCard() || !cardAuth.getIntroducedCard().equals(introducedCard)) {
            // Instantiate the Card Authentication object with this card
            cardAuth.instantiateAuthForCard(introducedCard);
        }

        // Load the HTML file and attach to it a form to receive a ping code from the user
        ModelAndView modelAndView = new ModelAndView("customer/atm/introduce-card");
        modelAndView.addObject("form", form);

        return modelAndView;
    }

    private ModelAndView redirectToIndexPage() {
        return new ModelAndView("redirect:/customer/atm/index");
    }
}
