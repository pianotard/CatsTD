import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;

public class UIAsyncRunner implements Runnable {

    private AbstractMap map;
    private int delay;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Future<?> completed;
    private LogicRunner logicRunner;

    public UIAsyncRunner(AbstractMap map, int delay) {
        this.map = map;
        this.delay = delay;
        this.logicRunner = new LogicRunner(map);
    }

    @Override
    public void run() {
        System.out.println("run() called by: " + Thread.currentThread().getName());
        while (!map.finishedLevel()) {

            LevelPackage levelPack = this.map.getLevelPack();

            // Spawning Mobs
            if (!levelPack.finishedSpawning()) {
                MobPackage mobPack = levelPack.peek();
                if (mobPack.willSpawn(this.map.getTick())) {
                    levelPack.pop();
                    this.map.spawnMob(mobPack);
                }
            }

            // Moving Mobs
            for (MobPackage mobPack : this.map.getSpawnedMobs()) {
                mobPack.tick();
                mobPack.move();
                if (mobPack.exited()) {
                    this.map.despawnMob(mobPack);
                    this.map.damagePlayer(mobPack);
                }
            }

            this.map.cleanUpAndTick();
            this.completed = this.executor.submit(logicRunner);
            this.pause(this.delay);
            this.map.repaintCanvas();
            try {
                this.completed.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println(e);
            }
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
