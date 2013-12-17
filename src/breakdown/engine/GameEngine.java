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
public class GameEngine {
    private GameState gameState;
    private GameView gameView;
    private Ticker ticker;
    private Thread tickerThread;

    public GameEngine(GameState gameState, GameView gameView) {
        this.gameState = gameState;
        this.gameView = gameView;
    }

    public void reset() {
        if (ticker != null) {
            ticker.abort();
            try {
                tickerThread.join(10000);
            } catch (InterruptedException ex) {
                // TODO: Tratar Excecao do join do ticker
                Logger.getLogger(GameEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
            tickerThread = null;
            ticker = null;
        }

        ticker = new Ticker();
        gameState.reset();
        ticker.setGameState(gameState);
        tickerThread = new Thread(ticker, "GameTicker");
        tickerThread.start();

    }

}
