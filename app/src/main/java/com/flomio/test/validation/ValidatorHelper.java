package com.flomio.test.validation;

import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darien
 * on 5/18/16.
 * <p>
 * Validation chain
 */
public class ValidatorHelper {

    private List<Validation> validations;

    public ValidatorHelper() {
        this.validations = new ArrayList<>();
    }

    /**
     * Add a validation action with the related error message
     *
     * @param editText             Field to be validated
     * @param validator            Validator implementation
     * @param errorResourceMessage Error message if the validation not pass
     * @return Validator helper instance for add validation to chain
     */
    public ValidatorHelper addValidation(EditText editText, Validator validator, String errorResourceMessage) {
        this.validations.add(new Validation(editText, validator, errorResourceMessage));
        return this;
    }

    /**
     * Validate each field and return if any field is invalid
     *
     * @return true if every validation pass
     */
    public boolean isValid() {
        boolean result = true;
        for (Validation validation : validations) {
            result &= validation.apply();
        }
        return result;
    }

    /**
     * Represent a single validation for a field
     */
    private class Validation {
        private EditText editText;
        private Validator validator;
        private String errorMessage;

        public Validation(EditText editText, Validator validator, String errorMessage) {
            this.editText = editText;
            this.validator = validator;
            this.errorMessage = errorMessage;
        }

        /**
         * Apply the validation.
         * Set the error message if the validation fail
         *
         * @return false if the validation fail
         */
        public boolean apply() {
            String value = editText.getText().toString();
            boolean result = validator.isValid(value);
            if (!result) {
                editText.setError(errorMessage);
            }
            return result;
        }
    }


}
