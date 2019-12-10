package Services;

import Domain.Player;
import Domain.Game.Game;
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

    public synchronized Game newGame(int boardSize) {
        Game g = new Game(boardSize);
        games.add(g);
        return g;
    }

    public synchronized Game joinGame(GameData gameData, Player player) {
        for (Game g : games) {
            if (g.getOwnerUsername().equals(gameData.getUsername()) && g.getGameID() == gameData.getGameID()) {
                if (g.addPlayer(player)) return g;
                return null;
            }
        }
        return null;
    }

    public synchronized List<GameData> activeGames() {
        List<GameData> list = new ArrayList<>();
        for (Game g : games) {
            GameData d = new GameData();
            d.setGameID(g.getGameID());
            d.setUsername(g.getOwnerUsername());
            list.add(d);
        }
        return list;
    }

    public void endGame(Game game, Player player) {
        //skoncz gre jako poddanie osoby konczacej
        //wykonaj jakas robote typu staty do bazy
        //usun gre z listy
        //ewentualnie jakos usunac referencje do gry od przeciwnika
        //ale i tak jesli zacznie nowa gre albo sie rozlaczy to Garbage Collector usunie
        game.surrender(player);
        games.remove(game);
    }
}
