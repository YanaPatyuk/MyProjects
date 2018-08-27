package primitives;
import java.util.List;

/**
 * @author YanaPatyuk
 */
public class Line {
    private Point start;
    private Point end;
    private Double incline;
    private double b;

    /**
     * constructors-create line by given points. calculate incline by values of
     * points. m=dy/dx. calculate b = y-mx.
     * @param start
     *            point.
     * @param end
     *            point.
     */
    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
        if (start.getX() != end.getX()) {
            this.incline = (start.getY() - end.getY()) / (start.getX() - end.getX());
            this.b = start.getY() - (this.incline * start.getX());
        } else { // case when points are even-no line.
            this.incline = null;
            this.b = 0;
        }
    }
    /**
     * constructor- create line by x y values. calculate incline by values of
     * points. m=dy/dx. calculate b = y-mx.
     * @param x1
     *            for point 1.
     * @param y1
     *            for point 1.
     * @param x2
     *            for point 2.
     * @param y2
     *            for point 2.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
        if (x1 == x2) {
            this.incline = (Double) null; // Infinity case, line Parallel to X.
            this.b = 0;
        } else {
            this.incline = (y1 - y2) / (x1 - x2);
            this.b = y1 - (this.incline * x1);
        }
    }

    /**
     * calculate length of line.
     * @return the length.
     */
    public double length() {
        return this.start.distance(end);
    }

    /**
     * find middle point of line.
     * @return the middle point of the line.
     */
    public Point middle() {
        if (this.start.equals(end)) {
            return this.start;
        }
        double middelX = (this.start.getX() + this.end.getX()) / 2;
        double middelY = (this.start.getY() + this.end.getY()) / 2;
        return new Point(middelX, middelY);
    }

    /**
     * return start point of line.
     * @return point.
     */
    public Point start() {
        return this.start;
    }
    /**
     * return the end point of line.
     * @return point.
     */
    public Point end() {
        return this.end;
    }

    /**
     * check if given line intersect to this line.
     * @param other
     *            line.
     * @return Returns true if the lines intersect, false otherwise.
     */
    public boolean isIntersecting(Line other) {
        if (this.intersectionWith(other) == null) {
            return false;
        }
        return true;
    }
    /**
     * find intersection point between lines if exist, if not return null.
     * @param other
     *            line.
     * @return Returns the intersection point if the lines intersect, and null
     *         otherwise.
     */

    public Point intersectionWith(Line other) {
        double y, x; // if the inclines are different-check if the intersection
                     // point is in boundaries.s
        if (this.incline != other.incline) {
            if ((this.incline != null) && (other.incline != null)) {
                x = (other.b - this.b) / (this.incline - other.incline);
                y = this.incline * x + this.b;
                x = Math.round(x * Math.pow(10, 10)) / Math.pow(10, 10);
                y = Math.round(y * Math.pow(10, 10)) / Math.pow(10, 10);
                if ((this.checkRangeX(x)) && (other.checkRangeX(x))) {
                    if ((this.checkRangeY(y)) && (other.checkRangeY(y))) {
                        return new Point(x, y);
                        }
                }
                return null;
            } else if (this.incline == null) { // if only this incline is null
                x = this.start.getX();
                y = other.incline * this.start.getX() + other.b;
                x = Math.round(x * Math.pow(10, 10)) / Math.pow(10, 10);
                y = Math.round(y * Math.pow(10, 10)) / Math.pow(10, 10);
                if ((this.checkRangeX(x)) && (other.checkRangeX(x))) {
                    if ((this.checkRangeY(y)) && (other.checkRangeY(y))) {
                        return new Point(x, y);
                        }
                }
                return null;
            } else if (other.incline == null) { // if other inclines is null
                x = other.start.getX();
                y = this.incline * x + this.b;
                x = Math.round(x * Math.pow(10, 10)) / Math.pow(10, 10);
                y = Math.round(y * Math.pow(10, 10)) / Math.pow(10, 10);
                if ((this.checkRangeX(x)) && (other.checkRangeX(x))) {
                    if ((this.checkRangeY(y)) && (other.checkRangeY(y))) {
                        return new Point(x, y);
                        }
                }
                return null;
                } else {
                return null;
            } // if the inclines identical-they are not intersection in any point.
        }
        return null;
    }
    /**
     * check if x between value of start point & end point.
     * @param x value.
     * @return true is x in range and false if not.
     */
    public boolean checkRangeX(double x) {
        double maxValue = Math.max(this.start.getX(), this.end.getX());
        double minValue = Math.min(this.start.getX(), this.end.getX());
        if ((maxValue >= x) && (minValue <= x)) {
            return true;
            }
        return false;
    }
    /**
     * check if y between value of start point & end point.
     * @param y value.
     * @return true is y in range and false if not.
     */
    public boolean checkRangeY(double y) {
        double maxValue = Math.max(this.start.getY(), this.end.getY());
        double minValue = Math.min(this.start.getY(), this.end.getY());
        if ((maxValue >= y) && (minValue <= y)) {
            return true;
            }
        return false;
    }

    /**
     * check if lines are equals.
     * @param other
     *            line.
     * @return return true is the lines are equal, false otherwise.
     */
    public boolean equals(Line other) {
        if ((this.start.equals(other.start())) && (this.end.equals(other.end()))) {
            return true;
        }
        if ((this.start.equals(other.end())) && (this.end.equals(other.start()))) {
            return true;
        }
        return false;
    }
/**
 * If this line does not intersect with the rectangle, return null.
 * Otherwise, return the closest intersection point to the
 * start of the line.
 * @param rect recatangle object.
 * @return the closest point.
 */
      public Point closestIntersectionToStartOfLine(Rectangle rect) {
          if (rect.intersectionPoints(this).isEmpty()) {
              return null;
              }
          List<Point> intersection = rect.intersectionPoints(this);
          Point closePoint = closePoint(intersection);
          return closePoint;
      }
/**
 * check witch of the points is the closest to the start point in line.
 * @param points list.
 * @return point witch most closest to start point.
 */
      public Point closePoint(List<Point> points) {
          Point closePoint =  (Point) points.get(0);
          for (int i = 0; i < points.size(); i++) {
              if (closePoint.distance(this.start) >= ((Point) points.get(i)).distance(this.start)) {
                  closePoint = (Point) points.get(i);
              }
          }
          return closePoint;
      }
}
