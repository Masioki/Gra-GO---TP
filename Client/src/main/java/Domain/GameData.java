package Domain;

/**
 * Game data for Active Games list in Lobby
 */
public class GameData {

    private int gameID;
    private String username;


    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
