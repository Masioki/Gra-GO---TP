package Services;

import Commands.*;
import Commands.Builder.CommandBuilderProvider;
import Controllers.FullController;
import Domain.GameData;
import Domain.LoginData;

import java.awt.*;
import java.util.List;

//Class Service should be singleton
public class Service implements InvokableService {

    private static Service service;
    private ServiceInvoker invoker;
    private CommandParser parser;
    private FullController fullController;
    private PawnColor ownColor;
    private String username;

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

    /*
    SETTERS GETTERS
     */
    public void setServiceInvoker(ServiceInvoker invoker) {
        this.invoker = invoker;
    }

    public void setFullController(FullController controller) {
        this.fullController = controller;
    }

    public String getUsername() {
        return username;
    }

    /*
    COMMAND EXECUTION
     */
    @Override
    public void execute(Command request, Command response) {
        if (request != null) System.out.println(request.getType() + " : " + response.getType());
        else System.out.println("Przeciwnik : " + response.getType());

        if (response.getType() == CommandType.SUCCESS) {
            executeOwn(request, response);
        } else if (response.getType() == CommandType.ERROR) {
            try {
                errorHandler(parser.parseErrorCommand(response.getBody()));
            } catch (Exception e) {
                errorHandler("Blad komendy oraz blad wewnetrzny");
            }
        } else if (request == null) {
            executeIncomingRequest(response);
        }
    }

    private void sendCommand(Command c) {
        try {
            invoker.send(c);
        } catch (Exception e) {
            errorHandler("Connection error");
        }
    }

    private void executeOwn(Command request, Command response) {
        try {
            switch (request.getType()) {
                case ACTIVE_GAMES: {
                    List<GameData> data = parser.parseActiveGamesCommand(response.getBody());
                    fullController.loadActiveGames(data);
                    System.out.println(data.get(0).getUsername());
                    break;
                }
                case JOIN: {
                    fullController.joinGame(parser.parseGameData(request.getBody()));
                    ownColor = PawnColor.BLACK;
                    break;
                }
                case NEW_BOT:
                case NEW: {
                    fullController.joinGame(parser.parseGameData(response.getBody()));
                    ownColor = PawnColor.WHITE;
                    break;
                }
                case LOGIN: {
                    fullController.logIn(true);
                    username = parser.parseLoginData(request.getBody()).getUsername();
                    break;
                }
                case GAME: {
                    GameCommand gameCommand = parser.parseGameCommand(request.getBody());
                    System.out.print(" : " + gameCommand.getCommandType() + "\n");
                    if (gameCommand.getCommandType() == GameCommandType.SCORE) {
                        Point p = parser.parsePoint(response.getBody());
                        fullController.setScore((int) p.getX(), (int) p.getY());
                    } else if (gameCommand.getCommandType() == GameCommandType.MOVE) {
                        fullController.move(gameCommand.getX(), gameCommand.getY(), ownColor);
                    } else fullController.gameAction(gameCommand.getCommandType(), true);
                    break;
                }
            }
        } catch (Exception e) {
            errorHandler("Sprobuj ponownie");
            e.printStackTrace();
        }
    }

    private void executeIncomingRequest(Command command) {
        if (command.getType() == CommandType.END_CONNECTION) end();
        else if (command.getType() == CommandType.GAME) {
            try {
                GameCommand gameCommand = parser.parseGameCommand(command.getBody());
                System.out.print(" : " + gameCommand.getCommandType() + "\n");
                if (gameCommand.getCommandType() == GameCommandType.MOVE)
                    fullController.move(gameCommand.getX(), gameCommand.getY(), gameCommand.getColor());
                else
                    fullController.gameAction(gameCommand.getCommandType(), username.equals(gameCommand.getUsername()));
            } catch (Exception e) {
                errorHandler("Blad wewnetrzny");
                e.printStackTrace();
            }
        }
    }


    /*
    EVERY CONTROLLER
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

    private void errorHandler(String errorMessage) {
        if (fullController != null) fullController.error(errorMessage);
    }

    /*
    LOGIN CONTROLLER
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
    LOBBY CONTROLLER
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
            System.out.println("join");
            Command c = CommandBuilderProvider
                    .newSimpleCommandBuilder()
                    .newCommand()
                    .withHeader(CommandType.JOIN)
                    .withBody(gameData)
                    .build();
            sendCommand(c);
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }
    }

    public void newGame(boolean withBot) {
        CommandType type;
        if (withBot) type = CommandType.NEW_BOT;
        else type = CommandType.NEW;

        try {
            Command c = CommandBuilderProvider
                    .newSimpleCommandBuilder()
                    .newCommand()
                    .withHeader(type)
                    .build();
            sendCommand(c);
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }
    }

    /*
    GAME CONTROLLER
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
                    .withHeader(type)
                    .withPosition(x, y)
                    .build();
            sendCommand(c);
            getScore();
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }
    }

    private void getScore() {
        try {
            Command c = CommandBuilderProvider
                    .newGameCommandBuilder()
                    .newCommand()
                    .withHeader(GameCommandType.SCORE)
                    .build();
            sendCommand(c);
        } catch (Exception e) {
            errorHandler("Blad wewnetrzny");
        }
    }


}
