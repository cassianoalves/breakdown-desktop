/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package breakdown.view.awt;

import breakdown.engine.GameState;
import breakdown.sprite.Block;
import breakdown.view.awt.mapper.GameColorMapper;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Cassiano
 */
public class GameCanvas extends Canvas {

    private static final int CANVAS_MARGIN = 10;
    GameState gameState;
    private Point referencePoint;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (gameState == null) {
            return;
        }
        drawBlocks(g);
        drawNewBlocks(g);

        g.setColor(Color.WHITE);
        g.drawLine(CANVAS_MARGIN, (int) (this.getHeight() - (getBlockHeigth() * 1.25)) - CANVAS_MARGIN, this.getWidth() - CANVAS_MARGIN, (int) (this.getHeight() - (getBlockHeigth() * 1.25)) - CANVAS_MARGIN);


        //ManualTicker.drawGrid(gameState);

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    private void drawBlocks(Graphics g) {
        Block[][] gameGrid = gameState.getGameGrid();

        for (int row = 0; row < GameState.getMAX_ROWS(); row++) {
            for (int col = 0; col < GameState.getMAX_COLS(); col++) {
                if (gameGrid[row][col] != null) {
                    drawBlockAtPosition(col, row, g, GameColorMapper.game2Awt(gameGrid[row][col].getColor()));
                }
            }
        }        
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
        System.out.println("up" + g.getClipBounds().x + "," + g.getClipBounds().y + "," + (g.getClipBounds().x + g.getClipBounds().width) + "," + (g.getClipBounds().x + g.getClipBounds().height));

        if (g.hitClip(CANVAS_MARGIN, CANVAS_MARGIN, (getBlockWidth() * GameState.getMAX_COLS()), (getBlockHeigth() * GameState.getMAX_ROWS()))) {
            drawBlocks(g);
            System.out.println("drawBlocks(g);");
        }
        if (g.hitClip(CANVAS_MARGIN, this.getHeight() - CANVAS_MARGIN - getBlockHeigth(), getBlockWidth() * GameState.getMAX_COLS(), getBlockHeigth())) {
            drawNewBlocks(g);
            System.out.println("drawNewBlocks(g);");
        }
        

    }


    public void repaintGameGrid() {
        repaint(100, CANVAS_MARGIN, CANVAS_MARGIN, (getBlockWidth() * GameState.getMAX_COLS()), (getBlockHeigth() * GameState.getMAX_ROWS()) + 10);
        System.out.println("gd" + CANVAS_MARGIN + "," + CANVAS_MARGIN + "," + (getWidth() - CANVAS_MARGIN) + "," + (getHeight() - CANVAS_MARGIN - ((int)(getBlockHeigth() * 1.5))));
        //repaint();
    }

    public void repaintNewRow() {
        int newLineLength = 0;
        int newLineMultiplier = 1;
        if (gameState.getNewGameLine() == null) {
            newLineMultiplier = gameState.getMAX_COLS();
        } else {
            for (newLineLength = 0; (newLineLength < gameState.getNewGameLine().length) && (gameState.getNewGameLine()[newLineLength] != null); newLineLength++);
            if (newLineLength == 0) {
                newLineMultiplier = GameState.getMAX_COLS() + 1;
            }
        }

        repaint(100, CANVAS_MARGIN + ((newLineLength-1) * getBlockWidth()), this.getHeight() - CANVAS_MARGIN - getBlockHeigth(), getBlockWidth() * newLineMultiplier , getBlockHeigth());
        //repaint(100, CANVAS_MARGIN, this.getHeight() - CANVAS_MARGIN - getBlockHeigth(), getBlockWidth() * 20 , getBlockHeigth());
        System.out.println("rw[" + newLineLength + "] " + (CANVAS_MARGIN + (newLineLength * getBlockWidth())) + "," + (this.getHeight() - CANVAS_MARGIN - getBlockHeigth()) + "," + (getBlockWidth()  * newLineMultiplier)  + "," +  getBlockHeigth());
    }


    private void drawNewBlockAtPixel(int x, int y, Graphics g, java.awt.Color awtColor) {
        drawBlockAtPixel(x, y, g, awtColor);
        g.setColor(java.awt.Color.BLACK);
        g.drawRect(x, this.getHeight() - y - getBlockHeigth(), getBlockWidth(), getBlockHeigth());
    }

    private void drawBlockAtPixel(int x, int y, Graphics g, java.awt.Color awtColor) {
        g.setColor(awtColor);
        g.fillRect(x, this.getHeight() - y - getBlockHeigth(), getBlockWidth(), getBlockHeigth());        
    }

    private void drawBlockAtPosition(int col, int row, Graphics g, java.awt.Color awtColor) {
        drawBlockAtPixel(getGridReference().x + (getBlockWidth() * col), getGridReference().y + (getBlockHeigth() * row), g, awtColor);
    }

    private void drawNewBlockAtColumn(int col, Graphics g, java.awt.Color awtColor) {
        drawNewBlockAtPixel(CANVAS_MARGIN + (getBlockWidth() * col), CANVAS_MARGIN, g, awtColor);
    }

    private Point getGridReference() {
        if (referencePoint == null) {
            referencePoint = new Point();
        }

        referencePoint.x = CANVAS_MARGIN;
        referencePoint.y = (int) (CANVAS_MARGIN + (getBlockHeigth() * 1.5) + 0.5);
        return referencePoint;
    }

    private int getBlockHeigth() {
//        return (int) ((((this.getHeight() - (CANVAS_MARGIN * 2.0)) / ((GameState.getMAX_ROWS() * 2) + 3.0)) + 0.5) * 2);
        return (int) ((this.getHeight() - (CANVAS_MARGIN * 2.0)) / (GameState.getMAX_ROWS() + 1.5));
    }

    private int getBlockWidth() {
        return (int) (((this.getWidth() - (CANVAS_MARGIN * 2.0)) / GameState.getMAX_COLS()));
    }

    private int getSeparatorHeigth() {
        return getBlockHeigth() / 2;
    }

    private void drawNewBlocks(Graphics g) {
        Block[] newBlockRow = gameState.getNewGameLine();
        if (newBlockRow == null) {
            return;
        }

        for (int col = 0; col < GameState.getMAX_COLS(); col++) {
            if (newBlockRow[col] != null) {
                drawNewBlockAtColumn(col, g, GameColorMapper.game2Awt(newBlockRow[col].getColor()));
            }
        }
    }

    void breakDownOnCanvasPoint(Point point) {
        int col = 0;
        int row = 0;

        col = (point.x - CANVAS_MARGIN) / getBlockWidth();
        row = (GameState.getMAX_ROWS() - 1 - (point.y - CANVAS_MARGIN) / getBlockHeigth());
        System.out.println("Point: " + point + " = " + col + "," + row);

        if ((col >= 0) && (col < GameState.getMAX_COLS()) && (row >= 0) && (row < GameState.getMAX_ROWS())) {
            gameState.breakDown(col, row);
        }
    }
}
