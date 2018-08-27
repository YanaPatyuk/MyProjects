package sprite;

import java.awt.Color;

import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 *
 */
public class ColoredBackground implements Sprite {
    private Color color;
/**
 * comstractor.
 * @param c color.
 */
    public ColoredBackground(Color c) {
        this.color = c;
    }
    @Override
    public void drawOn(DrawSurface d) {
        d.setColor(color);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
    }

    @Override
    public void timePassed(double dt) {
    }

}
