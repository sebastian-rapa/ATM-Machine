package ro.sebastianrapa.atmapp.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ro.sebastianrapa.atmapp.form.BankAccountCreateForm;
import ro.sebastianrapa.atmapp.form.LinkCardToAccountForm;
import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.exception.runtime.BankAccountNotFoundException;
import ro.sebastianrapa.atmapp.service.BankAccountService;
import ro.sebastianrapa.atmapp.service.CardService;

import javax.validation.Valid;
import java.util.List;


/**
 * Class responsible to handle routes related to Bank Account Action
 * */
@Controller
@RequestMapping("admin/bank-account")
public class BankAccountWebController {

    private static final String EMPTY_STRING = "";

    /**
     * Bank Account Service Interface that will be linked to an implementation
     * */
    private transient final BankAccountService bankAccountService;
    /**
     * Card Service Interface that will be linked to an implementation
     * */
    private transient final CardService cardService;


    /**
     * Taking Advantage of Spring's Dependency Injection through class constructor
     * */
    public BankAccountWebController(final BankAccountService bankAccountService,
                                    final CardService cardService) {
        this.bankAccountService = bankAccountService;
        this.cardService = cardService;
    }

    @InitBinder
    public void preProcessData(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, "cardHolderName", stringTrimmerEditor);
    }


    /**
     * Method that loads the index HTML file and serves a list of bank accounts
     * */
    @GetMapping("/index")
    public ModelAndView index() {

        final ModelAndView modelAndView = new ModelAndView("admin/bankaccount/index");
        modelAndView.addObject("bankAccounts", bankAccountService.fetchAllBankAccounts());

        return modelAndView;
    }


    /**
     * Method that loads the create HTML file
     * */
    @GetMapping("/create")
    public ModelAndView create(final BankAccountCreateForm form) {
        return getCreateForm(form);
    }

    @PostMapping("/store")
    public ModelAndView store(@ModelAttribute("form") final BankAccountCreateForm form,
                              BindingResult bindingResult) {

        // Get the bank account holder name
        final String accountHolderName = form.getBankAccountHolderName();

        // Validate that the input card holder name is not Empty
        if (accountHolderName.equals(EMPTY_STRING)) {
            bindingResult.rejectValue("bankAccountHolderName", "empty.bankAccountHolderName.error", "Please enter a card holder name.");
        }
        // Validate the uniqueness of the card holder name
        if (bankAccountService.hasBankAccount(accountHolderName)) {
            bindingResult.rejectValue("bankAccountHolderName", "not.unique.bankAccountHolderName.error", "A bank account associated with the name: " + accountHolderName + " already exists");
        }

        // Check if there were any errors
        if (bindingResult.hasErrors()) {
            return getCreateForm(form);
        }
        // Create an instance of a bank account
        final BankAccount newBankAccount = new BankAccount(form);
        // Save the new Bank Account
        bankAccountService.saveBankAccount(newBankAccount);

        return redirectToIndexPage();
    }

    @GetMapping(value = "/create-card", params = "bankAccountIban")
    public ModelAndView createAccountsCard(@RequestParam("bankAccountIban") final String bankAccountIban,
                                           @ModelAttribute("linkCardToAccountForm") final LinkCardToAccountForm linkCardToAccountForm) {
        return getCreateAccountsCardForm(linkCardToAccountForm, bankAccountIban);
    }

    @PostMapping(value = "/link-card", params = "bankAccountIban")
    public ModelAndView linkCardToAccount(@RequestParam final String bankAccountIban,
                                          @ModelAttribute("linkCardToAccountForm") final LinkCardToAccountForm linkCardToAccountForm,
                                          final BindingResult bindingResult) {
        // Validate input
        validatePinInput(linkCardToAccountForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return getCreateAccountsCardForm(linkCardToAccountForm, bankAccountIban);
        }

        // Create card
        Card newCard = new Card(linkCardToAccountForm);

        // Save card
        cardService.save(newCard);

        return redirectToIndexPage();
    }

    private void validatePinInput(final LinkCardToAccountForm linkCardToAccountForm,
                                  final BindingResult bindingResult) {
        try {
            int pinCodeAsInt = Integer.parseInt(linkCardToAccountForm.getPinCode());
            if (pinCodeAsInt < 1000 || pinCodeAsInt > 9999) {
                bindingResult.rejectValue("pinCode", "pin.code.format.error", "The pin code must be a 4 digit number.");
            }
        } catch (NumberFormatException e) {
            bindingResult.rejectValue("pinCode", "pin.code.format.error", "The pin code must be a 4 digit number. Use only digits.");
        }
        try {
            int retypedPinAsInt = Integer.parseInt(linkCardToAccountForm.getRetypedPinCode());
            if (retypedPinAsInt < 1000 || retypedPinAsInt > 9999) {
                bindingResult.rejectValue("retypedPinCode", "pin.code.format.error", "The retyped pin code must be a 4 digit number.");
            }
        } catch (NumberFormatException e) {
            bindingResult.rejectValue("retypedPinCode", "retyped.pin.code.format.error", "The retyped pin code must be a 4 digit number.");
        }

        if (!linkCardToAccountForm.getPinCode().equals(linkCardToAccountForm.getRetypedPinCode())) {
            bindingResult.rejectValue("pinCode", "pin.code.not.match.error", "The pin code doesn't match.");
            bindingResult.rejectValue("retypedPinCode", "retyped.pin.code.not.match.error", "The retyped pin code doesn't match.");
        }
    }

    private ModelAndView getCreateAccountsCardForm(final LinkCardToAccountForm linkCardToAccountForm, final String bankAccountIban){
        BankAccount account;
        try {
            // Get the bank account
            account = bankAccountService.getBankAccountByIban(bankAccountIban);
        } catch (BankAccountNotFoundException e) {
            System.out.println("Receveied exception: ");
            System.out.println(e.getMessage());
            // TODO: Add a log and redirect to index page
            return redirectToIndexPage();
        }

        // Fill in the bank account data
        linkCardToAccountForm.fillInAccountData(account);

        // Load the HTML Page
        ModelAndView modelAndView = new ModelAndView("admin/bankaccount/create-card");
        // Attach the form to the page
        modelAndView.addObject("linkCardToAccountForm", linkCardToAccountForm);
        // Attach the bank account to the page
        modelAndView.addObject("bankAccount", account);
        // Return the model and view
        return modelAndView;
    }


    private ModelAndView getCreateForm(final BankAccountCreateForm form) {
        final ModelAndView modelAndView = new ModelAndView("admin/bankaccount/create");
        modelAndView.addObject("form", form);
        modelAndView.addObject("currencies", bankAccountService.fetchCurrencies());
        return modelAndView;
    }

    private ModelAndView redirectToIndexPage() {
        return new ModelAndView("redirect:/admin/bank-account/index");
    }
}
