package com.flomio.weathertest.async;

import android.app.ProgressDialog;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a callback for an Async Task
 */
public interface TaskCallback<T> {

    ProgressDialog findProgressDialog();

    void onSuccess(T result);

    void onCancelled();

    void onError(Throwable e);
}
