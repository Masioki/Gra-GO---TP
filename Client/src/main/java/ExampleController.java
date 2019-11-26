import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class ExampleController {
    public Label text;
    public Button button;

    @FXML
    public void initialize(){
    // "konstruktor" dla obiektow JavaFX
    }

    public void exampleMethod(ActionEvent actionEvent) {
        text.setText("zmieniono tekst");
    }
}
