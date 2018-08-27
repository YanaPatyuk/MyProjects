package game;

import java.io.Serializable;

/**
 * @author YanaPatyuk
 *
 */
public class ScoreInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private int score;
    private String name;
    /**
     * constractor.
     * @param name of player.
     * @param score pf player.
     */
    public ScoreInfo(String name, int score) {
        this.name = name;
        this.score = score;
    }
    /**
     * @return name of platyer.
     */
    public String getName() {
        return this.name;
    }
    /**
     * @return score of player.
     */
    public int getScore() {
        return this.score;
    }
 }
