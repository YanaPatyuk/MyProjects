package animations;

import java.awt.Color;

import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 *
 */
public class PauseScreen implements Animation {
    private boolean stop;
    /**
     */
    public PauseScreen() {
       this.stop = false;
    }
    /**
     * draw text on screen. if space key pressed-continue.
     * draw stop text
     * this.stop = true;
     * @param d DrawSurface.
     * @param dt time-passed.
     */
    public void doOneFrame(DrawSurface d, double dt) {
        d.setColor(Color.LIGHT_GRAY);
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
        d.setColor(Color.BLACK);
        d.drawText(40, 80, "Stop!", 100);
        d.setColor(Color.red);
        d.drawText(41, 81, "Stop", 100);
        d.setColor(Color.BLACK);
        d.drawText(42, 82, "Stop!", 100);

        d.setColor(Color.GRAY);
        d.fillCircle(400, 300, 100);
        d.setColor(Color.WHITE);
        d.fillCircle(400, 300, 98);
        d.setColor(Color.decode("#1788d0"));
        d.fillCircle(400, 300, 90);
        d.setColor(Color.GRAY);
        d.fillCircle(400, 300, 88);
        d.setColor(Color.RED);
        d.fillCircle(400, 300, 86);

        d.setColor(Color.GRAY);
        d.fillRectangle(350, 280, 100, 40);

        d.setColor(Color.WHITE);
        d.fillRectangle(353, 283, 97, 37);
        d.setColor(Color.BLACK);
        d.drawText(199, 500, "Press space to continue", 32);
        d.setColor(Color.red);
        d.drawText(200, 501, "Press space to continue", 32);
        d.setColor(Color.BLACK);
        d.drawText(202, 502, "Press space to continue", 32);
    }
    /**
     * @return true if need to stop.
     */
    public boolean shouldStop() {
        return this.stop;
        }
 }
