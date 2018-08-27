package animations;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import game.AnimationRunner;
import game.Collidable;
import game.GameEnvironment;
import levelfromfile.FrameBackground;
import levelfromfile.LevelInformation;
import primitives.Counter;
import primitives.Point;
import primitives.Rectangle;
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
       createBlocks(this.blockRemover, ballRemover, scoreTrace);
       ScoreIndicator scoreInd = new ScoreIndicator(this.scoreTrace);

       this.addSprite(scoreInd);
       this.addSprite(liveIndicator);
       FrameBackground frame = new FrameBackground();
       this.addSprite(frame);
       LevelName level = new LevelName(info.levelName());
       this.addSprite(level);


   }
   /**
    * Run the game -- start the animation loop.
    */
   public void playOneTurn() {
       this.createBallsOnTopOfPaddle(); // or a similar method
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
    * @param hl3 HitListener for points.
    */
   public void createBlocks(HitListener hl, HitListener hl2, HitListener hl3) {
   Block tamp;
   for (int i = 0; i < info.blocks().size(); i++) {
       tamp = info.blocks().get(i);
       tamp.addHitListener(hl);
       tamp.addHitListener(hl3);
       tamp.addHitListener(hl2);

       this.environment.addCollidable(tamp);
       this.sprites.addSprite(tamp);
   }

   }
   /**
    * create frame blocks.
    * @param hl listenert.
    */
   public void createFrame(HitListener hl) {
           Block up = new Block(new Rectangle(new Point(20, 20), 780, 20, java.awt.Color.darkGray), 0);
           Block down = new Block(new Rectangle(new Point(0, 600), 780, 20, java.awt.Color.darkGray), 0);
           Block left = new Block(new Rectangle(new Point(0, 20), 20, 580, java.awt.Color.darkGray), 0);
           Block right = new Block(new Rectangle(new Point(780, 20), 20, 580, java.awt.Color.darkGray), 0);

           down.addHitListener(hl);
           this.addCollidable(up);
           this.addCollidable(down);
           this.addCollidable(left);
           this.addCollidable(right);
       }
   /**
    * crate two balls.
    */
 public void createBalls() {
     for (int i = 0; i < info.numberOfBalls(); i++) {
     Ball ballA = new Ball(400, 560, 5, java.awt.Color.BLACK);
     ballA.setVelocity(info.initialBallVelocities().get(i));
     //add to each ball the enviornment.
     ballA.enviornment(this.getEnviornment());
     //add the ball to the game.
     this.addSprite(ballA);
     }
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
  * notify the sprites.
  * check if their is balls or block left. if not- running stop.
  * this.running = false;
  * @param d DrawSurface.
  * @param dt time-passed.
  */
public void doOneFrame(DrawSurface d, double dt) {
    this.sprites.drawAllOn(d);
    this.sprites.notifyAllTimePassed(dt);
    if ((this.remainBlock.getValue() == 0) || (this.remainBalls.getValue() == 0)) {
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
    createBalls();
    }
/**
 * @return true if their is no lives or all the blocks removed. false otherwise.
 */
public boolean stopTheLevel() {
    if ((numberOfLives.getValue() > 0) && (remainBlock.getValue() > 0)) {
        if (this.remainBalls.getValue() == 0) {
            this.paddle.removeFromGame(this);
            this.remainBalls.increase(info.numberOfBalls());
            numberOfLives.decrease(1);
            }
        if ((numberOfLives.getValue() <= 0)) {
            return true;
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
}
