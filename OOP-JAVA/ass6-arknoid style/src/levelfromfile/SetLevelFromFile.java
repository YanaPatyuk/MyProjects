package levelfromfile;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YanaPatyuk
 *
 */
public class SetLevelFromFile {
    /**
     * read the file and get list of level sets.
     * @param reader file.
     * @return liset of levels.
     */
public static List<LevelSet> readLevelFromFile(java.io.Reader reader) {
    List<LevelSet> levelSets = new ArrayList<LevelSet>();
    LineNumberReader in = null;
    LevelSet levelSet = null;
    try {
        in = new LineNumberReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            if (in.getLineNumber() % 2 == 0) {
                levelSet.setPathForLevel(line.trim());
                levelSets.add(levelSet);
                levelSet = null;
            } else {
            String[] lineParts = line.trim().split(":");
            levelSet = new LevelSet(lineParts[0].trim(), lineParts[1].trim());
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException("Failed creating set level from file");
            }
        }
    }
    return levelSets;
}
}
