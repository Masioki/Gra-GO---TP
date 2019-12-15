package Services;

import Commands.*;
import Commands.Builder.CommandBuilderProvider;
import DTO.GameData;
import DTO.LoginData;
import Domain.Game.Game;
import Domain.Game.GameObserver;
import Domain.Player;

import java.util.UUID;

/**
 * Klasa obslugujaca otrzymane komendy
 */
public class PlayerService implements InvokableService, GameObserver {

    private PlayerServiceInvoker invoker;
    private GameService gameService;
    private Player player;
    private CommandParser parser;

    public PlayerService() {
        gameService = GameService.getInstance();
        player = new Player();
        parser = new CommandParser();
    }

    public void setClientServiceInvoker(PlayerServiceInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public void action(int x, int y, String username, PawnColor color, GameCommandType type) {
        if(username != null && username.equals(player.getUsername()) && type == GameCommandType.MOVE) return;
        Command c = CommandBuilderProvider
                .newGameCommandBuilder()
                .newCommand()
                .withPosition(x, y)
                .withHeader(type)
                .withColor(color)
                .withUsername(username)
                .build();
        invoker.send(c);
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
                    return success(createGame(false), request.getUuid());
                case NEW_BOT:
                    return success(createGame(true), request.getUuid());
                case LOGIN: {
                    LoginData data = parser.parseLoginCommand(request.getBody());
                    if (!logIn(data)) return error("Niepoprawne dane", request.getUuid());
                    else return success(data, request.getUuid());
                }
                case ACTIVE_GAMES:
                    return success(gameService.activeGames(), request.getUuid());
                case GAME: {
                    GameCommand gameCommand = parser.parseGameCommand(request.getBody());
                    System.out.print(" : " + gameCommand.getCommandType() + "\n");
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
        boolean result = gameService.joinGame(data, player);
        if (result) gameService.observe(data, this);
        return result;
    }

    private boolean logIn(LoginData data) {
        if (Authenticator.authenticate(data)) {
            player.setUsername(data.getUsername());
            return true;
        }
        return false;
    }

    private GameData createGame(boolean withBot) {
        GameData gameData = gameService.newGame(19, player, withBot);
        gameService.observe(gameData, this);
        return gameData;
    }

    private void surrender() {
        gameService.endGame(player.getGame(), player);
        player.surrender();
        invoker.signalEnd();
    }

    private Command gameAction(int x, int y, GameCommandType type, UUID uuid) {
        switch (type) {
            case MOVE: {
                if (!player.move(x, y))
                    return error("Nie mozesz tutaj lub nie twoj ruch", uuid);
                else return success("", uuid);
            }
            case SURRENDER: {
                surrender();
                return success("", uuid);
            }
            case PASS: {
                if (!player.pass()) return error("Nie mozesz teraz", uuid);
                else return success("", uuid);
            }
            case SCORE: {
                return success(player.getScore(), uuid);
            }
        }
        return error("Blad wewnetrzny", uuid);
    }

}
