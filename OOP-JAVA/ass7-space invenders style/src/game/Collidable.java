package game;
import primitives.Point;
import primitives.Rectangle;
import primitives.Velocity;
import sprite.Ball;

/**
 * @author YanaPatyuk
 */
public interface Collidable {
    /**
     * @return the "collision shape" of the object.
     */
   Rectangle getCollisionRectangle();
   /**
    * Notify the object that we collided with it at collisionPoint with
    * a given velocity.
    * The return is the new velocity expected after the hit (based on
    * the force the object inflicted on us).
    * @param collisionPoint happend.
    * @param currentVelocity of the ball.
    * @param hitter ball.
    * @return new valocity.
    */
   Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity);
}