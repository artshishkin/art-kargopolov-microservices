package net.shyshkin.study.app.ws.shared;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Utils {

    public UUID generateUserId() {
        return UUID.randomUUID();
    }
}
