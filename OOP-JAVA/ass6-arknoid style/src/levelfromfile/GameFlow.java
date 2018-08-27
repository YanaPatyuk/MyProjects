package levelfromfile;

import java.util.List;

import animations.LoseWinScreen;
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
private KeyPressStoppableAnimation loseWinScreenKey;

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
        this.lives = new Counter(7);
        this.dialog = dialog;
        this.scoreTable = scoreTable;
        this.loseWinScreenKey = new KeyPressStoppableAnimation(ks, KeyboardSensor.SPACE_KEY,
                                    new LoseWinScreen(this.lives, this.score));
        this.scoreAnimation = new KeyPressStoppableAnimation(ks, KeyboardSensor.SPACE_KEY,
                                    new HighScoresAnimation(scoreTable));

    }
/**
 * run the levels from the list. in the end print the score.
 * @param levels list.
 */
    public void runLevels(List<LevelInformation> levels) {
       for (LevelInformation levelInfo : levels) {

          GameLevel level = new GameLevel(this.animationRunner, levelInfo,
                this.keyboardSensor, this.lives, this.score);
          level.initialize();
          while (!level.stopTheLevel()) {
             level.playOneTurn();
          }
          if (level.endOfLives()) {
             break;
          }
          this.score.increase(100);

       }
       //print the score and if the user lose or win.
       this.animationRunner.run(this.loseWinScreenKey);
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
