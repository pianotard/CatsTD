import java.awt.Point;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class RangedAttack extends AbstractAttack {

    public RangedAttack() {}

    @Override
    public Optional<AbstractProjectile> attemptAttack(List<MobPackage> spawnedMobs) {
        if (this.atkCoolDown < this.msAtkDelay) {
            return Optional.empty();
        }
        // Targets first Mob
        for (MobPackage mobPack : spawnedMobs) {
            Point aim = this.projectile.computeTarget(mobPack);
            if (this.centre.distance(aim) <= this.atkRadius.getRadius()) {
                this.atkCoolDown = 0;
                return Optional.of(this.projectile.initTarget(mobPack, this.atkRadius.getRadius()));
            }
        }
        return Optional.empty();
    }
/*
    @Override
    public List<MobPackage> findTargets(List<MobPackage> spawnedMobs) {
        List<MobPackage> found = new ArrayList<>();
        Optional<MobPackage> mainTarget = Optional.empty();
        // Targets first Mob
        for (MobPackage mobPack : spawnedMobs) {
            Point aim = this.projectile.computeTarget(this.centre, this.projectile.getSpeed(),
                    mobPack.getMob().getLocation(), mobPack.getVelocity());
            if (this.centre.distance(aim) <= this.atkRadius.getRadius()) {
                mainTarget = Optional.of(mobPack);
                break;
            }
        }
        if (!mainTarget.isPresent()) {
            return found;
        }
        found.add(mainTarget.get());
        Direction projectileDir = new Direction(
                this.centre, 
                mainTarget.get().getCentre(), 
                this.atkRadius.getRadius());
        for (MobPackage mobPack : spawnedMobs) {
            if (mobPack.equals(mainTarget.get())) {
                continue;
            }
            if (mobPack.inLineOfFire(projectileDir)) {
                found.add(mobPack);
            }
        }
        return found;
    }
*/
}


