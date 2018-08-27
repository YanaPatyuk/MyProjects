package animations;

import java.awt.Color;

import biuoop.DrawSurface;
import biuoop.Sleeper;
import sprite.SpriteCollection;

/**
 * @author YanaPatyuk
 *The CountdownAnimation will display the given gameScreen,
 *for numOfSeconds seconds, and on top of them it will show
 *a countdown from countFrom back to 1, where each number will
 *appear on the screen for (numOfSeconds / countFrom) secods, before
 *it is replaced with the next one.
 */
public class CountdownAnimation implements Animation {
    private int countFrom;
    private int currentNum;

    private double numOfSeconds;
    private SpriteCollection gameScreen;
    private Sleeper sleeper;
    /**
     * constracctor.
     * @param numOfSeconds time.
     * @param countFrom number.
     * @param gameScreen object.
     */
public CountdownAnimation(double numOfSeconds,
                          int countFrom,
                          SpriteCollection gameScreen) {
    this.numOfSeconds = numOfSeconds;
    this.countFrom = countFrom;
    this.currentNum = countFrom + 1;
    this.gameScreen = gameScreen;
    sleeper = new biuoop.Sleeper();

     }
/**
 * print count down on top of the scressn.
 * @param d surface.
 * @param dt time-passed.
 */
public void doOneFrame(DrawSurface d, double dt) {
    this.gameScreen.drawAllOn(d);
    d.setColor(Color.GREEN);
    d.drawText(400, 300, "" + (currentNum - 1), 50);
    if (currentNum - 1 != countFrom) {
    sleeper.sleepFor((long) (this.numOfSeconds * 1000) / this.countFrom);
    }
        --currentNum;
    }

/**
 * check if need to stop.
 * @return return true for stop.
 */
public boolean shouldStop() {
    if (currentNum <= 0) {
        return true;
        }
    return false;
}
}
