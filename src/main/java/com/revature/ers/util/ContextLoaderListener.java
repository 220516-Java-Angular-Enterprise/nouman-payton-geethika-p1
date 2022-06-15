package com.revature.ers.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ers.daos.ReimbursementDAO;
import com.revature.ers.daos.UserDAO;
import com.revature.ers.services.ReimbursementsServices;
import com.revature.ers.services.TokenService;
import com.revature.ers.services.UserService;
import com.revature.ers.servlets.AuthServlet;
import com.revature.ers.servlets.ReimbursementServlet;
import com.revature.ers.servlets.UserServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("\nInitializing ers web application");

        ObjectMapper mapper = new ObjectMapper();

        UserServlet userServlet = new UserServlet(mapper, new UserService(new UserDAO()), new TokenService(new JwtConfig()));
        AuthServlet authServlet = new AuthServlet(mapper, new UserService(new UserDAO()), new TokenService(new JwtConfig()));
        ReimbursementServlet reimbursementServlet = new ReimbursementServlet(mapper, new ReimbursementsServices(new ReimbursementDAO()), new TokenService(new JwtConfig()));

        ServletContext context = sce.getServletContext();
        context.addServlet("UserServlet", userServlet).addMapping("/users/*");
        context.addServlet("AuthServlet", authServlet).addMapping("/auth");
        context.addServlet("ReimbursementServlet", reimbursementServlet).addMapping("/reimbursement");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down ers web application");
    }
}

