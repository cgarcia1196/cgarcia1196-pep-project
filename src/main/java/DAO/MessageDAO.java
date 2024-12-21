package DAO;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2.command.Prepared;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    public Message insertMessage(Message msg){
        Connection conn = ConnectionUtil.getConnection();
        //sql
        String sql = "INSERT INTO MESSAGE(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?);";
        try {
            //prepared statement
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());

            ps.executeUpdate();

            //return message with generated id
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int generatedId = rs.getInt(1);
                return new Message(generatedId, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        List<Message> allMessages = new ArrayList<>();

        Connection conn = ConnectionUtil.getConnection();

        //sql
        String sql = "SELECT * FROM message;";
        //prepared statement
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            //return list of messages if any
            while(rs.next()){
                allMessages.add(new Message(
                    rs.getInt("message_id"), 
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    ));
            }
            return allMessages;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allMessages;
    }

    public Message getMessageById(int id){
        Message message = null;
        Connection conn = ConnectionUtil.getConnection();
        //sql
        String sql = "SELECT * FROM message WHERE message_id = ?;";
        //prepared statement
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public Message deleteMessage(int id){
        Message message = null;
        Connection conn = ConnectionUtil.getConnection();
        //sql
        String sql = "DELETE FROM  message WHERE message_id = ?;";
        //sql for getting message
        String sqlGet = "SELECT * FROM message WHERE message_id = ?;";

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            PreparedStatement psGet = conn.prepareStatement(sqlGet);
            ps.setInt(1,1);
            psGet.setInt(1,id);

            ResultSet rs = psGet.executeQuery();
            if(rs.next()){
                message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                ps.executeUpdate();

            }   
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        };
        

        return message;
    }

    public Message updateMessage(int id, Message message){
        Message fullMessage  = null;
        Connection conn = ConnectionUtil.getConnection();
        //sql 
        String sqlSelect = "SELECT * FROM message WHERE message_id = ?;";
        String sqlUpdate = "UPDATE message SET message_text = ? WHERE message_id = ?;";

        //prepared statements

        try {
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, message.getMessage_text());
            psUpdate.setInt(2, id);

            int rowsUpdated = psUpdate.executeUpdate();
            //check if message was changed
            if(rowsUpdated > 0){
                //get updated message
                PreparedStatement psSelect = conn.prepareStatement(sqlSelect);
                psSelect.setInt(1, id);
                ResultSet rs = psSelect.executeQuery();
                while(rs.next()){
                    fullMessage = new Message(
                        rs.getInt("message_id"), 
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                        );
                } 
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return fullMessage;
    }
}
