/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package breakdown.view.awt;

import breakdown.engine.GameStateFactory;
import java.awt.Canvas;

/**
 *
 * @author Cassiano
 */
public class GameCanvasFactory {
    private static Canvas instance = null;
    private static enum GraphType {AWT, J3D};

    private static GraphType graphType = GraphType.AWT;


    private GameCanvasFactory() {

    }

    public static Canvas getInstance() {
        if (instance != null) {
            return instance;
        }

        switch (graphType) {
            case AWT:
                instance = new breakdown.view.awt.GameCanvas();
                ((breakdown.view.awt.GameCanvas)instance).setGameState(GameStateFactory.getInstance());
                break;
            case J3D:
                instance = breakdown.view.awt.j3d.GameCanvasFactory.getInstance();
                break;
        }

        //instance.setGameState(GameStateFactory.getInstance());

        return instance;
    }



}
