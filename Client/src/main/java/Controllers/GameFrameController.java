package Controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.io.File;


public class GameFrameController {

    public GridPane gridPanelBoard;
    public AnchorPane gameBoardFrame;
    public Button buttonPause;
    public Button buttonSurrender;
    public Label labelCapturedPawnsPlayer;
    public Label labelCapturedPawnsEnemy;
    private ImageView[][] grids;

    public void initialize() {
        final int gridsInRowNumber = 19;
        //double frameWidth = gameBoardFrame.getScene().getWindow().getX();
        //double frameHeight = gameBoardFrame.getScene().getWindow().getY();
        final int boardWidth = 650;
        final int boardHeight = 650;
        gridPanelBoard.setPrefWidth(boardWidth);
        gridPanelBoard.setPrefHeight(boardWidth);
        gridPanelBoard.setMaxWidth(boardHeight);
        gridPanelBoard.setMaxHeight(boardHeight);

        for (int i = 0; i < gridsInRowNumber; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / gridsInRowNumber);
            gridPanelBoard.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < gridsInRowNumber; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / gridsInRowNumber);
            gridPanelBoard.getRowConstraints().add(rowConst);
        }
        grids = new ImageView[gridsInRowNumber][gridsInRowNumber];
        for (int i = 0; i < gridsInRowNumber; i++) {
            for (int b = 0; b < gridsInRowNumber; b++) {
                File file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\emptyGrid.jpg");
                Image image = new Image(file.toURI().toString(), boardWidth / gridsInRowNumber, boardHeight / gridsInRowNumber, false, false);

                //Tak tez mozna, sam znajduje prawdziwa sciezke, kliknij z Ctrl i otworzy ci odrazu obrazek
                //Image image = new Image(this.getClass().getResource("/images/emptyGrid.jpg").getFile(),
                //      boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
                ImageView led = new ImageView();
                led.setImage(image);

                grids[i][b] = led;
                gridPanelBoard.add(grids[i][b], i, b);
            }
        }
    }
}
