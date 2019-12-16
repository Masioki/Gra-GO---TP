package Domain;

import Commands.GameCommandType;
import Commands.PawnColor;
import Domain.Game.Game;
import Domain.Game.GameObserver;
import Domain.Game.GridState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.util.Map;

public class Bot extends Player implements GameObserver {

    private Map<Point, PawnColor> board;

    public Bot(int boardSize, Game game) {
        this.game = game;
        username = "bot";
        board = new HashMap<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board.put(new Point(i, j), PawnColor.EMPTY);
            }
        }
        Runnable r = () -> {
            while (game.inProgress()) {
                doMove();
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    @Override
    public void action(int x, int y, String username, PawnColor color, GameCommandType type) {
        if (type == GameCommandType.MOVE) board.replace(new Point(x, y), color);
    }

    private synchronized void doMove() {
        int max = Integer.MIN_VALUE;
        Point bestPoint = new Point(0, 0);

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
        int x = (int) Math.round(bestPoint.getX());
        int y = (int) Math.round(bestPoint.getY());
        System.out.println(x + " " + y);
        //znajduje najlepsze miejsce ale jakims cudem zwracane jest false przy probie ruszenia tam
        boolean result = move(x, y);
        System.out.println(result);
        if (!result) {
            moveToRandom();
        }
    }

    //obliczamy rozmiar grupy rekurencyjnie
    //dobrze znajduje
    private int countGroupSize(Point point, List<Point> checked) {
        int sum = 1;
        checked.add(point);
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX(), (int) point.getY() + i - 1);
            if (!checked.contains(temp) && board.get(temp) == PawnColor.BLACK) {
                sum += countGroupSize(temp, checked);
            }
        }
        for (int i = 0; i < 3; i += 2) {
            Point temp = new Point((int) point.getX() + i - 1, (int) point.getY());
            if (!checked.contains(temp) && board.get(temp) == PawnColor.BLACK) {
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

        boolean done = false;
        for (Point p : emptyPlaceList) {
            if (move((int) p.getX(), (int) p.getY())) {
                done = true;
                break;
            }
        }
        if (!done) pass();
    }


}
