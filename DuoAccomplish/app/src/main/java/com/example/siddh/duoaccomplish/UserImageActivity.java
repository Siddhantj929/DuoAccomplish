package com.example.siddh.duoaccomplish;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class UserImageActivity extends SingleFragmentActivity
        implements UserImageFragment.Callbacks {

    @Override
    public void switchToActivity(Class<? extends Activity> activityToOpen) {
        Intent intent = new Intent(this, activityToOpen);
        startActivity(intent);
        finish();
    }

    @Override
    public Fragment createFragment() {
        return UserImageFragment.newInstance();
    }
}
