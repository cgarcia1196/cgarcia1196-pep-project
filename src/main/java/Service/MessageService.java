package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    public MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message msg){
        //check that the message text is not blank and less than 255 chars;
        if(!msg.getMessage_text().isBlank() && msg.getMessage_text().length() < 255){
            //insert message to database and return to controller
            return messageDAO.insertMessage(msg);
        }else{
            return null;
        }
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }
    
}
