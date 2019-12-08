package Commands;

import DTO.GameData;
import DTO.LoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommandParser {

    private ObjectMapper objectMapper;


    public CommandParser() {
        objectMapper = new ObjectMapper();
    }


    public GameData parseGameData(String body) throws Exception {
        return objectMapper.readValue(body, GameData.class);
    }

    public LoginData parseLoginCommand(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, LoginData.class);
    }

    public GameCommand parseGameCommand(String body) throws JsonProcessingException {
        return objectMapper.readValue(body, GameCommand.class);
    }
}
