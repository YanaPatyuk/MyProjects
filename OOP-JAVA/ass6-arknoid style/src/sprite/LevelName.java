package sprite;

import java.awt.Color;

import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 *
 */
public class LevelName implements Sprite {
    private String level;
    /**
     * constractor.
     * @param name of level.
     */
public LevelName(String name) {
    this.level = name;
}
/**
 * draw the level.
 * @param d DrawSurface.
 */
    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText((int) 650,
                15,
                   "Level:" + this.level, 11);

    }
    @Override

    public void timePassed(double dt) {
    }

}
