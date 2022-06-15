package com.revature.ers.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.dtos.requests.ApproveNewUser;
import com.revature.ers.dtos.requests.NewUserRequest;
import com.revature.ers.dtos.requests.RejectUser;
import com.revature.ers.dtos.requests.ResetUserPassword;
import com.revature.ers.dtos.responses.Principal;
import com.revature.ers.models.User;
import com.revature.ers.services.TokenService;
import com.revature.ers.services.UserService;
import com.revature.ers.util.annotations.Inject;
import com.revature.ers.util.custom_exceptions.InvalidRequestException;
import com.revature.ers.util.custom_exceptions.ResourceConflictException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {

    @Inject
    private final ObjectMapper mapper;
    private final UserService userService;
    private final TokenService tokenService;

    @Inject

    public UserServlet(ObjectMapper mapper, UserService userService,  TokenService tokenService) {
        this.mapper = mapper;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NewUserRequest request = mapper.readValue(req.getInputStream(), NewUserRequest.class);
            User createdUser = userService.register(request);
            resp.setStatus(201);
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(createdUser.getUser_id()));
        } catch (InvalidRequestException e) {
            resp.setStatus(404);
        } catch (ResourceConflictException e) {
            resp.setStatus(409);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if(requester ==null){
            resp.setStatus(401);
            return;
        }
        if(!requester.getRole().equals("ADMIN")){
            resp.setStatus(403);
            return;
        }

        List<User> users = userService.getAllUsers();
        resp.setContentType("application/json");
        resp.getWriter().write(mapper.writeValueAsString(users));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));


        if(requester == null){
            resp.setContentType("application/html");
            resp.getWriter().write("<h1>403</h1>");
            resp.setStatus(403);
            return;
        }

        if (!requester.getRole().equals("ADMIN")){
            resp.getWriter().write("<h1>Access Denied</h1>");
            resp.setStatus(403);
            return;
        }

        String[] uris = req.getRequestURI().split("/");

        if(uris.length >= 4 && uris[3].equals("reset")){

            ResetUserPassword request = mapper.readValue(req.getInputStream(), ResetUserPassword.class);

            String password = String.valueOf(userService.randomPassword());
            userService.changePass(request, password );
            resp.setContentType("application/html");
            resp.getWriter().write("<h2> New password is " + password);
            resp.setStatus(202);
        }

        if(uris.length >= 4 && uris[3].equals("approve")){
            ApproveNewUser request = mapper.readValue(req.getInputStream(), ApproveNewUser.class);
            userService.approveUser(request);
            resp.setContentType("application/html");
            resp.setStatus(202);
        }

        if(uris.length >= 4 && uris[3].equals("reject")){
            RejectUser request = mapper.readValue(req.getInputStream(), RejectUser.class);
            userService.reject(request);

            resp.setContentType("application/html");
            resp.getWriter().write("<h2>" + request.getUsername() + " account has been deactivated </h2>");
            resp.setStatus(202);

        }
    }

}
