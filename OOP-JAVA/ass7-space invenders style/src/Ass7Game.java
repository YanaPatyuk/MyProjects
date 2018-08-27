
import java.io.File;
import java.io.IOException;

import animations.HighScoresAnimation;
import animations.KeyPressStoppableAnimation;
import animations.MenuAnimation;
import animations.ShowHiScoresTask;
import animations.Task;
import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import game.AnimationRunner;
import game.HighScoresTable;
import level.GameFlow;
import level.LoopLevel;
/**
 * @author YanaPatyuk
 *
 */
public class Ass7Game {
    /**
     * Main program for the game. create and run the game.
     * @param args no need.
     */
    public static void main(String[] args) {
        GUI gui = new GUI("The Best Game ever!", 800, 600);
        KeyboardSensor key = gui.getKeyboardSensor();
        DialogManager dialog = gui.getDialogManager();
        AnimationRunner runner = new AnimationRunner(gui);
        //open high score table. of not exist - create one and seve it.
        File f = new File("resources/highscores");
        HighScoresTable scoreSable = HighScoresTable.loadFromFile(f);
        if (scoreSable.getHighScores().size() == 0) {
            try {
                  scoreSable.save(f);
                } catch (IOException e) {
                    ;
                }
        }
        //create sub menu for levels.
         MenuAnimation menu = new MenuAnimation(key, "Space Invaders", runner);
         //add start game option
         menu.addSelection("s", "Start the game",       new Task<Void>() {
             public Void run() {

                 GameFlow g = new GameFlow(runner, key, dialog, scoreSable);
                 g.runLevels(new LoopLevel());
                 try {
                     scoreSable.save(f);
                 } catch (IOException e) {
                     ;
                 }
                 return null;
             }
             });
        //add view table option.
         menu.addSelection("h", "To Show Winners Table", new ShowHiScoresTask(runner,
                    new KeyPressStoppableAnimation(key, KeyboardSensor.SPACE_KEY,
                    new HighScoresAnimation(scoreSable))));
            //add exit option.
         menu.addSelection("e", "To exit", new Task<Void>() {
                public Void run() {
                    gui.close();
                    System.exit(0);
                    return null;
                }
                });
            //run the menu.
            while (true) {
                runner.run(menu);
                Task<Void> task =  menu.getStatus();
                task.run();
                menu.clearSelection();
            }
    }
}
