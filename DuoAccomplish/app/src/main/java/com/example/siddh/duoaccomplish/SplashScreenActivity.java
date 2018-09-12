package com.example.siddh.duoaccomplish;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.Window;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set enter exit transitions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // activity transition
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
            getWindow().setAllowEnterTransitionOverlap(true);
        }

        setContentView(R.layout.activity_splash_screen);

        ImageView headingImageView = findViewById(R.id.headingImageView);
        ImageView launcherImageView = findViewById(R.id.launcherImageView);

        final Activity activity = this;

        // Initialize the user
        User.get().setContext(this);
        User.get().setUpUser();

        /* ==================== NOTE ===========================================
         *  DBHandler() must only be initialized after setting up the User model.
          */

        new DBHandler().setContext(this);

        // start animations
        launcherImageView.setAlpha(0f);

        headingImageView.setAlpha(0f);

        launcherImageView.animate()
                .setInterpolator(new MyBounceInterpolator(0.2, 9))
                .alpha(1f)
                .scaleX(1.0f)
                .rotation(360f)
                .scaleY(1.0f)
                .setStartDelay(200)
                .setDuration(1800)
                .start();

        headingImageView.animate()
                .alpha(1f)
                .translationYBy(-56f)
                .setStartDelay(1200)
                .setDuration(400)
                .start();

        new Handler().postDelayed(new Runnable() {

            // After 5 seconds do this task.
            @Override
            public void run() {

                Intent intent;

                if(!User.get().hasLoggedIn()) {
                    intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);

                } else {

                    intent = new Intent(SplashScreenActivity.this, MainUserPanelActivity.class);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());

                } else {
                    startActivity(intent);
                }

                finish();
            }

        }, 2100);
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}
