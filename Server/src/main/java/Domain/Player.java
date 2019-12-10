package Domain;


import Domain.Game.Game;

import java.util.Objects;

public class Player {

    // private ClientState state;
    private Game game;
    private String username;


    public boolean move(int x, int y) {
        return true;
    }

    public boolean pass() {
        return true;
    }

    public boolean continueGame() {
        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
