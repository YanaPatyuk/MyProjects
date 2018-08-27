package sprite;

import java.awt.Image;

import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 *
 */
public class ImageBackground implements Sprite {
    private Image i;
/**
 * constractor.
 * @param i image.
 */
    public ImageBackground(Image i) {
        this.i = i;
    }
    @Override
    public void drawOn(DrawSurface d) {
        d.drawImage(0, 0, i);
    }

    @Override
    public void timePassed(double dt) {
    }

}
