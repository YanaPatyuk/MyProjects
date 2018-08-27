package level;
import animations.GameLevel;
import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import biuoop.DialogManager;
import biuoop.KeyboardSensor;
import game.AnimationRunner;
import game.HighScoresTable;
import game.ScoreInfo;
import primitives.Counter;
/**
 * @author YanaPatyuk
 *
 */
public class GameFlow {
private AnimationRunner animationRunner;
private KeyboardSensor keyboardSensor;
private DialogManager dialog;
private HighScoresTable scoreTable;
private Counter score;
private Counter lives;
private KeyPressStoppableAnimation scoreAnimation;

/**
 * constractor.
 * @param ar AnimationRunner.
 * @param ks KeyboardSensor.
 * @param dialog to add a score;
 * @param scoreTable table.
 */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks, DialogManager dialog, HighScoresTable scoreTable) {
        this.animationRunner = ar;
        this.keyboardSensor = ks;
        this.score = new Counter(0);
        this.lives = new Counter(3);
        this.dialog = dialog;
        this.scoreTable = scoreTable;
        this.scoreAnimation = new KeyPressStoppableAnimation(ks, KeyboardSensor.SPACE_KEY,
                                    new HighScoresAnimation(scoreTable));
    }
/**
 * run the levels from the list. in the end print the score.
 * @param level loop level(end-less).
 */
    public void runLevels(LevelInformation level) {
          GameLevel game = new GameLevel(this.animationRunner, level,
                this.keyboardSensor, this.lives, this.score);
          game.initialize();
          while (!game.stopTheLevel()) {
              game.playOneTurn();
          }
          this.score.increase(100);
       //print the score and if the user lose or win.
       addName();
       this.animationRunner.run(this.scoreAnimation);
    }
    /**
     * add a new name to the score table if players score in rank.
     */
    private void addName() {
        if (this.scoreTable.getRank(this.score.getValue()) <= this.scoreTable.getHighScores().size()
            || this.scoreTable.getHighScores().size() == 0) {
            String name = dialog.showQuestionDialog("Name", "What is your name?", "I'm Batman");
            this.scoreTable.add(new ScoreInfo(name, this.score.getValue()));
        }
    }
 }
