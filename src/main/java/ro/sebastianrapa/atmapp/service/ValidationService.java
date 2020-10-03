package ro.sebastianrapa.atmapp.service;

import org.springframework.validation.BindingResult;

public interface ValidationService {

    void validatePinCodeInput(String pinCode, String retypedPinCode, BindingResult bindingResult);
}
