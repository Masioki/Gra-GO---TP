package Domain;


import java.util.List;

//Tutaj i w jakis klasach pobocznych zabawa w logike gry
public class Game {

    private List<Client> players;

    public List<Client> getPlayers() {
        return players;
    }

    public void setPlayers(List<Client> players) {
        this.players = players;
    }
}
