package sprite;

import level.Alien;

/**
 * @author YanaPatyuk
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     * @param beingHit block.
     * @param hitter ball.
     */
    void hitEvent(Block beingHit, Ball hitter);
    /**
     * This method is called whenever the beingHit object is hit.
     * The hitter parameter is the Ball that's doing the hitting.
     * @param beingHit alien.
     * @param hitter ball.
     */
    void hitEvent(Alien beingHit, Ball hitter);
 }
