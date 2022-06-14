package com.revature.ers.daos;

import com.revature.ers.models.Reimbursements;
import com.revature.ers.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReimbursementDAO implements CrudDAO<Reimbursements> {

    @Override
    public void save(Reimbursements obj) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_reimbursements (reimb_id, amount, submitted, resolved, description, receipt, payment_id, author_id, resolver_id, status_id, type_id) VALUES (?, ?, ?, ?, ?, NULL, ?, ?, ?, ?, ?)");
            ps.setString(1, obj.getReimb_id());
            ps.setDouble(2, obj.getAmount());
            ps.setTimestamp(3, obj.getSubmitted());
            ps.setTimestamp(4,obj.getResolved());
            ps.setString(5, obj.getDescription());
            ps.setString(6, obj.getPayment_id());
            ps.setString(7, obj.getAuthor_id());
            ps.setString(8, obj.getResolver_id());
            ps.setString(9, obj.getStatus_id());
            ps.setString(10, obj.getType_id());
            ps.executeUpdate();
        } catch (SQLException e){
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void update(Reimbursements obj) {

        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("Update ers_reimbursements SET amount = ?, submitted = ?, resolved = ?, description = ?, receipt = ?, payment_id = ?, author_id = ?, resolver_id = ?, status_id = ?, type_id = ? WHERE reimb_id = ?");
            ps.setString(11, obj.getReimb_id());
            ps.setDouble(1, obj.getAmount());
            ps.setTimestamp(2, obj.getSubmitted());
            ps.setTimestamp(3,obj.getResolved());
            ps.setString(4, obj.getDescription());
            ps.setBlob(5, obj.getReceipt());
            ps.setString(6, obj.getPayment_id());
            ps.setString(7, obj.getAuthor_id());
            ps.setString(8, obj.getResolver_id());
            ps.setString(9, obj.getStatus_id());
            ps.setString(10, obj.getType_id());
            ps.executeUpdate();
        } catch (SQLException e){
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void delete(String id) {
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_reimbursements where reimb_id = ?");
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Reimbursements getById(String id) {
        return null;
    }

    public List<String> getAllReimbursementTypes() {
        List<String> reimbursementTypes = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT type_id FROM ers_reimbursement_types");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                reimbursementTypes.add(rs.getString("type_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return reimbursementTypes;
    }

    @Override
    public List<Reimbursements> getAll() {
        List<Reimbursements> reimbursements = new ArrayList<>();

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_reimbursements");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Reimbursements reimburse = new Reimbursements(rs.getString("reimb_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("submitted"),
                        rs.getTimestamp("resolved"),
                        rs.getString("description"),
                        rs.getBlob("receipt"),
                        rs.getString("payment_id"),
                        rs.getString("author_id"),
                        rs.getString("resolver_id"),
                        rs.getString("status_id"),
                        rs.getString("type_id"));
                reimbursements.add(reimburse);
            }
        } catch (SQLException e) {
            //Need to create a custom sql exception throw to UserService. UserService should handle error logging.
            throw new RuntimeException(e.getMessage());
        }
        return reimbursements;
    }

    }
