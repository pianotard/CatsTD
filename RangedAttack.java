import java.awt.Point;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

public class RangedAttack extends AbstractAttack {

    public RangedAttack() {}

    @Override
    public List<MobPackage> findTargets(List<MobPackage> spawnedMobs) {
        List<MobPackage> found = new ArrayList<>();
        Optional<MobPackage> mainTarget = Optional.empty();
        for (MobPackage mobPack : spawnedMobs) {
            Point mob = mobPack.getCentre();
            if (this.centre.distance(mob) <= this.atkRadius.getRadius()) {
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
}


