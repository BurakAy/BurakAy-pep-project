package Controller;

import static org.mockito.ArgumentMatchers.contains;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Message;
import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getOneMessageHandler);
        app.get("accounts/{account_id}/messages", this::getAllUserMessagesHandler);
        app.post("register", this::registrationHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::postMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void getAllMessagesHandler(Context context) {
        context.json(messageService.getAllMessages());
    }

    private void getOneMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message oneMessage = messageService.getMessageByID(message_id);

        if (oneMessage == null) {
            context.res();
        } else {
            context.json(mapper.writeValueAsString(oneMessage));
        }
    }

    private void getAllUserMessagesHandler(Context context) {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(messageService.getAllAccountMessages(account_id));
    }

    private void registrationHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account newAccount;

        // Validating passed in data before attempting to create an account
        if (account.username != null && !account.username.isEmpty() && account.password.length() >= 4) {
            newAccount = accountService.createAccount(account);
            if (newAccount == null) {
                context.status(400);
            } else {
                context.json(mapper.writeValueAsString(newAccount));
            }
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account verifiedAccount = accountService.verifyLogin(account);

        if (verifiedAccount == null) {
            context.status(401);
        } else {
            context.json(mapper.writeValueAsString(verifiedAccount));
        }
    }

    private void postMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message newMessage;

        // Validating passed in data before attempting to create an account
        if (message.message_text != null && !message.message_text.isEmpty() && message.message_text.length() < 255) {
            newMessage = messageService.createMessage(message);
            if (newMessage == null) {
                context.status(400);
            } else {
                context.json(mapper.writeValueAsString(newMessage));
            }
        } else {
            context.status(400);
        }
    }

    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messageToDelete = messageService.getMessageByID(message_id);
        int wasDeleted = messageService.deleteMessage(message_id);

        // if a message was deleted, 1 is returned for row count by executeUpdate from
        // MessageDAO
        if (wasDeleted == 1) {
            context.json(mapper.writeValueAsString(messageToDelete));
        }
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message.message_text);

        if (updatedMessage == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(updatedMessage));
        }
    }
}