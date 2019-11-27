package Controllers;

import Services.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class LobbyFrameController implements EventHandler<ActionEvent> {
    public Button buttonJoinGame;
    public Button buttonRefresh;
    public GridPane gridPanelLobby;
    private Label[] lobbyList;
    Service service = null;

    @FXML
    public void initialize() {
        buttonJoinGame.setOnAction(this);
        buttonRefresh.setOnAction(this);
        service = Service.getInstance();
        final int lobbyMaxNumber = 10;
        for(int i = 0; i < lobbyMaxNumber; i++)
        {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / lobbyMaxNumber);
            gridPanelLobby.getRowConstraints().add(rowConst);
        }
        lobbyList = new Label[lobbyMaxNumber];
        for (int i = 0; i < lobbyMaxNumber; i++) {
            Label l = new Label("Game is Dead...");
            lobbyList[i] = l;
            gridPanelLobby.add(lobbyList[i],0,i);
        }
    }

    void startGameWindow(ActionEvent e) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gameFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Game Board");
            stage.setScene(new Scene(root));
            stage.setWidth(850);
            stage.setHeight(650);
            stage.show();

            ((Node) (e.getSource())).getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void loadGames() {
        service.loadActiveGames();
    }

    void joinGame(ActionEvent e) {
        //service.joinGame();
        //TODO - implement join game in Service
        startGameWindow(e);
    }

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource().equals(buttonJoinGame)) {
            joinGame(e);
        }
        if (e.getSource().equals(buttonRefresh)) {
            loadGames();
        }
    }
}
