package com.flomio.test.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.flomio.test.async.AbstractTask;
import com.flomio.test.async.TaskCallback;
import com.flomio.test.util.DialogHelper;

/**
 * Created by Darien
 * on 5/19/16.
 */
public class TaskFragment<P, G, R> extends Fragment {

    private TaskCallback<R> mCallback;
    private AbstractTask<P, G, R> mTask;
    private P[] mParam;
    private boolean mRunning;

    protected ProgressDialog mProgressDialog;

    public TaskFragment addConfiguration(AbstractTask<P, G, R> task, P[] param) {
        this.mTask = task;
        this.mParam = param;
        return this;
    }

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //noinspection unchecked
        mCallback = (TaskCallback<R>) context;

        if (mProgressDialog != null && !mProgressDialog.isShowing() && mRunning) {
            mProgressDialog.show();
        } else if (mProgressDialog == null && mRunning) {
            this.mProgressDialog = DialogHelper.buildProgressDialog(context);
            this.mProgressDialog.show();
        }
    }

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
        this.mTask.setCallback(mCallback);

        // Execute task
        //noinspection unchecked
        this.mTask.execute(mParam);

        if (mProgressDialog == null && mRunning) {
            this.mProgressDialog = DialogHelper.buildProgressDialog(getActivity());
            this.mProgressDialog.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRunning = false;
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void stopRunning() {

        this.mRunning = false;

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

    }
}