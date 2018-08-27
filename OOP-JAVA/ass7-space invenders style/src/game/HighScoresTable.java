package game;
/**
 * @author YanaPatyuk
 *
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * @author YanaPatyuk
 *
 */
public class HighScoresTable implements Serializable {
    private static final long serialVersionUID = 1L;
    private int sizeOfTable;
    private List<ScoreInfo> highScores;
    /**
     * Create an empty high-scores table with the specified size.
     * The size means that the table holds up to size top scores.
     * @param size of table.
     */
    public  HighScoresTable(int size) {
        this.sizeOfTable = size;
        this.highScores = new ArrayList<ScoreInfo>();
    }

    /**
     * @param score Add a high-score.
     */
    public void add(ScoreInfo score) {
        this.highScores.add(score);
        //ObjectCompere is a comperator.
        Collections.sort(highScores, new ObjectCompere());
        //if the table has more data then we need-remove the lower score.
        if (this.highScores.size() > this.sizeOfTable) {
           this.highScores.remove(sizeOfTable);
        }
    }

    /**
     * @return Return table size.
     */
    public int size() {
        return this.sizeOfTable;
    }

    /**
     * The list is sorted such that the highest
     * scores come first.
     * @return Return the current high scores.
     */
    public List<ScoreInfo> getHighScores() {
        return this.highScores;
    }

    /**
     * return the rank of the current score: where will it
     * be on the list if added?
     * Rank 1 means the score will be highest on the list.
     * Rank `size` means the score will be lowest.
     * Rank > `size` means the score is too low and will not
     * be added to the list.
     * @param score to check.
     * @return place of score.
     */
    public int getRank(int score) {
    int rank = 1;
    for (int i = 0; i < this.highScores.size(); i++) {
        if (this.highScores.get(i).getScore() >= score) {
            rank++;
        }
    }
    return rank;
}

/**
 * Clears the table.
 */
    public void clear() {
        this.highScores.clear();
    }

    /**
     * load table data from file.
     *  Current table data is cleared.
     * @param filename of file.
     * @throws IOException could not read.
     */
    public void load(File filename) throws IOException {
        ObjectInputStream in = null;
        try {
        FileInputStream fileIn = new FileInputStream(filename);
         in = new ObjectInputStream(fileIn);
         HighScoresTable tamp = (HighScoresTable) in.readObject();
         this.sizeOfTable = tamp.sizeOfTable;
         this.highScores = new ArrayList<ScoreInfo>(this.highScores);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Save table data to the specified file..
     * @param filename file.
     * @throws IOException could not write.
     */
    public void save(File filename) throws IOException {
        ObjectOutputStream oOs = null;
        try {
            oOs = new ObjectOutputStream(new FileOutputStream(filename));
            oOs.writeObject(this);
        } catch (Exception e) {
            System.out.print(" problem 1");
         } finally {
             if (oOs != null) {
                 oOs.close();
             }
         }
    }

    /**
     * Read a table from file and return it.
     * If the file does not exist, or there is a problem with
     * reading it, an empty table is returned.
     * @param filename of file.
     * @return If the file does not exist, or there is a problem with
     * reading it, an empty table is returned.
     */
    public static HighScoresTable loadFromFile(File filename) {
        ObjectInputStream in = null;
        try {
        FileInputStream fileIn = new FileInputStream(filename);
         in = new ObjectInputStream(fileIn);
         HighScoresTable newTable = (HighScoresTable) in.readObject();
         return newTable;
        } catch (IOException | ClassNotFoundException e) {
            //returne empty table-problem with reading;
          return new HighScoresTable(5);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new RuntimeException("Failed close file");
                    }
            }
        }
    }
 }
