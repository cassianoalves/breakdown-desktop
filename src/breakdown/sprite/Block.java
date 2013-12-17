/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package breakdown.sprite;

import breakdown.basics.Color;

/**
 *
 * @author Cassiano
 */
public class Block {

    private Color color;
    private static Color[] colors = {Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED};
    private boolean breakMe;

    public Block() {
        int ncolor = (int)(Math.random()*100)%colors.length;
        this.color = colors[ncolor];
    }

    public Block(Color color) {
        this.color = color;
    }

    public void breakMe() {
        breakMe = true;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMarkedToBreak() {
        return breakMe;
    }

    public void notToBreak() {
        breakMe = false;
    }


}
