package com.flomio.test.async;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a callback for an Async Task
 */
public interface TaskCallback<T> {

    void onSuccess(T result);

    void onCancelled();

    void onError(Throwable e);
}
