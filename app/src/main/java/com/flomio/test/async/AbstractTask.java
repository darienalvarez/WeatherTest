package com.flomio.test.async;

import android.os.AsyncTask;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent an Abstract Async Task
 */
public abstract class AbstractTask<P, G, R> extends AsyncTask<P, G, R> {

    private TaskCallback<R> mCallback;
    protected Exception mException;

    public AbstractTask(TaskCallback<R> callback) {
        super();
        mCallback = callback;
    }

    @Override
    protected void onCancelled() {
        if (mCallback != null) {
            mCallback.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (mCallback != null) {
            if (mException != null) {
                mCallback.onError(mException);
            } else {
                mCallback.onSuccess(result);
            }
        }
    }
}
