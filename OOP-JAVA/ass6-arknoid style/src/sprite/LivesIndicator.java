package sprite;

import java.awt.Color;

import biuoop.DrawSurface;
import primitives.Counter;
import primitives.Point;
/**
 * @author YanaPatyuk
 *
 */
public class LivesIndicator implements Sprite {
    private Counter numberOfLives;
    private Point center;
    private int radius;
/**
 * constractor.
 * @param number counter of lifes.
 */
    public LivesIndicator(Counter number) {
        this.numberOfLives = number;
        this.center = new Point(100, 10);
        this.radius = 8;
    }
    /**
     * draw the lives on top of frame with the number left.
     * @param d draw surfice.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(Color.RED);
        d.fillCircle((int) center.getX(), (int) center.getY(), radius);
        d.setColor(Color.BLACK);
        d.drawCircle((int) center.getX(), (int) center.getY(), radius);
        d.setColor(Color.BLACK);
        d.drawText((int) center.getX() - 47, (int) center.getY() + 5, "Lives:  " + this.numberOfLives.getValue(), 14);
    }
/**
 * un used.
 * @param dt time.
 */
    public void timePassed(double dt) {
        // TODO Auto-generated method stub
    }

}
