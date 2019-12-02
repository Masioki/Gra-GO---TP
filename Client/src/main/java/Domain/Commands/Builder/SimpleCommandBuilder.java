package Domain.Commands.Builder;

import Domain.Commands.Command;
import Domain.Commands.CommandType;
import com.fasterxml.jackson.databind.ObjectMapper;

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


    public SimpleCommandBuilder withBody(Object body) throws Exception {
        if (command != null) {
            command.setBody(objectMapper.writeValueAsString(body));
        }
        return this;
    }


    public Command build() {
        return command;
    }
}
