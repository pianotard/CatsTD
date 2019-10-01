import java.awt.Rectangle;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import javax.swing.Timer;

public abstract class AbstractMap extends JLayeredPane implements ActionListener {

    protected MobPath mobPath;
    protected List<LevelPackage> levelPacks = new ArrayList<>();
    protected int currentLevel = 1;

    protected TDPlayer player = new TDPlayer();
    protected TowerPalette towerPalette = new TowerPalette();
    protected List<AbstractTower> placedTowers = new ArrayList<>();

    protected int tick = 0;
    protected Timer mobMoverTimer = new Timer(4, this);
    protected List<MobPackage> spawnedMobs = new ArrayList<>();

    public AbstractMap(LayoutManager lm) {
        super();
        this.setBounds(new Rectangle(0, 0, CatsTD.WINDOW_WIDTH, CatsTD.WINDOW_HEIGHT));
        this.add(this.player);
        this.add(this.towerPalette, JLayeredPane.POPUP_LAYER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LevelPackage levelPack = this.levelPacks.get(this.currentLevel - 1);
        if (!levelPack.finishedSpawning()) {
            MobPackage mobPack = levelPack.peek();
            if (mobPack.willSpawn(this.tick)) {
                levelPack.pop();
                this.add(mobPack.mob);
                this.spawnedMobs.add(mobPack);
                this.repaint();
            }
        }
        for (MobPackage mobPack : this.spawnedMobs) {
            mobPack.move();
            if (mobPack.exited()) {
                this.remove(mobPack.mob);
                this.repaint();
                this.player.inflictDamage(mobPack.getAttack());
            }
        }
        for (AbstractTower tower : this.placedTowers) {
            tower.tick();
            MobPackage inRadius = tower.findMob(this.spawnedMobs);
            if (inRadius == null) {
                continue;
            }
            tower.attemptAttack(inRadius);
            if (inRadius.dead()) {
                this.remove(inRadius.mob);
                this.repaint();
            }
        }
        this.spawnedMobs.removeIf(mp -> mp.exited());
        this.spawnedMobs.removeIf(mp -> mp.dead());
        this.tick++;
        if (this.spawnedMobs.isEmpty()) {
            this.mobMoverTimer.stop();
            this.currentLevel++;
            this.tick = 0;
            System.out.println("AbstractMap de-spawned all mobs, Timer stopped");
        }
    }

    public AbstractTower add(AbstractTower t) {
        if (!this.hasIntersect(t.getBounds())) {
            this.placedTowers.add(t);
            super.add(t);
            System.out.println("AbstractMap placed " + t);
            return t;
        } else {
            System.out.println("AbstractMap unable to place " + t);
            return null;
        }
    }

    public boolean hasIntersect(Rectangle bounds) {
        boolean pathClash = this.mobPath.hasIntersect(bounds);
        boolean towerClash = false;
        for (AbstractTower t : this.placedTowers) {
            towerClash = towerClash || t.hasIntersect(bounds);
        }
        return pathClash || towerClash;
    }

    protected void applyPathToLevelPacks() {
        for (LevelPackage lp : this.levelPacks) {
            lp.applyPath(this.mobPath);
        }
    }

    public void startLevel() {
        if (this.currentLevel <= this.levelPacks.size()) {
            this.mobMoverTimer.start();
        } else {
            System.out.println("AbstractMap no more levels!");
        }
    }
}
