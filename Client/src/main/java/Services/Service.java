package Services;

import Commands.*;
import Commands.Builder.CommandBuilderProvider;
import Domain.GameData;
import Domain.LoginData;

//Class Service should be singleton
public class Service implements InvokableService {

    private static Service service;
    private ServiceInvoker invoker;
    private CommandParser parser;

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

    public void setServiceInvoker(ServiceInvoker invoker) {
        this.invoker = invoker;
    }


    @Override
    public void execute(Command request, Command response) {
        if (response.getType() == CommandType.SUCCESS) {
            executeOwn(request, response);
        } else if (response.getType() == CommandType.ERROR) {
            try {
                errorHandler(parser.parseErrorCommand(response.getBody()));
            } catch (Exception e) {
                errorHandler("Blad komendy oraz blad wewnetrzny");
            }
        } else if (request == null) {
            executeIncoming(response);
        }
    }

    private void sendCommand(Command c) {
        try {
            invoker.send(c);
        } catch (Exception e) {
            errorHandler("Connection error");
        }
    }


    /*
    KAZDY KONTROLER
     */
    private void errorHandler(String errorMessage) {
        //TODO: error handling
    }

    private void executeOwn(Command request, Command response) {
        try {
            switch (request.getType()) {
                case ACTIVE_GAMES: {
                    //TODO: wywolania zachowan w kontrolerze
                }
                case JOIN: {

                }
                case LOGIN: {

                }
                case GAME: {
                    // GameCommand game = parser.parseGameCommand(out);

                }
            }
        } catch (Exception e) {
            errorHandler("Sprobuj ponownie");
        }
    }

    private void executeIncoming(Command command) {

    }

    /*
    KAZDY KONTROLER
     */
    public void end() {
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.END_CONNECTION)
                .build();
        sendCommand(c);


        invoker.signalEnd();
    }

    /*
    KONTROLER LOGOWANIA
     */
    public void signUp(String login, String password) {
        if (login == null || password == null || login.length() == 0 || password.length() == 0) {
            errorHandler("Za malo danych");
            return;
        }

        LoginData loginData = new LoginData();
        loginData.setUsername(login);
        loginData.setPassword(password);

        try {
            Command c = CommandBuilderProvider
                    .newSimpleCommandBuilder()
                    .newCommand()
                    .withHeader(CommandType.LOGIN)
                    .withBody(loginData)
                    .build();
            sendCommand(c);
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }
    }

    /*
    LOBBY
     */
    public void loadActiveGames() {
        Command c = CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.ACTIVE_GAMES)
                .build();
        sendCommand(c);
    }

    public void joinGame(GameData gameData) {
        try {
            Command c = CommandBuilderProvider
                    .newSimpleCommandBuilder()
                    .withHeader(CommandType.JOIN)
                    .withBody(gameData)
                    .build();
            sendCommand(c);
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }

    }

    /*
    GRA
     */
    /*
     Przyjmuje koordynaty przy komendzie MOVE.
     Przy SURRENDER, PASS, CONTINUE koordynaty sa obojetne.
     */
    public void gameMove(int x, int y, GameCommandType type) {
        try {
            Command c = CommandBuilderProvider
                    .newGameCommandBuilder()
                    .newCommand()
                    .withHeader(GameCommandType.MOVE)
                    .withPosition(x, y)
                    .build();
            sendCommand(c);
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }
    }


}
