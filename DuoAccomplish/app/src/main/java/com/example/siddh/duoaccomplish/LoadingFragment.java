package com.example.siddh.duoaccomplish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LoadingFragment extends DialogFragment {

    String mLoadingText;

    public LoadingFragment setLoadingText(String loadingText) {
        mLoadingText = loadingText;
        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.loading_fragment, null);

        TextView textView = view.findViewById(R.id.registerTextView);
        textView.setText(mLoadingText);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }
}
