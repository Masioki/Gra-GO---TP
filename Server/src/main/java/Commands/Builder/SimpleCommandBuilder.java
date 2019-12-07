package Commands.Builder;

import Commands.Command;
import Commands.CommandType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

public class SimpleCommandBuilder {

    private ObjectMapper objectMapper;
    private Command command;

    public SimpleCommandBuilder() {
        objectMapper = new ObjectMapper();
    }


    public SimpleCommandBuilder newCommand() {
        command = new Command();
        return this;
    }


    public SimpleCommandBuilder withHeader(CommandType header) {
        if (command != null) {
            command.setType(header);
        }
        return this;
    }

    public SimpleCommandBuilder withUUID(UUID uuid) {
        if (command != null) {
            command.setUuid(uuid);
        }
        return this;
    }

    public SimpleCommandBuilder withBody(Object body) {
        try {
            if (command != null) {
                command.setBody(objectMapper.writeValueAsString(body));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    public Command build() {
        return command;
    }
}
