package com.revature.ers.dtos.requests;

import com.revature.ers.models.User;

public class ResetUserPassword {

    private String username;

    public ResetUserPassword(){

    }

    public ResetUserPassword(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User extractUser(){
        User getUser = new User();
        getUser.setUsername(this.getUsername());
        return  getUser;
    }
}
