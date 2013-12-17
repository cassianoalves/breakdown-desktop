/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package breakdown.engine;

import breakdown.engine.GameState;
import breakdown.view.awt.GameCanvasFactory;

/**
 *
 * @author Cassiano
 */
public class GameStateFactory {
    private static GameState instance = null;

    private GameStateFactory() {}

    public static GameState getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new GameState();

        return instance;
    }

}
