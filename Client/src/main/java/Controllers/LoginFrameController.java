package Controllers;

import Services.Service;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
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
    }

    //for now its just an empty method
    Boolean signUpToServer(String login, String password) {
        return true;
    }

    void startLobbyWindow(ActionEvent e) {
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

            ((Node) (e.getSource())).getScene().getWindow().hide();
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
            //signUp wysyla request do serwera, kiedy dostaniemy odpowiedz serwis powinien wywolac metode w kontrolerze
            // typu loginResult(boolean)
            //TODO: poprawa logowania
            startLobbyWindow(e);
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
