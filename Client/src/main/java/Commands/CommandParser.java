package Commands;

import Domain.GameData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CommandParser {

    private ObjectMapper objectMapper;


    public CommandParser() {
        objectMapper = new ObjectMapper();
    }


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

    public GameData parseGameData(String body) throws Exception {
        return objectMapper.readValue(body, GameData.class);
    }
}
