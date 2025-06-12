package com.s23010621.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.s23010621.data.local.UserModel;
import com.s23010621.data.local.ValidationModel;
import com.s23010621.repository.LoginRepository;

public class LoginVM extends ViewModel {
    private LoginRepository loginRepository;
    private MutableLiveData<ValidationModel> usernameValidationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ValidationModel> passwordValidationMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> buttonValidation = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUsedAddedSuccess = new MutableLiveData<>();
    private MutableLiveData <Boolean> isUserLoggedSuccess = new MutableLiveData<>();
    public LoginVM(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }


    public MutableLiveData<Boolean> getIsUsedAddedSuccess() {
        return isUsedAddedSuccess;
    }

    public MutableLiveData<Boolean> getIsUserLoggedSuccess() {
        return isUserLoggedSuccess;
    }

    public void addUserToDB(UserModel user) {
        isUsedAddedSuccess.setValue(loginRepository.addUser(user));
    }
    public void loginUser(String username,String password) {
        isUserLoggedSuccess.setValue(loginRepository.loginUser(username,password));
    }

    public MutableLiveData<Boolean> getButtonValidation() {
        return buttonValidation;
    }

    public void setButtonValidation() {
        if (usernameValidationMutableLiveData.getValue()!=null&&usernameValidationMutableLiveData.getValue().isValid()&&
        passwordValidationMutableLiveData.getValue()!=null&&passwordValidationMutableLiveData.getValue().isValid()){
            buttonValidation.setValue(true);
        }else {
            buttonValidation.setValue(false);
        }
    }

    public MutableLiveData<ValidationModel> getPasswordValidationMutableLiveData() {
        return passwordValidationMutableLiveData;
    }

    public void setPasswordValidationMutableLiveData(ValidationModel passwordValidation) {
        this.passwordValidationMutableLiveData.setValue(passwordValidation);
        setButtonValidation();
    }

    public MutableLiveData<ValidationModel> getUsernameValidationMutableLiveData() {
        return usernameValidationMutableLiveData;
    }

    public void setUsernameValidationMutableLiveData(ValidationModel usernameValidation) {
        this.usernameValidationMutableLiveData.setValue(usernameValidation);
        setButtonValidation();
    }
}
