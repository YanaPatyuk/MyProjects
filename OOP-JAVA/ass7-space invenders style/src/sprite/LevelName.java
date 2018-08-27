package sprite;

import java.awt.Color;

import animations.GameLevel;
import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 *
 */
public class LevelName implements Sprite {
    private String level;
    private GameLevel game;
    /**
     * constractor.
     * @param name of level.
     * @param g game.
     */
public LevelName(String name, GameLevel g) {
    this.level = name;
    this.game = g;
}
/**
 * draw the level.
 * @param d DrawSurface.
 */
    public void drawOn(DrawSurface d) {
        d.setColor(Color.black);
        d.drawText((int) 650,
                15,
                   "Level:" + this.level + " " + this.game.getNumberOfLevel(), 11);

    }
    @Override
    public void timePassed(double dt) {
    }

}
