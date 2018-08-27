package game;
import primitives.Point;

/**
 * @author YanaPatyuk
 */
public class CollisionInfo {
    private Collidable object;
    private Point collisionPoint;
    /**
     * constractor.
     * @param object collideable.
     * @param point that its collide.
     */
    public CollisionInfo(Collidable object, Point point) {
        this.object = object;
        this.collisionPoint = point;
    }
   // the point at which the collision occurs.
    /**
     * @return  the point at which the collision occurs.
     */
   public Point collisionPoint() {
       return this.collisionPoint; }

   /**
    * @return the collidable object involved in the collision.
    */
   public Collidable collisionObject() {
       return this.object; }
}