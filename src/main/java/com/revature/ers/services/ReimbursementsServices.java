package com.revature.ers.services;

import com.revature.ers.daos.ReimbursementDAO;
import com.revature.ers.daos.ReimbursementsTypesDAO;
import com.revature.ers.dtos.requests.NewReimbursementRequest;
import com.revature.ers.dtos.requests.SolveReimbursement;
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

    private boolean isValidType(String reimburseType){

        List<String> reimbursementType = reimbursementDAO.getAllReimbursementTypes();
        for(int i = 0; i < reimbursementType.size(); i++){
            if(reimbursementType.contains(reimburseType)){
                return true;
            }
        }
        return false;
    }

    public List<Reimbursements> allPending(){
        return reimbursementDAO.allPending();
    }


    public Reimbursements register(NewReimbursementRequest request, String author_id){
        Reimbursements reimburse = request.extractReimbursement();
        reimburse.setAuthor_id(author_id);
        if(isValidType(request.getType_id())){
            reimburse.setReimb_id(UUID.randomUUID().toString());
            reimburse.setSubmitted(new Timestamp(System.currentTimeMillis()));
            reimbursementDAO.save(reimburse);
        } else throw new InvalidRequestException("Invalid reimbursement type");
        return reimburse;
    }


}

