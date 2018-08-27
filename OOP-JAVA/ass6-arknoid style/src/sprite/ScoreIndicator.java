package sprite;

import java.awt.Color;

import animations.ScoreTrackingListener;
import biuoop.DrawSurface;
import primitives.Point;
import primitives.Rectangle;
/**
 * @author YanaPatyuk
 *
 */
public class ScoreIndicator implements Sprite {
    private Rectangle rectangle;
    private ScoreTrackingListener score;
    /**
     * Contractor.
     * @param score ScoreTrackingListener.
     */
    public ScoreIndicator(ScoreTrackingListener score) {
        this.score = score;
        this.rectangle = new Rectangle(new Point(0, 0), 800, 20, Color.WHITE);
    }
/**
 * draw the score.
 * @param d draw surface.
 */
    public void drawOn(DrawSurface d) {
        //draw the block.
        d.setColor(this.rectangle.getColor());
        d.fillRectangle((int) this.rectangle.getUpperLeft().getX(), (int) this.rectangle.getUpperLeft().getY(),
                         (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());
        //draw the frame of the block.
        d.setColor(java.awt.Color.black);
        d.drawRectangle((int) this.rectangle.getUpperLeft().getX(), (int) this.rectangle.getUpperLeft().getY(),
                        (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());
        //draw the number of hits.
        d.drawText((int) (this.rectangle.getUpperLeft().getX() + this.rectangle.getLowerRight().getX()) / 2,
                   (int) (this.rectangle.getUpperLeft().getY() + this.rectangle.getLowerRight().getY()) / 2 + 5,
                      "Socore:" + this.score.getScore().getValue(), 11);
    }
    @Override
    public void timePassed(double dt) {
        // TODO Auto-generated method stub

    }

}
