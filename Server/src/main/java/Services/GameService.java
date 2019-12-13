package Services;

import Domain.Bot;
import Domain.Game.GameObserver;
import Domain.Player;
import Domain.Game.Game;
import DTO.GameData;

import java.util.ArrayList;
import java.util.List;

//Koniecznie singleton

/**
 * Klasa zarzadzajaca aktywnymi grami oraz przygotowujaca nowe rozgrywki
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


    public synchronized GameData newGame(int boardSize, Player player, boolean withBot) {
        Game g = new Game(player.getUsername(), boardSize);
        games.add(g);
        g.addPlayer(player);
        player.setGame(g);
        if (withBot) {
            Bot bot = new Bot();
            g.addPlayer(bot);
            bot.setGame(g);
            g.addObserver(bot);
        }
        GameData gameData = new GameData();
        gameData.setUsername(g.getOwnerUsername());
        gameData.setGameID(g.getGameID());
        return gameData;
    }

    public synchronized boolean joinGame(GameData gameData, Player player) {
        for (Game g : games) {
            if (g.getOwnerUsername().equals(gameData.getUsername()) && g.getGameID() == gameData.getGameID()) {
                if (g.addPlayer(player)) {
                    player.setGame(g);
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public synchronized void observe(GameData gameData, GameObserver observer) {
        for (Game g : games) {
            if (g.getGameID() == gameData.getGameID() && g.getOwnerUsername().equals(gameData.getUsername())) {
                g.addObserver(observer);
            }
        }
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
