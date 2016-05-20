package com.flomio.test.exception;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent an invalid response
 */
public class InvalidResponseException extends NetworkException {

    public InvalidResponseException() {
    }

    public InvalidResponseException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidResponseException(Throwable throwable) {
        super(throwable);
    }
}
