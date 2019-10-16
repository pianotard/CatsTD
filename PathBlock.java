import java.awt.Point;
import java.awt.Rectangle;

public class PathBlock {

    private static final int PATH_WIDTH = 30;

    Point topLeft;
    Rectangle illegal;

    public PathBlock(Point point) {
        this.topLeft = point;
        this.illegal = new Rectangle(point.x, point.y, PATH_WIDTH, PATH_WIDTH);
    }

    public boolean intersects(Rectangle r) {
        return this.illegal.intersects(r);    
    }

    @Override
    public String toString() {
        return "PathBlock " + this.illegal;
    }
}
