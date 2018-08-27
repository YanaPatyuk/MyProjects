package level;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Color;

import primitives.Point;
import primitives.Rectangle;
import primitives.Velocity;
import sprite.Block;
import sprite.ImageBackground;
import sprite.Sprite;
/**
 * @author YanaPatyuk
 * endless loop level.
 */
public class LoopLevel implements LevelInformation {

    private int numberOfBalls;
    private int paddleSpeed;
    private int paddleWidht;
    private String levelName;
    private Sprite background;
    private List<Block> blockList;
    private List<Alien> aliensList;
    private Image image;
    private Image imageBack;
    /**
     * constructor.
     */
    public LoopLevel() {
        this.numberOfBalls = 1;
        this.paddleSpeed = 500;
        this.paddleWidht = 60;
        this.levelName = "Space Round";
        this.blockList = blocks();
        this.aliensList = new ArrayList<Alien>();
        try {
            this.image = ImageIO.read((new File("resources/background_images/alien.jpg")));
            this.imageBack = ImageIO.read((new File("resources/background_images/space.jpg")));

        } catch (IOException e) {
            ;
        }
        this.background = new ImageBackground(this.imageBack);
        createAliens();
    }
/**
 * @return number of balls.
 */
    public int numberOfBalls() {
        return numberOfBalls;
    }
/**
 * @return list of veloctys.
 */
    public List<Velocity> initialBallVelocities() {
        List<Velocity> velocities = new ArrayList<Velocity>();
        velocities.add(new Velocity(1, -300));
        return velocities;
    }
/**
 * @return pasddles speed.
 */
    public int paddleSpeed() {
        return this.paddleSpeed;
    }
/**
 * @return paddle width.
 */
    public int paddleWidth() {
        return this.paddleWidht;
    }
/**
 * @return name of level.
 */
    public String levelName() {
        return this.levelName;
    }
/**
 * @return background.
 */
    public Sprite getBackground() {
        return this.background;
    }
/**
 * create balls.
 * @return list of balls.
 */
    public List<Block> blocks() {
        List<Block> blockList1 = new ArrayList<Block>();
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 30; j++) {
            Rectangle rect = new Rectangle(new Point(j * 5 + 30, i * 5 + 450),
                    5, 5, java.awt.Color.getHSBColor(i * 0.1F, 0.9F, 0.8F));
            Block block = new Block(rect, 1);
            block.addStroke(Color.blue);
            block.addOneColor(1, Color.blue);
            blockList1.add(block);
        }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 30; j++) {
                Rectangle rect = new Rectangle(new Point(j * 5 + 300, i * 5 + 450),
                        5, 5, java.awt.Color.getHSBColor(i * 0.1F, 0.9F, 0.8F));
                Block block = new Block(rect, 1);
                block.addStroke(Color.blue);
                block.addOneColor(1, Color.blue);
                blockList1.add(block);
            }
        }
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 30; j++) {
                Rectangle rect = new Rectangle(new Point(j * 5 + 550, i * 5 + 450),
                        5, 5, java.awt.Color.getHSBColor(i * 0.1F, 0.9F, 0.8F));
                Block block = new Block(rect, 1);
                block.addStroke(Color.blue);
                block.addOneColor(1, Color.blue);
                blockList1.add(block);
            }
        }
        return blockList1;
    }
/**
 * @return number of block to remove.
 */
    public int numberOfBlocksToRemove() {
        return (int) blockList.size() + 1;
    }
    /**
     * @return list of aliens.(coped list).
     */
public List<Alien> aliens() {
    List<Alien> copy = new ArrayList<Alien>();
    for (int i = 0; i < this.aliensList.size(); i++) {
        copy.add(this.aliensList.get(i).copy());
    }
    return copy;
}
/**
 * create list of aliens.
 */
private void createAliens() {
    for (int i = 0; i < 6; i++) {
        for (int j = 1; j < 9; j++) {
        Rectangle rect = new Rectangle(new Point(j * 65, i * 40 + 40),
                30, 55, java.awt.Color.getHSBColor(i * 0.1F, 0.9F, 0.8F));
        Alien alien = new Alien(rect, 1, this.image);
        this.aliensList.add(alien);
    }
    }
}
/**
 * @return get number of aliens to remove.
 */
public int getNumberOfAliensToRemove() {
    return this.aliensList.size();
}

}

