/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package breakdown.engine;

import breakdown.sprite.Block;
import breakdown.basics.Point;

/**
 *
 * @author Cassiano
 */
public class GameState {
//    private static final Logger logger = Logger.getLogger(GameState.class.getName());

    /**
     * Configuracoes do jogo
     */
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLS = 20;
    private static final int MIN_BLOCK_TO_SCORE = 3;
    private static final double SCORE_BLOCK_FACTOR = 0.1;
    private static final double SCORE_BLOCK_UNIT = 10.0;
    private static final long LEVEL_INCREASE_SCORE_DIVIDER = 1000;
    private static final double MIN_VELOCITY = 1.0; // Ticks per second
    private static final double VELOCITY_FACTOR = 0.5; // Ticks per second

    /**
     * Propriedades
     */
    private GameView gameView;
    private Block[][] gameGrid = new Block[MAX_ROWS][MAX_COLS];
    private Block[] newGameLine;
    private boolean gameOver;
    private int newBlockPosition = 0;
    private long score = 0;
    private int level = 1;

    public GameState() {
        gameView = new GameView() {
            public void updateScore() {}
            public void updateGameGrid() {}
            public void updateGameNewLine() {}
        };
    }

    public Block[][] getGameGrid() {
        return gameGrid;
    }

    public static int getMAX_COLS() {
        return MAX_COLS;
    }

    public static int getMAX_ROWS() {
        return MAX_ROWS;
    }

    public Block[] getNewGameLine() {
        return newGameLine;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private void makeBlockFall(int startX, int startY) {
        int x = startX;
        int y = startY;

        while (((y - 1) >= 0) && (gameGrid[y - 1][x]) == null) {
            y--;
        }
        Block block = gameGrid[startY][startX];
        gameGrid[startY][startX] = null;
        gameGrid[y][x] = block;
    }

    private void makePartsJoin() {
        int start = 0;
        int end = 0;
        int offset = 0;

        for (int i = 1; i < (MAX_COLS - 1); i++) {
            if ((gameGrid[0][i] != null) && (gameGrid[0][i - 1] == null)) {
                if (start == 0) {
                    offset = i;
                } else {
                    end = i;
                    break;
                }
            }

            if ((gameGrid[0][i] == null) && (gameGrid[0][i - 1] != null)) {
                start = i;
            }
        }


        if (end == 0) {
            return;
        }
        if (start > (MAX_COLS / 2)) {
            offset++;
        }

//        System.out.println(">>>>>>>>>>>>>>>>>>");
//        drawGrid(this);
//        System.out.println(offset + " " + start + " " + end);

        Block[][] oldGameGrid = gameGrid;
        gameGrid = new Block[MAX_ROWS][MAX_COLS];
        int j = 0;
        for (int i = (((end - start) / 2) + offset); i < MAX_COLS; i++) {
            for (; (j < MAX_COLS) && (oldGameGrid[0][j] == null); j++);

            for (int k = 0; (j < MAX_COLS) && (k < MAX_ROWS); k++) {
//                System.out.println(k + " " + i + " " + j);
                gameGrid[k][i] = oldGameGrid[k][j];
                // drawGrid(this);
            }
            j++;
        }
        //      System.out.println(">>>>>>>>>>>>>>>>>>");
    }

    public double getVelocity() {
        return MIN_VELOCITY + (VELOCITY_FACTOR * (level - 1));
    }

    private void setLevelByScore(long score) {
        level = (int) ((score / LEVEL_INCREASE_SCORE_DIVIDER) + 1);
    }

    private void scoreBrokenBlocks(int brokenBloks) {
        //int points = (int) (SCORE_BLOCK_UNIT * (Math.pow(SCORE_BLOCK_FACTOR, brokenBloks) - 1));
        int points = (int) ((SCORE_BLOCK_UNIT * brokenBloks) * ((brokenBloks * SCORE_BLOCK_FACTOR) + 1));
        score += points;
        setLevelByScore(score);
    }

    synchronized public void breakDown(int x, int y) {
        int brokenBlocks = breakOn(x, y);

        for (int j = 0; j < MAX_ROWS; j++) {
            for (int i = 0; i
                    < MAX_COLS; i++) {
                makeBlockFall(i, j);
            }
        }
        makePartsJoin();

        if (brokenBlocks > 0) {
            gameView.updateGameGrid();
            gameView.updateScore();
        }
    }

    private void breakMarkedBlocks() {
        int brokenBlocks = 0;

        if (gameGrid == null) {
            return;
        }

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int column = 0; column < MAX_COLS; column++) {
                Block block = gameGrid[row][column];
                if ((block != null) && (block.isMarkedToBreak())) {
                    gameGrid[row][column] = null;
                    brokenBlocks++;
                }
            }
        }
        System.out.println("Broken Blocks: " + brokenBlocks);
        scoreBrokenBlocks(brokenBlocks);


    }

    private void unmarkBlocks() {
        if (gameGrid == null) {
            return;
        }

        for (int row = 0; row < MAX_ROWS; row++) {
            for (int column = 0; column < MAX_COLS; column++) {
                Block block = gameGrid[row][column];
                if ((block != null) && (block.isMarkedToBreak())) {
                    gameGrid[row][column].notToBreak();
                }
            }
        }
    }

    private int breakOn(int x, int y) {
        int brokenBlocks = breakOn(x, y, 0);
        if (brokenBlocks < MIN_BLOCK_TO_SCORE) {
            brokenBlocks = 0;
            unmarkBlocks();
        } else {
            breakMarkedBlocks();
        }
        return brokenBlocks;
    }

    private int breakOn(int x, int y, int previousBroken) {
        Point[] around = {
            new Point(x - 1, y),
            new Point(x, y - 1),
            new Point(x, y + 1),
            new Point(x + 1, y),};
        Block block = gameGrid[y][x];
        int currentBroken = previousBroken;

//        System.out.println("breakOn " + x + "," + y);

        if (block == null) {
            return currentBroken;
        }

        gameGrid[y][x].breakMe();
        currentBroken++;

        for (Point p : around) {

//            try {
//                System.out.println("=> " + p + " " + gameGrid[p.y][p.x].getColor() + " " + block.getColor());
//            } catch (Exception ex) {
//                System.out.println(ex.getMessage());
//            }

            if ((p.x >= 0)
                    && (p.x < MAX_COLS)
                    && (p.y >= 0)
                    && (p.y < MAX_ROWS)
                    && gameGrid[p.y][p.x] != null
                    && !gameGrid[p.y][p.x].isMarkedToBreak()
                    && gameGrid[p.y][p.x].getColor().equals(block.getColor())) {
                //              System.out.println("Marking: " + p.x + "," + p.y);
                currentBroken = breakOn(p.x, p.y, currentBroken);
            }
        }
//        System.out.println("broken: " + currentBroken);
        return currentBroken;
    }

    private boolean isRowEmpty(Block[] blockRow) {
        for (Block block : blockRow) {
            if (block != null) {
                return false;
            }
        }
        return true;
    }

    synchronized public void advanceStep() {
        if (isGameOver()) {
            return;
        }

        if (newGameLine == null) {
            newGameLine = new Block[MAX_COLS];
            newGameLine[0] = new Block();
            newBlockPosition = 1;
        } else if (newBlockPosition >= MAX_COLS) {
            if ((gameGrid[MAX_ROWS - 1] != null) && (!isRowEmpty(gameGrid[MAX_ROWS - 1]))) {
                gameOver = true;
            } else {
                insertNewGameLine(newGameLine);
                gameView.updateGameGrid();
            }
            newGameLine = null;
        } else {
            newGameLine[newBlockPosition] = new Block();
            newBlockPosition++;
        }
        gameView.updateGameNewLine();
    }

    private void insertNewGameLine(Block[] newGameLine) {
        Block[][] newGameGrid = new Block[MAX_ROWS][MAX_COLS];

        newGameGrid[0] = newGameLine;

        for (int i = 1; i < MAX_ROWS; i++) {
            newGameGrid[i] = gameGrid[i - 1];
        }
        gameGrid = newGameGrid;
    }

    public long getScore() {
        return score;
    }

    synchronized public void reset() {
        gameGrid = new Block[MAX_ROWS][MAX_COLS];
        newGameLine = null;
        gameOver = false;
        newBlockPosition = 0;
        score = 0;
        level = 1;
        gameView.updateGameGrid();
        gameView.updateGameNewLine();
        gameView.updateScore();
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public long getLevel() {
        return level;
    }


}
