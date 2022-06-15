package com.revature.ers.daos;

import com.revature.ers.models.ReimbursementTypes;
import com.revature.ers.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReimbursementsTypesDAO implements CrudDAO<ReimbursementTypes>{


    @Override
    public void save(ReimbursementTypes obj) {

    }

    @Override
    public void update(ReimbursementTypes obj) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public ReimbursementTypes getById(String id) throws SQLException {
        return null;
    }

    @Override
    public List<ReimbursementTypes> getAll() {
        return null;
    }

}
