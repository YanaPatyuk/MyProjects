
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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
import levelfromfile.GameFlow;
import levelfromfile.LevelInformation;
import levelfromfile.LevelSet;
import levelfromfile.LevelSpecificationReader;
import levelfromfile.SetLevelFromFile;
/**
 * @author YanaPatyuk
 *
 */
public class Ass6Game {
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
        MenuAnimation levelSetmenu = new MenuAnimation(key, "Level Set", runner);
        InputStreamReader sReader = null;
        String levelSetPath = "resources/level_sets.txt";
        //check if given new path.
        if (args.length > 0) {
            levelSetPath = args[0];
        }
        try {
            sReader = new InputStreamReader(new FileInputStream(levelSetPath));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        List<LevelSet> levels = SetLevelFromFile.readLevelFromFile(sReader);
        for (LevelSet set: levels) {
            levelSetmenu.addSelection(set.getKey(), set.getMessage(),
                     new Task<Void>() {
                public Void run() {
                    InputStreamReader sReader = null;
                    try {
                        sReader = new InputStreamReader(new FileInputStream(set.getPath()));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    LevelSpecificationReader b = new LevelSpecificationReader();
                    List<LevelInformation> levels = b.fromReader(sReader);
                    if (levels == null) {
                        gui.close();
                        System.exit(0);
                    }
                    GameFlow g = new GameFlow(runner, key, dialog, scoreSable);
                    g.runLevels(levels);
                    try {
                        scoreSable.save(f);
                    } catch (IOException e) {
                        ;
                    }
                    return null;
                }
                });
        }

         MenuAnimation menu = new MenuAnimation(key, "Arkanoid", runner);
         //add start game option
         menu.addSubMenu("s", "Start the game", levelSetmenu);
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
