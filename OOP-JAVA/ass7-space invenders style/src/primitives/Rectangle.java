package primitives;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YanaPatyuk
 *
 */
public class Rectangle {
    private Point upperLeft;
    private Point lowerRight;
    private double width;
    private double height;
    private Line[] linesOfFrame;
    private java.awt.Color color;
    /**
     * Create a new rectangle with location and width/height.
     * @param upperLeft point.
     * @param width of rect.
     * @param height rect.
     * @param color of rect.
     */
    public Rectangle(Point upperLeft, double width, double height, java.awt.Color color) {
        this.upperLeft = upperLeft;
        this.lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);
        this.width = width;
        this.height = height;
        this.color = color;
        linesOfFrame = new Line[4];
        createListForFrameLines();
    }
    /**
     * Create a new rectangle with location and width/height.
     * @param upperLeft point of rect.
     * @param width of rect.
     * @param height of rect.
     */
    public Rectangle(Point upperLeft, double width, double height) {
        this.upperLeft = upperLeft;
        this.lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);
        this.width = width;
        this.height = height;
        linesOfFrame = new Line[4];
        createListForFrameLines();
    }
/**
 * create array of frame lines.
 */
    public void createListForFrameLines() {
        linesOfFrame[0] = new Line(this.upperLeft.getX(), this.upperLeft.getY(),
                                    this.upperLeft.getX(), this.lowerRight.getY());
        linesOfFrame[1] = new Line(this.upperLeft.getX(), this.upperLeft.getY(),
                                   this.lowerRight.getX(), this.upperLeft.getY());
        linesOfFrame[2] = new Line(this.upperLeft.getX(), this.lowerRight.getY(),
                                    this.lowerRight.getX(), this.lowerRight.getY());
        linesOfFrame[3] = new Line(this.lowerRight.getX(), this.upperLeft.getY(),
                                    this.lowerRight.getX(), this.lowerRight.getY());
    }

    /**
     * Return a (possibly empty) List of intersection points.
     * with the specified line.
     * @param line to check.
     * @return list of points.
     */
    public java.util.List<Point> intersectionPoints(Line line) {
        List<Point> list = new ArrayList<Point>();
        for (int i = 0; i < 4; i++) {
            if (line.isIntersecting(this.linesOfFrame[i])) {
                list.add(line.intersectionWith(this.linesOfFrame[i]));
            }
        }
        return list;
    }
/**
 * @return width of rect.
 */
    public double getWidth() {
        return this.width; }
/**
 * @return height of rect.
 */
    public double getHeight() {
        return this.height; }
    /**
     * @return Returns the upper-left point of the rectangle
     */
    public Point getUpperLeft() {
        return this.upperLeft; }
    /**
     * @return Returns the lower-right point of the rectangle.
     */
    public Point getLowerRight() {
        return this.lowerRight; }
/**
 * @return color of rectangle.
 */
    public Color getColor() {
        return this.color; }
    /**
     * @param newColor to change;
     */
    public void setColor(Color newColor) {
        this.color = newColor;
    }
    public void moveRect(Velocity v) {
        this.upperLeft = new Point(v.applyToPoint(upperLeft));
        this.lowerRight = new Point(upperLeft.getX() + width, upperLeft.getY() + height);
        createListForFrameLines();
    }
}