import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.List;
import javax.swing.JLabel;

public abstract class AbstractProjectile extends JLabel {
   
    private static final int DEFAULT_DAMAGE = 1;
    private static final int DEFAULT_PIERCE = 1;

    protected Point startPoint;
    protected int dimension;
    protected Direction directionToTarget;
    protected int damage = DEFAULT_DAMAGE;
    protected int pierce = DEFAULT_PIERCE;
    protected int moveCD;
    protected int tickDelay;

    public AbstractProjectile() {
        super();
        this.setLayout(null);        
    }

    public AbstractProjectile initTarget(MobPackage mobPack, int maxDist) {
        Point target = this.computeTarget(mobPack);
        return this.clone().setTargetDirection(new Direction(this.startPoint, target, maxDist));
    }

    public abstract void setRotation(int degree);
    public abstract AbstractProjectile clone();

    public Optional<MobPackage> findCollision(List<MobPackage> spawnedMobs) {
        for (MobPackage mobPack : spawnedMobs) {
            if (this.collides(mobPack)) {
                return Optional.of(mobPack);
            }
        }
        return Optional.empty();
    }

    public boolean exhausted() {
        return this.pierce == 0;
    }

    public void pierce() {
        System.out.println("Pierce");
        this.pierce--;
    }

    public boolean collides(MobPackage mobPack) {
        return this.getBounds().intersects(mobPack.getMob().getBounds());
    }

    public boolean exited() {
        return this.directionToTarget.endsAt(this.getLocation());
    }

    public void move() {
        if (this.moveCD < this.tickDelay) {
            return;
        }
        this.moveCD = 0;
        this.setLocation(this.directionToTarget.getNextTick(this.getLocation()));
    }

    public void tick() {
        if (this.moveCD < this.tickDelay) {
            this.moveCD++;
        }
    }

    public int getDamage() {
        return this.damage;
    }

    public int getPierce() {
        return this.pierce;
    }

    public int getDegrees() {
        return this.directionToTarget.getDegrees();
    }

    public double getSpeed() {
        return 1.0 / this.tickDelay;
    }

    public AbstractProjectile setDamage(int damage) {
        this.damage = damage;
        return this;
    }

    public AbstractProjectile setPierce(int pierce) {
        this.pierce = pierce;
        return this;
    }

    private AbstractProjectile setTargetDirection(Direction direction) {
        this.directionToTarget = direction;
        this.setLocation(this.startPoint.x - dimension / 2, this.startPoint.y - dimension / 2);
        this.setRotation(direction.getDegrees());
        return this;
    }

    public void setStartPoint(Point start) {
        this.startPoint = start;
    }

    public Point computeTarget(MobPackage mobPack) {
        double p1x = this.startPoint.x;
        double p1y = this.startPoint.y;
        double p2x = mobPack.getMob().getLocation().x;
        double p2y = mobPack.getMob().getLocation().y;
        double v2x = mobPack.getVelocity().getX();
        double v2y = mobPack.getVelocity().getY();
        double bulletSpeed = this.getSpeed();

        double a = v2x * v2x + v2y + v2y - bulletSpeed * bulletSpeed;
        double b = 2 * (p2x * v2x - p1x * v2x + p2y * v2y - p1y * v2y);
        double c = p2x * p2x + p1x * p1x + p2y * p2y + p1y * p1y - 2 * (p2x * p1x + p2y * p1y);

        double result = (- b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
    
        return new Point((int) (p2x + result * v2x), (int) (p2y + result * v2y));
    }

    @Override
    public String toString() {
        return "Projectile with direction: " + this.directionToTarget;
    }
}
