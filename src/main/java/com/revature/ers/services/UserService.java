package com.revature.ers.services;

import com.revature.ers.daos.UserDAO;
import com.revature.ers.dtos.requests.*;
import com.revature.ers.models.User;
import com.revature.ers.models.UserRoles;
import com.revature.ers.util.annotations.Inject;
import com.revature.ers.util.custom_exceptions.AuthenticationException;
import com.revature.ers.util.custom_exceptions.InvalidRequestException;
import com.revature.ers.util.custom_exceptions.NotAuthorizedException;
import com.revature.ers.util.custom_exceptions.ResourceConflictException;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class UserService{

    @Inject
    private final UserDAO userDAO;

    @Inject
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(LoginRequest request) {
        User user = new User();
        if (!isValidUsername(request.getUsername()) || !isValidPassword(request.getPassword())) throw new InvalidRequestException("Invalid username or password");
        user = userDAO.getUserByUsernameAndPassword(request.getUsername(), request.getPassword());

        if(user.getRole_id() == null){
            throw new NotAuthorizedException("Account creation pending");
        }
        if(user.getRole_id().equals("Deactivated")){
            throw new NotAuthorizedException("Not allowed to login");
        }
        return user;
    }

    public User register(NewUserRequest request) {
        User user = request.extractUser();

        if (isNotDuplicateUsername(user.getUsername())) {
            if (isValidUsername(user.getUsername())) {
                if (isValidPassword(user.getPassword())) {
                    user.setUser_id(UUID.randomUUID().toString());
                    user.setActive(false);
                    userDAO.save(user);
                } else throw new InvalidRequestException("Invalid password. Minimum eight characters, at least one letter, one number and one special character.");
            } else throw new InvalidRequestException("Invalid username. Username needs to be 8-20 characters long.");
        } else throw new ResourceConflictException("Username is already taken :(");

        return user;
    }

    private boolean isValidUsername(String username) {
        return username.matches("^(?=[a-zA-Z0-9._]{8,20}$)(?!.*[_.]{2})[^_.].*[^_.]$");
    }

    private boolean isNotDuplicateUsername(String username) {
        return !userDAO.getAllUsernames().contains(username);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }

    public List<User> getAllUsers(){
        return userDAO.getAll();
    }

    public void approveUser(ApproveNewUser user){
        userDAO.userIsActive(user.extractUser());
    }

    public void reject(RejectUser user) {
        userDAO.reject(user.extractUser());
    }

    public void changePass(ResetUserPassword request, String pass) {
        userDAO.changePass(request,pass);
    }

    public char[] randomPassword(){
        int length = 8;
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 4; i< length ; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }



}
