package com.flomio.test.exception;

/**
 * Created by darien
 * on 5/19/16.
 *
 * Exception when the request per minute are exceded
 */
public class RequestExcededException extends NetworkException {

    public RequestExcededException() {
    }

    public RequestExcededException(String detailMessage) {
        super(detailMessage);
    }

    public RequestExcededException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RequestExcededException(Throwable throwable) {
        super(throwable);
    }
}
