package Domain.Game;


import Commands.GameCommandType;
import Commands.PawnColor;
import Domain.Player;

import java.awt.*;
import java.util.*;
import java.util.List;


public class Game {


    private int gameID;
    private String ownerUsername;
    private List<GameObserver> observers;
    private List<Player> players;
    private GameLogic gameLogic;
    private int boardSize;

    //nazwa uzytkownika ktorego jest teraz kolej
    private String lastMoved;

    // czy w poprzednim ruchu uzytkownik zrobil pass
    private boolean pass;

    //czy gra jest zakonczona
    //jesli tak to blokuje wywolywanie czegokolwiek
    private boolean endGame;


    public Game(String ownerUsername, int boardSize) {
        gameID = (int) (Math.random() * 1000);
        this.ownerUsername = ownerUsername;
        this.boardSize = boardSize;
        observers = new ArrayList<>();
        players = new ArrayList<>();
        gameLogic = new GameLogic(boardSize);
        pass = false;
        endGame = false;
        lastMoved = "";
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
        refreshWholeGame();
    }

    public int getOwnScore(String username) {
        if (username.equals(ownerUsername)) return gameLogic.getPlayerPoints(true);
        return gameLogic.getPlayerPoints(false);
    }

    public int getOpponentScore(String username) {
        if (username.equals(ownerUsername)) return gameLogic.getPlayerPoints(false);
        return gameLogic.getPlayerPoints(true);
    }

    public int getSize() {
        return boardSize;
    }

    public boolean inProgress() {
        return !endGame;
    }

    /*
    WYKONYWAC PO KAZDEJ UDANEJ AKCJI
     */
    private void signalObservers(int x, int y, String username, PawnColor color, GameCommandType type) {
        observers.forEach(o -> o.action(x, y, username, color, type));
    }

    private void refreshWholeGame() {
        Map<Point, GridState> board = gameLogic.getBoard();
        for (Point p : board.keySet()) {
            switch (board.get(p)) {
                case EMPTY:
                    signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.EMPTY, GameCommandType.MOVE);
                    break;
                case BLACK:
                    signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.BLACK, GameCommandType.MOVE);
                    break;
                case WHITE:
                    signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.WHITE, GameCommandType.MOVE);
                    break;
            }
        }
    }

    /*
    LOGIKA
     */

    public boolean isPlayerTurn(Player player) {
        System.out.println(player.getUsername() + " " + lastMoved);
        return !player.getUsername().equals(lastMoved);
    }

    private void changeTurn() {
      /*  for (int i = 0; i < players.size(); i++) {
            if (!lastMoved.equals(players.get(i).getUsername())) {
                lastMoved = players.get(i).getUsername();
                break;
            }
        }*/
    }


    public synchronized boolean move(int x, int y, Player player) {
        Map<Point, GridState> previous = new HashMap<>(gameLogic.getBoard());
        if (!endGame && isPlayerTurn(player) && gameLogic.placePawn(x, y, player.getUsername().equals(ownerUsername))) {
            PawnColor color;
            if (player.getUsername().equals(ownerUsername)) color = PawnColor.WHITE;
            else color = PawnColor.BLACK;
            signalObservers(x, y, player.getUsername(), color, GameCommandType.MOVE);
            Map<Point, GridState> after = gameLogic.getBoard();
            Map<Point, GridState> changes = new HashMap<>();
            for (Point point : after.keySet()) {
                if (previous.get(point) != after.get(point)) changes.put(point, after.get(point));
            }
            for (Point p : changes.keySet()) {
                switch (changes.get(p)) {
                    case EMPTY:
                        signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.EMPTY, GameCommandType.MOVE);
                        break;
                    case WHITE:
                        signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.WHITE, GameCommandType.MOVE);
                        break;
                    case BLACK:
                        signalObservers((int) p.getX(), (int) p.getY(), null, PawnColor.BLACK, GameCommandType.MOVE);
                        break;
                }
            }
            //changeTurn();
            lastMoved = player.getUsername();
            pass = false;
            return true;
        }
        return false;
    }

    public synchronized boolean pass(Player player) {
        if (!endGame && isPlayerTurn(player)) {
            if (pass) {
                int white = getOwnScore(ownerUsername);
                int black = getOpponentScore(ownerUsername);
                if (white == black) signalObservers(0, 0, null, PawnColor.BLACK, GameCommandType.DRAW);
                else if (white > black) signalObservers(0, 0, ownerUsername, PawnColor.WHITE, GameCommandType.WIN);
                else {
                    for (Player p : players) {
                        if (!ownerUsername.equals(p.getUsername())) {
                            signalObservers(0, 0, p.getUsername(), PawnColor.BLACK, GameCommandType.WIN);
                            break;
                        }
                    }
                }
                endGame = true;
            }
            pass = true;
            signalObservers(0, 0, player.getUsername(), PawnColor.BLACK, GameCommandType.PASS);
            //changeTurn();
            lastMoved = player.getUsername();
            return true;
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
