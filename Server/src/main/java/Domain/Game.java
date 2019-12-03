package Domain;


import java.util.List;

//Tutaj i w jakis klasach pobocznych zabawa w logike gry
public class Game {

    //gameID i ownerUsername dosyc wazne dla komunikacji
    //reszta dowolnie
    private int gameID;
    private String ownerUsername;
    private List<Client> players;


    public Game() {
        gameID = (int) (Math.random() * 1000);
    }

    public Game(String ownerUsername) {
        gameID = (int) (Math.random() * 1000);
        this.ownerUsername = ownerUsername;
    }

public boolean addPlayer(Client client){
        return false;
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
