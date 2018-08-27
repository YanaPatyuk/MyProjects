package sprite;

import animations.GameLevel;
import level.Alien;
import primitives.Counter;

/**
 * a BlockRemover is in charge of removing blocks from the game, as well as keeping count
 * of the number of blocks that remain.
 * @author YanaPatyuk
 *
 */
public class BlockRemover implements HitListener {
private GameLevel game;
private Counter remainingBlocks;
/**
 * constructor.
 * @param game game.
 * @param removedBlocks counter.
 */
public BlockRemover(GameLevel game, Counter removedBlocks) {
    this.game = game;
    this.remainingBlocks = removedBlocks;
}

/**
 * Blocks that are hit and reach 0 hit-points should be removed
 * from the game. Remember to remove this listener from the block
 * that is being removed from the game.
 * @param beingHit block.
 * @param hitter ball.
 */
public void hitEvent(Block beingHit, Ball hitter) {
    if (beingHit.getHitPoints() <= 1) {
        this.remainingBlocks.decrease(1);
        beingHit.removeFromGame(game);
        beingHit.removeHitListener(this);
    }

}
/**
 * @return remainingBlocks.
 */
public Counter getRemovedBlocks() {
    return this.remainingBlocks;
    }

@Override
public void hitEvent(Alien beingHit, Ball hitter) {
}


}