package ro.sebastianrapa.atmapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.sebastianrapa.atmapp.config.CardAuthenticationConfig;
import ro.sebastianrapa.atmapp.form.CardLoginForm;
import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.exception.runtime.BankAccountNotFoundException;
import ro.sebastianrapa.atmapp.model.exception.runtime.CardNotFoundException;
import ro.sebastianrapa.atmapp.security.CardAuthentication;
import ro.sebastianrapa.atmapp.service.ATMService;
import ro.sebastianrapa.atmapp.service.BankAccountService;
import ro.sebastianrapa.atmapp.service.CardService;
import ro.sebastianrapa.atmapp.service.ValidationService;

@Controller
@RequestMapping("customer/atm")
public class ATMController {

    private transient final ATMService atmService;
    private transient final BankAccountService bankAccountService;
    private transient final CardService cardService;
    private transient final CardAuthenticationConfig cardAuthConfig;
    private transient final ValidationService validationService;

    public ATMController(final ATMService atmService,
                         final BankAccountService bankAccountService,
                         final CardService cardService,
                         final CardAuthenticationConfig cardAuthConfig,
                         final ValidationService validationService) {
        this.atmService = atmService;
        this.bankAccountService = bankAccountService;
        this.cardService = cardService;
        this.cardAuthConfig = cardAuthConfig;
        this.validationService = validationService;
    }

    @GetMapping("/index")
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView("customer/atm/index");
        modelAndView.addObject("cards", cardService.fetchAllCards());

        return modelAndView;
    }


    @GetMapping(value = "/introduce-card", params = "cardNumber")
    public ModelAndView introduceCard(@RequestParam final String cardNumber,
                                      final CardLoginForm form) {
        return getIntroducedCardPage(cardNumber, form);
    }

    @PostMapping(value = "/authenticate-card")
    public ModelAndView modelAndView(@ModelAttribute("form") CardLoginForm form,
                                     BindingResult bindingResult) {

        CardAuthentication cardAuth = cardAuthConfig.getCardAuthentication();

        String introducedPin = form.getPinCode();
        Card introducedCard = cardAuth.getIntroducedCard();
        String cardNumber = introducedCard.getCardNumber();

        // Validate the pin code format
        validationService.validatePinCodeFormat(introducedPin, bindingResult);

        if (bindingResult.hasErrors()) {
            // If we have ping code format error return to the introduced card page
            return getIntroducedCardPage(cardNumber, form);
        }

        // Check if the pin code is correct
        if (!introducedCard.checkPinCode(introducedPin)) {
            bindingResult.rejectValue("pinCode", "wrong.pin.code.error", "Wrong ping code introduced.");
            // Count the wrong input
            cardAuth.wrongPinAttempt();
            // Check if there were to many wrong attempts
            if (cardAuth.tooManyWrongAttempts()) {
                // Block Card
                cardService.blockCard(introducedCard);
                // TODO: Add Log

                // Redirect to index page
                return redirectToIndexPage();
            }
            // TODO: Add Log

            // Return to the introduced-card page
            return getIntroducedCardPage(cardNumber, form);
        }

        // TODO: Think if this is useful?!
        // Successfully authenticate this card
        cardAuth.setAuthenticated(true);

        // Get the bank account associated with this card
        BankAccount associatedAccount;
        try {
            associatedAccount = bankAccountService.getBankAccountByIban(introducedCard.getBankAccountIban());
        } catch (BankAccountNotFoundException e) {
            // TODO: Add log

            return redirectToIndexPage();
        }
        // Load the authenticated HTML file
        ModelAndView modelAndView = new ModelAndView("/customer/atm/authenticated");
        modelAndView.addObject("bankAccount", associatedAccount);

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
            // TODO: Add log
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
