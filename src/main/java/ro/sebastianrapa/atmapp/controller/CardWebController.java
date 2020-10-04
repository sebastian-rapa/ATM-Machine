package ro.sebastianrapa.atmapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ro.sebastianrapa.atmapp.form.CardCreateForm;
import ro.sebastianrapa.atmapp.model.BankAccount;
import ro.sebastianrapa.atmapp.model.Card;
import ro.sebastianrapa.atmapp.model.Log;
import ro.sebastianrapa.atmapp.model.exception.runtime.BankAccountNotFoundException;
import ro.sebastianrapa.atmapp.service.BankAccountService;
import ro.sebastianrapa.atmapp.service.CardService;
import ro.sebastianrapa.atmapp.service.LogService;
import ro.sebastianrapa.atmapp.service.ValidationService;

import java.util.List;


/**
 * Class responsible to handle routes related to Card Action
 * */
@Controller
@RequestMapping("admin/card")
public class CardWebController {

    /**
     * Bank Account Service Interface that will be linked to an implementation
     * */
    private transient final BankAccountService bankAccountService;
    /**
     * Card Service Interface that will be linked to an implementation
     * */
    private transient final CardService cardService;
    /**
     * Validation Service Interface that will be linked to an implementation
     * */
    private transient final ValidationService validationService;
    /**
     * Log Service Interface that will be linked to an implementation
     * */
    private transient final LogService logService;


    /**
     * Taking Advantage of Spring's Dependency Injection through class constructor
     * */
    public CardWebController(final CardService cardService,
                             final BankAccountService bankAccountService,
                             final ValidationService validationService,
                             final LogService logService) {
        this.cardService = cardService;
        this.bankAccountService = bankAccountService;
        this.validationService = validationService;
        this.logService = logService;
    }


    /**
     * Method that loads the index HTML file and serves a list of cards
     * */
    @GetMapping("/index")
    public ModelAndView index() {

        final ModelAndView modelAndView = new ModelAndView("admin/card/index");
        modelAndView.addObject("cards", cardService.fetchAllCards());

        return modelAndView;
    }


    /**
     * Method that loads the create HTML file
     * */
    @GetMapping("/create")
    public ModelAndView create(@ModelAttribute("form") final CardCreateForm form) {
        return getCreateForm(form);
    }

    @PostMapping("/store")
    public ModelAndView store(@ModelAttribute("form") final CardCreateForm form,
                              BindingResult bindingResult) {

        // Validate user input
        validationService.validatePinCodeInput(
                form.getPinCode(),
                form.getRetypedPinCode(),
                bindingResult
        );

        try {
            // Make sure the bank account exists
            BankAccount account = bankAccountService.getBankAccountByIban(form.getBankAccountIban());
            form.setCardHolderName(account.getHolderName());
        } catch (BankAccountNotFoundException e) {
            // Add fail log
            logService.addNewLog(new Log(e.getMessage()));
            return redirectToIndexPage();
        }
        // Return the form page giving the errors
        if (bindingResult.hasErrors()) {
            return getCreateForm(form);
        }

        // Create new Card
        Card newCard = new Card(form);

        // Store new Card
        cardService.save(newCard);

        return redirectToIndexPage();
    }

    private ModelAndView getCreateForm(final CardCreateForm form) {
        final ModelAndView modelAndView = new ModelAndView("admin/card/create");
        modelAndView.addObject("form", form);

        final List<BankAccount> bankAccounts = bankAccountService.fetchAllBankAccounts();


        // If there are no Bank Accounts registered, redirect to index page and add a log
        if (bankAccounts.size() <= 0) {
            // Add log
            logService.addNewLog(new Log("Creat bank account first, then you can create a card"));
            return redirectToIndexPage();
        }

        modelAndView.addObject("bankAccounts", bankAccounts);
        return modelAndView;
    }

    private ModelAndView redirectToIndexPage() {
        return new ModelAndView("redirect:/admin/card/index");
    }
}
