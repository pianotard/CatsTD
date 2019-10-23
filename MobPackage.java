import java.awt.Point;
import java.awt.geom.Point2D;

public class MobPackage implements Comparable<MobPackage> {
    
    AbstractMob mob;
    MobPath path;
    int spawnTick;
    int moveCD;
    Direction movementDirection;

    public MobPackage(AbstractMob mob, int spawnTick) {
        this.mob = mob;
        this.spawnTick = spawnTick;
        this.moveCD = mob.getTickDelay();
    }

    public Point2D getVelocity() {
        return this.movementDirection.getVelocity(this.mob.getTickDelay());
    }

    public int getReward() {
        return this.mob.getReward();
    }

    public int getAttack() {
        return this.mob.getAttack();
    }

    public boolean dead() {
        return this.mob.dead();
    }

    public boolean exited() {
        Point currentLoc = this.mob.getLocation();
        return currentLoc.equals(this.path.getEndPoint());
    }

    public void inflictDamage(int dmg) {
        this.mob.inflictDamage(dmg);
    }

    public void move() {
        if (this.moveCD < this.mob.getTickDelay()) {
            return;
        }
        this.moveCD = 0;
        Point currentLoc = this.mob.getLocation();
        if (this.movementDirection == null) {
            this.movementDirection = this.path.getFirstDirection();
        }
        this.movementDirection = this.movementDirection.endsAt(currentLoc) ? 
            this.path.getNextDirection(this.movementDirection) : this.movementDirection;
        if (this.movementDirection == null) {
            return;
        }
        Point nextTick = this.movementDirection.getNextTick(currentLoc);
        this.mob.setLocation(nextTick);
    }

    public void applyPath(MobPath path) {
        this.path = path;
        this.mob.setSpawn(path.getSpawnPoint());
    }

    public void tick() {
        if (this.moveCD < this.mob.getTickDelay()) {
            this.moveCD++;
        }
    }

    public boolean inLineOfFire(Direction lineOfFire) {
        return lineOfFire.intersects(this.mob.getBounds(), this.mob.getBounds().width);
    }

    public boolean willSpawn(int tick) {
        return this.spawnTick == tick;
    }

    public Point getCentre() {
        return this.mob.getCentre();
    }

    public AbstractMob getMob() {
        return this.mob;
    }

    @Override
    public int compareTo(MobPackage mp) {
        return this.spawnTick - mp.spawnTick;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MobPackage) {
            MobPackage mp = (MobPackage) o;
            return this.mob.equals(mp.mob) && this.spawnTick == mp.spawnTick;
        }
        return false;
    }

    @Override
    public String toString() {
        Point currentLoc = this.mob.getLocation();
        String coordinates = "(" + currentLoc.x + ", " + currentLoc.y + ")";
        return this.mob + " set to spawn at tick " + this.spawnTick + " at " + coordinates;
    }
}
