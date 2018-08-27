package sprite;
import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 */
public class SpriteCollection {
    private List<Sprite> sprites;
    /**
     * create new list.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<Sprite>();
    }
    /**
     * add sprite to list.
     * @param s sprite.
     */
   public void addSprite(Sprite s) {
       this.sprites.add(s);
   }
   /**
    * make a copy of the sprite list.
    *  call timePassed() on all sprites.
    *  @param dt time-passed.
    */
   public void notifyAllTimePassed(double dt) {
       List<Sprite> spritesTamp = new ArrayList<Sprite>(this.sprites);
       for (Object entity:spritesTamp) {
           ((Sprite) entity).timePassed(dt);
       }
   }
   /**
    * call drawOn(d) on all sprites.
    * @param d surface.
    */
   public void drawAllOn(DrawSurface d) {
       for (Object entity:sprites) {
           ((Sprite) entity).drawOn(d);
       }
   }
   /**
    * @return list of sprites objects.
    */
   public List<Sprite> getSpriteList() {
       return this.sprites; }
   /**
    * @param s to remove frpm the list.
    */
   public void removeSpriteList(Sprite s) {
        this.sprites.remove(s); }
}