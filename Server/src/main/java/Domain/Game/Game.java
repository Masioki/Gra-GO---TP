package Domain.Game;


import Commands.GameCommandType;
import Domain.Player;

import java.util.ArrayList;
import java.util.List;

//Tutaj i w jakis klasach pobocznych zabawa w logike gry
public class Game {

    //gameID i ownerUsername dosyc wazne dla komunikacji
    //reszta dowolnie
    private int gameID;
    private String ownerUsername;
    private List<GameObserver> observers;
    private List<Player> players;
    private GameLogic gameLogic;


    public Game(int boardSize) {
        gameID = (int) (Math.random() * 1000);
        players = new ArrayList<>();
        observers = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
    }

    public Game(String ownerUsername, int boardSize) {
        //TODO: poprawic to zalosne id xd
        gameID = (int) (Math.random() * 1000);
        this.ownerUsername = ownerUsername;
        observers = new ArrayList<>();
        players = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
    }

    public boolean addPlayer(Player player) {
        if (!players.contains(player) && players.size() < 2) {
            players.add(player);
            return true;
        }
        return false;
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /*
    WYKONYWAC PO KAZDEJ UDANEJ AKCJI
     */
    private void signalObservers(int x, int y, String username, GameCommandType type) {
        observers.forEach(o -> o.action(x, y, username, type));
    }


    //TODO: logic


    public void surrender(Player player) {
        if (players.contains(player)) {
            // gameLogic.surrender(client.getUsername());
            signalObservers(0, 0, player.getUsername(), GameCommandType.SURRENDER);
        }
    }



    /* Getters Setters */

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
