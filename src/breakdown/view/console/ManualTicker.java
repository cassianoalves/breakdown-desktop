/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package breakdown.view.console;

import breakdown.engine.GameState;
import breakdown.sprite.Block;
import breakdown.basics.Color;
import java.io.Console;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cassiano
 */
public class ManualTicker {


    static public void drawGrid(GameState gs) {
        System.out.print("  |");

        for (int i = 0; i
                < GameState.getMAX_COLS(); i++) {
            System.out.printf("%02d|", i);
        }
        System.out.println();

        for (int i = (GameState.getMAX_ROWS() - 1); i
                >= 0; i--) {
            System.out.printf("%02d|", i);

            for (Block block : gs.getGameGrid()[i]) {
                String colorName;

                if (block == null) {
                    colorName = "__|";
                } else if (block.getColor().equals(Color.BLUE)) {
                    colorName = "BB|";
                } else if (block.getColor().equals(Color.GREEN)) {
                    colorName = "GG|";
                } else if (block.getColor().equals(Color.YELLOW)) {
                    colorName = "YY|";
                } else if (block.getColor().equals(Color.RED)) {
                    colorName = "RR|";
                } else if (block.getColor() == null) {
                    colorName = "==|";
                } else {
                    colorName = "??|";
                }
                System.out.print(colorName);
            }
            System.out.println();
        }
        System.out.print("---");

        for (int i = 0; i
                < GameState.getMAX_COLS(); i++) {
            System.out.printf("---", i);
        }
        System.out.println();

        if (gs.getNewGameLine() == null) {
            System.out.print("***");

            for (int i = 0; i
                    < GameState.getMAX_COLS(); i++) {
                System.out.printf("***", i);
            }
            System.out.println();
        } else {
            System.out.print("=> ");

            for (Block block : gs.getNewGameLine()) {
                String colorName;

                if (block == null) {
                    colorName = "__|";

                } else if (block.getColor() == Color.BLUE) {
                    colorName = "BB|";
                } else if (block.getColor() == Color.GREEN) {
                    colorName = "GG|";
                } else if (block.getColor() == Color.YELLOW) {
                    colorName = "YY|";
                } else if (block.getColor() == Color.RED) {
                    colorName = "RR|";
                } else if (block.getColor() == null) {
                    colorName = "__|";
                } else {
                    colorName = "??|";
                }
                System.out.print(colorName);
            }
            System.out.println();
        }
        System.out.println("=========" + (gs.isGameOver() ? " BROKEN " : " GO  ON ") + "== SCORE: " + gs.getScore() + " ==");
    }


    static public void main(String[] args) {
        GameState gs = new GameState();
        Block[][] gameGrid = gs.getGameGrid();
        Console console = System.console();

        boolean insertBreakPoint = !(console == null);

        while (true) {

            drawGrid(gs);
            gs.advanceStep();

            if (insertBreakPoint) {
                String s = console.readLine("X: ");

                if (s.length() != 0) {
                    int x = Integer.parseInt(s);
                    s = System.console().readLine("Y: ");

                    int y = Integer.parseInt(s);
                    gs.breakDown(x, y);
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameState.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }
}
