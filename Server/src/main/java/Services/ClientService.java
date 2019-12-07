package Services;

import Commands.*;
import Commands.Builder.CommandBuilderProvider;
import DTO.GameData;
import DTO.LoginData;
import Domain.Game;

import java.util.List;
import java.util.UUID;

/**
 * Klasa obslugujaca otrzymane komendy
 */
public class ClientService implements InvokableService {

    private ClientServiceInvoker invoker;
    private GameService gameService;
    private ClientFacade clientFacade;
    private CommandParser parser;

    public ClientService() {
        gameService = GameService.getInstance();
        clientFacade = new ClientFacade(this);
        parser = new CommandParser();
    }

    public void setClientServiceInvoker(ClientServiceInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Command execute(Command request) {
        System.out.println("Otrzymano : " + request.getType());
        try {
            switch (request.getType()) {
                case END_CONNECTION: {
                    surrender();
                    invoker.signalEnd();
                    break;
                }
                case JOIN: {
                    GameData data = parser.parseGameData(request.getBody());
                    if (!joinGame(data)) return error("Nie udalo sie dolaczyc", request.getUuid());
                    else return success(data, request.getUuid());
                }
                case NEW:
                    return success(createGame(), request.getUuid());
                case LOGIN: {
                    LoginData data = parser.parseLoginCommand(request.getBody());
                    if (!logIn(data)) return error("Niepoprawne dane", request.getUuid());
                    else return success(data, request.getUuid());
                }
                case ACTIVE_GAMES:
                    return success(gameService.activeGames(), request.getUuid());
                case GAME: {
                    GameCommand gameCommand = parser.parseGameCommand(request.getBody());
                    return gameAction(gameCommand.getX(), gameCommand.getY(), gameCommand.getCommandType(), request.getUuid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return error("Blad wewnetrzny", request.getUuid());
    }

    private Command error(String message, UUID uuid) {
        return CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.ERROR)
                .withBody(message)
                .withUUID(uuid)
                .build();
    }

    private Command success(Object object, UUID uuid) {
        return CommandBuilderProvider
                .newSimpleCommandBuilder()
                .newCommand()
                .withHeader(CommandType.SUCCESS)
                .withBody(object)
                .withUUID(uuid)
                .build();
    }

    private boolean joinGame(GameData data) {
        Game g = gameService.joinGame(data, clientFacade.getClient());
        if (g == null) return false;
        else clientFacade.getClient().setGame(g);
        return true;
    }

    private boolean logIn(LoginData data) {
        if (Authenticator.authenticate(data)) {
            clientFacade.logIn(data);
            return true;
        }
        return false;
    }

    private GameData createGame() {
        Game g = gameService.newGame();
        g.setOwnerUsername(clientFacade.getClient().getUsername());
        clientFacade.setGame(g);
        GameData data = new GameData();
        data.setGameID(g.getGameID());
        data.setUsername(g.getOwnerUsername());
        return data;
    }

    private void surrender() {
        gameService.endGame(clientFacade.getGame(), clientFacade.getClient());
        invoker.signalEnd();
    }

    private Command gameAction(int x, int y, GameCommandType type, UUID uuid) {
        switch (type) {
            case MOVE: {
                if (!clientFacade.move(x, y))
                    return error("Nie mozesz teraz", uuid);
                else return success("", uuid);
            }
            case SURRENDER: {
                surrender();
                return success("", uuid);
            }
            case CONTINUE: {
                if (!clientFacade.continueGame()) return error("Nie mozesz teraz", uuid);
                else return success("", uuid);
            }
            case PASS: {
                if (!clientFacade.pass()) return error("Nie mozesz teraz", uuid);
                else return success("", uuid);
            }
        }
        return error("Blad wewnetrzny", uuid);
    }
}
