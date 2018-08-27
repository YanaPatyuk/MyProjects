package animations;
import game.HighScoresTable;
import game.ScoreInfo;

import java.awt.Color;
import java.util.List;
import biuoop.DrawSurface;

/**
 * @author YanaPatyuk
 *
 */
public class HighScoresAnimation implements Animation {
    private HighScoresTable table;
    private boolean stop;


    /**
     * Contractor.
     * @param scores table.
     */
    public HighScoresAnimation(HighScoresTable scores) {
        this.table = scores;
        this.stop = false;

    }
    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        List<ScoreInfo> tamp = this.table.getHighScores();
            d.setColor(Color.LIGHT_GRAY);
            d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
            d.setColor(Color.BLACK);
            d.drawText(51, 50, "High Scores - Top " + this.table.size(), 32);
            d.drawText(49, 50, "High Scores - Top " + this.table.size(), 32);
            d.drawText(50, 51, "High Scores - Top " + this.table.size(), 32);
            d.drawText(50, 49, "High Scores - Top " + this.table.size(), 32);
            d.setColor(Color.YELLOW);
            d.drawText(50, 50, "High Scores - Top " + this.table.size(), 32);

            d.setColor(Color.BLACK);
            d.drawText(101, 120, "Player Name", 24);
            d.drawText(99, 120, "Player Name", 24);
            d.drawText(100, 121, "Player Name", 24);
            d.drawText(100, 119, "Player Name", 24);
            d.setColor(Color.GREEN);
            d.drawText(100, 120, "Player Name", 24);
            d.setColor(Color.BLACK);
            d.drawText(451, 120, "Score", 24);
            d.drawText(449, 120, "Score", 24);
            d.drawText(450, 121, "Score", 24);
            d.drawText(450, 119, "Score", 24);
            d.setColor(Color.GREEN);
            d.drawText(450, 120, "Score", 24);

            d.setColor(Color.BLACK);
            d.drawLine(100, 130, 700, 130);
            d.setColor(Color.GREEN);
            d.drawLine(100, 131, 700, 131);
            d.setColor(Color.BLACK);
            d.drawLine(100, 132, 700, 132);
        for (int i = 0; i < this.table.getHighScores().size(); i++) {
            d.setColor(Color.black);
            d.drawText(100, i * 30 + 160, tamp.get(i).getName(), 32);
            d.drawText(450, i * 30 + 160, "" + tamp.get(i).getScore(), 32);
        }
    }

    @Override
    public boolean shouldStop() {
        return this.stop;
    }
    /**
     * @return table of scores.
     */
    public HighScoresTable getTable() {
        return this.table;
    }
    }
