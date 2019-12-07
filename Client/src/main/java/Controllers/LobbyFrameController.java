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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


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
    private List<GameData> gamesList = null;
    @FXML
    public void initialize() {
        chosenGame = -1;
        buttonJoinGame.setOnAction(this);
        buttonRefreshLobbies.setOnAction(this);
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
                System.out.println("Error massage...");
            }

            @Override
            public void joinGame(GameData data) {
                startGameWindow();
            }

            @Override
            public void loadActiveGames(List<GameData> games) {
                gamesList = games;
                for(int i = 0; i < lobbyMaxNumber; i++)
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
            lobbyList[chosenGame].setText("Game is ded...");
            lobbyList[chosenGame].getStylesheets().remove(getClass().getResource("/css/activeLabelStylesheet.css").toExternalForm());
            lobbyList[chosenGame].getStylesheets().add(getClass().getResource("/css/basicStylesheet.css").toExternalForm());
        }
        lobbyList[number].setText("Active Game");
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
        service.loadActiveGames();//returns GameData
        //TODO: wyswietlic zwrocone List<GameData>
    }

    void joinGame(ActionEvent e) {
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

    @Override
    public void handle(ActionEvent e) {
        if (e.getSource().equals(buttonJoinGame)) {
            joinGame(e);
        }
        if (e.getSource().equals(buttonRefreshLobbies)) {
            loadGames();
        }
    }
}
