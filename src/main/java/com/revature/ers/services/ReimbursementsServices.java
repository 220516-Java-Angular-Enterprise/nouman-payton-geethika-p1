package com.revature.ers.services;

import com.revature.ers.daos.ReimbursementDAO;
import com.revature.ers.dtos.requests.NewReimbursementRequest;
import com.revature.ers.models.Reimbursements;
import com.revature.ers.util.custom_exceptions.InvalidRequestException;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class ReimbursementsServices {

    private final ReimbursementDAO reimbursementDAO;

    public ReimbursementsServices(ReimbursementDAO reimbursementDAO) {
        this.reimbursementDAO = reimbursementDAO;
    }

    public Reimbursements register(NewReimbursementRequest request, String author_id){
        Reimbursements reimburse = request.extractReimbursement();
        reimburse.setAuthor_id(author_id);
        if(isValidReimbursemnetType(request.getType_id())){
            reimburse.setReimb_id(UUID.randomUUID().toString());
            reimburse.setSubmitted(new Timestamp(System.currentTimeMillis()));
            reimbursementDAO.save(reimburse);
        } else throw new InvalidRequestException("Invalid reimbursemnt type");
        return reimburse;
    }

    private boolean isValidReimbursemnetType(String reimType){
        List<String> reimbursementType = reimbursementDAO.getAllReimbursementTypes();
        for(String reqType : reimbursementType){
            if(reqType.equals(reimType)){
                return true;
            }
        }
        return false;
    }



}

