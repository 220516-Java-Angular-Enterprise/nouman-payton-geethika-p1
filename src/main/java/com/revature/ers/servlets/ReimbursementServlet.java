package com.revature.ers.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.dtos.requests.ApproveNewUser;
import com.revature.ers.dtos.requests.LoginRequest;
import com.revature.ers.dtos.requests.NewReimbursementRequest;
import com.revature.ers.dtos.requests.RejectUser;
import com.revature.ers.dtos.responses.Principal;
import com.revature.ers.models.Reimbursements;
import com.revature.ers.services.ReimbursementsServices;
import com.revature.ers.services.TokenService;
import com.revature.ers.services.UserService;
import com.revature.ers.util.annotations.Inject;
import com.revature.ers.util.custom_exceptions.InvalidRequestException;
import com.revature.ers.util.custom_exceptions.NotAuthorizedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ReimbursementServlet  extends HttpServlet {

    @Inject
    private final ObjectMapper mapper;
    private ReimbursementsServices reimbursementsServices;
    private final TokenService tokenService;

    @Inject
    public ReimbursementServlet(ObjectMapper mapper, ReimbursementsServices reimbursementsServices, TokenService tokenService) {
        this.mapper = mapper;
        this.reimbursementsServices = reimbursementsServices;
        this.tokenService = tokenService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            NewReimbursementRequest request = mapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if(requester == null){
                throw new NotAuthorizedException("User is not authorized");
            }

            if(!requester.getRole().equals("DEFAULT")){
                resp.setStatus(403);
                throw new NotAuthorizedException("User is not authorized");
            }

            Reimbursements newReimbursement = reimbursementsServices.register(request, requester.getId());
            resp.setStatus(201);
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(newReimbursement.getReimb_id()));
        }catch (NotAuthorizedException e){
            resp.setStatus(401);
            resp.getWriter().write(e.getMessage());
        } catch (InvalidRequestException e) {
            resp.setStatus(404);
            resp.getWriter().write(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

            if (requester.getRole().equals("Deactivated") || requester.getRole().equals("ADMIN")) {
                throw new NotAuthorizedException("Not authorized to view this page");
            }

            String[] uris = req.getRequestURI().split("/");


        } catch (InvalidRequestException e) {
            resp.setStatus(400);
            resp.getWriter().write(e.getMessage());
        } catch (NotAuthorizedException e) {
            resp.setStatus(401);
            resp.getWriter().write(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Principal requester = tokenService.extractRequesterDetails(req.getHeader("Authorization"));

        if (requester == null) {
            resp.setStatus(401);
            return;
        }
        if (!requester.getRole().equals("MANAGER")) {
            resp.setStatus(403);
            resp.getWriter().write("<h1>You are not authorized</h1>");
            return;
        }

        String[] uris = req.getRequestURI().split("/");
        if(uris.length == 3) {
            List<Reimbursements> reimbursements = reimbursementsServices.allPending();
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(reimbursements));
        }
    }
}
