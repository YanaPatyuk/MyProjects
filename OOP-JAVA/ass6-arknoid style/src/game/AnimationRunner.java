package game;

import animations.Animation;
import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

/**
 * @author YanaPatyuk
 *
 */
public class AnimationRunner {
    private GUI gui;
    private int framesPerSecond;
    private Sleeper sleeper;
    /**
     * constractor.
     * @param gui to start.
     */
    public AnimationRunner(GUI gui) {
        framesPerSecond = 60;
        this.gui = gui;
        sleeper = new biuoop.Sleeper();
        }
    /**
     * run the given animation.
     * @param animation object.
     */
    public void run(Animation animation) {
        int millisecondsPerFrame = 1000 / framesPerSecond;
       while (!animation.shouldStop()) {
          long startTime = System.currentTimeMillis(); // timing
          DrawSurface d = gui.getDrawSurface();
          animation.doOneFrame(d, millisecondsPerFrame / 1000.0D);
          gui.show(d);
          long usedTime = System.currentTimeMillis() - startTime;
          long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
          if (milliSecondLeftToSleep > 0) {
              this.sleeper.sleepFor(milliSecondLeftToSleep);
          }
       }
    }
    /**
     * @return gui.
     */
    public GUI getGui() {
        return this.gui;
    }
 }
