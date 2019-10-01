import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;

public class MobPath {

    List<Point> points = new ArrayList<>();

    public boolean hasIntersect(Rectangle bounds) {
        for (int i = 0; i < this.points.size() - 1; i++) {
            Point p1 = this.points.get(i);
            Point p2 = this.points.get(i + 1);
            PathBlock path = new PathBlock(p1, p2);
            if (path.intersects(bounds)) {
                return true;
            }
        }
        return false;
    }

    public Point getNextTick(Point currentLoc) {
        for (int i = 0; i < this.points.size() - 1; i++) {
            Point p1 = this.points.get(i);
            if (p1.equals(currentLoc)) {
                Point p2 = this.points.get(i + 1);
                int dx = p2.x - p1.x;
                int dy = p2.y - p1.y;
                dx = dx == 0 ? 0 : dx / Math.abs(dx);
                dy = dy == 0 ? 0 : dy/ Math.abs(dy);
                return new Point(dx, dy);
            }
        }
        return new Point(0, 0);
    }

    public boolean atCorner(Point p) {
        for (Point corner : this.points) {
            if (corner.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public Point getEndPoint() {
        return this.points.get(this.points.size() - 1);
    }

    public Point getSpawnPoint() {
        return this.points.get(0);
    }

    public static MobPath start(Point p) {
        if (MobPath.exceedBounds(p)) {
            return null;
        }
        return new MobPath(p);
    }

    public MobPath add(Point p) {
        List<Point> ret = new ArrayList<>(this.points);
        ret.add(p);
        return new MobPath(ret);
    }

    private MobPath(List<Point> points) {
        this.points = points;
    }

    private MobPath(Point p) {
        this.points.add(p);
    }

    private static boolean exceedBounds(Point p) {
        int w = CatsTD.WINDOW_WIDTH;
        int h = CatsTD.WINDOW_HEIGHT;
        return (p.x < -30 || p.x > w + 30 || p.y < -30 || p.y > h + 30);
    }
}
