package primitives;

/**
 * @author YanaPatyuk
 */
public class Point {
    private double x;
    private double y;

    /**
     * constructor to new point.
     * @param x
     *            value.
     * @param y
     *            value.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
/**
 * copy point from other point.
 * @param applyToPoint p.
 */
    public Point(Point applyToPoint) {
        this.x = applyToPoint.getX();
        this.y = applyToPoint.getY();
    }

    /**
     * return the distance of this point to the other point.
     * @param other
     *            is another point.
     * @return distance.
     */
    public double distance(Point other) {
        double dx = this.x - other.getX();
        double dy = this.y - other.getY();
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    /**
     * return true is the points are equal, false otherwise.
     * @param other
     *            point.
     * @return boolean.
     */
    public boolean equals(Point other) {
        if (this.x == other.getX()) {
            if (this.y == other.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return the x value of this point.
     * @return x
     */
    public double getX() {
        return this.x;
    }
    /**
     * Return the y value of this point.
     * @return y.
     */
    public double getY() {
        return this.y;
    }
}