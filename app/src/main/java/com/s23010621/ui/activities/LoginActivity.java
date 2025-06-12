package com.s23010621.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.s23010621.R;
import com.s23010621.Utils.ViewModelFactory;
import com.s23010621.data.local.UserModel;
import com.s23010621.data.local.ValidationModel;
import com.s23010621.repository.LoginRepository;
import com.s23010621.ui.viewmodel.LoginVM;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout materialEditTextUsername;
    private TextInputLayout materialEditTextPassword;
    private Button buttonLogin;
    private LoginRepository loginRepository;
    private LoginVM loginVM;
    private String username;
    private String password;
    private Button buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialView();
        initialVM();
        viewModelObserver();
        setListener();
        setOnClick();


    }

    private void setOnClick() {
        buttonLogin.setOnClickListener(v -> {
            loginVM.loginUser(username,password);
        });
        buttonCreateAccount.setOnClickListener(v -> {
            UserModel user = new UserModel(username, password);
            loginVM.addUserToDB(user);
        });
    }

    private void viewModelObserver() {
        loginVM.getIsUsedAddedSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(LoginActivity.this, "User saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginVM.getIsUserLoggedSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if (success) {
                    Toast.makeText(LoginActivity.this, "User login successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,GoogleMapActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication is invalid.", Toast.LENGTH_SHORT).show();
                    loginVM.setUsernameValidationMutableLiveData(new ValidationModel("Authentication is invalid.",false));
                    loginVM.setPasswordValidationMutableLiveData(new ValidationModel("Authentication is invalid.",false));
                }
            }
        });
        loginVM.getPasswordValidationMutableLiveData().observe(this, new Observer<ValidationModel>() {
            @Override
            public void onChanged(ValidationModel validationModel) {
                if (validationModel.isValid()) {
                    materialEditTextPassword.setError(null);
                } else {
                    materialEditTextPassword.setError(validationModel.getMessageError());
                }

            }
        });
        loginVM.getUsernameValidationMutableLiveData().observe(this, new Observer<ValidationModel>() {
            @Override
            public void onChanged(ValidationModel validationModel) {
                if (validationModel.isValid()) {
                    materialEditTextUsername.setError(null);
                } else {
                    materialEditTextUsername.setError(validationModel.getMessageError());
                }

            }
        });
        loginVM.getButtonValidation().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isValid) {
                if (isValid) {
                    buttonLogin.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.orange)));
                    buttonLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.white));
                    buttonLogin.setEnabled(true);
                    buttonCreateAccount.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.orange)));
                    buttonCreateAccount.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.white));
                    buttonCreateAccount.setEnabled(true);

                } else {
                    buttonLogin.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.gray)));
                    buttonLogin.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.black));
                    buttonLogin.setEnabled(false);
                    buttonCreateAccount.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.gray)));
                    buttonCreateAccount.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.black));
                    buttonCreateAccount.setEnabled(false);
                }

            }
        });
    }

    private void initialVM() {
        loginRepository = new LoginRepository(this);
        ViewModelFactory<LoginRepository> loginRepositoryViewModelFactory = new ViewModelFactory<>(loginRepository);
        loginVM = new ViewModelProvider(this, loginRepositoryViewModelFactory).get(LoginVM.class);
    }

    private void setListener() {
        loginVM.setUsernameValidationMutableLiveData(new ValidationModel("Username is required", false));
        loginVM.setPasswordValidationMutableLiveData(new ValidationModel("Password is required", false));
        if (materialEditTextUsername.getEditText() != null) {
            materialEditTextUsername.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        loginVM.setUsernameValidationMutableLiveData(new ValidationModel("Username is required", false));
                    } else {
                        if (isSQLInjection(s.toString())) {
                            loginVM.setUsernameValidationMutableLiveData(new ValidationModel("Username is invalid", false));
                        } else {
                            loginVM.setUsernameValidationMutableLiveData(new ValidationModel("", true));
                            username = s.toString();
                        }

                    }


                }
            });
        }
        if (materialEditTextPassword.getEditText() != null) {
            materialEditTextPassword.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().isEmpty()) {
                        loginVM.setPasswordValidationMutableLiveData(new ValidationModel("Password is required", false));
                    } else {
                        loginVM.setPasswordValidationMutableLiveData(new ValidationModel("", true));
                        password = s.toString();
                    }

                }
            });
        }

    }

    private void initialView() {
        materialEditTextUsername = findViewById(R.id.material_edit_text_username);
        materialEditTextPassword = findViewById(R.id.material_edit_text_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonCreateAccount = findViewById(R.id.button_create_account);
    }

    public boolean isSQLInjection(String input) {
        String lowerInput = input.toLowerCase();

        return lowerInput.contains(" or ")
                || lowerInput.contains(" and ")
                || lowerInput.contains("--")
                || lowerInput.contains(";")
                || lowerInput.contains("'")
                || lowerInput.contains("\"")
                || lowerInput.contains("/*")
                || lowerInput.contains("*/")
                || lowerInput.contains("drop")
                || lowerInput.contains("insert")
                || lowerInput.contains("delete")
                || lowerInput.contains("update")
                || lowerInput.contains("select");
    }
}