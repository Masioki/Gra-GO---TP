package example;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ExampleController {
    public Label text;
    public Button button;

    public void exampleMethod(ActionEvent actionEvent) {
        text.setText("zmieniono tekst");
    }
}
