import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;

public class Direction {
    
    private final Point p1;
    private final Point p2;
    private List<Point> points = new ArrayList<>();

    public Direction(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        int xDist = Math.abs(p1.x - p2.x);
        int yDist = Math.abs(p1.y - p2.y);
        int pixels = xDist > yDist ? xDist : yDist;
        
        boolean horizontal = yDist == 0;
        boolean vertical = xDist == 0;
        boolean goingUp = p1.y > p2.y;
        boolean goingRight = p1.x < p2.x;
       
        if (horizontal) {
            for (int i = 0; i < xDist; i++) {
               Point p = goingRight ? new Point(p1.x + i, p1.y) : new Point(p2.x - i, p2.y);
               this.points.add(p);
            }
            this.points.add(p2);
            return;
        }

        if (vertical) {
            for (int i = 0; i < yDist; i++) {
                Point p = goingUp ? new Point(p1.x, p1.y - i) : new Point(p1.x, p1.y + i);
                this.points.add(p);
            }
            this.points.add(p2);
            return;
        }

        double gradient = (yDist + 0.0) / xDist;

        double climbed = 0.0;
        double walked = 0.0;

        int x = p1.x;
        int y = p1.y;
        for (int i = 0; i < pixels; i++) {
            int beforeClimb = (int) climbed;
            int beforeWalk = (int) walked;
            this.points.add(new Point(x, y));
            climbed += gradient;
            walked += 1 / gradient;
            if ((int) climbed != beforeClimb) {
                if (goingUp) {
                    y--;
                } else {
                    y++;
                }
            }
            if ((int) walked != beforeWalk) {
                if (goingRight) {
                    x++;
                } else {
                    x--;
                }
            }
        }
        this.points.add(p2);
       
    }

    public Point getNextTick(Point p) {
        int index = this.points.indexOf(p);
        if (index + 1 == this.points.size()) {
            return null;
        }
        return this.points.get(index + 1);
    }

    public int getDegrees() {
        return (int) Math.toDegrees(Math.atan2(p1.y - p2.y, p1.x - p2.x)) - 90;
    }

    public boolean intersects(Rectangle r) {
        for (Point p : this.points) {
            if (new PathBlock(p).intersects(r)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Point p) {
        return this.points.contains(p);
    }

    public boolean endsAt(Point p) {
        boolean x = Math.abs(this.p2.x - p.x) < 1E-3;
        boolean y = Math.abs(this.p2.y - p.y) < 1E-3;
        return x && y;
    }

    public Point getEndPoint() {
        return this.p2;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Direction) {
            Direction d = (Direction) o;
            return d.p1.equals(this.p1) && d.p2.equals(this.p2);
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + p1.x + ", " + p1.y + ") to (" + p2.x + ", " + p2.y + ")";
    }
}
