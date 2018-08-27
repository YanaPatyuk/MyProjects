package animations;

import java.awt.Color;
import java.util.Random;

import biuoop.DrawSurface;
import primitives.Counter;
/**
 * @author YanaPatyuk
 *
 */
public class LoseWinScreen implements Animation {
        private boolean stop;
        private Counter lives;
        private Counter score;
        /**
         * @param lives counter.
         * @param score counter.
         */
        public LoseWinScreen(Counter lives, Counter score) {
           this.stop = false;
           this.lives = lives;
           this.score = score;
        }
        /**
         * draw text on screen. if space key pressed-continue.
         * draw stop text
         * this.stop = true;
         * @param d DrawSurface.
         * @param dt timepassed.
         */
        public void doOneFrame(DrawSurface d, double dt) {
            Random rand = new Random();
            if (this.lives.getValue() > 0) {
                d.setColor(java.awt.Color.getHSBColor(rand.nextInt(20) * 0.5F, rand.nextInt(20) * 0.2F, 0.8F));
                d.fillRectangle(0, 0, 800, 600);
                d.setColor(Color.black);
           d.drawText(350, d.getHeight() / 2 - 50, "You Win!", 50);
            } else {
                d.fillRectangle(0, 0, 800, 600);
                d.setColor(Color.black);
                d.setColor(Color.magenta);
            d.drawText(350, d.getHeight() / 2 - 50, "You Lost!", 50);
            }
            d.drawText(350, d.getHeight() / 2, "Your score is:" + this.score.getValue(), 32);
            d.drawText(100, d.getHeight() / 2 + 150, "Press Space To Continue", 32);
        }
        /**
         * @return true if need to stop.
         */
        public boolean shouldStop() {
            return this.stop;
            }
}
