package DAO;

import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;

public class AccountDAO {

    //returns null if no username exists
    public Account getAccountByUsername(String username){
        Connection con = ConnectionUtil.getConnection();
        try {
            //sql
            String sql = "SELECT * FROM account WHERE username = ?";
            //prepared statements
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs =  ps.executeQuery();
            //return account if exists
            if(rs.next()){
                return new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //return null if no account exists with username
        return null;
    }

    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try{
            //sql logic
            String sql = "INSERT INTO account(username, password) VALUES(?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //prepared statements
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();
            //return account with generated id
            ResultSet rs  = ps.getGeneratedKeys();
            if(rs.next()){
                int generatedId = rs.getInt(1);
                return new Account(generatedId, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }
    
}
