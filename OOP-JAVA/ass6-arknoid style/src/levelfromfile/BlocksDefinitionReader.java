package levelfromfile;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;

/**
 * @author YanaPatyuk
 *
 */
public class BlocksDefinitionReader {
/**
 * create BlocksFromSymbolsFactory.
 * @param reader file.
 * @return BlocksFromSymbolsFactory.
 */
    public static BlocksFromSymbolsFactory fromReader(java.io.Reader reader) {
        BlocksFromSymbolsFactory info = new BlocksFromSymbolsFactory();
        BufferedReader in = null;
        Integer  defHight = null, defWight = null, defPoints = null;
        Color defColor = null, defColorStroke = null;
        Image defImage = null;
        int numberOfbackgrounds = 1;
        in = new BufferedReader(reader);
        String line;
        try {
              while ((line = in.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("#")) {
                        continue;
                    } else if (line.startsWith("default")) {
                        String l = line.substring("default".length()).trim();
                        String[] lineInfo = l.split(" ");
                        for (int i = 0; i < lineInfo.length; i++) {
                            String[] pair = lineInfo[i].split(":");
                            String key = pair[0];
                            String value = pair[1];
                            if (key.equals("width")) {
                                defWight = Integer.parseInt(value);
                            } else if (key.equals("height")) {
                                defHight = Integer.parseInt(value);
                            } else if (key.equals("hit_points")) {
                                defPoints = Integer.parseInt(value);
                            } else if (key.equals("stroke")) {
                                defColorStroke = createColorFromString(value);
                            }
                        }
                    } else if (line.startsWith("bdef")) {
                        String l = line.substring("bdef".length()).trim();
                        BlockInfo block = new BlockInfo();
                        String[] lineInfo = l.split(" ");
                        String symbol = null;
                        //add the default param.
                        if (defHight != null) {
                            block.addHight(defHight);
                            }
                        if (defWight != null) {
                            block.addWight(defWight);
                            }
                        if (defPoints != null) {
                            block.addHitPoints(defPoints);
                            }
                        if (defColor != null) {
                            block.addColor(1, defColor);
                            }
                        if (defColorStroke != null) {
                            block.addStrok(defColorStroke);
                            }
                        if (defImage != null) {
                            block.addImage(1, defImage);
                            }
                        for (int i = 0; i < lineInfo.length; i++) {
                            String[] pair = lineInfo[i].split(":");
                            String key = pair[0];
                            String value = pair[1];
                            if (key.equals("width")) {
                                block.addWight(Integer.parseInt(value));
                            } else if (key.equals("height")) {
                                block.addHight(Integer.parseInt(value));
                            } else if (key.equals("hit_points")) {
                                block.addHitPoints(Integer.parseInt(value));
                            } else if (key.equals("stroke")) {
                                block.addStrok(createColorFromString(value));
                            } else if (key.equals("hit_points")) {
                                block.addHitPoints(Integer.parseInt(value));
                            } else if (key.startsWith("fill")) {
                                if (key.startsWith("fill-")) {
                                    String[] pairF = key.split("-");
                                    String valueF = pairF[1];
                                    int numOfBlock = Integer.parseInt(valueF);
                                    Color c = createColorFromString(value);
                                    if (c != null) {
                                    block.addColor(numOfBlock, c);
                                    } else {
                                        block.addImage(numOfBlock, getImageFromString(value));
                                    }
                                } else {
                                Color c = createColorFromString(value);
                                if (c != null) {
                                block.addColor(numberOfbackgrounds, c);
                                } else {
                                    block.addImage(numberOfbackgrounds, getImageFromString(value));
                                }
                                numberOfbackgrounds++;
                                }
                            } else if (key.equals("symbol")) {
                                symbol = value;
                            }
                        }
                        numberOfbackgrounds = 1;
                        //if the block is not correct-dont add it.
                        if (!block.checkCorrect()) {
                            continue;
                        }
                        info.addBlockCreatoe(symbol, block);
                    } else if (line.startsWith("sdef")) {
                        String l = line.substring("sdef".length()).trim();
                        String[] lineInfo = l.split(" ");
                        String symbol = null;
                        Integer num = null;
                        for (int i = 0; i < lineInfo.length; i++) {
                            String[] pair = lineInfo[i].split(":");
                            String key = pair[0];
                            String value = pair[1];
                            if (key.equals("symbol")) {
                                symbol = value;
                            } else if (key.equals("width")) {
                                num = Integer.parseInt(value);
                            }
                        }
                        info.addSpacer(symbol, num);
                        }
                    }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
                return info;
    }
    /**
     * create color from string type "color(...)".
     * @param s srting.
     * @return color. if wrong input return null.
     */
    public static Color createColorFromString(String s) {
        Color color;
        if ((s.startsWith("color(RGB(")) && (s.endsWith("))"))) {
            String c = cutTheString(s, "color(RGB(", "))");
            String[] parts = c.split(",");
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());
            color = new Color(r, g, b);
    } else {
        String c = cutTheString(s, "color(", ")");
        try {
            Field field = Color.class.getField(c);
            color = (Color) field.get(null);
        } catch (Exception e) {
            color = null; // Not defined
            }
        }
        return color;
    }
    /**
     * cut the string start and end letters and return the leftover string.
     * @param s string.
     * @param start part to cut.
     * @param end part to cut.
     * @return string.
     */
    private static String cutTheString(String s, String start, String end) {
        return s.substring(start.length(), s.length() - end.length());
    }
    /**
     * create an image from string.
     * @param s string.
     * @return image.
     */
    public static Image getImageFromString(String s) {
        if (s.startsWith("image(") && (s.endsWith(")"))) {
            String i = cutTheString(s, "image(", ")");
            InputStream is = null;
            try {
                is = ClassLoader.getSystemClassLoader().getResourceAsStream(i);
                Image picture = ImageIO.read((new File(i)));
                return picture;
            } catch (IOException e) {
                throw new RuntimeException("Failed load image");
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed loading image: " + s, e);
                    }
                }
            }
        }
        return null;
        }
}
