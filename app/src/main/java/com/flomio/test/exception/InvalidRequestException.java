package com.flomio.test.exception;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a request with an invalid result
 */
public class InvalidRequestException extends NetworkException {

    public InvalidRequestException() {
    }

    public InvalidRequestException(String detailMessage) {
        super(detailMessage);
    }

    public InvalidRequestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public InvalidRequestException(Throwable throwable) {
        super(throwable);
    }
}
