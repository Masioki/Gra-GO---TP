package Domain;


public class Client {

    // private ClientState state;
    private Game game;
    private String username;

    public Client() {
    }




    /* getters setters */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
