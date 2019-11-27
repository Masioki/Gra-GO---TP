package Commands;

import Domain.LoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandParser {

    private ObjectMapper objectMapper;


    public CommandParser() {
        objectMapper = new ObjectMapper();
    }


    public LoginData parseLoginCommand(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, LoginData.class);
    }

    public GameCommand parseGameCommand(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, GameCommand.class);
    }
}
