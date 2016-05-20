package com.flomio.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.flomio.test.validation.ValidatorHelper;
import com.flomio.test.view.TaskFragment;

/**
 * Created by darien
 * on 5/20/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;

    protected FragmentManager mFragmentManager;
    protected TaskFragment mTaskFragment;

    protected ValidatorHelper mValidatorHelper;

    protected void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected void releaseFragment() {
        synchronized (this) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }

            if (mTaskFragment != null) {
                mTaskFragment.stopRunning();
                mTaskFragment = null;
            }
        }
    }

}
