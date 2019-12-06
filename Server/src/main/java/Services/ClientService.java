package Services;

import Commands.Command;
import DTO.LoginData;
import Domain.Game;

/**
 * Klasa obslugujaca otrzymane komendy
 */
public class ClientService implements InvokableService {

    private ClientServiceInvoker invoker;
    private GameService gameService;
    private ClientFacade clientFacade;

    public ClientService() {
        gameService = GameService.getInstance();
        clientFacade = new ClientFacade(this);
    }

    public void setClientServiceInvoker(ClientServiceInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Command execute(Command request) {
        //TODO:command executing
        System.out.println("Otrzymano : " + request.getType());
        return new Command();
    }

    private boolean logIn(LoginData data) {
        if (Authenticator.authenticate(data)) clientFacade.logIn(data);
        return false;
    }

    private void createGame() {
        Game g = gameService.newGame();
        g.setOwnerUsername(clientFacade.getClient().getUsername());
        clientFacade.setGame(g);
    }

    private void surrender() {
        gameService.endGame(clientFacade.getGame(), clientFacade.getClient());
        invoker.signalEnd();
    }
}
