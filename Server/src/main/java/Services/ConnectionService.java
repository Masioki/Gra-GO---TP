package Services;

import DTO.Commands.Command;
import Connection.ClientConnection;
import DTO.LoginData;
import Domain.Game;

/**
 * Klasa obslugujaca otrzymane komendy
 */
public class ConnectionService {

    private ClientConnection connection;
    private GameService gameService;
    private ClientFacade clientFacade;

    public ConnectionService(ClientConnection connection) {
        gameService = GameService.getInstance();
        this.connection = connection;
        clientFacade = new ClientFacade(this);
    }

    public Command execute(Command command) {
        //TODO:command executing
        return null;
    }

    private boolean logIn(LoginData data) {
        if (Authenticator.authenticate(data)) clientFacade.logIn(data);
        return false;
    }

    private void createGame(){
        Game g = gameService.newGame();
        g.setOwnerUsername(clientFacade.getClient().getUsername());
        clientFacade.setGame(g);
    }

    private void surrender() {
        gameService.endGame(clientFacade.getGame(), clientFacade.getClient());
        connection.end();
    }
}
