package animations;

import primitives.Counter;
import sprite.Ball;
import sprite.Block;
import sprite.HitListener;
/**
 * @author YanaPatyuk
 *
 */
public class ScoreTrackingListener implements HitListener {
    private Counter currentScore;
    /**
     * constractor.
     * @param scoreCounter to add.
     */
    public ScoreTrackingListener(Counter scoreCounter) {
       this.currentScore = scoreCounter;
    }
    /**
     * add points for score if ball got hitted.
     * @param beingHit block got hitted.
     * @param hitter ball.
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getHitPoints() != 0) {
            currentScore.increase(5);
        } else if (beingHit.getHitPoints() == 0) {
            currentScore.increase(10);
        }
    }
    /**
     * @return score.
     */
    public Counter getScore() {
        return this.currentScore;
    }

}
