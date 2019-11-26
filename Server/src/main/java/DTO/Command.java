package DTO;

import java.io.Serializable;

public class Command implements Serializable {

    /*
    LOGIN - login, haslo w body
    ACTIVE_GAMES - puste body, zwraca liste aktywnych gier
    GAME - GameCommand w body
    ERROR - wiadomosc bledu w body
    SUCCESS - zaleznie od zapytania
     */
    private CommandType type;
    private String body;


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
}
