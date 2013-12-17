/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package breakdown.view.awt.mapper;

/**
 *
 * @author Cassiano
 */
public class GameColorMapper {
    public static java.awt.Color game2Awt(breakdown.basics.Color gameColor) {
        java.awt.Color awtColor = null;

        if (gameColor.equals(breakdown.basics.Color.BLACK)) {
            awtColor = java.awt.Color.BLACK;
        } else if (gameColor.equals(breakdown.basics.Color.BLUE)) {
            awtColor = java.awt.Color.BLUE;
        } else if (gameColor.equals(breakdown.basics.Color.GREEN)) {
            awtColor = java.awt.Color.GREEN;
        } else if (gameColor.equals(breakdown.basics.Color.YELLOW)) {
            awtColor = java.awt.Color.YELLOW;
        } else if (gameColor.equals(breakdown.basics.Color.RED)) {
            awtColor = java.awt.Color.RED;
        } else {
            awtColor = java.awt.Color.BLACK;
        }
        return awtColor;
    }
}
