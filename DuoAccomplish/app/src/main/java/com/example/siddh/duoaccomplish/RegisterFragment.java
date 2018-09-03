package com.example.siddh.duoaccomplish;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RegisterFragment extends Fragment
        implements DBHandler.UserRegisteredListener {

    private static final String TAG = "DialogLoading";

    private String loadingText;

    private DBHandler mDBHandler;

    private TextInputEditText mNameEditText;
    private TextInputEditText mEmailEditText;
    private TextInputEditText mPasswordEditText;

    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mNameTextInputLayout;

    private MaterialButton mRegisterButton;

    private Callbacks mCallbacks;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    public interface Callbacks {
        void switchToActivity(Class<? extends Activity> activityToOpen);
    }

    @Override
    public void onUserRegistered() {
        Log.i("Debug", "onUserRegistered: Reached to Callbacks!");
        mCallbacks.switchToActivity(UserImageActivity.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadingText = getActivity().getString(R.string.registering_to_database_please_wait);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBHandler = new DBHandler();
        mDBHandler.setUserRegisteredListener(this);

        User.get().setContext(getActivity());
        mDBHandler.setContext(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mEmailTextInputLayout = (TextInputLayout) v.findViewById(R.id.email_TextInputLayout);
        mPasswordTextInputLayout = (TextInputLayout) v.findViewById(R.id.password_TextInputLayout);
        mNameTextInputLayout = (TextInputLayout) v.findViewById(R.id.name_TextInputLayout);

        mEmailEditText = (TextInputEditText) v.findViewById(R.id.email_EditText);
        mPasswordEditText = (TextInputEditText) v.findViewById(R.id.password_EditText);
        mNameEditText = (TextInputEditText) v.findViewById(R.id.name_EditText);

        mRegisterButton = (MaterialButton) v.findViewById(R.id.register_Button);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable email = mEmailEditText.getText();
                Editable password = mPasswordEditText.getText();
                Editable name = mNameEditText.getText();

                String email_text = "";
                String password_text = "";
                String name_text = "";

                boolean validCredentials = true;

                if (email != null) {
                    email_text = email.toString();
                }

                if (password != null) {
                    password_text = password.toString();
                }

                if (name != null) {
                    name_text = name.toString();
                }

                // Checking for valid email address
                if (TextUtils.isEmpty(email_text) ||
                        !Patterns.EMAIL_ADDRESS.matcher(email_text).matches()) {

                    mEmailTextInputLayout.setError("Please enter a valid email address!");
                    validCredentials = false;

                } else {
                    mEmailTextInputLayout.setError(null);
                }

                // Checking for non-empty password
                if (TextUtils.isEmpty(password_text)) {
                    mPasswordTextInputLayout.setError("Please enter a password!");
                    validCredentials = false;

                } else {
                    mPasswordTextInputLayout.setError(null);
                }

                // Checking for non-empty name
                if (TextUtils.isEmpty(name_text)) {
                    mNameTextInputLayout.setError("Please enter your name!");
                    validCredentials = false;

                } else {
                    mNameTextInputLayout.setError(null);
                }

                if (validCredentials) {
                    User.get().setEmail(email_text);
                    User.get().setPassword(password_text);
                    User.get().setName(name_text);

                    // ================ Start a new activity here =============== //
                    FragmentManager manager = getFragmentManager();
                    new LoadingFragment()
                            .setLoadingText(loadingText)
                            .show(manager, TAG);

                    mDBHandler.registerUser();
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
