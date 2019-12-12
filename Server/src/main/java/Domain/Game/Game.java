package Domain.Game;


import Commands.GameCommandType;
import Domain.Player;

import java.util.ArrayList;
import java.util.List;


public class Game {


    private int gameID;
    private String ownerUsername;
    private List<GameObserver> observers;
    private List<Player> players;
    private GameLogic gameLogic;

    //nazwa uzytkownika ktorego jest teraz kolej
    private String turn;

    // czy w poprzednim ruchu uzytkownik zrobil pass
    private boolean pass;

    //czy gra jest zakonczona
    //jesli tak to blokuje wywolywanie czegokolwiek
    private boolean endGame;


    public Game(int boardSize) {
        gameID = (int) (Math.random() * 1000);
        players = new ArrayList<>();
        observers = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
        pass = false;
        endGame = false;
    }

    public Game(String ownerUsername, int boardSize) {
        //TODO: poprawic to zalosne id xd
        gameID = (int) (Math.random() * 1000);
        this.ownerUsername = ownerUsername;
        observers = new ArrayList<>();
        players = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
        pass = false;
        endGame = false;
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

    /*
    LOGIKA
     */

    private boolean isPlayerTurn(Player player) {
        if (turn == null) {
            turn = ownerUsername;
            if (turn == null) return false;
        }
        return player.getUsername().equals(turn);
    }

    private void changeTurn() {
        for (Player p : players) if (!turn.equals(p.getUsername())) turn = p.getUsername();
    }


    public boolean move(int x, int y, Player player) {
        if (!endGame && isPlayerTurn(player) && gameLogic.placePawn(x, y, player.getUsername().equals(ownerUsername))) {
            changeTurn();
            signalObservers(x, y, player.getUsername(), GameCommandType.MOVE);
        }
        return false;
    }

    public boolean pass(Player player) {
        if (!endGame && isPlayerTurn(player)) {
            if (pass) {
                //TODO: policzyc pola i zasygnalizowac kto wygral
                endGame = true;
            }
            pass = true;
            signalObservers(0, 0, player.getUsername(), GameCommandType.PASS);
        }
        return false;
    }

    //TODO: sprawdzic czy metoda wogole konieczna
    public boolean continueGame(Player player) {
        if (!endGame && isPlayerTurn(player)) {
            if (pass) {
                pass = false;
                changeTurn();
                return true;
            }
        }
        return false;
    }

    public void surrender(Player player) {
        if (!endGame && players.contains(player)) {
            signalObservers(0, 0, player.getUsername(), GameCommandType.SURRENDER);
            endGame = true;
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
