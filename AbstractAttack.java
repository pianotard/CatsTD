import java.awt.Point;
import java.util.List;

public abstract class AbstractAttack {

    protected Point centre;
    protected TowerAttackRadius atkRadius;
    protected AbstractProjectile projectile;
    protected int pierce = 1;
    protected int atkDamage = 1;

    protected int msAtkDelay = 0;
    protected int atkCoolDown = 0;

    public static AbstractAttack ranged() {
        return new RangedAttack();
    }

    public boolean attemptAttack(List<MobPackage> targets) {
        if (this.atkCoolDown < this.msAtkDelay) {
            return false;
        }
        int attacked = 0;
        for (MobPackage mobPack : targets) {
            Point mob = mobPack.getCentre();
            if (this.centre.distance(mob) <= this.atkRadius.getRadius()) {
                mobPack.inflictDamage(this.atkDamage);
                attacked++;
                if (attacked > 1) System.out.println("Pierce");
                this.atkCoolDown = 0;
                if (attacked == this.pierce) {
                    break;
                }
            }
        }
        return true;
    }

    public void showRedRadius() {
        this.atkRadius.setRed();
    }

    public void showAtkRadius() {
        this.atkRadius.setDefaultColor();
    }

    public void hideAtkRadius() {
        this.atkRadius.setTransparent();
    }

    public void tick() {
        if (this.atkCoolDown < this.msAtkDelay) {
            this.atkCoolDown++;
        }
    }

    public void initProjectile() {
        this.projectile.setStartPoint(this.centre);
    }

    public void setLocation(Point centre) {
        this.centre = centre;
        this.projectile.setStartPoint(centre);
        this.atkRadius.setLocation(centre);
    }

    public abstract List<MobPackage> findTargets(List<MobPackage> spawnedMobs);

    public AbstractAttack assertElements() {
        boolean noCentre = this.centre == null;
        boolean noRadius = this.atkRadius == null;
        boolean noProjectile = this.projectile == null;
        boolean noDmg = this.atkDamage < 1;
        boolean noPierce = this.pierce < 1;
        boolean noDelay = this.msAtkDelay < 0;
        boolean noCD = this.atkCoolDown < 0;
        if (noCentre || noRadius || noProjectile || noDmg || noPierce || noDelay || noCD) {
            System.out.println("AbstractAttack found error!");   
        }
        return this;
    }

    public AbstractAttack setCentre(Point centre) {
        this.centre = centre;
        return this;
    }

    public AbstractAttack setRadius(int radius) {
        if (this.centre == null) {
            System.err.println(new NullPointerException("No centre!"));
        }
        this.atkRadius = new TowerAttackRadius(this.centre, radius);
        this.atkRadius.setTransparent();
        return this;
    }

    public AbstractAttack setProjectile(AbstractProjectile projectile) {
        this.projectile = projectile;
        return this;
    }

    public AbstractAttack setDamage(int damage) {
        this.atkDamage = damage;
        return this;
    }

    public AbstractAttack setPierce(int pierce) {
        this.pierce = pierce;
        return this;
    }

    public AbstractAttack setAtkCoolDown(int cd) {
        this.msAtkDelay = cd;
        this.atkCoolDown = cd;
        return this;
    }

    public AbstractProjectile getProjectile() {
        return this.projectile;
    }

    public TowerAttackRadius getAtkRadius() {
        return this.atkRadius;
    }
}
