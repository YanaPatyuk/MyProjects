package levelfromfile;

import sprite.Block;
/**
 * @author YanaPatyuk
 *
 */
public interface BlockCreator {
    /**
     * Create a block at the specified location.
     * @param xpos place of block.
     * @param ypos  place of block.
     * @return new Block.
     */
    Block create(int xpos, int ypos);
 }
