package com.example.siddh.duoaccomplish;

import android.support.v4.app.Fragment;

public class RegisterActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return RegisterFragment.newInstance();
    }
}
