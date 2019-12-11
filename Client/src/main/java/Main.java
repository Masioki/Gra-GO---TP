
import Services.ServerConnection;
import Services.Service;
import Services.ServiceInvoker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale.setDefault(new Locale("pl"));
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("loginFrame.fxml"));
        root = loader.load();
        primaryStage.setTitle("Login");
        Scene s = new Scene(root);
        //dodajemy plik css
        s.getStylesheets().add(getClass().getResource("/css/basicStylesheet.css").toExternalForm());
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            System.exit(1);
        });
    }

    public static void main(String[] args) {
        try {
            Service s = Service.getInstance();
            ServiceInvoker invoker = new ServiceInvoker(s);
            ServerConnection connection = new ServerConnection(invoker);
            s.setServiceInvoker(invoker);
            invoker.addListener(connection);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("NIE POLACZONO");
        }

        launch(args);
    }
}
