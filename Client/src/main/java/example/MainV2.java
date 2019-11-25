package example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;

public class MainV2 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(new Locale("pl"));
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("loginFrame.fxml"));
        //loader.setController(new LoginFrameController());
        root = loader.load();
        //Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getClassLoader().getResource("loginFrame.fxml")));
        primaryStage.setTitle("Login");
        Scene s = new Scene(root);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) {
       launch();
    }
}
