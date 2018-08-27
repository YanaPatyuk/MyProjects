package sprite;

import animations.GameLevel;
import primitives.Counter;
import primitives.Velocity;
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
            Ball newBall = new Ball(400, 560, 5, java.awt.Color.BLACK);
            Velocity v = new Velocity(5, -3);
            newBall.setVelocity(v);
            newBall.enviornment(this.game.getEnviornment());
            this.game.addSprite(newBall);
            this.remainingBalls.increase(1);
        }
    }
    /**
     * @return counter of balls.
     */
    public Counter getBallsCount() {
        return this.remainingBalls;
    }

}
