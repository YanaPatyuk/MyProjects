package sprite;
import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 */
public interface Sprite {
    /**
     * draw the sprite to the screen.
     * @param d drawsurface.
     */
   void drawOn(DrawSurface d);
   /**
    * notify the sprite that time has passed.
    * @param dt frame-rate.
    */
   void timePassed(double dt);
}