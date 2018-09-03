package com.example.siddh.duoaccomplish;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    TextInputEditText mNameEditText;
    TextInputEditText mEmailEditText;
    TextInputEditText mPasswordEditText;

    TextInputLayout mEmailTextInputLayout;
    TextInputLayout mPasswordTextInputLayout;
    TextInputLayout mNameTextInputLayout;

    MaterialButton mRegisterButton;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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

                    Toast.makeText(getActivity(), "Registered!", Toast.LENGTH_SHORT).show();

                    // ================ Start a new activity here =============== //
                }
            }
        });

        return v;
    }
}
