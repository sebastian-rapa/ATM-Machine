package ro.sebastianrapa.atmapp.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Override
    public void validatePinCodeInput(final String pinCode,
                                      final String retypedPinCode,
                                      final BindingResult bindingResult) {
        try {
            int pinCodeAsInt = Integer.parseInt(pinCode);
            if (pinCodeAsInt < 1000 || pinCodeAsInt > 9999) {
                bindingResult.rejectValue("pinCode", "pin.code.format.error", "The pin code must be a 4 digit number.");
            }
        } catch (NumberFormatException e) {
            bindingResult.rejectValue("pinCode", "pin.code.format.error", "The pin code must be a 4 digit number. Use only digits.");
        }
        try {
            int retypedPinAsInt = Integer.parseInt(retypedPinCode);
            if (retypedPinAsInt < 1000 || retypedPinAsInt > 9999) {
                bindingResult.rejectValue("retypedPinCode", "pin.code.format.error", "The retyped pin code must be a 4 digit number.");
            }
        } catch (NumberFormatException e) {
            bindingResult.rejectValue("retypedPinCode", "retyped.pin.code.format.error", "The retyped pin code must be a 4 digit number.");
        }

        if (!pinCode.equals(retypedPinCode)) {
            bindingResult.rejectValue("pinCode", "pin.code.not.match.error", "The pin code doesn't match.");
            bindingResult.rejectValue("retypedPinCode", "retyped.pin.code.not.match.error", "The retyped pin code doesn't match.");
        }
    }
}
