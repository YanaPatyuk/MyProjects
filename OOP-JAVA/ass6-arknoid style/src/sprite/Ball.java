package sprite;

import java.awt.Color;

import animations.GameLevel;
import biuoop.DrawSurface;
import game.CollisionInfo;
import game.GameEnvironment;
import primitives.Line;
import primitives.Point;
import primitives.Velocity;

/**
 * create a moving ball.
 * @author YanaPatyuk
 */
public class Ball implements Sprite {
    private Point center;
    private int r;
    private java.awt.Color color;
    private Velocity v;
    // squer for boundaries of the ball.
    private GameEnvironment gameEnvironment;

    /**
     * constructor-create a ball.
     * @param center
     *            Point.
     * @param r
     *            radius of the ball.
     * @param color
     *            of the ball.
     */
    public Ball(Point center, int r, Color color) {
        this.center = center;
        this.r = r;
        this.color = color;
        this.gameEnvironment = new GameEnvironment();
        this.v = new Velocity(2, 2);
    }

    /**
     * constuctor-create a ball by paramters x and y.
     * @param x
     *            value of X point in center.
     * @param y
     *            value Y point in center.
     * @param r
     *            radius of the ball.
     * @param color
     *            of the ball.
     */
    public Ball(int x, int y, int r, java.awt.Color color) {
        this.center = new Point(x, y);
        this.r = r;
        this.color = color;
        this.gameEnvironment = new GameEnvironment();
        this.v = new Velocity(2, 2);
    }

    // accessors
    /**
     * @return x value of center Point.
     */
    public int getX() {
        return (int) this.center.getX();
    }

    /**
     * @return Y value of center Point.
     */
    public int getY() {
        return (int) this.center.getY();
    }

    /**
     * @return radius of the ball.
     */
    public int getSize() {
        return this.r;
    }

    /**
     * @return color of the ball.
     */
    public java.awt.Color getColor() {
        return this.color;
    }


    /**
     * draw the ball on the given DrawSurface.
     * @param d
     *            to draw on.
     */
    public void drawOn(DrawSurface d) {
        d.setColor(Color.WHITE);
        d.fillCircle(this.getX(), this.getY(), this.r);
        d.setColor(this.color);
        d.drawCircle(this.getX(), this.getY(), this.r);
    }

    /**
     * set velocity by given velocity.
     * @param velocity
     *            new velocity.
     */
    public void setVelocity(Velocity velocity) {
        this.v = velocity;
    }

    /**
     * setVelocity by value dx and dy.
     * @param dx
     *            value.
     * @param dy
     *            value.
     */
    public void setVelocity(double dx, double dy) {
        this.v = new Velocity(dx, dy);
    }

    /**
     * @return velocity value.
     */
    public Velocity getVelocity() {
        return this.v;
    }

    /**
     * move the ball one step. note:first check if the next step is illigel.
     * @param dt scale of frame.
     */
    public void moveOneStep(double dt) {
        // check the next step.
        Velocity fixedV = this.v.fixedVelocity(dt);
        Velocity copyOfVelocity = new Velocity(this.v);

        Point tampPoint = fixedV.applyToPoint(this.center);
        Line trajectory = new Line(this.center, tampPoint);
        CollisionInfo info = this.gameEnvironment.getClosestCollision(trajectory);
        //if info is not null means there is a hit.
        if (info != null) {
            this.center = allmostHitPoint(info.collisionPoint());
            this.setVelocity(info.collisionObject().hit(this, info.collisionPoint(), this.v));
            //if the object is paddle-and the ball is inside it-take it out.
            if (info.collisionObject() instanceof Paddle) {
                if (copyOfVelocity == v) {
                    this.center = this.getVelocity().applyToPoint(this.center);
                    }
            }
      } else { //if there is no hit-continu to next point.
          this.center = fixedV.applyToPoint(this.center);
          }
    }
    /**
     * move the point 0.00001 pixels.
     * @param p point to change.
     * @return new point.
     */
    public Point allmostHitPoint(Point p) {
    double x, y;
    if (this.v.getDx() != 0) {
        x = p.getX() - (this.v.getDx() / Math.abs(this.v.getDx())) / 100000;
        } else {
            x = p.getX() + 0.00000000;
            }
   if (this.v.getDy() != 0) {
       y = p.getY() - (this.v.getDy() / Math.abs(this.v.getDy())) / 100000;
       } else {
           y = p.getY() + 0.000000000;
           }
     return new Point(x, y);
    }
/**
 * add new game enviornment to ball.
 * @param c enviornmnent.
 */
    public void enviornment(GameEnvironment c) {
        this.gameEnvironment = c;
        }

    /**
     * apply one step.
     * @param dt scale of frame.
     */
    public void timePassed(double dt) {
        this.moveOneStep(dt);
        }
    /**
     * @param g game to remove from is the ball.
     */
    public void removeFromGame(GameLevel g) {
        g.removeSprite(this);
    }


}
