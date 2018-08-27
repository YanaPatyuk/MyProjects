package sprite;
import java.awt.Color;

import animations.GameLevel;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import game.Collidable;
import primitives.Point;
import primitives.Rectangle;
import primitives.Velocity;
/**
 * @author YanaPatyuk
 */
public class Paddle implements Sprite, Collidable {
   private biuoop.KeyboardSensor keyboard;
   private Rectangle rect;
   private int speed;
   /**
    * constractor.
    * @param keyboard sensor.
    * @param point to create the rectangle.
    * @param width of paddle.
    * @param speed for paddle.
    */
   public Paddle(biuoop.KeyboardSensor keyboard, Point point, int width, int speed) {
       this.keyboard = keyboard;
       this.rect = new Rectangle(new Point(point.getX() - width / 2, point.getY()), width, 10, Color.yellow);
       this.speed = speed;
       }
/**
 * move paddle to left.
 * if paddles get to the end but their is still space to move. move by one.
 * @param dt time-passed.
 */
   public void moveLeft(double dt) {
       if (this.rect.getUpperLeft().getX() <= 20) {
           return;
           }
       if (this.rect.getUpperLeft().getX() - speed * dt > 20) {
           this.rect = new Rectangle(new Point(this.rect.getUpperLeft().getX() - speed * dt,
                                 this.rect.getUpperLeft().getY()), rect.getWidth(), 10, Color.YELLOW);
       } else {
           this.rect = new Rectangle(new Point(this.rect.getUpperLeft().getX() - 1,
                                   this.rect.getUpperLeft().getY()), rect.getWidth(), 10, Color.YELLOW);
           }
   }
   /**
    * move paddle right.
    * if paddles get to the end but their is still space to move. move by one.
    * @param dt time-passed.
    */
   public void moveRight(double dt) {
       if (this.rect.getLowerRight().getX() >= 780) {
           return;
           }
       if (this.rect.getLowerRight().getX() + (speed * dt) < 780) {
       this.rect = new Rectangle(new Point(this.rect.getUpperLeft().getX() + (speed * dt),
                                 this.rect.getUpperLeft().getY()), rect.getWidth(), 10, Color.YELLOW);
       } else {
           this.rect = new Rectangle(new Point(this.rect.getUpperLeft().getX() + 1,
                   this.rect.getUpperLeft().getY()), rect.getWidth(), 10, Color.YELLOW);
       }
   }
   /**
    * check what user asks and apply.
    * @param dt time-passed.
    */
   public void timePassed(double dt) {
       if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
           moveLeft(dt);
           }
       if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
           moveRight(dt);
           }
   }
   /**
    * draw the paddle.
    * @param d surface.
    */
   public void drawOn(DrawSurface d) {
   d.setColor(this.rect.getColor());
   d.fillRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                   (int) this.rect.getWidth(), (int) this.rect.getHeight());
   d.setColor(java.awt.Color.black);
   d.drawRectangle((int) this.rect.getUpperLeft().getX(), (int) this.rect.getUpperLeft().getY(),
                   (int) this.rect.getWidth(), (int) this.rect.getHeight()); }

   /**
    * @return this collidable.
    */
   public Rectangle getCollisionRectangle() {
       return this.rect;
       }
/**
    * Notify the object that we collided with it at collisionPoint with
    * a given velocity.
    * The return is the new velocity expected after the hit (based on
    * the force the object inflicted on us).
    * @param collisionPoint hitted.
    * @param currentVelocity of the hitted object.
    * @return new velocity.
    * @param hitter ball.
 */
   public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
       double valueX = this.rect.getUpperLeft().getX();
       double segment = this.rect.getWidth() / 5;

       if (!hitFromAbov(currentVelocity)) {
           return currentVelocity;
           }


       if (checkPlace(collisionPoint, valueX, valueX + segment)) {
           return Velocity.fromAngleAndSpeed(300, currentVelocity.getSpeed());
       } else if (checkPlace(collisionPoint, valueX + segment, valueX + segment * 2)) {
           return Velocity.fromAngleAndSpeed(330, currentVelocity.getSpeed());
       } else if (checkPlace(collisionPoint, valueX + (segment * 2), valueX + segment * 3)) {
           currentVelocity.changeDy();
       } else if (checkPlace(collisionPoint, valueX + (segment * 3), valueX + segment * 4)) {
           return Velocity.fromAngleAndSpeed(30, currentVelocity.getSpeed());
       } else if (checkPlace(collisionPoint, valueX + (segment * 4), valueX + segment * 5)) {
           return Velocity.fromAngleAndSpeed(60, currentVelocity.getSpeed());
       }
        return currentVelocity;
   }
   /**
    * check if X value of point between bonders.
    * @param collisionPoint to check.
    * @param left value x.
    * @param right value x.
    * @return true if its in range. false otherwise.
    */
   public boolean checkPlace(Point collisionPoint, double left, double right) {
       if ((collisionPoint.getX() >= left) && (collisionPoint.getX() <= right)) {
           if (collisionPoint.getY() == this.rect.getUpperLeft().getY()) {
               return true;
               }
           }
       return false;
   }
   /**
    * chack the direction of the hitting object. return true if hitted from above.
    * @param currentVelocity of object.
    * @return false if not from above.
    */
   public boolean hitFromAbov(Velocity currentVelocity) {
       if (currentVelocity.getDy() < 0) {
           return false;
           }
      // if (currentVelocity.getDy() > this.rect.getLowerRight().)
       return true;
   }

   /**
    * Add this paddle to the game.
    * @param g game.
    */
   public void addToGame(GameLevel g) {
       g.addSprite(this);
       g.addCollidable(this);
   }
   /**
    * remove this paddle to the game.
    * @param g game.
    */
   public void removeFromGame(GameLevel g) {
       g.removeCollidable(this);
       g.removeSprite(this);
   }
}