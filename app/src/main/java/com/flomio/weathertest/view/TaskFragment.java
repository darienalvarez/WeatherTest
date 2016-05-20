package com.flomio.weathertest.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.flomio.weathertest.async.AbstractTask;
import com.flomio.weathertest.async.TaskCallback;

/**
 * Created by Darien
 * on 5/19/16.
 */
public class TaskFragment<P, G, R> extends Fragment {

    private TaskCallback<R> mCallback;
    private AbstractTask<P, G, R> mTask;
    private P[] mParam;
    private boolean mRunning;

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

        mCallback = (TaskCallback<R>) context;
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

        // Execute task
        this.mRunning = true;
        this.mTask.setCallback(mCallback);
        this.mTask.execute(mParam);
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;
    }

    public boolean isRunning() {
        return mRunning;
    }

    public void stopRunning() {
        this.mRunning = false;
    }
}