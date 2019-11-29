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
public class Service implements ResponseListener {

    private static Service service;
    private ServerConnection serverConnection;
    private CommandParser parser;

    /**
     * Private Constructor
     */
    private Service() {
        parser = new CommandParser();
        try {
            serverConnection = new ServerConnection();
        } catch (Exception e) {
            //TODO: obsluga bledu polaczenia
        }
    }

    private boolean sendBasicCommand(Command c) {
        try {
            Command response = serverConnection.sendCommand(c);
            if (response.getType() == CommandType.ERROR) {
                errorHandler(parser.parseErrorCommand(response.getBody()));
            } else return response.getType() == CommandType.SUCCESS;
        } catch (Exception e) {
            errorHandler("Connection error");
        }
        return false;
    }

    public static Service getInstance() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }


    public void errorHandler(String errorMessage) {
        //TODO: error handling
    }

    public List<GameData> loadActiveGames() {
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.ACTIVE_GAMES)
                .build();
        try {
            Command response = serverConnection.sendCommand(c);
            if (response.getType() == CommandType.SUCCESS) {
                return parser.parseActiveGamesCommand(response.getBody());
            } else if (response.getType() == CommandType.ERROR) {
                errorHandler(parser.parseErrorCommand(response.getBody()));
            }
        } catch (Exception e) {
            errorHandler("Connection error");
        }
        return new ArrayList<>();
    }

    public boolean signUp(String login, String password) {
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
    return true;
    }

    public boolean gameMove(int x, int y) {
        //TODO: basic check
        /*Command c = CommandBuilderProvider
                .newGameCommandBuilder()
                .newCommand()
                .withHeader(GameCommandType.MOVE)
                .withPosition(x, y)
                .build();

        return sendBasicCommand(c);
         */
        return true;
    }

    public boolean joinGame(GameData gameData) {
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .withHeader(CommandType.JOIN)
                .build();
        return sendBasicCommand(c);
    }


    @Override
    public void recieve(Command command) {

    }
}
