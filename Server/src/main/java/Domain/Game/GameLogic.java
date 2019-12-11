package Domain.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class GameLogic {
    private int size;
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
    //metoda zwraca fałsz gdy nie uda się postawić pionka
    //prawdę w przeciwnym wypadku
    boolean placePawn(int x, int y, boolean white) {
        //upewniamy się że pole puste
        if (!gridStateMap.get(new Point(x, y)).equals(GridState.EMPTY)) {
            return false;
        }
        //żeby uniknąć rozpatrywania przypadków
        GridState gridState;
        if (white) {
            gridState = GridState.WHITE;
        } else {
            gridState = GridState.BLACK;
        }
        //sprawdzamy czy ruch jest niedozwolonym samobujstwem
        if (checkSuicide(x, y, white)) {
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
        newGridStateMap.replace(new Point(x, y), gridState);
        //updatujemy naszą mapę po wyonaniu ruchu
        updateMap(x, y, newGridStateMap);
        //sprawdzamy Komi
        if (checkKomi(newGridStateMap)) {
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
    boolean checkSuicide(int x, int y, boolean white) {
        if (checkNormalSuicide(x, y, white)) {
            if (checkSpecialSuicide(x, y, white)) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    boolean checkNormalSuicide(int x, int y, boolean white) {
        GridState gridState;
        if (white) {
            gridState = GridState.WHITE;
        } else {
            gridState = GridState.BLACK;
        }
        return true;
    }

    boolean checkSpecialSuicide(int x, int y, boolean white) {
        return true;
    }

    //updatujemy mapę po wykonanym ruchu
    private Map<Point, GridState> updateMap(int x, int y, Map<Point, GridState> map) {
        GridState gridState = map.get(new Point(x, y));
        //TODO - cały kod
        return map;
    }

    //funkcja zwraca true, jeśli zachodzi komi tzn. nie możemy wykonać ruchu
    private boolean checkKomi(Map<Point, GridState> map) {
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                //sprawdzamy czy rużnią się chociaż na jednym polu
                if(! map.get(new Point(i,j)).equals(previousGridStateMap.get( new Point(i,j) ) ) )
                {
                    return false;
                }
            }
        }
        return true;
    }

    //funkcja zwraca grupę pionka
    private Map<Point, GameGrid> findGroup(int x, int y, Map<Point, GridState> map)
    {
        //zwracana mapa
        Map<Point, GameGrid> outcomeMap = new HashMap<>();
        GridState gridState = map.get(new Point(x,y));
        if(gridState.equals(GridState.EMPTY))
        {
            return null;
        }
        //tabelka booleanów sprawdzających czy pole było odwiedzone
        //true - nieodwiedzone
        boolean [][]visited;
        visited = new boolean[size][size];
        //ustawiamy każde pole jako nieodwiedzone
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                visited[i][j] = true;
            }
        }
        //kolejka pól którym sprawdzamy sąsiadów
        ArrayList<Point> queue = new ArrayList<Point>();
        queue.add(new Point(x,y));
        //uznajemy nasze pierwsze pole za odwiedzone
        visited[x][y]=false;
        //dodajemy je do wyniku
        outcomeMap.put(new Point(x,y), new GameGrid());
        //algorytm DFS
        while (queue.size()>0)
        {
            Point point = queue.get(0);
            //pozbywamy się sprawdzonego pola z kolejki
            queue.remove(0);
            //zapisujemy współrzędne pobranego punktu
            x = point.x;
            y = point.y;
            //współrzędne sprawdzanych sąsiadów
            int x1, y1;
            //liczba oddechów pionka
            int breaths = 4;
            //sprawdzamy pierwszy punkt
            x1 = x + 1;
            y1 = y + 1;
            if(checkIfPointInsideBoard(x1,y1) )
            {
                //jeśli jego sąsiad nie jest pusty zmniejszamy size
                if(!map.get(new Point(x1,y1)).equals(GridState.EMPTY))
                {
                    breaths--;
                }

                if(visited[x1][y1])
                {
                    //zaznaczamy że odwiedziliśmy to pole
                    visited[x1][y1] = false;
                    //jeśli jest tego samego typu co nasz startowy punkt to dodajemy do listy
                    if( map.get(new Point(x1, y1)).equals(gridState) )
                    {
                        queue.add(new Point(x1, y1));
                        //dodajemy punkt do mapy wynikowej
                        outcomeMap.put(new Point(x1,y1), new GameGrid());
                    }
                }
            }
            else
            {
                breaths--;
            }
            //sprawdzamy drugi punkt
            x1 = x - 1;
            y1 = y + 1;
            if(checkIfPointInsideBoard(x1,y1) )
            {
                //jeśli jego sąsiad nie jest pusty zmniejszamy size
                if(!map.get(new Point(x1,y1)).equals(GridState.EMPTY))
                {
                    breaths--;
                }

                if(visited[x1][y1])
                {
                    //zaznaczamy że odwiedziliśmy to pole
                    visited[x1][y1] = false;
                    //jeśli jest tego samego typu co nasz startowy punkt to dodajemy do listy
                    if( map.get(new Point(x1, y1)).equals(gridState) )
                    {
                        queue.add(new Point(x1, y1));
                        //dodajemy punkt do mapy wynikowej
                        outcomeMap.put(new Point(x1,y1), new GameGrid());
                    }
                }
            }
            else
            {
                breaths--;
            }

            //sprawdzamy trzeci punkt
            x1 = x + 1;
            y1 = y - 1;
            if(checkIfPointInsideBoard(x1,y1) )
            {
                //jeśli jego sąsiad nie jest pusty zmniejszamy size
                if(!map.get(new Point(x1,y1)).equals(GridState.EMPTY))
                {
                    breaths--;
                }

                if(visited[x1][y1])
                {
                    //zaznaczamy że odwiedziliśmy to pole
                    visited[x1][y1] = false;
                    //jeśli jest tego samego typu co nasz startowy punkt to dodajemy do listy
                    if( map.get(new Point(x1, y1)).equals(gridState) )
                    {
                        queue.add(new Point(x1, y1));
                        //dodajemy punkt do mapy wynikowej
                        outcomeMap.put(new Point(x1,y1), new GameGrid());
                    }
                }
            }
            else
            {
                breaths--;
            }

            //sprawdzamy czwarty punkt
            x1 = x - 1;
            y1 = y - 1;
            if(checkIfPointInsideBoard(x1,y1) )
            {
                //jeśli jego sąsiad nie jest pusty zmniejszamy size
                if(!map.get(new Point(x1,y1)).equals(GridState.EMPTY))
                {
                    breaths--;
                }

                if(visited[x1][y1])
                {
                    //zaznaczamy że odwiedziliśmy to pole
                    visited[x1][y1] = false;
                    //jeśli jest tego samego typu co nasz startowy punkt to dodajemy do listy
                    if( map.get(new Point(x1, y1)).equals(gridState) )
                    {
                        queue.add(new Point(x1, y1));
                        //dodajemy punkt do mapy wynikowej
                        outcomeMap.put(new Point(x1,y1), new GameGrid());
                    }
                }
            }
            else
            {
                breaths--;
            }
            //ustawiamy oddechy
            outcomeMap.get(new Point(x,y)).setBreathsNumber(breaths);
        }
        return outcomeMap;
    }
    //funkcja zwraca true jeśli pionek leży wewnątrz szachownicy
    //w przeciwnym wypadku fałsz
    boolean checkIfPointInsideBoard(int x, int y)
    {
        if(x >= 0 && x < size && y >=0 && y < size)
        {
            return true;
        }
        return false;
    }
}
