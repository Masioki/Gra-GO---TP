package Controllers;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFrameController implements EventHandler<ActionEvent> {
    public Label labelLogin;
    public TextField textFieldLogin;
    public Label labelPassword;
    public TextField textFieldPassword;
    public Button buttonSignUp;

    private Service service;
    @FXML
    public void initialize() {
        buttonSignUp.setOnAction(this);
        service = Service.getInstance();


        FullController controller = new FullController() {
            @Override
            public void error(String message) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Sadly error occurred cannot do anything about it...");
                alert.setContentText(message);
                alert.showAndWait();
            }

            @Override
            public void logIn(boolean success){
                if(success)
                {
                    startLobbyWindow();
                }
                else
                {
                    System.out.println("Nie udało się zalogować");
                }
            }
        };
        service.setFullController(controller);

    }


    void startLobbyWindow() {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("lobbyFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Lobby");
            //dodajemy stylescheet
            Scene s = new Scene(root);
            s.getStylesheets().add(getClass().getResource("/css/basicStylesheet.css").toExternalForm());
            stage.setScene(s);
            stage.setWidth(600);
            stage.setHeight(650);
            stage.setResizable(false);
            stage.show();

            buttonSignUp.getScene().getWindow().hide();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void signUp(ActionEvent e) {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if (!login.equals("") && !password.equals("")) {
            service.signUp(login, password);
            //TODO: trzeba usunąć startLobbyWindow, powinno być wywoływanie przez server, na razie zostawiłem je do testów
            startLobbyWindow();
        } else {

        }
    }


    @Override
    public void handle(ActionEvent e) {
        if (e.getSource().equals(buttonSignUp)) {
            signUp(e);
        }
    }
}
