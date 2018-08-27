package game;
import java.util.List;

import primitives.Line;
import primitives.Point;

import java.util.ArrayList;
/**
 * @author YanaPatyuk
 */
public class GameEnvironment {
    private List<Collidable> paramters;
    /**
     * consractor.
     */
    public GameEnvironment() {
        this.paramters = new ArrayList<Collidable>();
    }
    /**
     * @return list of ccollideble objects.
     */
    public List<Collidable> getEnvironment() {
        return this.paramters; }
    /**
     * add the given collidable to the environment.
     * @param c collideble.
     */
   public void addCollidable(Collidable c) {
       this.paramters.add(c);
   }

   /**
    * Assume an object moving from line.start() to line.end().
    * If this object will not collide with any of the collidables
    * in this collection, return null. Else, return the information
    * about the closest collision that is going to occur.
    * @param trajectory line.
    * @return info about the collision point.
    */
   public CollisionInfo getClosestCollision(Line trajectory) {
       Point tamp = null;
       List<Point> closePoints = new ArrayList<Point>();
       List<Integer> indexList = new ArrayList<Integer>();
       int indexOfCloseObject;
       for (int i = 0; i < this.paramters.size(); i++) {
           tamp = trajectory.closestIntersectionToStartOfLine(this.paramters.get(i).getCollisionRectangle());
           if (tamp != null) {
               closePoints.add(tamp);
               indexList.add(i);
               }
       }
       if (closePoints.isEmpty()) {
           return null;
           }
       Point closePoint = trajectory.closePoint(closePoints);
       indexOfCloseObject = (int) indexList.get(closePoints.indexOf(closePoint));
       return new CollisionInfo(this.paramters.get(indexOfCloseObject), closePoint);
   }

   /**
    * remove the given collidable to the environment.
    * @param c collideble.
    */
  public void removeCollidable(Collidable c) {
      this.paramters.remove(c);
  }
  /**
   * remove all collidabels.
   */
public void removeAll() {
    this.paramters.clear();
}

}