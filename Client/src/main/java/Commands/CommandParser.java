package Commands;

import Domain.GameData;
import Domain.LoginData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CommandParser {

    private ObjectMapper objectMapper;


    public CommandParser() {
        objectMapper = new ObjectMapper();
    }

    //TODO: more parsers

    public String parseErrorCommand(String body) throws Exception {
        return objectMapper.readValue(body, String.class);
    }


    public List<GameData> parseActiveGamesCommand(String body) throws Exception {
        return objectMapper.readValue(body, new TypeReference<List<GameData>>() {
        });
    }

    public GameCommand parseGameCommand(String body) throws Exception {
        return objectMapper.readValue(body, GameCommand.class);
    }
}
