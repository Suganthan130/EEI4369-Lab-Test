package com.s23010621.repository;

import android.content.Context;
import android.widget.Toast;

import com.s23010621.data.local.UserModel;
import com.s23010621.ui.Helper.DatabaseHelper;

public class LoginRepository {
    private Context context;
    private DatabaseHelper databaseHelper;

    public LoginRepository(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean addUser(UserModel user) {
        return databaseHelper.addUser(user);
    }
    public boolean loginUser(String username,String password) {
        return databaseHelper.checkLogin(username, password);
    }

}
