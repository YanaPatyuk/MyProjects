package sprite;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import animations.GameLevel;
import biuoop.DrawSurface;
import game.Collidable;
import primitives.Counter;
import primitives.Point;
import primitives.Rectangle;
import primitives.Velocity;
/**
 * @author YanaPatyuk
 */
public class Block implements Collidable, Sprite, HitNotifier {
    private List<HitListener> hitListeners;
    private Rectangle rectangle;
    private Counter hitsNumber;
    private int hitsDisplay;
    private String secretBlock;
    private Map<Integer, Color> colorTofill;
    private Color stroke;
    private Map<Integer, Image> imageToFill;
/**
 * crate a block.
 * @param rect rectangle.
 * @param hitsDisplay string represnt hits.
 */
    public Block(Rectangle rect, int hitsDisplay) {
        this.hitListeners = new ArrayList<HitListener>();
        this.hitsNumber = new Counter(hitsDisplay);
        this.rectangle = rect;
        this.hitsDisplay = hitsDisplay;
        this.secretBlock = null;
    }
/**
 * @return the rectangle of the block.
 */
    public Rectangle getCollisionRectangle() {
        return this.rectangle;
    }
    /**
     * Notify the object that we collided with it at collisionPoint with
     * a given velocity.
     * The return is the new velocity expected after the hit (based on
     * the force the object inflicted on us).
     * @see Collidable#hit(Point, Velocity).
     * @return currentVelocity for ball.
     * @param collisionPoint of the ball.
     * @param currentVelocity of ball.
     * @param hitter ball.
     **/
    public Velocity hit(Ball hitter, Point collisionPoint, Velocity currentVelocity) {
        this.notifyHit(hitter);
        countHits();
        //check witch side of block got hitted.
        if (this.rectangle.getLowerRight().getX() == collisionPoint.getX()) {
            currentVelocity.changeDx();
            }
        if (this.rectangle.getUpperLeft().getX() == collisionPoint.getX()) {
            currentVelocity.changeDx();
            }
        if (this.rectangle.getLowerRight().getY() == collisionPoint.getY()) {
            currentVelocity.changeDy();
            }
        if (this.rectangle.getUpperLeft().getY() == collisionPoint.getY()) {
                currentVelocity.changeDy();
                }
                return currentVelocity;
    }
    /**
     * notify the block its been hitted.
     */
    public void countHits() {
        if (this.hitsNumber.getValue() <= 0) {
            this.hitsDisplay = 0;
        } else {
            this.hitsNumber.decrease(1);
            this.hitsDisplay = this.hitsNumber.getValue();
        }

    }
    /**
     * draw the triangle.
     * first fill with color and then its bounder.
     * draw number of hits.
     * @param d surfice.
     */
    public void drawOn(DrawSurface d) {
        //draw the block.
       /* String text = "" + hitsDisplay;
        if (hitsDisplay <= 0) {
            text = "";
        }*/
        if (this.colorTofill.containsKey(this.hitsDisplay)) {
            d.setColor(this.colorTofill.get(this.hitsDisplay));
            d.fillRectangle((int) this.rectangle.getUpperLeft().getX(), (int) this.rectangle.getUpperLeft().getY(),
                    (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());
        } else {
            d.drawImage((int) this.rectangle.getUpperLeft().getX(), (int) this.rectangle.getUpperLeft().getY(),
                    this.imageToFill.get(hitsDisplay));
        }
        if (this.stroke != null) {
        d.setColor(this.stroke);
        d.drawRectangle((int) this.rectangle.getUpperLeft().getX(), (int) this.rectangle.getUpperLeft().getY(),
                (int) this.rectangle.getWidth(), (int) this.rectangle.getHeight());
        }
      //  d.setColor(Color.yellow);
     //   d.drawText((int) this.rectangle.getUpperLeft().getX(), (int) this.rectangle.getUpperLeft().getY(),
         //       "" + this.hitsDisplay, 30);
    }

@Override
    public void timePassed(double dt) { }
    /**
     * add this block to the game lists so its recognize it.
     * @param g game.
     */
    public void addToGame(GameLevel g) {
        g.addSprite(this);
        g.addCollidable(this);
    }
    /**
     * remove Block from the game.
     * @param game parm.
     */
    public void removeFromGame(GameLevel game) {
        game.removeCollidable(this);
        game.removeSprite(this);

    }
    /**
     * @param hl Add hl as a listener to hit events.
     */
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }
    /**
     * @param hl Remove hl from the list of listeners to hit events.
     */
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }
    /**
     * Notify all listeners about a hit event.
     * @param hitter ball.
     */
    private void notifyHit(Ball hitter) {
        // Make a copy of the hitListeners before iterating over them.
        List<HitListener> listenersTamp = new ArrayList<HitListener>(this.hitListeners);
        // Notify all listeners about a hit event:
        for (HitListener hl : listenersTamp) {
           hl.hitEvent(this, hitter);
        }
     }
    /**
     * @return hit number.s
     */
    public int getHitPoints() {
        return this.hitsNumber.getValue(); }
    /**
     * make the block to be a killingBall.
     */
    public void killingBall() {
        this.secretBlock = "killer";
        this.rectangle.setColor(Color.BLACK);
    }
    /**
     * make the block to be a creat ball.
     */
    public void addBall() {
        this.secretBlock = "creator";
        this.rectangle.setColor(Color.GREEN);

    }
    /**
     * @return special qualities.
     */
    public String getSpecial() {
        return this.secretBlock;
    }
    /**
     * @param stroke2 to add.
     */
    public void addStroke(Color stroke2) {
        this.stroke = stroke2;
    }
    /**
     * @param fillImage map to copy into block.
     */
    public void addIMages(Map<Integer, Image> fillImage) {
        this.imageToFill = new HashMap<Integer, Image>(fillImage);
    }
    /**
     * @param fillColor map to copy into block.
     */
    public void addColors(Map<Integer, Color> fillColor) {
        this.colorTofill = new HashMap<Integer, Color>(fillColor);
    }

}
