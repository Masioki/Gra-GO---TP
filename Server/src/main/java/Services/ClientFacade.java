package Services;

import Commands.Command;
import Connection.ClientConnection;
import Domain.Client;
import Domain.Game;
import DTO.GameData;
import DTO.LoginData;


public class ClientFacade {

    private Client client;
    private GameService gameService;
    private ClientConnection clientConnection;


    public ClientFacade(ClientConnection clientConnection) {
        client = new Client();
        gameService = GameService.getInstance();
        this.clientConnection = clientConnection;
    }

    public Command executeCommand(Command command) {

    }

    public void end() {
        client.surrender();
        gameService.endGame(client.getGame(),client);
    }

    private void sendCommand(Command command) {
        clientConnection.sendCommand(command);
    }

    private boolean logIn(LoginData loginData) {
        return Authenticator.authenticate(loginData);
    }

    private boolean move(int x, int y) {
        return client.move(x, y);
    }

    private boolean joinGame(GameData gameData) {
        //TODO: poprawic dolaczenie do gry w fasadzie
        Game g = gameService.joinGame(gameData, client);
        return g != null;
    }

    private void createGame() {

    }
}
