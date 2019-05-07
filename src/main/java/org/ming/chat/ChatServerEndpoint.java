package org.ming.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ming.chat.model.Message;
import org.ming.chat.store.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/session/{username}")
public class ChatServerEndpoint {
    private static Logger log = LoggerFactory.getLogger(ChatServerEndpoint.class);
    protected Gson gson;
    protected SessionStore sessionStore;

    public ChatServerEndpoint() {
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();

        this.sessionStore = SessionStore.buildSessionStore();
    }

    @OnOpen
    public void onOpen(
            @PathParam("username") String username,
            Session session) {
        log.info("Here comes new user: " + username);

        sessionStore.put(username, session);
    }

    @OnMessage
    public void onMessage(
            @PathParam("username") String username,
            Session session,
            String message) {
        Message msg = gson.fromJson(message, Message.class);
        if (!sessionStore.containsKey(msg.getTo())) {
            // This user is off line

            return;
        }

        sessionStore.send(sessionStore.get(msg.getTo()), message);
    }
}
