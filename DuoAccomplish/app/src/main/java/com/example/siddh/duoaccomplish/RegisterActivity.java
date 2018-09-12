package com.example.siddh.duoaccomplish;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.transition.Explode;
import android.transition.Transition;

public class RegisterActivity extends SingleFragmentActivity
        implements RegisterFragment.Callbacks {

    @Override
    @TargetApi(21)
    public Transition getTransition() {
        return new Explode();
    }

    @Override
    public void switchToActivity(Class<? extends Activity> activityToOpen) {
        Intent intent = new Intent(this, activityToOpen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

        } else {
            startActivity(intent);
        }

        finish();
    }

    @Override
    public Fragment createFragment() {
        return RegisterFragment.newInstance();
    }
}
