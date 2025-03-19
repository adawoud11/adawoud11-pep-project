package Controller;

import DTO.UpdateMessageTextDTO;
import Model.Account;
import Model.Message;
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
    AccountService accountService = new AccountService();
    MessageService messageService = new MessageService();

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
        // Account endpoints
        app.post("register", this::createAccount);
        app.post("login", this::login);
        // Message endpoints

        app.post("messages", this::createMessage);
        app.patch("messages/{msgId}", this::updateMessage);
        app.delete("messages/{msgId}", this::deleteMessage);
        app.get("messages", this::getAllMessages);
        app.get("messages/{msgId}", this::getMessageById);
        app.get("accounts/{accountId}/messages", this::getAccountMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */

    // Account helpers
    private void createAccount(Context ctx) {
        try {
            Account account = accountService.registerUser(ctx.bodyAsClass(Account.class));
            ctx.json(account, Account.class);
            ctx.status(200);

        } catch (Exception e) {
            ctx.status(400);
        }

    }

    private void login(Context ctx) {
        try {
            Account account = accountService.userlogin(ctx.bodyAsClass(Account.class));
            ctx.json(account).status(200);

        } catch (Exception e) {
            ctx.status(401);
        }
    }

    // Message helpers
    private void createMessage(Context ctx) {
        try {

            Message newMsg = messageService.createMessage(ctx.bodyAsClass(Message.class));
            ctx.json(newMsg).status(200);
        } catch (Exception e) {
            ctx.status(400);
        }
    }

    private void updateMessage(Context ctx) {
        try {
            Integer msgId = Integer.parseInt(ctx.pathParam("msgId"));
            UpdateMessageTextDTO msgDto = ctx.bodyAsClass(UpdateMessageTextDTO.class);
            Message updateMessage = messageService.updateMessage(msgDto.message_text, msgId);
            if (null != updateMessage)
                ctx.json(updateMessage).status(200);
            else
                ctx.status(400);
        } catch (Exception e) {
            ctx.status(400);
        }
    }

    private void deleteMessage(Context ctx) {
        try {
            Integer msgId = Integer.parseInt(ctx.pathParam("msgId"));
            Message msg = messageService.deleteMessageById(msgId);
            if (null != msg && msg.getMessage_id() > 0)
                ctx.json(msg);
            else
                ctx.result();
            ctx.status(200);
        } catch (NumberFormatException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception ex) {
            ctx.status(500).result(ex.getMessage());
        }

    }

    private void getAllMessages(Context ctx) {
        try {
            ctx.json(messageService.getAllMessages()).status(200);
        } catch (Exception e) {
            ctx.status(500);
        }

    }

    private void getMessageById(Context ctx) {
        try {
            Integer msgId = Integer.parseInt(ctx.pathParam("msgId"));
            Message msg = messageService.getMessageById(msgId);
            if (null != msg && msg.getMessage_id() > 0)
                ctx.json(msg);
            else
                ctx.result();
            ctx.status(200);

        } catch (NumberFormatException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            ctx.status(500).result(e.getMessage());
        }
    }

    private void getAccountMessages(Context ctx) {
        try {
            Integer accountId = Integer.parseInt(ctx.pathParam("accountId"));
            ctx.status(200).json(messageService.getMessagesByAccountId(accountId));
        } catch (NumberFormatException e) {
            ctx.status(400).result(e.getMessage());
        } catch (Exception ex) {
            ctx.status(500).result(ex.getMessage());
        }

    }
}