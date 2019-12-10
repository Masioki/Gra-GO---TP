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
    private int whitPoints;
    private int blackPoints;


    private Map<Point, GridState> gridStateMap;
    //mapa do wyznaczenia grupy pionków
    private Map<Point, GameGrid> group;
    //mapa zawierająca stan planszy z poprzedniego ruchu
    private Map<Point, GridState> previousGridStateMap;
    public GameLogic(int size) {
        this.size = size;
        //grids = new Grid[size][size];
        //deklaracja planszy
        gridStateMap = new HashMap<>(size * size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridStateMap.put(new Point(i, j), GridState.EMPTY);
            }
        }
        //deklaracja mapy do tworzenia grup
        group = new HashMap<>(size * size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                group.put(new Point(i, j), new GameGrid());
            }
        }
        //deklaracja mapy przechowującej poprzedni ruch
        previousGridStateMap = new HashMap<>(size * size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                previousGridStateMap.put(new Point(i, j), GridState.EMPTY);
            }
        }
        //deklarujemy startowe punkty graczy
        whitPoints = 0;
        blackPoints = 0;
    }
    //x pierwsza współrzędna, y druga
    boolean placePawn(int x, int y, boolean white)
    {
        //upewniamy się że pole puste
        if( !gridStateMap.get(new Point(x,y)).equals(GridState.EMPTY) )
        {
            return false;
        }
        //żeby uniknąć rozpatrywania przypadków
        GridState gridState;
        if(white)
        {
            gridState = GridState.WHITE;
        }
        else
        {
            gridState = GridState.BLACK;
        }
        //sprawdzamy czy ruch jest niedozwolonym samobujstwem
        if(checkSuicide(x, y, white))
        {
            return false;
        }
        //wykonujemy ruch
        Map<Point, GridState> newGridStateMap;
        newGridStateMap = new HashMap<>(size * size);
        for (int i = 0; i < size; i++) {                       // nie jest taka czasochlonna
            for (int j = 0; j < size; j++) {
                gridStateMap.put(new Point(i, j), gridStateMap.get(new Point(i, j)));
            }
        }
        //zmieniamy nasze jedno pole
        newGridStateMap.replace(new Point(x,y), gridState);
        //updatujemy naszą mapę po wyonaniu ruchu
        updateMap(x,y,newGridStateMap);
        //sprawdzamy Komi
        if(checkKomi(newGridStateMap))
        {
            return false;
        }
        //zatwierdzamy zmianę mapy
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridStateMap.replace(new Point(i, j), newGridStateMap.get(new Point(i, j)));
            }
        }
        //TODO - trzeba nadpisać mapę z poprzedniego ruchu
        return true;



    }
    //metoda zwraca true kiedy nie można postawić pionka
    boolean checkSuicide(int x, int y, boolean white)
    {
        if(checkNormalSuicide(x,y,white))
        {
            if(checkSpecialSuicide(x,y,white))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    boolean checkNormalSuicide (int x, int y, boolean white)
    {
        GridState gridState;
        if(white)
        {
            gridState = GridState.WHITE;
        }
        else
        {
            gridState = GridState.BLACK;
        }

        return true;
    }

    boolean checkSpecialSuicide (int x, int y, boolean white)
    {
        return true;
    }

    //updatujemy mapę po wykonanym ruchu
    private Map<Point, GridState> updateMap(int x, int y, Map<Point, GridState> map)
    {
        GridState gridState = map.get(new Point(x,y));
        //TODO - cały kod
        return  map;
    }

    //funkcja zwraca true, jeśli zachodzi komi tzn. nie możemy wykonać ruchu
    private boolean checkKomi(Map<Point, GridState> map)
    {
        //TODO - wystarczy porównać mapę z mapą z poprzedniego ruchu, jeśli są takie same zwracamy true
        return true;
    }
}
