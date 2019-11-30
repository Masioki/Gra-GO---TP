package Services;

import Commands.Builder.CommandBuilderProvider;
import Commands.Command;
import Commands.CommandParser;
import Commands.CommandType;
import Commands.GameCommandType;
import Domain.GameData;
import Domain.LoginData;

import java.util.ArrayList;
import java.util.List;

//Class Service should be singleton
//Wzorzec Facade
public class Service implements CommandListener {

    private static Service service;
    private ServiceInvoker invoker;
    private CommandParser parser;
    private Command commandInProgress;

    /**
     * Private Constructor
     */
    private Service() {
        parser = new CommandParser();
    }


    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }

    private void sendBasicCommand(Command c) {
        try {
            invoker.send(c);
        } catch (Exception e) {
            errorHandler("Connection error");
        }
    }

    @Override
    public void execute(Command command) {
        /*if (command.getType() == CommandType.SUCCESS) {
            executeCommand(command.getBody());
        } else if (command.getType() == CommandType.ERROR) {
            commandInProgress = null;
            errorHandler(parser.parseErrorCommand(command.getBody()));
        }*/
    }

    public void setServiceInvoker(ServiceInvoker invoker) {
        this.invoker = invoker;
    }


    /*
           LOGIKA
     */

    private void executeCommand(String responseBody) {
        if (commandInProgress != null) {

        }
    }

    public void errorHandler(String errorMessage) {
        //TODO: error handling
    }

    public void loadActiveGames() {
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.ACTIVE_GAMES)
                .build();

    }

    public void signUp(String login, String password) {
        //TODO: basic check
        LoginData loginData = new LoginData();
        loginData.setUsername(login);
        loginData.setPassword(password);
/*
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.LOGIN)
                .withBody(loginData)
                .build();

        return sendBasicCommand(c);*/
    }

    /*
     Przyjmuje koordynaty przy komendzie MOVE.
     Przy SURRENDER, PASS, CONTINUE koordynaty sa obojetne.
     */
    public void gameMove(int x, int y, GameCommandType type) {
        //TODO: basic check
        /*Command c = CommandBuilderProvider
                .newGameCommandBuilder()
                .newCommand()
                .withHeader(GameCommandType.MOVE)
                .withPosition(x, y)
                .build();

        return sendBasicCommand(c);
         */
    }

    public void joinGame(GameData gameData) {
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .withHeader(CommandType.JOIN)
                .build();
        sendBasicCommand(c);
    }


}
