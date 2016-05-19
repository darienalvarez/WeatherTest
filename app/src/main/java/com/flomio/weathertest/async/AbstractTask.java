package com.flomio.weathertest.async;

import android.os.AsyncTask;
import android.view.View;

/**
 * Created by Darien
 * on 5/19/16.
 * <p>
 * Represent an Abstract Async Task
 */
public abstract class AbstractTask<P, G, R> extends AsyncTask<P, G, R> {

    protected TaskCallback<R> mCallback;
    protected Exception mException;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCallback.findProgressBar() != null) {
            mCallback.findProgressBar().setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCancelled() {

        if (mCallback != null) {
            if (mCallback.findProgressBar() != null) {
                mCallback.findProgressBar().setVisibility(View.GONE);
            }

            mCallback.onCancelled();
        }
    }

    @Override
    protected void onPostExecute(R result) {
        if (mCallback != null) {
            if (mCallback.findProgressBar() != null) {
                mCallback.findProgressBar().setVisibility(View.GONE);
            }

            if (mException != null) {
                mCallback.onError(mException);
            } else {
                mCallback.onSuccess(result);
            }
        }
    }

    public void setCallback(TaskCallback callback) {
        this.mCallback = callback;
    }
}
