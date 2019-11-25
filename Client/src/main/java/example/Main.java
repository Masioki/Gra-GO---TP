package example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;

public class Main extends Application {
//TESTOWY KOMENTARZ

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(new Locale("pl"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getClassLoader().getResource("example.fxml")));
        primaryStage.setTitle("Example");
        Scene s = new Scene(root);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}