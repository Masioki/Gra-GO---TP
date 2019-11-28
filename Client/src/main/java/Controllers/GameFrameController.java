package Controllers;
import Services.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.File;

import static java.lang.StrictMath.abs;

public class GameFrameController implements EventHandler<ActionEvent> {

    public GridPane gridPanelBoard;
    public AnchorPane gameBoardFrame;
    public Button buttonPause;
    public Button buttonSurrender;
    public Label labelCapturedPawnsPlayer;
    public Label labelCapturedPawnsEnemy;
    private MyImageView[][] grids;

    private Service service = null;
    private final int gridsInRowNumber = 19;
    private final int boardWidth = 650;
    private final int boardHeight = 650;
    private Image imageEmptyGrid;
    private Image imagePlayerPawn;
    private Image imageEnemyPawn;
    //ustawiamy zdjęcia bezpośrednio ze ścieżek
    private void setUpImages()
    {
        File file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\emptyGrid.jpg");
        imageEmptyGrid = new Image(file.toURI().toString(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\gridWhitePawn.jpg");
        imagePlayerPawn = new Image(file.toURI().toString(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\gridBlackPawn.jpg");
        imageEnemyPawn = new Image(file.toURI().toString(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
    }
    public void initialize() {
        setUpImages();
        gridPanelBoard.setPrefWidth(boardWidth);
        gridPanelBoard.setPrefHeight(boardWidth);
        gridPanelBoard.setMaxWidth(boardHeight);
        gridPanelBoard.setMaxHeight(boardHeight);
        //ten fragment kodu powoduje że pojawiają się te brzydkie białe linie
        /*for(int i = 0; i < gridsInRowNumber; i++)
        {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / gridsInRowNumber);
            gridPanelBoard.getColumnConstraints().add(colConst);
        }
        for(int i = 0; i < gridsInRowNumber; i++)
        {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / gridsInRowNumber);
            gridPanelBoard.getRowConstraints().add(rowConst);
        }*/
        grids = new MyImageView[gridsInRowNumber][gridsInRowNumber];
        for (int i = 0; i < gridsInRowNumber; i++) {
            for(int b= 0; b< gridsInRowNumber; b++) {
                MyImageView myImageView = new MyImageView();
                myImageView.setImage(imageEmptyGrid);
                myImageView.setRowNumber(b);
                myImageView.setColumnNumber(i);
                myImageView.setOnMouseClicked((event) -> {
                    int colNumber = myImageView.getColumnNumber();
                    int rowNumber = myImageView.getRowNumber();
                    placePawn(colNumber, rowNumber);

                });
                grids[i][b] = myImageView;
                gridPanelBoard.add(grids[i][b], i, b);
            }
        }
        service = Service.getInstance();
    }
    private void placePawn(int colNumber, int rowNumber)
    {
        grids[colNumber][rowNumber].setImage(imagePlayerPawn);
    }
    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
