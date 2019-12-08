package Controllers;

import Domain.GameData;
import Services.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


public class LobbyFrameController implements EventHandler<ActionEvent> {
    public Button buttonJoinGame;
    public GridPane gridPanelLobby;
    public Button buttonRefreshLobbies;
    public Button buttonCreateLobby;
    public Label labelLoggedAs;
    public Label labelPlayerLogin;
    public Label labelGameTurn;


    private MyLabel[] lobbyList;
    private Service service = null;
    final int lobbyMaxNumber = 10;
    //numerek gry wybranej przez użytkownika
    private int chosenGame;

    //lista gier
    private List<GameData> gamesList;
    @FXML
    public void initialize() {
        gamesList = new ArrayList<GameData>();
        chosenGame = -1;
        buttonJoinGame.setOnAction(this);
        buttonRefreshLobbies.setOnAction(this);
        buttonCreateLobby.setOnAction(this);
        service = Service.getInstance();
        for(int i = 0; i < lobbyMaxNumber; i++)
        {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / lobbyMaxNumber);
            gridPanelLobby.getRowConstraints().add(rowConst);
        }
        lobbyList = new MyLabel[lobbyMaxNumber];
        for (int i = 0; i < lobbyMaxNumber; i++) {
            MyLabel l = new MyLabel();
            l.setText("Game is dead...");
            l.setPrefWidth(gridPanelLobby.getPrefWidth());
            l.setPrefHeight(buttonJoinGame.getPrefHeight()-20);
            l.setTextAlignment(TextAlignment.CENTER);
            l.setNumber(i);
            l.setOnMouseClicked((event) -> {
                int number = l.getNumber();
                chooseGame(number);

            });
            lobbyList[i] = l;
            gridPanelLobby.add(lobbyList[i],0,i);
        }
        //ustawiamy kontroler
        FullController controller = new FullController() {
            @Override
            public void error(String message) {
                try
                {
                    //TODO - ten alert wyrzuca błąd
                    /*Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Sadly error occurred cannot do anything about it...");
                    alert.setContentText(message);
                    alert.showAndWait();*/
                    System.out.println(message);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void joinGame(GameData data) {
                startGameWindow();
            }

            @Override
            public void loadActiveGames(List<GameData> games) {
                if(games==null)
                {
                    System.out.println("ww");
                }
                gamesList = games;
                for(int i = 0; i < gamesList.size(); i++)
                {
                    if(gamesList.get(i)!=null)
                    {
                        lobbyList[i].setText(gamesList.get(i).getUsername()  );
                    }
                }
            }
        };
        service.setFullController(controller);
    }
    private void  chooseGame(int number)
    {
        //resetujemy poprzednią grę
        if(chosenGame != -1)
        {
            //lobbyList[chosenGame].setText("Game is ded...");
            lobbyList[chosenGame].getStylesheets().remove(getClass().getResource("/css/activeLabelStylesheet.css").toExternalForm());
            lobbyList[chosenGame].getStylesheets().add(getClass().getResource("/css/basicStylesheet.css").toExternalForm());
        }
        //lobbyList[number].setText("Active Game");
        chosenGame = number;
        lobbyList[chosenGame].getStylesheets().remove(getClass().getResource("/css/basicStylesheet.css").toExternalForm());
        lobbyList[number].getStylesheets().add(getClass().getResource("/css/activeLabelStylesheet.css").toExternalForm());
    }
    private void startGameWindow() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("gameFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Game Board");
            //dodajemy stylesheer
            Scene s = new Scene(root);
            s.getStylesheets().add(getClass().getResource("/css/basicStylesheet.css").toExternalForm());
            stage.setScene(s);
            stage.setWidth(850);
            stage.setHeight(700);
            stage.setResizable(false);
            stage.show();

            buttonJoinGame.getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void loadGames() {
        service.loadActiveGames();
    }

    void joinGame(ActionEvent e) {
        //TODO - trzeba usunąć start game windows
        if(chosenGame > 0)
        {
            if(gamesList.get(chosenGame)!=null)
            {
                GameData gameData = gamesList.get(chosenGame);
                service.joinGame(gameData);
            }
        }
        startGameWindow();
    }
    void createLobby()
    {
        service.newGame();
    }
    @Override
    public void handle(ActionEvent e) {
        if (e.getSource().equals(buttonJoinGame)) {
            joinGame(e);
        }
        if (e.getSource().equals(buttonRefreshLobbies)) {
            loadGames();
        }
        if(e.getSource().equals(buttonCreateLobby))
        {
            createLobby();
        }
    }
}
