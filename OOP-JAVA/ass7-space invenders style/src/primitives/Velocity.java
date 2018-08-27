package primitives;
/**
 * Velocity specifies the change in position on the `x` and the `y` axes.
 * @author YanaPatyuk
 */
public class Velocity {
    // constructor
    private double dx;
    private double dy;

    /**
     * create velocity by given parameters.
     * @param dx
     *            value.
     * @param dy
     *            value.
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
/**
 * constrctor by velocity.
 * @param v velocity.
 */
    public  Velocity(Velocity v) {
        this.dx = v.getDx();
        this.dy = v.getDy();
    }

    /**
     * Take a point with position (x,y) and return a new point.
     * @param p
     *            corrent point.
     * @return new point with position (x+dx, y+dy).
     */
    public Point applyToPoint(Point p) {
        double newX = p.getX() + this.dx;
        double newY = p.getY() + this.dy;
        return new Point(newX, newY);
    }

    /**
     * @return Dx value.
     */
    public double getDx() {
        return dx;
    }

    /**
     * @return Dy value.
     */
    public double getDy() {
        return dy;
    }

    /**
     * change dx value to oposit direction.
     */
    public void changeDx() {
        this.dx = this.dx * (-1);
    }

    /**
     * * change dy value to oposit direction.
     */
    public void changeDy() {
        this.dy = this.dy * (-1);
    }

    /**
     * create velocity parameters by angle and speed.
     * @param angle
     *            value.
     * @param speed
     *            value.
     * @return new velocity.
     */
    public static  Velocity fromAngleAndSpeed(double angle, double speed) {
        // change angle to radians values.
        angle = Math.toRadians(angle);
        Double dx = Math.sin(angle) * speed;
        Double dy = -Math.cos(angle) * speed;
        return new Velocity(dx, dy);
    }
    /**
     * @return calculated speed.
     */
    public double getSpeed() {
        double speed = Math.sqrt((dx * dx) + (dy * dy));
         return  speed;
    }
    /**
     * change velocity to adjusted frame time.
     * @param dt frame per sec.
     * @return new fixed velocity.
     */
    public Velocity fixedVelocity(double dt) {
        return new Velocity(this.dx * dt, this.dy * dt);
    }
}
