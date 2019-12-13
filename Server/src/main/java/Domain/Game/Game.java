package Domain.Game;


import Commands.GameCommandType;
import Commands.PawnColor;
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


    public Game(String ownerUsername, int boardSize) {
        //TODO: poprawic to zalosne id xd
        gameID = (int) (Math.random() * 1000);
        this.ownerUsername = ownerUsername;
        observers = new ArrayList<>();
        players = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
        pass = false;
        endGame = false;
        turn = ownerUsername;
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
    private void signalObservers(int x, int y, String username, PawnColor color, GameCommandType type) {
        observers.forEach(o -> o.action(x, y, username, color, type));
    }

    /*
    LOGIKA
     */

    private boolean isPlayerTurn(Player player) {
        return player.getUsername().equals(turn);
    }

    private synchronized void changeTurn() {
        for (Player p : players) {
            if (!turn.equals(p.getUsername())) {
                turn = p.getUsername();
                break;
            }
        }
    }


    public synchronized boolean move(int x, int y, Player player) {
        if (!endGame && isPlayerTurn(player) && gameLogic.placePawn(x, y, player.getUsername().equals(ownerUsername))) {
            PawnColor color;
            if (player.getUsername().equals(ownerUsername)) color = PawnColor.WHITE;
            else color = PawnColor.BLACK;
            signalObservers(x, y, player.getUsername(), color, GameCommandType.MOVE);
            //TODO: sprawdzic czy zmienily sie jakies inne pola i zasygnalizowac
            changeTurn();
            return true;
        }
        return false;
    }

    public synchronized boolean pass(Player player) {
        if (!endGame && isPlayerTurn(player)) {
            if (pass) {
                //TODO: policzyc pola i zasygnalizowac kto wygral
                endGame = true;
            }
            pass = true;
            signalObservers(0, 0, player.getUsername(), PawnColor.BLACK, GameCommandType.PASS);
        }
        return false;
    }


    public synchronized void surrender(Player player) {
        if (!endGame && players.contains(player)) {
            signalObservers(0, 0, player.getUsername(), PawnColor.BLACK, GameCommandType.SURRENDER);
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
