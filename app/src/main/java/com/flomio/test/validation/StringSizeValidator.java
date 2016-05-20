package com.flomio.test.validation;

import android.text.TextUtils;

/**
 * Created by Darien
 * on 5/18/16.
 * <p>
 * Validate if the string size is higher than a number
 */
public class StringSizeValidator implements Validator {

    private final int minSize;

    /**
     * Create an instance of StringSizeValidator
     *
     * @param minSize min size of the string
     * @throws IllegalArgumentException if the value is less than 1
     */
    public StringSizeValidator(int minSize) {
        if (minSize < 1) {
            throw new IllegalArgumentException("min value must be higher than 0");
        }

        this.minSize = minSize;
    }

    @Override
    public boolean isValid(String value) {
        return !TextUtils.isEmpty(value) && value.length() >= minSize;
    }
}
