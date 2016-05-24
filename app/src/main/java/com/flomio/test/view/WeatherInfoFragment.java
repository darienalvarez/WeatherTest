package com.flomio.test.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flomio.test.async.AbstractTask;
import com.flomio.test.async.TaskCallback;
import com.flomio.test.networking.dto.Weather;
import com.flomio.test.util.DialogHelper;

/**
 * Created by darien
 * on 5/23/16.
 */
public class WeatherInfoFragment extends Fragment implements TaskCallback<Weather> {

    private boolean mRunning;
    private AbstractTask mTask;
    private ProgressDialog mProgressDialog;

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // set task parameters
        this.mRunning = true;
        this.mTask.setCallback(this);

        // Execute task
        //noinspection unchecked
        this.mTask.execute(mParam);

        if (mProgressDialog == null && mRunning) {
            // create an showing dialog if is running (first time)
            this.mProgressDialog = DialogHelper.buildProgressDialog(getActivity());
            this.mProgressDialog.show();
        }
    }

    @Override
    public void onSuccess(Weather result) {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
