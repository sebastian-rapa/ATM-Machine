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
            if (wrongPinFormat(pinCodeAsInt)) {
                bindingResult.rejectValue("pinCode", "pin.code.format.error", "The pin code must be a 4 digit number.");
            }
        } catch (NumberFormatException e) {
            bindingResult.rejectValue("pinCode", "pin.code.format.error", "The pin code must be a 4 digit number. Use only digits.");
        }
        try {
            int retypedPinAsInt = Integer.parseInt(retypedPinCode);
            if (wrongPinFormat(retypedPinAsInt)) {
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

    @Override
    public void validatePinCodeFormat(final String introducedPinCode,
                                      final BindingResult bindingResult) {
        try {
            int pinCodeAsInt = Integer.parseInt(introducedPinCode);
            if (wrongPinFormat(pinCodeAsInt)) {
                bindingResult.rejectValue(
                        "pinCode",
                        "pin.code.format.error",
                        "The pin code must be a 4 digit number.");
            }
        } catch (NumberFormatException e) {
            bindingResult.rejectValue(
                    "pinCode",
                    "pin.code.format.error",
                    "The pin code must be a 4 digit number. Use only digits.");
        }
    }

    private boolean wrongPinFormat(Integer pinCode) {
        return pinCode < 1000 || pinCode > 9999;
    }

}
