package animations;

import biuoop.DrawSurface;
/**
 * @author YanaPatyuk
 *
 */
public interface Animation {
    /**
     * draw and change one frame.
     * @param d DrawSurface.
     * @param dt time-passed indicator.
     */
    void doOneFrame(DrawSurface d, double dt);
    /**
     * check if anumation should stop.
     * @return true if should. false otherwise.
     */
    boolean shouldStop();
 }