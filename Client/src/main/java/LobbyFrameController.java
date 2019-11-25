package example;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyFrameController implements EventHandler<ActionEvent> {
    public Button buttonJoinGame;
    public Button buttonRefresh;

    Service service = null;
    void startGameWindow(ActionEvent e)
    {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gameFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Game Board");
            stage.setScene(new Scene(root));
            stage.show();

            ((Node)(e.getSource())).getScene().getWindow().hide();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void loadGames()
    {
        service.loadGames();
    }

    void joinGame(ActionEvent e)
    {
        service.joinGame();
        startGameWindow(e);
    }
    public void initialize()
    {
        buttonJoinGame.setOnAction(this);
        buttonRefresh.setOnAction(this);
        service = Service.getInstance();
    }
    @Override
    public void handle(ActionEvent e) {
        if(e.getSource().equals(buttonJoinGame))
        {
            joinGame(e);
        }
        if(e.getSource().equals(buttonRefresh))
        {
            loadGames();
        }
    }
}
