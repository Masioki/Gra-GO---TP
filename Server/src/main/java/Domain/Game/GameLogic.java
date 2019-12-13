package Domain.Game;

import java.awt.*;
import java.util.*;

class GameLogic {
    private int size;
    private int whitPoints;
    private int blackPoints;


    private Map<Point, GridState> gridStateMap;
    //mapa do wyznaczenia grupy pionków
    //private Map<Point, GameGrid> group;
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
        /*
        group = new HashMap<>(size * size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                group.put(new Point(i, j), new GameGrid());
            }
        }*/
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
        if (!gridStateMap.get(new Point(x, y)).equals(GridState.EMPTY)) return false;

        //żeby uniknąć rozpatrywania przypadków
        GridState gridState;
        if (white) gridState = GridState.WHITE;
        else gridState = GridState.BLACK;

        //sprawdzamy czy ruch jest niedozwolonym samobujstwem
        if (checkSuicide(x, y, white)) return false;

        //wykonujemy ruch
        Map<Point, GridState> newGridStateMap = new HashMap<>(gridStateMap);

        //zmieniamy nasze jedno pole
        newGridStateMap.replace(new Point(x, y), gridState);
        //updatujemy naszą mapę po wyonaniu ruchu
        updateMap(x, y, newGridStateMap);
        //sprawdzamy Komi
        if (checkKomi(newGridStateMap)) return false;

        //TODO - nadpisujemy mapę z poprzedniego ruchu
        previousGridStateMap = gridStateMap;

        //dodajemy punkty graczom po wykonanym ruchu
        countPoints( newGridStateMap, gridStateMap, x, y, gridState);

        //zatwierdzamy zmianę mapy
        gridStateMap = newGridStateMap;
        return true;
    }

    //metoda zwraca true kiedy nie można postawić pionka
    boolean checkSuicide(int x, int y, boolean white) {
        GridState gridState;
        if (white) gridState = GridState.WHITE;
        else gridState = GridState.BLACK;

        //tworzymy mapę którą przekazujemy do sprawdzania samobujstwa
        Map<Point, GridState> newMap = new HashMap<>(gridStateMap);

        //dodajemy pionek postawiony przez gracza
        newMap.replace(new Point(x, y), gridState);

        if (checkNormalSuicide(x, y, newMap)) return !checkSpecialSuicide(x, y, newMap);
        else return false;
    }

    //zwraca true kiedy popełniliśmy samobujstwo
    boolean checkNormalSuicide(int x, int y, Map<Point, GridState> map) {
        //TODO - można zrobić szybviej, wystarczy sprawdzić sąsiadów pionka ale kod byłby dłuższy
        //pobieramy grupę pionka i sprawdzamy czy jej nie zabiliśmy
        Map<Point, GameGrid> group = findGroup(x, y, map);

        //jeśli grupa ma zero oddechów, popełniliśmy samobójstwo
        return countGroupBreaths(group) == 0;
    }

    //zwraca true kiedy popełniliśmy specjalne samobujstwo
    boolean checkSpecialSuicide(int x, int y, Map<Point, GridState> map) {
        //kolor naszego pionka
        GridState gridState = map.get(new Point(x, y));

        //dla każdego sąsiada jeśli jest innego koloru niż nasz pionek
        //generujemy grupę i sprawdzamy czy ma zero oddechów
        int x1, y1;
        for (int i = 0; i < 3; i += 2) {
            for (int j = 0; j < 3; j += 2) {
                x1 = x - 1 + i;
                y1 = y - 1 + j;
                if (checkIfPointInsideBoard(x1, y1)) {
                    GridState checkedGridState = map.get(new Point(x1, y1));
                    if (!checkedGridState.equals(GridState.EMPTY) && !checkedGridState.equals(gridState)) {
                        Map<Point, GameGrid> group = findGroup(x1, y1, map);
                        int breaths = countGroupBreaths(group);
                        if (breaths == 0) return true;
                    }
                }
            }
        }
        return false;
    }

    //funkcja zwraca true, jeśli zachodzi komi tzn. nie możemy wykonać ruchu
    private boolean checkKomi(Map<Point, GridState> map) {
        //sprawdzamy czy rużnią się chociaż na jednym polu
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (!map.get(new Point(i, j)).equals(previousGridStateMap.get(new Point(i, j))))
                    return false;

        return true;
    }

    //funkcja zwraca grupę pionka
    private Map<Point, GameGrid> findGroup(int x, int y, Map<Point, GridState> map) {
        //zwracana mapa
        Map<Point, GameGrid> outcomeMap = new HashMap<>();
        GridState gridState = map.get(new Point(x, y));
        if (gridState.equals(GridState.EMPTY)) return null;

        //tabelka booleanów sprawdzających czy pole było odwiedzone
        //true - nieodwiedzone
        boolean[][] visited = new boolean[size][size];
        for (boolean[] b : visited) Arrays.fill(b, true);

        //kolejka pól którym sprawdzamy sąsiadów
        LinkedList<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        //uznajemy nasze pierwsze pole za odwiedzone
        visited[x][y] = false;
        //dodajemy je do wyniku
        outcomeMap.put(new Point(x, y), new GameGrid());

        //algorytm DFS
        while (queue.size() > 0) {
            //pozbywamy się sprawdzonego pola z kolejki
            Point point = queue.pollFirst();
            //zapisujemy współrzędne pobranego punktu
            x = point.x;
            y = point.y;
            //współrzędne sprawdzanych sąsiadów
            int x1, y1;
            //liczba oddechów pionka
            int breaths = 4;

            for (int i = 0; i < 3; i += 2) {
                for (int j = 0; j < 3; j += 2) {
                    // 1) x - 1 + 0, y - 1 + 0  == x - 1, y - 1
                    // 2) x - 1 + 0, y - 1 + 2  == x - 1, y + 1
                    // 3) x - 1 + 2, y - 1 + 0  == x + 1, y - 1;
                    // 4) x - 1 + 2, y - 1 + 2  == x + 1, y + 1
                    x1 = x - 1 + i;
                    y1 = y - 1 + j;
                    if (checkIfPointInsideBoard(x1, y1)) {
                        //jeśli jego sąsiad nie jest pusty zmniejszamy size
                        if (!map.get(new Point(x1, y1)).equals(GridState.EMPTY)) breaths--;
                        if (visited[x1][y1]) {
                            //zaznaczamy że odwiedziliśmy to pole
                            visited[x1][y1] = false;
                            //jeśli jest tego samego typu co nasz startowy punkt to dodajemy do listy
                            if (map.get(new Point(x1, y1)).equals(gridState)) {
                                queue.add(new Point(x1, y1));
                                //dodajemy punkt do mapy wynikowej
                                outcomeMap.put(new Point(x1, y1), new GameGrid());
                            }
                        }
                    } else breaths--;
                }
            }
            //ustawiamy oddechy
            outcomeMap.get(new Point(x, y)).setBreathsNumber(breaths);
        }
        return outcomeMap;
    }

    //funkcja zwraca true jeśli pionek leży wewnątrz szachownicy
    //w przeciwnym wypadku fałsz
    boolean checkIfPointInsideBoard(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    //funkcja zwraca nam ile oddechów ma grupa
    //map - mapa z wyrozniona grupa
    int countGroupBreaths(Map<Point, GameGrid> map) {
        int outcome = 0;
        for (GameGrid gameGrid : map.values()) outcome += gameGrid.getBreathsNumber();
        return outcome;
    }

    //updatujemy mapę po wykonanym ruchu
    private Map<Point, GridState> updateMap(int x, int y, Map<Point, GridState> map) {
        GridState gridState = map.get(new Point(x, y));
        //ten if nigdy nie powinien mieć miejsca
        if(gridState.equals(GridState.EMPTY))
        {
            return null;
        }
        //tworzymy grupę którą potencjalnie musimy usunąć
        Map<Point, GameGrid> group = findGroup(x,y,map);
        int breaths = countGroupBreaths(group);
        //jeśli oddechy 0 usuwamy naszą grupę
        if(breaths==0)
        {
            //TODO-iterujemy po mapie
            Set< Map.Entry< Point,GameGrid> > st = group.entrySet();
            for (Map.Entry< Point,GameGrid> me:st)
            {
                //usuwamy pionki
                map.replace(me.getKey(),GridState.EMPTY);
            }
        }
        //sprawdzamy sąsiadów, w razie czego im też kasujemy grupy
        int x1, y1;
        for (int i = 0; i < 3; i += 2) {
            for (int j = 0; j < 3; j += 2) {
                x1 = x - 1 + i;
                y1 = y - 1 + j;
                //jeśli sąsiad jest innego koloru niż nasz pionek
                //sprawdzamy czy nie powinniśmy jego grupy usunąć
                if( ! map.get(new Point(x1,y1)).equals(GridState.EMPTY) && ! map.get(new Point(x1,y1)).equals(gridState) )
                {
                    //tworzymy grupę którą potencjalnie musimy usunąć
                    group = findGroup(x1,y1,map);
                    breaths = countGroupBreaths(group);
                    //jeśli oddechy 0 usuwamy naszą grupę
                    if(breaths==0)
                    {
                        Set< Map.Entry< Point,GameGrid> > st = group.entrySet();
                        for (Map.Entry< Point,GameGrid> me:st)
                        {
                            //usuwamy pionki
                            map.replace(me.getKey(),GridState.EMPTY);
                        }
                    }
                }
            }
        }
        //zwracamy naszą wejściową mapę troszkę oczyszczoną
        return map;
    }

    //funkcja licząca punkty za zbite pionki
    private void countPoints( Map<Point, GridState> newMap, Map<Point, GridState> previousMap, int x, int y, GridState gridState)
    {
        //bardzo ważne najpierw musimy dołożyć do mapy ten pionek który stawiamy
        previousMap.replace(new Point(x, y), gridState);

        GridState newGridState;
        GridState previousGridState;
        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                newGridState = newMap.get(new Point(i,j));
                previousGridState = previousMap.get(new Point(i,j));
                //przydzielamy punkty jeśli pole nie było puste a stało się puste
                if( ! previousGridState.equals(GridState.EMPTY) && newGridState.equals(GridState.EMPTY))
                {
                    //przydzielamy punkty jeśli czarny zbił pionka
                    if(previousGridState.equals(GridState.WHITE))
                    {
                        blackPoints++;
                    }
                    else // przeciwny przypadek
                    {
                        whitPoints++;
                    }
                }
            }
        }
    }
}
