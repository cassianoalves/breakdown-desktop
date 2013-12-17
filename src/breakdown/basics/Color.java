/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package breakdown.basics;

/**
 *
 * @author Cassiano
 */
public class Color {
    public static Color BLACK = new Color(0);
    public static Color RED = new Color(1);
    public static Color YELLOW = new Color(2);
    public static Color GREEN = new Color(3);
    public static Color BLUE = new Color(4);

    private int colorDef;

    public Color () {
        colorDef=0;
    }

    private Color (int colorDef) {
        this.colorDef=colorDef;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Color other = (Color) obj;
        if (this.colorDef != other.colorDef) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.colorDef;
        return hash;
    }



}
