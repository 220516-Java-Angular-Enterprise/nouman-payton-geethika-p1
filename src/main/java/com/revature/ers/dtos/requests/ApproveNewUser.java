package com.revature.ers.dtos.requests;

import com.revature.ers.models.User;

public class ApproveNewUser {
    private String username;


    public ApproveNewUser(){

    }

    public ApproveNewUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public User extractUser(){
        User approveUser = new User();
        approveUser.setUsername(this.getUsername());
        approveUser.setActive(true);
        approveUser.setRole_id("DEFAULT");
        return approveUser;
    }


}
