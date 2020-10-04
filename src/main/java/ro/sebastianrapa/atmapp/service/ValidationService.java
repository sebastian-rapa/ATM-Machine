package ro.sebastianrapa.atmapp.service;

import org.springframework.validation.BindingResult;

public interface ValidationService {

    void validatePinCodeInput(String pinCode, String retypedPinCode, BindingResult bindingResult);

    void validatePinCodeFormat(String introducedPinCode, BindingResult bindingResult);

    void validateDepositAmount(String depositAmount, BindingResult bindingResult);

    void validateWithdrawAmount(String withdrawAmount, BindingResult bindingResult);
}
