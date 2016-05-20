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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCallback.findProgressDialog() != null) {
            mCallback.findProgressDialog().show();
        }
    }

    @Override
    protected void onCancelled() {

        if (mCallback != null) {
            if (mCallback.findProgressDialog() != null) {
                mCallback.findProgressDialog().dismiss();
            }

            mCallback.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (mCallback != null) {
            if (mCallback.findProgressDialog() != null) {
                mCallback.findProgressDialog().dismiss();
            }

            if (mException != null) {
                mCallback.onError(mException);
            } else {
                mCallback.onSuccess(result);
            }
        }
    }

    public void setCallback(TaskCallback<R> callback) {
        this.mCallback = callback;
    }
}
