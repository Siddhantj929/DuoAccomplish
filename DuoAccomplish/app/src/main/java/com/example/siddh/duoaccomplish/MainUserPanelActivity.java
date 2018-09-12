package com.example.siddh.duoaccomplish;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;

public class MainUserPanelActivity extends SingleFragmentActivity
        implements MainUserPanelFragment.Callbacks {

    private Menu mMenu;

    private int currentStatusBarColor = R.color.primaryDarkColor;

    @Override
    @TargetApi(21)
    public void changeMenuIcon(int MODE) {

        if(mMenu == null) {
            return;
        }

        int Id;

        if(MODE == 1) {
            Id = R.drawable.ic_search;
            currentStatusBarColor = R.color.primaryDarkColor;

        } else {
            Id = R.drawable.ic_search_light;
            currentStatusBarColor = R.color.statusBarDarkColor;
        }

        mMenu.getItem(0).setIcon(Id);

        getWindow().setStatusBarColor(
                getResources().getColor(currentStatusBarColor)
        );
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
    public void setNewActionBar(Toolbar toolbar, String titleText) {
        setSupportActionBar(toolbar);

        if(titleText != null) {
            getSupportActionBar().setTitle(titleText);
        }
    }

    @Override
    public Fragment createFragment() {
        return MainUserPanelFragment.newInstance();
    }

    @Override
    public Transition getTransition() {
        return new Fade();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigation_menu, menu);

        mMenu = menu;

        return true;
    }
}
