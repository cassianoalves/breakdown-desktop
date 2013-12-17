/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package breakdown.engine;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cassiano
 */
public class Ticker implements Runnable {
    private GameState gameState;
    private boolean abortAsked = false;
    private boolean pause;

    public void run() {
        double velocity;
        long period;
        while (!gameState.isGameOver() && !abortAsked) {
            velocity = gameState.getVelocity();
            period = (long) (1000 / velocity);
            try {
                Thread.sleep(period);
            } catch (InterruptedException ex) {
                Logger.getLogger(Ticker.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!pause) {
                gameState.advanceStep();
            }
        }
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void abort() {
        abortAsked = true;
    }

    public void pause() {
        pause = true;
    }

}
