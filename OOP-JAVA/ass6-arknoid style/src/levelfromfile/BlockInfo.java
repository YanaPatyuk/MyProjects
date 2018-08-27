package levelfromfile;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import primitives.Point;
import primitives.Rectangle;
import sprite.Block;
/**
 * @author YanaPatyuk
 *
 */
public class BlockInfo implements BlockCreator {
    private Integer high;
    private Integer weight;
    private Map<Integer, Image> fillImage;
    private Map<Integer, Color> fillColor;
    private Integer hitPoints;
    private Color stroke;
    /**
     * constractor.
     */
    public BlockInfo() {
        this.fillColor = new HashMap<Integer, Color>();
        this.fillImage = new HashMap<Integer, Image>();
        this.high = null;
        this.weight = null;
        this.hitPoints = null;
        this.stroke = null;
    }
    /**
     * @param h high for block.
     */
    public void addHight(int h) {
        this.high = h;
    }
    /**
     * @param w for weight.
     */
    public void addWight(int w) {
        this.weight = w;
    }
    /**
     * @param points for hit points to block.
     */
    public void addHitPoints(int points) {
        this.hitPoints = points;
    }
    /**
     * add new color to ball when their left x hits to destroy.
     * @param x hitnumber.
     * @param c color.
     */
    public void addColor(int x, Color c) {
        this.fillColor.put(x, c);
    }
    /**
     *add new image to ball when their left x hits to destroy.
     * @param x number of hits.
     * @param i image.
     */
    public void addImage(int x, Image i) {
        this.fillImage.put(x, i);
    }
    /**
     * add color for stroke.
     * @param x Color.
     */
    public void addStrok(Color x) {
        this.stroke = x;
    }
    /**
     * @return false if ball is not complited.
     */
    public boolean checkCorrect() {
        if (this.high == null || this.hitPoints == null || this.weight == null || (this.fillColor.size() == 0
                && this.fillImage.size() == 0)) {
            return false;
        }
        return true;
    }
    @Override
    public Block create(int xpos, int ypos) {
        Block newBlock = new Block(new Rectangle(new Point((double) xpos, (double) ypos),
                (double) this.weight, (double) this.high), this.hitPoints);
        newBlock.addColors(this.fillColor);
        newBlock.addIMages(this.fillImage);
        newBlock.addStroke(this.stroke);
        return newBlock;
    }

}
