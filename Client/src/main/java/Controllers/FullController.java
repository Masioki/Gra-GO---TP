package Controllers;

import Commands.GameCommandType;
import Commands.PawnColor;
import Domain.GameData;

import java.util.List;

public abstract class FullController {

    /**
     * Metoda wyswietlajaca wiadomosc bledu
     *
     * @param message wiadomosc bledu
     */
    public abstract void error(String message);

    /**
     * Metoda informujaca czy udalo sie zalogowac
     *
     * @param success true jesli sie udalo, false nie udalo
     */
    public void logIn(boolean success) {

    }

    /**
     * Metoda wyswietlajaca aktywne gry
     *
     * @param games dane aktywnych gier
     * @see GameData
     */
    public void loadActiveGames(List<GameData> games) {

    }

    /**
     * Metoda ktora informuje o dolaczeniu do gry
     *
     * @param data informacje o grze, gdzie Username to nazwa wlasciciela gry, czyli osoby o bialych pionkach.
     * @see GameData
     */
    public void joinGame(GameData data) {

    }

    /**
     * Metoda informujaca o wykonanym ruchu
     *
     * @param x  wspolrzedna x
     * @param y  wspolrzdna y
     * @param me true - ja, false - przeciwnik
     */
    public void move(int x, int y, PawnColor color) {

    }

    /**
     * Metoda dodatkowych akcji w grze, czyli Surrender, Pass, Continue itd.
     *
     * @param actionType typ akcji
     * @param me         true - ja, false - przeciwnik
     * @see GameCommandType
     */
    public void gameAction(GameCommandType actionType, boolean me) {

    }

}
