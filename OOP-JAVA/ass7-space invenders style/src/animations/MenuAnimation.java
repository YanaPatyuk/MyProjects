package animations;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import game.AnimationRunner;

/**
 * @author YanaPatyuk
 *
 */
public class MenuAnimation implements Menu<Task<Void>> {
    private List<Task<Void>> selections;
    private List<String> keys;
    private List<String> messages;
    private KeyboardSensor keyBoard;
    private Task<Void> status;
    private boolean stop;
    private String name;
    private List<Menu<Task<Void>>> subMe;
    private AnimationRunner runner;
    private Image back;
    private Image option;
    /**
     * @param ks keyboadrd.
     * @param nameOfGame sting.
     * @param runner to run subanimation.
     */
    public  MenuAnimation(KeyboardSensor ks, String nameOfGame, AnimationRunner runner) {
        this.selections = new ArrayList<Task<Void>>();
        this.keys = new ArrayList<String>();
        this.messages = new ArrayList<String>();
        this.subMe = new ArrayList<Menu<Task<Void>>>();
        this.keyBoard = ks;
        this.stop = false;
        this.name = nameOfGame;
        this.runner = runner;
        try {
           this.back = ImageIO.read((new File("resources/background_images/menu.jpg")));
           this.option = ImageIO.read((new File("resources/background_images/botton.jpg")));
        } catch (IOException e) {
            ;
        }
    }
    @Override
    public void doOneFrame(DrawSurface d, double dt) {
            d.drawImage(0, 0, this.back);
            d.setColor(Color.yellow);
            d.drawText(100 + 1, 100, this.name, 100);

        for (int i = 0; i < this.messages.size(); ++i) {
            int optionX = 200;
            int optionY = 200 + i * 100;
            d.drawImage(optionX, optionY, this.option);
            String optionText = "(" + ((String) this.keys.get(i)) + ") " + ((String) this.messages.get(i));
            optionX = optionX + 40;
            d.setColor(Color.BLACK);
            d.drawText(optionX + 1, optionY + 40, optionText, 24);
            d.drawText(optionX - 1, optionY + 40, optionText, 24);
            d.drawText(optionX, optionY + 1  + 40, optionText, 24);
            d.drawText(optionX, optionY - 1  + 40, optionText, 24);

            d.setColor(Color.GREEN);
            d.drawText(optionX, optionY + 40, optionText, 24);
        }
        for (int i = 0; i < this.selections.size(); i++) {
            if (this.keyBoard.isPressed((String) this.keys.get(i))) {
                if (this.selections.get(i) != null) {
                this.status = this.selections.get(i);
                this.stop = true;
                return;
            } else if (this.subMe.get(i) != null) {
                Menu<Task<Void>> m = this.subMe.get(i);
                this.runner.run(this.subMe.get(i));
                this.status = m.getStatus();
                this.stop = true;
                ((MenuAnimation) this.subMe.get(i)).clearSelection();
                return;
            }
            }
        }

    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }

    @Override
    public void addSelection(String key, String message, Task<Void> returnVal) {
        this.keys.add(key);
        this.messages.add(message);
        this.selections.add(returnVal);
        this.subMe.add(null);
    }

    @Override
    public Task<Void> getStatus() {
        return this.status;
    }
    /**
     * restart the menu to start again.
     */
    public void clearSelection() {
        this.stop = false;
    }
    @Override
    public void addSubMenu(String key, String message, Menu<Task<Void>> subMenu) {
        this.keys.add(key);
        this.messages.add(message);
        this.subMe.add(subMenu);
        this.selections.add(null);
    }
}
