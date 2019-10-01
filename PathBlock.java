import java.awt.Point;
import java.awt.Rectangle;

public class PathBlock {
    
    private static final int PATH_WIDTH = 20;

    Point p1;
    Point p2;

    public PathBlock(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public boolean intersects(Rectangle r) {
        int xDist = Math.abs(p1.x - p2.x);
        int yDist = Math.abs(p1.y - p2.y);
        if (p1.x == p2.x) {
            // Vertical Block
            int topY = p1.y < p2.y ? p1.y : p2.y;
            Rectangle illegal = new Rectangle(p1.x, topY, PathBlock.PATH_WIDTH, yDist);
            boolean contains = illegal.contains(r.getLocation());
            boolean towerEatLeft = illegal.contains(r.x + r.width, r.y);
            boolean towerEatTop = illegal.contains(r.x, r.y + r.height);
            return contains || towerEatLeft || towerEatTop;
        } else if (p1.y == p2.y) {
            // Horizontal Block
            int leftX = p1.x < p2.x ? p1.x : p2.x;
            Rectangle illegal = new Rectangle(leftX, p1.y, xDist, PathBlock.PATH_WIDTH);
            boolean contains = illegal.contains(r.getLocation());
            boolean towerEatLeft = illegal.contains(r.x + r.width, r.y);
            boolean towerEatTop = illegal.contains(r.x, r.y + r.height);
            return contains || towerEatLeft || towerEatTop;            
        } else {
            // Diagonal Block
            int diagSteps = xDist < yDist ? xDist : yDist;
            Point direction = new Point((p2.x -  p1.x) / xDist, (p2.y - p1.y) / yDist);
            boolean contains = false;
            for (int i = 1; i <= diagSteps; i++) {
                Point q1 = new Point(p1.x + direction.x * i, p1.y + direction.y * i);
                Point q2 = new Point(p1.x + direction.x * i + 1, p1.y + direction.y * i);
                PathBlock block = new PathBlock(q1, q2);
                contains = contains || block.intersects(r);
                if (contains) {
                    return contains;
                }
            }
            return false;
        }
    }
}
