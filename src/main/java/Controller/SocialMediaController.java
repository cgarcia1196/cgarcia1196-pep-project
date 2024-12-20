package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHander);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        //todo
        //
        //
        //
        //
        //app.patch("/messages/{message_id}", this::updateMessageHandler);
        //app.get("/accounts/{account_id}", this::getUserMessagesHandler);
        
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    private void registerHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addAccount(account);

        if(newAccount != null){
            ctx.json(mapper.writeValueAsString(newAccount)).status(200);
        }else{
            ctx.status(400);
        }

    }

    private void loginHander(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedInAccount = accountService.login(account);
        //login failed
        if(loggedInAccount == null){
            ctx.status(401);
        }
        //login OK
        else{
            ctx.json(mapper.writeValueAsString(loggedInAccount)).status(200);
        }
    }
    private void newMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.addMessage(message);
        //login failed
        if(createdMessage == null){
            ctx.status(400);
        }
        //login OK
        else{
            ctx.json(mapper.writeValueAsString(createdMessage)).status(200);
        }
    }
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        //get all mesages and save them to list
        List<Message> allMsgs;// = new ArrayList<>();
        allMsgs = messageService.getAllMessages();
        //return messages and set status 200:OK
        ctx.json(mapper.writeValueAsString(allMsgs)).status(200);
    }

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        //get id from path parameter
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if(message == null){
            ctx.status(200);
        }
        else{
            ctx.json(mapper.writeValueAsString(message)).status(200);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();

        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(id);
        if(message == null){
            ctx.status(200);
        }
        else{
            ctx.json(mapper.writeValueAsString(message)).status(200);
        }
    }
}