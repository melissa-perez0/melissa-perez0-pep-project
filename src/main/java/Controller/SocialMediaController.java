package Controller;

import Model.Account;
import Service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public SocialMediaController() {
        accountService = new AccountService();
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
        // post endpoint on /register
        app.post("/register", this::postAccountRegistration);

        return app;
    }

    /**
     * Handler to register a new user.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST
     * request into an Account object.
     * If accountService returns a null account (meaning registering an account was
     * unsuccessful, the API will return a 400
     * message (client error).
     * 
     * @param context the context object handles information HTTP requests and
     *                generates responses within Javalin. It will
     *                be available to this method automatically thanks to the
     *                app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue
     *                                 converting JSON into an object.
     */
    private void postAccountRegistration(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredUser = accountService.addAccount(account);
        if (registeredUser == null) {
            context.status(400);
        } else {
            context.json(mapper.writeValueAsString(registeredUser));
        }
    }

}