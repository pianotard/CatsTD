import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;

public abstract class AbstractProjectile extends JLabel {
   
    protected static final int SPEED = 2;

    protected Point startPoint;
    protected BufferedImage img;
    protected Direction directionToTarget;

    public AbstractProjectile() {
        super();
        super.setLayout(null);        
    }

    public AbstractProjectile initTarget(MobPackage mobPack) {
        Point mobLoc = mobPack.getMob().getLocation();
        double dist = mobLoc.distance(this.startPoint);
        double theta1 = Math.atan2(mobLoc.y - this.startPoint.y, mobLoc.x - this.startPoint.x);
        return this.clone().setTargetDirection(new Direction(this.startPoint, mobLoc));
    }

    public abstract AbstractProjectile clone();

    public int getDegrees() {
        return this.directionToTarget.getDegrees();
    }

    private AbstractProjectile setTargetDirection(Direction direction) {
        this.directionToTarget = direction;
        this.setLocation(this.startPoint);

        return this;
    }

    public void setStartPoint(Point start) {
        this.startPoint = start;
    }

    @Override
    public String toString() {
        return "Projectile with direction: " + this.directionToTarget;
    }
}
