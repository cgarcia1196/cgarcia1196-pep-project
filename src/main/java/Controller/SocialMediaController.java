package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;

    public SocialMediaController(){
        this.accountService = new AccountService();
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
        //todo
        //app.post("/messages", this::newMessageHandler);
        //app.get("/messages", this::getAllMessagesHandler);
        //app.get("/messages/{message_id}", this::getMessageByIdHandler);
        //app.delete("/messages/{message_id}", this::deleteMessageHandler);
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
}