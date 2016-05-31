package com.flomio.test;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.flomio.test.controller.UserInfoController;
import com.flomio.test.networking.dto.Location;
import com.flomio.test.view.UserInfoFragment;

public class UserInfoActivity extends BaseActivity  {

    private static final String TAG_TASK_FRAGMENT = "task_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        UserInfoController con = new UserInfoController(this);
        String[] values = con.validatePreferences();
        // if values is not null then you should show the Weather Info Activity instance of User Info Activity
        if (values != null) {
            con.navigate(values[0], values[1], new Location(values[2], values[3]));
            finish();
        } else {
            // find if any async task is mRunning
            FragmentManager mFragmentManager = getSupportFragmentManager();
            UserInfoFragment mTaskFragment = (UserInfoFragment) mFragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

            if(mTaskFragment == null) {
                mTaskFragment = new UserInfoFragment();
                mFragmentManager.beginTransaction().add(R.id.container, mTaskFragment, TAG_TASK_FRAGMENT).commit();
            }
        }
    }

}
