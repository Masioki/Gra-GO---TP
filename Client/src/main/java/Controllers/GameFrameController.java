package Controllers;
import Commands.GameCommandType;
import Services.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import static java.lang.StrictMath.abs;

public class GameFrameController implements EventHandler<ActionEvent> {

    public GridPane gridPanelBoard;
    public AnchorPane gameBoardFrame;
    public Button buttonPass;
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
    private final int boardWidth = 620;
    private final int boardHeight = 620;
    private Image imageEmptyGrid;
    private Image imagePlayerPawn;
    private Image imageEnemyPawn;


    //ustawiamy zdjęcia bezpośrednio ze ścieżek
    private void setUpImages()
    {
        imageEmptyGrid =  new Image("file:"+this.getClass().getResource("/images/emptyGrid.jpg").getFile(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        imagePlayerPawn = new Image("file:"+this.getClass().getResource("/images/gridWhitePawn.jpg").getFile(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
        imageEnemyPawn = new Image("file:"+this.getClass().getResource("/images/gridBlackPawn.jpg").getFile(),boardWidth/gridsInRowNumber,boardHeight/gridsInRowNumber,false,false);
    }
    @FXML
    public void initialize() {
        setUpImages();
        gridPanelBoard.setPrefWidth(boardWidth);
        gridPanelBoard.setPrefHeight(boardWidth);
        gridPanelBoard.setMaxWidth(boardHeight);
        gridPanelBoard.setMaxHeight(boardHeight);
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
                    tryPlacePawn(colNumber, rowNumber);

                });
                grids[i][b] = myImageView;
                gridPanelBoard.add(grids[i][b], i, b);
            }
        }
        service = Service.getInstance();
        labelError.setVisible(false);
        gridPanelBoard.getStylesheets().add(getClass().getResource("/css/gridPaneStylesheet.css").toExternalForm());
        buttonPass.setOnAction(this);
        buttonSurrender.setOnAction(this);
    }
    public void showError()
    {
        labelError.setVisible(true);
    }

    /**
     * Metoda sprawdza czy pole jest puste i jeśli tak wysyła zappytanie do serwisu
     * @param colNumber
     * @param rowNumber
     */
    private void tryPlacePawn(int colNumber, int rowNumber)
    {
        if(grids[colNumber][rowNumber].gridstate.equals(GRIDSTATE.FULL))
        {
            System.out.println("ZAJĘTE!");
        }
        else
        {
            //service.gameMove(x,y, GameCommandType typ ruchu(MOVE,SURRENDER etc.))
            //TODO-tutaj wysyłamy do serwisu zapytanie o postawienie pionka zamiast od razu go postawić
            placePawn(colNumber, rowNumber);
        }
    }
    /**
     * Metoda stawia pionka, powinna być wywoływana przez serwis
     * @param colNumber
     * @param rowNumber
     */
    private void placePawn(int colNumber, int rowNumber)
    {
        grids[colNumber][rowNumber].gridstate = GRIDSTATE.FULL;
        grids[colNumber][rowNumber].setImage(imagePlayerPawn);

        //na potrzeby prezentacji dodaje to tutaj
        //service.gameMove(colNumber,rowNumber, GameCommandType.MOVE);
    }
    @Override
    public void handle(ActionEvent e) {
        if(e.getSource().equals(buttonPass))
        {
            /*TODO-tutaj powinniśmy wywoływać metodę z servera*/
            System.out.println("Pass");
            //service.gameMove(x,y, GameCommandType typ ruchu(MOVE,SURRENDER etc.))
        }
        if(e.getSource().equals(buttonSurrender))
        {
            /*TODO-tutaj powinniśmy wywoływać metodę z servera*/
            System.out.println("Give up!");
        }
    }
}
