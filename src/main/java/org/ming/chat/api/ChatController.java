package org.ming.chat.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.ming.chat.model.Message;
import org.ming.chat.store.SessionStore;
import org.ming.chat.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;

@RestController
public class ChatController {
    private static Logger log = LoggerFactory.getLogger(ChatController.class);

    public ChatController() {
        GsonBuilder builder = new GsonBuilder();
        this.gson = builder.create();
        this.sessionStore = SessionStore.buildSessionStore();
    }

    protected Gson gson;
    protected SessionStore sessionStore;

    @PostMapping("send")
    String send(Message msg) {
        msg.setId(RandomUtils.nextLong());
//        msg.setCategory("");
        String messageJsonString = gson.toJson(msg);
        log.debug(messageJsonString);

        Session session = sessionStore.get(msg.getTo());

        if (session != null) {
            sessionStore.send(session, messageJsonString);

            return "success";
        } else {
            return "No such user";
        }

    }
}
