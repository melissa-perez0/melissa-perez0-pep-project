package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * Method defines the structure of the Javalin Account API. Javalin methods will
     * use handler methods
     * to manipulate the Context object, which is a special object provided by
     * Javalin which contains information about
     * HTTP requests and can generate responses. There is no need to change anything
     * in this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountRegistration);
        app.post("/login", this::postAccountLogin);
        app.get("/messages", this::getMessages);
        app.post("/messages", this::postCreateMessage);
        app.get("/messages/{message_id}", this::getMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getAccountMessages);
        return app;
    }

    /**
     * Handler to register a new account.
     * 
     * @param context
     * 
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void postAccountRegistration(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        // check username and password requirements
        boolean isValidUsernamePassword = validateUsernamePassword(account.getUsername(), account.getPassword());
        if (!isValidUsernamePassword) {
            context.status(400);
            return;
        }
        // determine the status code from account insert
        Account registeredUser = accountService.addAccount(account);
        if (registeredUser == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(registeredUser));
        }
    }

    /**
     * Handler to login an account.
     * 
     * @param context
     * 
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void postAccountLogin(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);

        Account loggedUser = accountService.getAccount(account);
        if (loggedUser == null) {
            context.status(401);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(loggedUser));
        }
    }

    /**
     * Validate username and password requirements.
     * username must not be blank.
     * password is at least 4 characters long.
     * username must not be taken -- is validated in service layer.
     * 
     * @param context
     * 
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private boolean validateUsernamePassword(String username, String password) {
        final int passwordMinLength = 4;
        if (username == null || username.trim().isEmpty()) {
            return false;
        } else if (password == null || password.length() < passwordMinLength) {
            return false;
        }
        return true;
    }

    /**
     * Handler to create a new message.
     * 
     * @param context
     * 
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void postCreateMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);

        // check message_text requirements
        boolean isValidMessageText = validateMessage(message.getMessage_text());
        if (!isValidMessageText) {
            context.status(400);
            return;
        }

        Account account = accountService.getAccountId(message.getPosted_by());
        if (account == null) {
            context.status(400);
            return;
        }

        // determine the status code from message insert
        Message newMessage = messageService.addMessage(message);
        if (messageService == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(newMessage));
        }
    }

    /**
     * Validate message_text requirements.
     * message_text must not be blank.
     * message_text is not longer than 255 characters.
     * message_text must be posted by a real, existing user.
     * 
     * @param text
     * 
     * @return boolean
     */
    private boolean validateMessage(String text) {
        final int messageMaxLength = 255;

        if (text == null || text.trim().isEmpty()) {
            return false;
        } else if (text.length() > messageMaxLength) {

            return false;
        }
        return true;
    }

    /**
     * Handler to retrieve all messages.
     * 
     * @param context the context object handles information HTTP requests and
     *                generates responses within Javalin. It will
     *                be available to this method automatically thanks to the
     *                app.put method.
     */
    private void getMessages(Context context) {
        context.json(messageService.getAllMessages());
    }

    /**
     * Handler to retrieve a message.
     * 
     * @param context the context object handles information HTTP requests and
     *                generates responses within Javalin. It will
     *                be available to this method automatically thanks to the
     *                app.put method.
     */
    private void getMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messageFound = messageService.getMessage(message_id);
        if (messageFound != null) {
            context.status(200).json(mapper.writeValueAsString(messageFound));
        } else {
            context.status(200);

        }
    }

    /**
     * Handler to delete a message.
     * 
     * @param context the context object handles information HTTP requests and
     *                generates responses within Javalin. It will
     *                be available to this method automatically thanks to the
     *                app.put method.
     */
    private void deleteMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message messageFound = messageService.deleteMessage(message_id);
        if (messageFound != null) {
            context.status(200).json(mapper.writeValueAsString(messageFound));
        } else {
            context.status(200).result("");

        }
    }

    /**
     * Handler to update a message.
     * 
     * @param context the context object handles information HTTP requests and
     *                generates responses within Javalin. It will
     *                be available to this method automatically thanks to the
     *                app.put method.
     */
    private void updateMessage(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = mapper.readValue(context.body(), Message.class);

        // check message_text requirements
        boolean isValidMessageText = validateMessage(message.getMessage_text());
        if (!isValidMessageText) {
            context.status(400);
            return;
        }
        // determine the status code from message update
        Message messageUpdated = messageService.updateMessage(message_id, message.getMessage_text());
        if (messageUpdated == null) {
            context.status(400);
        } else {
            context.status(200);
            context.json(mapper.writeValueAsString(messageUpdated));
        }

    }

    /**
     * Handler to retrieve a account messages.
     * 
     * @param context the context object handles information HTTP requests and
     *                generates responses within Javalin. It will
     *                be available to this method automatically thanks to the
     *                app.put method.
     */
    private void getAccountMessages(Context context) throws JsonProcessingException {
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.status(200).json(accountService.getAccountMessages(account_id));
       
    }
}