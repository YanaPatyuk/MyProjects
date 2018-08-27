package animations;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
/**
 * @author YanaPatyuk
 *
 */
public class KeyPressStoppableAnimation implements Animation {
    private Animation animation;
    private String key;
    private KeyboardSensor keyboard;
    private boolean stop;
    private boolean isAlreadyPressed;
    /**
     * constractor.
     * @param sensor keyboard.
     * @param key string to press.
     * @param animation decorator.
     */
    public KeyPressStoppableAnimation(KeyboardSensor sensor, String key, Animation animation) {
        this.animation = animation;
        this.key = key;
        this.keyboard = sensor;
        this.stop = false;
        this.isAlreadyPressed = true;
    }
    @Override
    public void doOneFrame(DrawSurface d, double dt) {
        this.animation.doOneFrame(d, dt);
        if (this.keyboard.isPressed(this.key)) {
            if (!this.isAlreadyPressed) {
                this.stop = true;
            }
        } else {
        this.isAlreadyPressed = false;
        }
    }
    @Override
    public boolean shouldStop() {
        return this.stop;
    }

}
