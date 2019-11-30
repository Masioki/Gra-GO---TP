package Controllers;
import Services.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.File;

import static java.lang.StrictMath.abs;

public class GameFrameController implements EventHandler<ActionEvent> {

    public GridPane gridPanelBoard;
    public AnchorPane gameBoardFrame;
    public Button buttonPause;
    public Button buttonSurrender;
    public Label labelCapturedPawns;
    public Label labelCapturedPawnsEnemy;
    public Label labelLogin;
    public Label labelTurn;
    public Label labelCapturedPawnsEnemyScore;
    public Label labelCapturedPawnsScore;
    public Label labelError;
    private MyImageView[][] grids;

    private Service service = null;
    private final int gridsInRowNumber = 19;
    private final int boardWidth = 650;
    private final int boardHeight = 650;
    private Image imageEmptyGrid;
    private Image imagePlayerPawn;
    private Image imageEnemyPawn;

    private void customizeFrame()
    {
        Color blue = Color.BLUE;
        BackgroundFill backgroundFillBlue = new BackgroundFill(blue, CornerRadii.EMPTY, Insets.EMPTY);
        Background backgroundBlue = new Background(backgroundFillBlue);
        Color red = Color.RED;
        BackgroundFill backgroundFillRed = new BackgroundFill(red, CornerRadii.EMPTY, Insets.EMPTY);
        Background backgroundRed = new Background(backgroundFillRed);
        //customize labels
        labelCapturedPawns.setBackground(backgroundBlue);
        labelCapturedPawnsEnemy.setBackground(backgroundBlue);
        labelLogin.setBackground(backgroundBlue);
        labelTurn.setBackground(backgroundBlue);
        labelCapturedPawnsEnemyScore.setBackground(backgroundBlue);
        labelCapturedPawnsScore.setBackground(backgroundBlue);
        //customize buttons
        buttonPause.setBackground(backgroundRed);
        buttonSurrender.setBackground(backgroundRed);
    }
    //ustawiamy zdjęcia bezpośrednio ze ścieżek
    private void setUpImages()
    {
        //File file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\emptyGrid.jpg");
        //System.out.println(this.getClass().getResource("/images/emptyGrid.jpg").getFile() + "\n" + file.toURI().toString());
        imageEmptyGrid =  new Image("file:"+this.getClass().getResource("/images/emptyGrid.jpg").getFile(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        //new Image(file.toURI().toString(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        //file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\gridWhitePawn.jpg");
        imagePlayerPawn = new Image("file:"+this.getClass().getResource("/images/gridWhitePawn.jpg").getFile(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        //        new Image(file.toURI().toString(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        //file = new File("C:\\Users\\ciche\\IdeaProjects\\Gra-GO---TP\\Client\\src\\main\\resources\\images\\gridBlackPawn.jpg");
        imageEnemyPawn = new Image("file:"+this.getClass().getResource("/images/gridBlackPawn.jpg").getFile(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        //        new Image(file.toURI().toString(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
    }
    @FXML
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
        labelError.setVisible(false);
        customizeFrame();
    }
    public void showError()
    {
        labelError.setVisible(true);
    }
    private void placePawn(int colNumber, int rowNumber)
    {
        grids[colNumber][rowNumber].setImage(imagePlayerPawn);
    }
    @Override
    public void handle(ActionEvent actionEvent) {

    }
}
