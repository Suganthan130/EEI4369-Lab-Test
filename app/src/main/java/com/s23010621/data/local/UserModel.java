package com.s23010621.data.local;

public class UserModel {
    private int uniqueId;
    private String username;
    private String password;

    public UserModel(int uniqueId, String username, String password) {
        this.uniqueId = uniqueId;
        this.username = username;
        this.password = password;
    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
