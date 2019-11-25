package example;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    public void initialize()
    {
        buttonSignUp.setOnAction(this);
        service = Service.getInstance();
    }
    //for now its just an empty method
    Boolean signUpToServer(String login, String password)
    {
        return true;
    }

    void startLobbyWindow(ActionEvent e)
    {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("lobbyFrame.fxml"));
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();

            ((Node)(e.getSource())).getScene().getWindow().hide();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void signUp(ActionEvent e)
    {
        String login = textFieldLogin.getText();
        String password = textFieldPassword.getText();
        if(login != null && password != null)
        {
            service.signUp(login, password);
            startLobbyWindow(e);
        }
        else
        {

        }
    }



    @Override
    public void handle(ActionEvent e) {
        if(e.getSource().equals(buttonSignUp))
        {
            signUp(e);
        }
    }
}
