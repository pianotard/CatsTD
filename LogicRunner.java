import java.util.Optional;
import java.util.List;

public class LogicRunner implements Runnable {

    private AbstractMap map;

    public LogicRunner(AbstractMap map) {
        this.map = map;
    }

    @Override
    public void run() {
        for (AbstractTower tower : this.map.getPlacedTowers()) {
            tower.tick();
            Optional<AbstractProjectile> projectile = tower.attemptAttack(this.map.getSpawnedMobs());
            if (!projectile.isPresent()) {
                continue;
            }
            System.out.println("Init projectile");
            tower.setRotation(projectile.get().getDegrees());
            this.map.addProjectile(projectile.get());
        }
        // Moving Projectiles
        for (AbstractProjectile proj : this.map.getProjectiles()) {
            proj.tick();
            proj.move();
            if (proj.exited()) {
                this.map.removeProjectile(proj);
                continue;
            }
            Optional<MobPackage> collided = proj.findCollision(this.map.getSpawnedMobs());
            if (collided.isPresent()) {
                collided.get().inflictDamage(proj.getDamage());
                proj.pierce();
            }
            if (proj.exhausted()) {
                this.map.removeProjectile(proj);
            }
        }
        for (MobPackage mobPack : this.map.getSpawnedMobs()) {
            if (mobPack.dead()) {
                this.map.despawnMob(mobPack);
                this.map.givePlayerMoney(mobPack);
            }
        }
    }
}
