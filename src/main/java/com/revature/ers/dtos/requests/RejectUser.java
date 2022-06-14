package com.revature.ers.dtos.requests;

import com.revature.ers.models.User;

public class RejectUser {
    private String username;

    public RejectUser(){

    }

    public RejectUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User extractUser(){
        User rejectUser = new User();
        rejectUser.setUsername(this.getUsername());
        rejectUser.setActive(false);
        rejectUser.setRole_id("Deactivated");
        return rejectUser;
    }
}
