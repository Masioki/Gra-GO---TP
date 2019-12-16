package Commands;

import Domain.GameData;
import Domain.LoginData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public LoginData parseLoginData(String body) throws Exception {
        return objectMapper.readValue(body, LoginData.class);
    }

    public Point parsePoint(String body) throws Exception {
        return objectMapper.readValue(body, Point.class);
    }
}
