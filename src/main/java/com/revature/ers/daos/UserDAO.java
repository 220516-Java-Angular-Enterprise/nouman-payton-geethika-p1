package com.revature.ers.daos;

import com.revature.ers.dtos.requests.ResetUserPassword;
import com.revature.ers.models.Reimbursements;
import com.revature.ers.models.User;
import com.revature.ers.util.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements CrudDAO<User>{

    @Override
    public void save(User user) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("INSERT INTO ers_users (user_id, username, email, password, given_name, surname, is_active, role_id) VALUES (?, ?, ?, crypt(?, gen_salt('bf')), ?, ?, ?, ?)");
            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5,user.getGiven_name());
            ps.setString(6, user.getSurname());
            ps.setBoolean(7, user.isActive());
            ps.setString(8, user.getRole_id());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("Update ers_users SET username = ?, email = ?, PASSWORD = ?, given_name = ?, surname = ?, is_active = ?, role_id = ? WHERE user_id = ?");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4,user.getGiven_name());
            ps.setString(5, user.getSurname());
            ps.setBoolean(6, user.isActive());
            ps.setString(7, user.getRole_id());
            ps.setString(8, user.getUser_id());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(String id) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ers_users where user_id = ?");
            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;

        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users WHERE username = ? AND password = crypt(?, password)");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                user = new User(rs.getString("username"), rs.getString("user_id"), rs.getString("role_id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return user;
    }

    public List<String> getAllUsernames(){
        List<String> usernames = new ArrayList<>();
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("select username from ers_users");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                usernames.add(rs.getString("username"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return usernames;
    }



    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {

        List<User> users = new ArrayList<>();
        try (Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ers_users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setUser_id(rs.getString("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setGiven_name(rs.getString("given_name"));
                user.setSurname(rs.getString("surname"));
                user.setActive(rs.getBoolean("is_active"));
                user.setRole_id(rs.getString("role_id"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return users;
    }

    public void setIsActive(User user) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("Update ers_users SET is_active = ?, role_id = ? WHERE username = ?");
            ps.setBoolean(1, user.isActive());
            ps.setString(2, user.getRole_id());
            ps.setString(3, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void reject(User user) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("Update ers_users SET is_active = ?, role_id = ? WHERE username = ?");
            ps.setBoolean(1, user.isActive());
            ps.setString(2, user.getRole_id());
            ps.setString(3, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public void changePass(ResetUserPassword request, String pass) {
        try(Connection con = ConnectionFactory.getInstance().getConnection()){
            PreparedStatement ps = con.prepareStatement("Update ers_users SET password = crypt(?, password) WHERE username = ?");
            ps.setString(1, pass);
            ps.setString(2, request.getUsername());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
