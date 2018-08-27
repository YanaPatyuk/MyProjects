package levelfromfile;

import java.util.HashMap;
import java.util.Map;

import sprite.Block;
/**
 * @author YanaPatyuk
 *
 */
public class BlocksFromSymbolsFactory {
    private Map<String, Integer> spacerWidths;
    private Map<String, BlockCreator> blockCreators;
    /**
     * constractor.
     */
    public BlocksFromSymbolsFactory() {
        this.spacerWidths = new HashMap<String, Integer>();
        this.blockCreators = new HashMap<String, BlockCreator>();
    }
    /**
     * add new spacer.
     * @param s key.
     * @param i value.
     */
    public void addSpacer(String s, Integer i) {
        this.spacerWidths.put(s, i);
    }
    /**
     * add new blockCreator.
     * @param s key.
     * @param i value.
     */
    public void addBlockCreatoe(String s, BlockCreator i) {
        this.blockCreators.put(s, i);
    }
    /**
     * returns true if 's' is a valid space symbol.
     * @param s stmbol.
     * @return true or false.
     */
    public boolean isSpaceSymbol(String s) {
        if (spacerWidths.containsKey(s)) {
            return true;
        }
        return false;
    }
    /**
     * @param s symbol.
     * @return  true if 's' is a valid block symbol.
     */
    public boolean isBlockSymbol(String s) {
        if (this.blockCreators.containsKey(s)) {
            return true;
        }
        return false;
    }

    /**
     *  Return a block according to the definitions associated
     *  with symbol s. The block will be located at position (xpos, ypos).
     * @param s symbol.
     * @param xpos parm.
     * @param ypos param.
     * @return block.
     */
    public Block getBlock(String s, int xpos, int ypos) {
        return this.blockCreators.get(s).create(xpos, ypos);
    }

    /**
     *  Returns the width in pixels associated with the given spacer-symbol.
     * @param s symol.
     * @return number widht.
     */
    public int getSpaceWidth(String s) {
        return this.spacerWidths.get(s);
    }

 }
