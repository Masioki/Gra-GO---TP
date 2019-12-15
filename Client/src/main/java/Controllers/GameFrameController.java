package Controllers;

import Commands.GameCommandType;
import Commands.PawnColor;
import Services.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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

        //ustawiamy kontroler
        FullController controller = new FullController() {
            @Override
            public void error(String message) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("ERROR");
                            alert.setHeaderText("Sadly error occurred cannot do anything about it...");
                            alert.setContentText(message);
                            alert.showAndWait();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void move(int x, int y, PawnColor color) {
                placePawn(x,y,color);
            }

            @Override
            public void setScore(int your, int opponent) {
                //TODO - to dołożyliśmy
                showScore(your, opponent);
            }

            @Override
            public void gameAction(GameCommandType actionType, boolean me) {
                //TODO - to dołożyliśmy
                switch (actionType){
                    case WIN:
                        if(me)
                        {

                        }
                        break;
                    case LOSE:
                        if(me)
                        {

                        }
                        break;
                    case PASS:
                        pass(me);
                        break;
                }
            }
        };
        service.setFullController(controller);
    }

    /**
     * Metoda ustawia aktualny wynik (zbite pionki) obu graczy
     * @param mine - wynik gracza
     * @param opponent - wynik przeciwnika
     */
    private void showScore(int mine, int opponent)
    {
        labelCapturedPawnsScore.setText(Integer.toString(mine));
        labelCapturedPawnsEnemyScore.setText(Integer.toString(opponent));
    }

    /**
     * Metoda wyświetla komunikat o spasowaniu przez odpowiedniego gracza
     * @param me - true, oznacza że gracz spasował, fałsz, że przeciwnik
     */
    private void pass(boolean me)
    {
        String ms;
        if(me)
        {
            ms = "Wykonano PASS, oczekiwanie na decyzję przeciwnika";
        }
        else
        {
            ms = "Przeciwnik wykonał PASS";
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("PASS");
                    alert.setHeaderText(ms);
                    alert.showAndWait();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void victory()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("VICTORY");
                    alert.setHeaderText("Zwyciężyełeś :) ");
                    alert.showAndWait();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        System.exit(1);
    }

    private void defeat()
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("PASS");
                    alert.setHeaderText("Przegrałeś. Niestety... :/");
                    alert.showAndWait();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        System.exit(1);
    }
    /**
     * Metoda sprawdza czy pole jest puste i jeśli tak wysyła zapytanie do serwisu
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
            service.gameMove(colNumber,rowNumber, GameCommandType.MOVE);
        }
    }
    /**
     * Metoda stawia pionka, powinna być wywoływana przez serwis
     * @param colNumber
     * @param rowNumber
     */
    private void placePawn(int colNumber, int rowNumber, PawnColor color)
    {
        if(color == PawnColor.WHITE)
        {
            grids[colNumber][rowNumber].setImage(imagePlayerPawn);
            grids[colNumber][rowNumber].gridstate = GRIDSTATE.FULL;
        }
        else if(color == PawnColor.BLACK)
        {
            grids[colNumber][rowNumber].setImage(imageEnemyPawn);
            grids[colNumber][rowNumber].gridstate = GRIDSTATE.FULL;
        }
        else if(color == PawnColor.EMPTY){
            grids[colNumber][rowNumber].gridstate = GRIDSTATE.EMPTY;
            grids[colNumber][rowNumber].setImage(imageEmptyGrid);
        }
    }

    @Override
    public void handle(ActionEvent e) {
        if(e.getSource().equals(buttonPass))
        {
            /*TODO-to dołożyliśmy*/
            service.gameMove(0,0, GameCommandType.PASS);
            //service.gameMove(x,y, GameCommandType typ ruchu(MOVE,SURRENDER etc.))
        }
        if(e.getSource().equals(buttonSurrender))
        {
            /*TODO-to dołożyliśmy*/
            service.gameMove(0,0, GameCommandType.SURRENDER);
        }
    }
}
