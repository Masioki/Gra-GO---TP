package Services;

import Domain.Client;
import Domain.Game;
import DTO.GameData;

import java.util.List;

//Koniecznie singleton
public class GameService {
    List<Game> games;
    private static GameService gameService;

    private GameService() {

    }

    //Double checked locking
    public static GameService getInstance() {
        if (gameService == null) {
            synchronized (GameService.class) {
                if (gameService == null) {
                    gameService = new GameService();
                }
                return gameService;
            }
        }
        return gameService;
    }

    public void addGame() {

    }

    public Game joinGame(GameData gameData, Client client) {

    }

    public void endGame(Game game,Client client) {
        //skoncz gre jako poddanie osoby konczacej
        //wykonaj jakas robote typu staty do bazy
        //usun gre z listy
        //ewentualnie jakos usunac referencje do gry od przeciwnika
        //ale i tak jesli zacznie nowa gre albo sie rozlaczy to Garbage Collector usunie
    }
}
