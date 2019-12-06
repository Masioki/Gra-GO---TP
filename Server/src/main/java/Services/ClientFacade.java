package Services;

import Domain.Client;
import Domain.Game;
import DTO.LoginData;
import Domain.WrongMoveException;

// wzorzec Facade

/**
 * Obiekt pomocniczy do obslugi klienta
 */
public class ClientFacade {

    private Client client;
    private ClientService clientService;


    public ClientFacade(ClientService clientService) {
        client = new Client();
        this.clientService = clientService;
    }

    public Client getClient() {
        return client;
    }

    public Game getGame() {
        return client.getGame();
    }


    public void surrender() {
        // client.surrender();
    }


    public void logIn(LoginData loginData) {
        client.setUsername(loginData.getUsername());
    }

    public boolean move(int x, int y) throws WrongMoveException {
        return false; // client.move(x, y);
    }

    public void joinGame(Game game) {
        client.setGame(game);
    }

    public void setGame(Game game) {
        client.setGame(game);
    }
}
