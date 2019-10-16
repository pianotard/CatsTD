import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;

public class MobPath {

    private final Point start;
    List<Direction> directions = new ArrayList<>();

    public boolean hasIntersect(Rectangle bounds) {
        for (Direction d : this.directions) {
            if (d.intersects(bounds)) {
                return true;
            }
        }
        return false;
    }

    public Point getNextTick(Point currentLoc) {
        for (Direction d : this.directions) {
            if (d.contains(currentLoc)) {
                return d.getNextTick(currentLoc);
            }
        }
        return new Point(0, 0);
    }

    public Direction getNextDirection(Direction last) {
        boolean found = false;
        for (Direction d : this.directions) {
            if (found) {
                return d;
            }
            if (d.equals(last)) {
                found = true;
            }
        }
        return null;
    }

    public Direction getFirstDirection() {
        return this.directions.get(0);
    }

    public boolean atCorner(Point p) {
        for (Direction d : this.directions) {
            if (d.endsAt(p)) {
                return true;
            }
        }
        return false;
    }

    public Point getEndPoint() {
        return this.directions.get(this.directions.size() - 1).getEndPoint();
    }

    public Point getSpawnPoint() {
        return this.start;
    }

    public static MobPath start(Point p) {
        if (MobPath.exceedBounds(p)) {
            return null;
        }
        return new MobPath(p);
    }

    public MobPath add(Point p) {
        List<Direction> ret = new ArrayList<>(this.directions);
        if (this.directions.isEmpty()) {
            ret.add(new Direction(this.start, p));
        } else {
            ret.add(new Direction(this.directions.get(this.directions.size() - 1).getEndPoint(), p));
        }
        return new MobPath(this.start, ret);
    }

    private MobPath(Point start, List<Direction> directions) {
        this.start = start;
        this.directions = directions;
    }

    private MobPath(Point p) {
        this.start = p;
    }

    private static boolean exceedBounds(Point p) {
        int w = CatsTD.WINDOW_WIDTH;
        int h = CatsTD.WINDOW_HEIGHT;
        return (p.x < -30 || p.x > w + 30 || p.y < -30 || p.y > h + 30);
    }
}
