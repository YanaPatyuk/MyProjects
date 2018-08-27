package sprite;

import animations.GameLevel;
import level.Alien;
import primitives.Counter;
/**
 * @author YanaPatyuk
 *
 */
public class BallRemover implements HitListener {
    private GameLevel game;
    private Counter remainingBalls;
    /**
     * constructor.
     * @param g game.
     * @param numberBalls to start.
     */
    public BallRemover(GameLevel g, Counter numberBalls) {
        this.game = g;
        this.remainingBalls = numberBalls;
    }
/**
 * check if a ball hitted block remover ball or an add ball.
 * @param hitter ball.
 * @param beingHit block.
 */
    public void hitEvent(Block beingHit, Ball hitter) {
        if (beingHit.getCollisionRectangle().getUpperLeft().getY() >= 600) {
            hitter.removeFromGame(this.game);
            this.remainingBalls.decrease(1);

        } else if (beingHit.getSpecial() == "killer") {
            hitter.removeFromGame(this.game);
            this.remainingBalls.decrease(1);
        } else if (beingHit.getSpecial() == "creator") {
        } else if (beingHit.getCollisionRectangle().getUpperLeft().getY() <= 20) {
            hitter.removeFromGame(this.game);
            this.remainingBalls.decrease(1);
        }
    }
    /**
     * @return counter of balls.
     */
    public Counter getBallsCount() {
        return this.remainingBalls;
    }
    @Override
    public void hitEvent(Alien beingHit, Ball hitter) {
        // TODO Auto-generated method stub
        
    }

}
