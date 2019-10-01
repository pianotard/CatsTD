import java.awt.Point;

public class MobPackage implements Comparable<MobPackage> {
    
    AbstractMob mob;
    MobPath path;
    int spawnTick;
    Point movementDirection;

    public MobPackage(AbstractMob mob, int spawnTick) {
        this.mob = mob;
        this.spawnTick = spawnTick;
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
        Point currentLoc = this.mob.getLocation();
        this.movementDirection = this.path.atCorner(currentLoc) ? this.path.getNextTick(currentLoc)
            : this.movementDirection;
        this.mob.setLocation(currentLoc.x + movementDirection.x, currentLoc.y + movementDirection.y);
    }

    public void applyPath(MobPath path) {
        this.path = path;
        this.mob.setSpawn(path.getSpawnPoint());
    }

    public boolean willSpawn(int tick) {
        return this.spawnTick == tick;
    }

    public AbstractMob getMob() {
        return this.mob;
    }

    @Override
    public int compareTo(MobPackage mp) {
        return this.spawnTick - mp.spawnTick;
    }

    @Override
    public String toString() {
        return this.mob + " set to spawn at " + this.spawnTick;
    }
}
