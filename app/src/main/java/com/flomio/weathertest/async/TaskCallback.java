package com.flomio.weathertest.async;

import android.widget.ProgressBar;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent a callback for an Async Task
 */
public interface TaskCallback<T> {

    ProgressBar findProgressBar();

    void onSuccess(T result);

    void onCancelled();

    void onError(Throwable e);
}
