package level;

import java.awt.Image;


import animations.GameLevel;
import biuoop.DrawSurface;
import game.Collidable;
import primitives.Point;
import primitives.Rectangle;
import sprite.Block;
/**
 * @author YanaPatyuk
 *
 */
public class Alien extends Block {
    private Image i;
/**
 * create new alien with image.
 * @param rect reactangle.
 * @param hitsDisplay number of hits to destroy.
 * @param i image.
 */
    public Alien(Rectangle rect, int hitsDisplay, Image i) {
        super(rect, hitsDisplay);
        this.i = i;
    }
    @Override
    public void drawOn(DrawSurface d) {
        d.drawImage((int) this.getCollisionRectangle().getUpperLeft().getX(),
               (int) this.getCollisionRectangle().getUpperLeft().getY(), this.i);
    }
    @Override
    public void removeFromGame(GameLevel game) {
        game.removeCollidable((Collidable) this);
        game.removeSprite(this);
        game.removeAlien(this);

    }
    /**
     * @return copy of alien.
     */
    public Alien copy() {
        return new Alien(new Rectangle(new Point(this.getCollisionRectangle().getUpperLeft()),
                                                this.getCollisionRectangle().getHeight(),
                          this.getCollisionRectangle().getWidth()),
                          this.getHitPoints(), this.i);
    }

}
