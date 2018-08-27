package animations;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import game.AnimationRunner;
import game.Collidable;
import game.GameEnvironment;
import level.Alien;
import level.AlienlList;
import level.FrameBackground;
import level.LevelInformation;
import primitives.Counter;
import primitives.Point;
import primitives.Rectangle;
import primitives.Velocity;
import sprite.Ball;
import sprite.BallRemover;
import sprite.Block;
import sprite.BlockRemover;
import sprite.HitListener;
import sprite.LevelName;
import sprite.LivesIndicator;
import sprite.Paddle;
import sprite.ScoreIndicator;
import sprite.Sprite;
import sprite.SpriteCollection;

/**
 * @author YanaPatyuk
 *
 */
public class GameLevel implements Animation  {
   private SpriteCollection sprites;
   private GameEnvironment environment;
   private KeyboardSensor keyboard;
   private Paddle paddle;
   private Counter remainBlock;
   private Counter remainBalls;
   private Counter score;
   private Counter numberOfLives;

   private BlockRemover blockRemover;
   private BallRemover ballRemover;
   private ScoreTrackingListener scoreTrace;

   private AnimationRunner runner;
   private LevelInformation info;
   private boolean running;
   private AlienlList aliens;
   private Image enemyBall;
   private Image playerBall;
private int levelNumber;
   /**
    * constractor.
    * @param animationRunner object.
    * @param info LevelInformation.
    * @param key KeyboardSensor.
    * @param numberOfLives counter.
    * @param score Counter.
    */
   public GameLevel(AnimationRunner animationRunner, LevelInformation info, KeyboardSensor key,
           Counter numberOfLives, Counter score) {
       this.keyboard = key;
       this.runner = animationRunner;
       this.info = info;
       this.running = true;
       this.numberOfLives = numberOfLives;
       this.score = score;
       this.aliens = new AlienlList(this);
       this.aliens.setVelocity(new Velocity(20, 0));
       this.levelNumber = 1;
       try {
               this.enemyBall = ImageIO.read((new File("resources/background_images/badFire.jpg")));
               this.playerBall = ImageIO.read((new File("resources/background_images/goodFire.jpg")));
       } catch (IOException e) {
         ;
       }
   }
   /**
    * remove all sprites and start level again.
    */
   public void clearBoard() {
       this.sprites.removeAll();
       this.getEnviornment().removeAll();
       this.initialize();
   }
/**
 * @param c collidable to add to game.
 */
   public void addCollidable(Collidable c) {
       this.environment.addCollidable(c);
   }
   /**
    * @param s sprite to add to the game.
    */
   public void addSprite(Sprite s) {
       this.sprites.addSprite(s);
   }
   /**
    * @return enviornment game.
    */
   public GameEnvironment getEnviornment() {
       return this.environment; }
   /**
    * Initialize a new game: create the Blocks and Ball (and Paddle)
    *  and add them to the game.
    */
   public void initialize() {

       this.environment = new GameEnvironment();
       this.sprites = new SpriteCollection();
       this.remainBlock = new Counter(info.numberOfBlocksToRemove());
       this.remainBalls = new Counter(info.numberOfBalls());
       this.blockRemover = new BlockRemover(this, remainBlock);
       this.ballRemover = new BallRemover(this, remainBalls);
       this.scoreTrace = new ScoreTrackingListener(this.score);
       LivesIndicator liveIndicator = new LivesIndicator(numberOfLives);
       this.addSprite(info.getBackground());
       createFrame(ballRemover);
       createBlocks(this.blockRemover, ballRemover);
       ScoreIndicator scoreInd = new ScoreIndicator(this.scoreTrace);
       createAlien(this.blockRemover, ballRemover, scoreTrace);

       this.addSprite(scoreInd);
       this.addSprite(liveIndicator);
       FrameBackground frame = new FrameBackground();
       this.addSprite(frame);
       LevelName level = new LevelName(info.levelName(), this);
       this.addSprite(level);
       this.addSprite(this.aliens);

   }
   /**
    * Run the game -- start the animation loop.
    */
   public void playOneTurn() {
       this.createBallsOnTopOfPaddle(); // or a similar method
      // resetAliens();
       this.runner.run(new CountdownAnimation(3, 3, this.sprites));
       this.running = true;
       // use our runner to run the current animation -- which is one turn of
       // the game.
       this.runner.run(this);
    }
   /**
    * create blocks in pattern.
    * @param hl listener.
    * @param hl2 listenert special.
    */
   public void createBlocks(HitListener hl, HitListener hl2) {
   Block tamp;
   for (int i = 0; i < info.blocks().size(); i++) {
       tamp = info.blocks().get(i);
       tamp.addHitListener(hl);
       tamp.addHitListener(hl2);
       tamp.killingBall();

       this.environment.addCollidable(tamp);
       this.sprites.addSprite(tamp);
   }
   }
   /**
    * create aliens.
    * @param hl ball remover.
    * @param hl2  block remover.
    * @param hl3 score tracing.
    */
   public void createAlien(HitListener hl, HitListener hl2, HitListener hl3) {
   Alien tamp;
   List<Alien> b = new ArrayList<Alien>(this.info.aliens());
   for (int i = 0; i < b.size(); i++) {
       tamp = b.get(i);
       tamp.addHitListener(hl);
       tamp.addHitListener(hl3);
       tamp.addHitListener(hl2);
       tamp.killingBall();
       this.environment.addCollidable(tamp);
       this.sprites.addSprite(tamp);
       this.aliens.addElien(tamp);
       //add to last row.
       if (i < 10) {
           this.aliens.addToLastRow(tamp);
       }
   }

   }
   /**
    * create frame blocks.
    * @param hl listenert of hitting.
    */
   public void createFrame(HitListener hl) {
           Block up = new Block(new Rectangle(new Point(20, 20), 780, 20, java.awt.Color.darkGray), 0);
           Block down = new Block(new Rectangle(new Point(0, 600), 780, 20, java.awt.Color.darkGray), 0);
           Block left = new Block(new Rectangle(new Point(0, 20), 20, 580, java.awt.Color.darkGray), 0);
           Block right = new Block(new Rectangle(new Point(780, 20), 20, 580, java.awt.Color.darkGray), 0);

           down.addHitListener(hl);
           up.addHitListener(hl);
           left.addHitListener(hl);
           this.addCollidable(up);
           this.addCollidable(down);
           this.addCollidable(left);
           this.addCollidable(right);
       }
   /**
    * crate two balls for the player.
    */
 public void createBalls() {
     double x = this.paddle.getCollisionRectangle().getLowerRight().getX();
     double x2 = this.paddle.getCollisionRectangle().getUpperLeft().getX();
     Ball ballA = new Ball(new Point(x, 530), 5, this.playerBall);
     Ball ballB = new Ball(new Point(x2, 530), 5, this.playerBall);
     ballA.setVelocity(info.initialBallVelocities().get(0));
     ballB.setVelocity(info.initialBallVelocities().get(0));

     //add to each ball the enviornment.
     ballA.enviornment(this.getEnviornment());
     ballB.enviornment(this.getEnviornment());

     this.addSprite(ballA);
     this.addSprite(ballB);

     this.remainBalls.increase(2);

     }
/**
 * remove collidable from list.
 * @param c collidable.
 */
 public void removeCollidable(Collidable c) {
     this.environment.getEnvironment().remove(c);
 }
 /**
  * remove sprite from list.
  * @param s sprite.
  */
 public void removeSprite(Sprite s) {
     this.sprites.removeSpriteList(s);

 }
 /**
  * @param a alien to remove.
  */
 public void removeAlien(Alien a) {
     this.aliens.removeAlien(a);
 }
 /**
  * notify the sprites.
  * check if their is balls or block left. if not- running stop.
  * this.running = false;
  * @param d DrawSurface.
  * @param dt time-passed.
  */
public void doOneFrame(DrawSurface d, double dt) {
    this.sprites.drawAllOn(d);
    this.sprites.notifyAllTimePassed(dt);
    if ((this.paddle.checkIfGotHitted() || this.aliens.checkIfLost() || this.aliens.checkIfWin())) {
        this.running = false;
    }
    if (this.keyboard.isPressed("p")) {
        this.runner.run(new KeyPressStoppableAnimation(this.keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen()));
     }
}
/**
 * check if animation should stop.
 * @return true if to stop-false otherwise.
 */
public boolean shouldStop() {
    return !(this.running);
    }
/**
 * create a paddle and balls.
 */
public void createBallsOnTopOfPaddle() {
    this.paddle = new Paddle(this.keyboard, new Point(400, 570), info.paddleWidth(), info.paddleSpeed());
    paddle.addToGame(this);
    paddle.addGameLevel(this);
    }
/**
 * @return true if their is no lives or all the blocks removed. false otherwise.
 */
public boolean stopTheLevel() {
    if ((numberOfLives.getValue() > 0)) {
        if (this.paddle != null) {
        if (this.paddle.checkIfGotHitted() || this.aliens.checkIfLost()) {
            this.paddle.removeFromGame(this);
            numberOfLives.decrease(1);
            resetAliens();
            //
            return false;
            }
        }
        if ((numberOfLives.getValue() <= 0)) {
            return true;
            }
        if ((this.aliens.checkIfWin())) {
            this.paddle.removeFromGame(this);
            this.remainBalls.increase(info.numberOfBalls());
            resetAliens();
            this.aliens.increaseSpeedEndLevel();
            this.levelNumber++;
        }
        return false;
    }
    return true;
}
/**
 * @return true of lives ended. flase otherwise.
 */
    public boolean endOfLives() {
        if ((numberOfLives.getValue() > 0)) {
            return false;
        }
        return true;
    }
    /**
     * add new ball.
     * @param collisionRectangle to shouut from.
     */
public void shoutABall(Rectangle collisionRectangle) {
    double y = collisionRectangle.getLowerRight().getY() + 1;
    double x = (collisionRectangle.getUpperLeft().getX() +  collisionRectangle.getLowerRight().getX()) / 2 - 10;
    Ball ballA = new Ball(new Point(x, y), 3, this.enemyBall);
    ballA.setVelocity(new Velocity(1, 70));
    //add to each ball the enviornment.
    ballA.enviornment(this.getEnviornment());
    ballA.isEnemy();
    this.addSprite(ballA);

    this.remainBalls.increase(1);

}
/**
 * remove all the aliens.
 */
public void resetAliens() {
    for (int i = 0; i < this.aliens.getList().size(); i++) {
        this.sprites.removeSpriteList(this.aliens.getList().get(i));
        this.removeCollidable(this.aliens.getList().get(i));
    }
    this.aliens.reastar();
    this.clearBoard();

}
/**
 * @return level number.
 */
public int getNumberOfLevel() {
    return this.levelNumber;
}

}

