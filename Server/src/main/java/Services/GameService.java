package Services;

import Domain.Client;
import Domain.Game;
import DTO.GameData;

import java.util.ArrayList;
import java.util.List;

//Koniecznie singleton

/**
 * Klasa obslugujaca wszystkie aktywne gry.
 */
public class GameService {
    private List<Game> games;
    private static GameService gameService;

    private GameService() {
        games = new ArrayList<>();
    }

    //Double checked locking
    public static GameService getInstance() {
        if (gameService == null) {
            synchronized (GameService.class) {
                if (gameService == null) {
                    gameService = new GameService();
                }
                return gameService;
            }
        }
        return gameService;
    }

    public synchronized Game newGame() {
      Game g = new Game();
      games.add(g);
      return g;
    }

    public synchronized Game joinGame(GameData gameData, Client client) {
        for (Game g : games) {
            if (g.getOwnerUsername().equals(gameData.getUsername()) && g.getGameID() == gameData.getGameID()) {
                if (g.addPlayer(client)) return g;
                return null;
            }
        }
        return null;
    }

    public void endGame(Game game, Client client) {
        //skoncz gre jako poddanie osoby konczacej
        //wykonaj jakas robote typu staty do bazy
        //usun gre z listy
        //ewentualnie jakos usunac referencje do gry od przeciwnika
        //ale i tak jesli zacznie nowa gre albo sie rozlaczy to Garbage Collector usunie
    }
}
