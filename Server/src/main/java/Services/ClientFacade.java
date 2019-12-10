package Services;

import Domain.Client;
import Domain.Game;
import DTO.LoginData;

/*

 Moze sie okazac ze ta klasa jest niepotrzebna, ale to zalezy jak wyjdzie logika

 */

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

    public boolean move(int x, int y) {
        return true; // client.move(x, y);
    }

    public boolean continueGame() {
        return true;
    }

    public boolean pass() {
        return true;
    }

    public void setGame(Game game) {
        client.setGame(game);
    }
}
