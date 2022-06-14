package com.revature.ers.daos;

import com.revature.ers.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReimbursementsTypes implements CrudDAO<ReimbursementsTypes>{

    @Override
    public void save(ReimbursementsTypes obj) {

    }

    @Override
    public void update(ReimbursementsTypes obj) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public ReimbursementsTypes getById(String id){
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursement_types");
            ResultSet rs = ps.executeQuery();
        } catch(SQLException e){
            throw new SQLException("Didnt able to get data from database");
        }
    }

    @Override
    public List<ReimbursementsTypes> getAll() {
        return null;
    }
}
