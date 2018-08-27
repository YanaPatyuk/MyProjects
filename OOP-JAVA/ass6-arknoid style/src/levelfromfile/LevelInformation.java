package levelfromfile;

import java.util.List;
import primitives.Velocity;
import sprite.Block;
import sprite.Sprite;

/**
 * @author YanaPatyuk
 *
 */
public interface LevelInformation {
    /**
     * @return number of balls.
     */
    int numberOfBalls();
    /**
     * The initial velocity of each ball.
     * Note that initialBallVelocities().size() == numberOfBalls()
     * @return list of velocity's.
     */
    List<Velocity> initialBallVelocities();
    /**
     * @return speed for paddle.
     */
    int paddleSpeed();
    /**
     * @return paddle's width.
     */
    int paddleWidth();
    /**
     * @return  the level name will be displayed at the top of the screen.
     */
    String levelName();
    /**
     * @return Returns a sprite with the background of the level
     */
    Sprite getBackground();
    /**
     * The Blocks that make up this level, each block contains.
     * its size, color and location.
     * @return list of blocks.
     */
    List<Block> blocks();
    /**
     * @return number of blocks to remove for end the leavel.
     */
    int numberOfBlocksToRemove();
 }
