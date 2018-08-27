package level;

import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;
import primitives.Point;
import primitives.Rectangle;
import sprite.Sprite;
/**
 * create frames.
 * @author YanaPatyuk
 *
 */
public class FrameBackground implements Sprite {
    private Rectangle back;
    private List<Rectangle> frame;
/**
 * create frames.
 */
public FrameBackground() {
    Rectangle up = new Rectangle(new Point(20, 20), 780, 20, java.awt.Color.darkGray);
    Rectangle left = new Rectangle(new Point(0, 20), 20, 580, java.awt.Color.darkGray);
    Rectangle right = new Rectangle(new Point(780, 20), 20, 580, java.awt.Color.darkGray);
    back = new Rectangle(new Point(0, 0), 800, 600,
            java.awt.Color.getHSBColor(0.1F, 0.1F, 0.1F));
    this.frame = new ArrayList<Rectangle>();
    this.frame.add(up);
    this.frame.add(left);
    this.frame.add(right);
}
/**
 * @param d to drowon.
 */
    public void drawOn(DrawSurface d) {
        for (Rectangle rectangle:frame) {
        d.setColor(rectangle.getColor());
        d.fillRectangle((int) rectangle.getUpperLeft().getX(), (int) rectangle.getUpperLeft().getY(),
                         (int) rectangle.getWidth(), (int) rectangle.getHeight());
        //draw the frame of the block.
        }
        d.setColor(java.awt.Color.black);
        d.drawRectangle((int) this.back.getUpperLeft().getX(), (int) this.back.getUpperLeft().getY(),
                        (int) this.back.getWidth(), (int) this.back.getHeight());
    }
    @Override
    public void timePassed(double dt) {
    }

}
