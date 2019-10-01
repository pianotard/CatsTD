import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class LevelPackage {
    
    int level;
    List<MobPackage> mobPackages = new ArrayList<>();
    int spawnNext;

    public LevelPackage(int level) {
        this.level = level;
        this.spawnNext = 0;
    }

    public void applyPath(MobPath path) {
        for (MobPackage mobPack : this.mobPackages) {
            mobPack.applyPath(path);
        }
    }

    public boolean finishedSpawning() {
        return this.spawnNext == this.mobPackages.size();
    }

    public MobPackage pop() {
        return this.mobPackages.get(this.spawnNext++);
    }

    public MobPackage peek() {
        return this.mobPackages.get(this.spawnNext);
    }

    public LevelPackage addMob(AbstractMob mob, int initSpawnTick, int spawnDelay, int mobCount) {
        List<MobPackage> ret = new ArrayList<>(this.mobPackages);
        int spawnTick = initSpawnTick - spawnDelay;
        for (int i = 0; i < mobCount; i++) {
            ret.add(new MobPackage(mob.clone(), spawnTick + spawnDelay));
            spawnTick += spawnDelay;
        }
        return new LevelPackage(this.level, ret);
    }

    public LevelPackage addMob(AbstractMob mob, int spawnTick) {
        List<MobPackage> ret = new ArrayList<>(this.mobPackages);
        ret.add(new MobPackage(mob.clone(), spawnTick));
        return new LevelPackage(this.level, ret);
    }

    private LevelPackage(int level, List<MobPackage> packages) {
        this.level = level;
        Collections.sort(packages);
        this.mobPackages = packages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Level " + this.level + "\n");
        List<AbstractMob> mobs = this.mobPackages.stream()
            .map(mp -> mp.mob)
            .collect(Collectors.toList());
        Map<AbstractMob, Integer> mobCounts = new HashMap<>();
        for (AbstractMob mob : mobs) {
            if (mobCounts.containsKey(mob)) {
                mobCounts.replace(mob, mobCounts.get(mob) + 1);
            } else {
                mobCounts.put(mob, 1);
            }
        }
        for (AbstractMob mob : mobCounts.keySet()) {
            builder.append(mob + " : " + mobCounts.get(mob) + "\n");
        }
        return builder.substring(0, builder.length() - 1);
    }
}
