package DTO.Commands;

import java.io.Serializable;
import java.util.UUID;

public class Command implements Serializable {

    /*
    LOGIN - login, haslo w body
    ACTIVE_GAMES - puste body, zwraca liste aktywnych gier
    GAME - GameCommand w body
    ERROR - wiadomosc bledu w body
    SUCCESS - zaleznie od zapytania
     */
    private UUID uuid;
    private CommandType type;
    private String body;

    public Command () {
        uuid = UUID.randomUUID();
    }

    public CommandType getType() {
        return type;
    }

    public void setType(CommandType type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


}
