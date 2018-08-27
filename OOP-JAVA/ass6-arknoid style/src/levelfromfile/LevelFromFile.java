package levelfromfile;

import java.util.ArrayList;
import java.util.List;

import primitives.Velocity;
import sprite.Block;
import sprite.Sprite;
/**
 * @author YanaPatyuk
 * create new level b input paramters.
 */
public class LevelFromFile implements LevelInformation {

    private Integer numberOfBalls;
    private Integer paddleSpeed;
    private Integer paddleWidht;
    private String levelName;
    private Sprite background;
    private List<Block> blockList;
    private  List<Velocity> velocityList;
    private Integer numberOfBlocksToRemove;
    private BlocksFromSymbolsFactory blockDef;
    private Integer startOfBloks;
    private Integer startOfBloksY;
    private Integer rowHight;
    /**
     * create new lists.
     * one for velocity and other for blocks.
     */
    public LevelFromFile() {
        this.velocityList = new ArrayList<Velocity>();
        this.blockList = new ArrayList<Block>();
        this.levelName = null;
        numberOfBalls = null;
        paddleSpeed = null;
        paddleWidht = null;
        levelName = null;
        background = null;
        numberOfBlocksToRemove = null;
        blockDef = null;
        startOfBloks = null;
        startOfBloksY = null;
        rowHight = null;
    }
        /**
    }
    /**
     * set the information about the different blocks.
     * @param levelBlockDef  info.
     */
    public void setBlockdef(BlocksFromSymbolsFactory levelBlockDef) {
        this.blockDef = levelBlockDef;
    }
    /**
     * @return BlocksFromSymbolsFactory.
     */
    public BlocksFromSymbolsFactory getBlockdef() {
        return this.blockDef;
    }
    /**
     * @param number of balls.
     */
    public void setNumberOfBalls(int number) {
        this.numberOfBalls = number;
    }
    /**
     * @param name of level.
     */
    public void setLevelName(String name) {
        this.levelName = name;
    }
    /**
     * @param s background to set..
     */
    public void setBackground(Sprite s) {
        this.background = s;
    }
    /**
     * @param b new block.
     */
    public void addBlock(Block b) {
        this.blockList.add(b);
    }
    /**
     * @param v velocity list for the balls.
     */
    public void addVelocity(Velocity v) {
        this.velocityList.add(v);
    }
    /**
     * set paddles speed.
     * @param speed num.
     */
    public void setPaddleSpeed(int speed) {
        this.paddleSpeed = speed;
    }
    /**
     * @param w to set paddles wight.
     */
    public void setPaddleWitght(int w) {
        this.paddleWidht = w;
    }
    /**
     * @param remove counter balls.
     */
    public void numberOfBLoksToRemove(int remove) {
        this.numberOfBlocksToRemove = remove;
    }

    @Override
    public int numberOfBalls() {
        return this.numberOfBalls;
    }

    @Override
    public List<Velocity> initialBallVelocities() {
        return this.velocityList;
    }

    @Override
    public int paddleSpeed() {
        return this.paddleSpeed;
    }

    @Override
    public int paddleWidth() {
        return this.paddleWidht;
    }

    @Override
    public String levelName() {
        return this.levelName;
    }

    @Override
    public Sprite getBackground() {
        return this.background;
    }

    @Override
    public List<Block> blocks() {
        return this.blockList;
    }

    @Override
    public int numberOfBlocksToRemove() {
        return this.numberOfBlocksToRemove;
    }
    /**
     * set first block to start in x cor.
     * @param x coordinate.
     */
    public void setSartOfBlocksX(int x) {
        this.startOfBloks = x;
    }
    /**
     * @return x coordinate.
     */
    public int getStartX() {
        return this.startOfBloks;
    }
    /**
     * @param y to set y coordinate for firts block.
     */
    public void setSartOfBlocksY(int y) {
       this.startOfBloksY = y;
    }
    /**
     * @return y first coordinate.
     */
    public int getStartY() {
        return this.startOfBloksY;
    }
    /**
     * @param h to set row hight.
     */
    public void setRowHeight(int h) {
        this.rowHight = h;
    }
    /**
     * @return rows hight.
     */
    public int getRowHight() {
        return this.rowHight;
    }
    /**
     * @return false if one parm is missing.
     */
    public boolean checkLevelCorrect() {
        if (this.numberOfBalls == null || this.numberOfBlocksToRemove == null || this.levelName == null
                || this.paddleSpeed == null || this.paddleWidht == null || this.blockList.isEmpty()
                || this.background == null || this.startOfBloks == null || this.startOfBloksY == null) {
            return false;
        }
        return true;
    }
}
