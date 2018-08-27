package levelfromfile;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import primitives.Velocity;
import sprite.Block;
import sprite.ColoredBackground;
import sprite.ImageBackground;
/**
 * @author YanaPatyuk
 *
 */
public class LevelSpecificationReader {
    //this member used to count rows in "START_BLOCKS" part.
    private Integer currentRowIndex = 1;
    /**
     * @param reader file.
     * @return list of levels.
     */
    public List<LevelInformation> fromReader(java.io.Reader reader) {
        List<LevelInformation> levels = new ArrayList<LevelInformation>();
        BufferedReader in = null;
        LevelFromFile level = null;
        BlocksFromSymbolsFactory levelBlockDef;
        Map<String, BlocksFromSymbolsFactory> blockDefinishions = new HashMap<String, BlocksFromSymbolsFactory>();
        boolean readBlocks = false;
        try {
            in = new BufferedReader(reader);
            String line;
            //loop ends at the end of file.
             while ((line = in.readLine()) != null) {
                 line = line.trim();
                 if (line.startsWith("#")) {
                     continue;
                 } else if (line.equals("")) {
                     continue;
                 } else if (line.equals("START_LEVEL")) {
                     level = new LevelFromFile();
                 } else if (line.equals("END_LEVEL")) {
                     if (!level.checkLevelCorrect()) {
                         System.out.println("error.levels files incorrect");
                         return null;
                     }
                     levels.add(level);
                     this.currentRowIndex = 1;
                 } else if (line.equals("START_BLOCKS")) {
                     readBlocks = true;
                 } else if (line.equals("END_BLOCKS")) {
                     readBlocks = false;
                 } else if (line.startsWith("block_definitions")) {
                     String[] parts = line.split(":");
                     String key = parts[0];
                     String value = parts[1];
                     if (key.equals("block_definitions")) {
                     if (!blockDefinishions.containsKey(value)) {
                         levelBlockDef =  BlocksDefinitionReader.fromReader(
                                                         new InputStreamReader(new FileInputStream(value)));
                         blockDefinishions.put(value, levelBlockDef);
                         level.setBlockdef(levelBlockDef);
                     } else {
                         level.setBlockdef(blockDefinishions.get(value));
                     }
                     }
                 } else if (readBlocks) {
                     int currentYValue = level.getRowHight() * this.currentRowIndex + level.getStartY();
                     int currentXValue = level.getStartX();
                     for (int i = 0; i < line.length(); i++) {
                         if (line.equals("")) {
                             continue;
                         }
                         char symbol = line.charAt(i);
                         if (level.getBlockdef().isSpaceSymbol("" + symbol)) {
                             currentXValue += level.getBlockdef().getSpaceWidth("" + symbol);
                         } else if (level.getBlockdef().isBlockSymbol("" + symbol)) {
                             Block block = level.getBlockdef().getBlock("" + symbol, currentXValue, currentYValue);
                             level.addBlock(block);
                             currentXValue = (int) block.getCollisionRectangle().getWidth() + currentXValue;
                         } else {
                             throw new RuntimeException("Failed creating block of type:" + symbol);
                         }
                     }
                     this.currentRowIndex++;
                 } else {
                     level = this.addPrefernceToBlock(level, line);
                 }
            }
        } catch (IOException e) {
            System.out.println(" Something went wrong while reading !");
        } finally {
             if (in != null) { // Exception might have happened at constructor
                 try {
                 in.close(); // closes FileInputStream too
                 } catch (IOException e) {
                 System.out.println(" Failed closing the file !");
                 }
            }
        }
        return levels;
     }
/**
 * add fields to level from info in line.
 * @param level to add to.
 * @param line string.
 * @return level.
 */
    private LevelFromFile addPrefernceToBlock(LevelFromFile level, String line) {
        String[] parts = line.split(":");
        String key = parts[0];
        String value = parts[1];
        if (key.equals("level_name")) {
            level.setLevelName(value);
        } else if (key.equals("ball_velocities")) {
            String[] velocitiesDef = value.split(" ");
            int numOfBalls = 0;
            for (String velDef : velocitiesDef) {
                String[] props = velDef.split(",");
                level.addVelocity(
                        Velocity.fromAngleAndSpeed(Double.parseDouble(props[0]), Double.parseDouble(props[1])));
                numOfBalls++;
            }
            level.setNumberOfBalls(numOfBalls);
        } else if (key.equals("background")) {
            if (value.startsWith("image")) {
                Image is = BlocksDefinitionReader.getImageFromString(value);
                level.setBackground(new ImageBackground(is));
            } else {
                level.setBackground(new ColoredBackground(BlocksDefinitionReader.createColorFromString(value)));
            }
        } else if (key.equals("paddle_speed")) {
            level.setPaddleSpeed(Integer.parseInt(value));
        } else if (key.equals("paddle_width")) {
            level.setPaddleWitght(Integer.parseInt(value));
        } else if (key.equals("blocks_start_x")) {
            level.setSartOfBlocksX(Integer.parseInt(value));
        } else if (key.equals("blocks_start_y")) {
            level.setSartOfBlocksY(Integer.parseInt(value));
        } else if (key.equals("row_height")) {
            level.setRowHeight(Integer.parseInt(value));
        } else if (key.equals("num_blocks")) {
            level.numberOfBLoksToRemove(Integer.parseInt(value));
        }
        return level;
    }
}
