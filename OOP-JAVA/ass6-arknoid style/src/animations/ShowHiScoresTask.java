package animations;

import game.AnimationRunner;
/**
 * @author YanaPatyuk
 *
 */
public class ShowHiScoresTask implements Task<Void> {
    private AnimationRunner runner;
    private Animation highScoresAnimation;
    /**
     * constactor.
     * @param runner to run.
     * @param highScoresAnimation table.
     */
    public ShowHiScoresTask(AnimationRunner runner, KeyPressStoppableAnimation highScoresAnimation) {
       this.runner = runner;
       this.highScoresAnimation = highScoresAnimation;
    }
    /**
     * run the task.
     * @return null.
     */
    public Void run() {
       this.runner.run(this.highScoresAnimation);
       return null;
    }
 }
