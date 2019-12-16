package Domain;

import Commands.GameCommandType;
import Commands.PawnColor;
import Domain.Game.GameObserver;
import Domain.Game.GridState;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Map;

public class Bot extends Player implements GameObserver {

    private Map<Point, PawnColor> board;

    public Bot() {
        username = "bot";
    }

    @Override
    public void action(int x, int y, String username, PawnColor color, GameCommandType type) {
        if (type == GameCommandType.MOVE) board.put(new Point(x, y), color);
        if (game.getTurn().equals(username)) doMove();
    }

    private void doMove() {
        int max = Integer.MIN_VALUE;
        Point bestPoint = null;

        for (Point p : board.keySet()) {
            if (board.get(p) == PawnColor.EMPTY) {
                List<Point> list = new ArrayList<>();
                list.add(p);
                int temp = countGroupSize(p, list);
                if (temp > max) {
                    max = temp;
                    bestPoint = p;
                }
            }
        }
        //zabezpieczenie przed jakims skrajnym nieprzewidzianym przypadkiem
        if (bestPoint == null || !move((int) bestPoint.getX(), (int) bestPoint.getY())) {
            moveToRandom();
        }
    }

    //obliczamy rozmiar grupy rekurencyjnie
    private int countGroupSize(Point point, List<Point> checked) {
        int sum = 1;
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX(), (int) point.getY() + i - 1);
            if (!checked.contains(temp) && board.get(temp) == PawnColor.BLACK) {
                checked.add(temp);
                sum += countGroupSize(temp, checked);
            }
        }
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX() + i - 1, (int) point.getY());
            if (!checked.contains(temp) && board.get(temp) == PawnColor.BLACK) {
                checked.add(temp);
                sum += countGroupSize(temp, checked);
            }
        }
        return sum;
    }

    private void moveToRandom() {
        List<Point> emptyPlaceList = new ArrayList<>();
        for (Point p : board.keySet()) {
            if (board.get(p) == PawnColor.EMPTY) emptyPlaceList.add(p);
        }

        if (emptyPlaceList.size() == 0) {
            pass();
            return;
        }

        //troche kontrowersyjny kod, ma za zadanie znalezc jakiekolwiek miejsce,
        // jesli nie byloby zapetlone to gra moglaby sie zablokowac
        boolean moveResult = false;
        do {
            Point randomPoint = emptyPlaceList.get((int) Math.round(Math.random() * (emptyPlaceList.size() - 1)));
            moveResult = move((int) randomPoint.getX(), (int) randomPoint.getY());
        } while (!moveResult);
    }
}
