package Domain;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameLogic {
    private int size;
    //private Grid[][] grids;
    /*
    Zamiast Grid ktory ma tylko GridState lepiej zrobic mape GridState.
    Daje to kilka opcji typu .replace(klucz,wartosc) .put(k,w) .get(klucz)

    Przyklad  gridStateMap.get(new Point(x,y)) daje nam odrazu stan na danej pozycji

    Obiekt java.awt.Point jest lekki, ale jak chcesz mozna zrobic swoj jeszcze lzejszy
    Wtedy zeby ta mapa dzialala trzeba tylko nadpisac metode equals()
     */
    private Map<Point, GridState> gridStateMap;

    public GameLogic(int size) {
        this.size = size;
        //grids = new Grid[size][size];
        gridStateMap = new HashMap<>(size * size); // dzieki argumentowi konstruktora Map operacja wstawiania ponizej
        for (int i = 1; i <= size; i++) {                       // nie jest taka czasochlonna
            for (int j = 1; j <= size; j++) {
                gridStateMap.put(new Point(i, j), GridState.EMPTY);
            }
        }
    }


}
