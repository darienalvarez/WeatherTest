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

    protected FragmentManager mFragmentManager;
    protected TaskFragment mTaskFragment;

}
