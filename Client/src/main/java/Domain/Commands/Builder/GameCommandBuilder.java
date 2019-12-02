package Domain.Commands.Builder;

import Domain.Commands.Command;
import Domain.Commands.CommandType;
import Domain.Commands.GameCommand;
import Domain.Commands.GameCommandType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameCommandBuilder {

    private ObjectMapper objectMapper;
    private Command command;
    private GameCommand gameCommand;


    public GameCommandBuilder() {
        objectMapper = new ObjectMapper();
    }


    public GameCommandBuilder newCommand() {
        command = new Command();
        command.setType(CommandType.GAME);
        gameCommand = new GameCommand();
        return this;
    }


    public GameCommandBuilder withHeader(GameCommandType header) {
        if (gameCommand != null) {
            gameCommand.setCommandType(header);
        }
        return this;
    }


    public GameCommandBuilder withPosition(int x, int y) {
        if (command != null && gameCommand != null) {
            gameCommand.setX(x);
            gameCommand.setY(y);
        }
        return this;
    }


    public Command build() throws Exception {
        if (command != null) {
            command.setBody(objectMapper.writeValueAsString(gameCommand));
        }
        return command;
    }
}
