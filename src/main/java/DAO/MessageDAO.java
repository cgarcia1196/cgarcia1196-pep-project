package DAO;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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
}
