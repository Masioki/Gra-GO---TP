package Domain;


import Domain.Game.Game;

import java.awt.*;
import java.util.Objects;

public class Player {

    // private ClientState state;
    protected Game game;
    protected String username;


    public boolean move(int x, int y) {
        return game.move(x, y, this);
    }

    public boolean pass() {
        return game.pass(this);
    }

    public void surrender() {
        game.surrender(this);
    }

    public Point getScore() {
        int own = game.getOwnScore(this);
        int opponent = game.getOpponentScore(this);
        return new Point(own,opponent);
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
