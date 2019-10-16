import java.util.List;

public class UIAsyncRunner implements Runnable {

    private AbstractMap map;
    private int delay = 4;

    public UIAsyncRunner(AbstractMap map, int delay) {
        this.map = map;
        this.delay = delay;
    }

    @Override
    public void run() {
        System.out.println("run() called by: " + Thread.currentThread().getName());
        while (!map.finishedLevel()) {
            LevelPackage levelPack = this.map.getLevelPack();

            if (!levelPack.finishedSpawning()) {
                MobPackage mobPack = levelPack.peek();
                if (mobPack.willSpawn(this.map.getTick())) {
                    levelPack.pop();
                    this.map.spawnMob(mobPack);
                }
            }

            for (MobPackage mobPack : this.map.getSpawnedMobs()) {
                mobPack.tick();
                mobPack.move();
                if (mobPack.exited()) {
                    this.map.despawnMob(mobPack);
                    this.map.damagePlayer(mobPack);
                }
            }

            for (AbstractTower tower : this.map.getPlacedTowers()) {
                tower.tick();
                MobPackage inRadius = tower.findMob(this.map.getSpawnedMobs());
                if (inRadius == null) {
                    continue;
                }
                boolean attacked = tower.attemptAttack(inRadius);
                if (attacked) {
                    System.out.println("Init projectile");
                    AbstractProjectile proj = tower.getProjectile().initTarget(inRadius);
                    tower.setRotation(proj.getDegrees());
                }
                if (inRadius.dead()) {
                    this.map.despawnMob(inRadius);
                    this.map.givePlayerMoney(inRadius);
                }
            }
            this.map.cleanUpAndTick();
            this.pause(this.delay);
            this.map.repaintCanvas();
        }
        this.map.resetAndIncreaseLevel();
    }

    public void setDelay(int ms) {
        this.delay = ms;
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
