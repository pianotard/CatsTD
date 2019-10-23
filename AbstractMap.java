import java.awt.Rectangle;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javax.swing.JLayeredPane;

public abstract class AbstractMap extends JLayeredPane 
    implements Controllable, Canvas {

    public static final Rectangle MAP_BOUNDS = new Rectangle(0, 0, 
            CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - 15, CatsTD.WINDOW_HEIGHT - 30);
    private static final int DEFAULT_SPEED = 3;
    private static final int FF_SPEED = 1;

    protected MobPath mobPath;
    protected List<LevelPackage> levelPacks = new ArrayList<>();
    protected int currentLevel = 1;

    protected TDPlayer player = new TDPlayer();
    protected TowerPalette towerPalette = new TowerPalette();
    protected List<AbstractTower> placedTowers = new ArrayList<>();

    protected Optional<AbstractTower> dragTower = Optional.empty();
    protected MouseListener resetMapListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {
            Optional<AbstractTower> drag = AbstractMap.this.dragTower;
            if (drag.isPresent()) {
                AbstractMap.this.removeDragTower(drag.get());
            }
            Optional<PlacedTowerMenu> placedTowerMenu = AbstractMap.this.placedTowerMenu;
            if (placedTowerMenu.isPresent()) {
                AbstractMap.this.removePlacedTowerMenu(placedTowerMenu.get());
            }
        }
    };

    protected Optional<PlacedTowerMenu> placedTowerMenu = Optional.empty();

    protected TickerButton tickerButton = new TickerButton(this);
    protected boolean isSpedUp = false;
    protected int tick = 0;
    protected List<MobPackage> spawnedMobs = new ArrayList<>();
    protected List<AbstractProjectile> projectiles = new ArrayList<>();

    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected UIAsyncRunner runner = new UIAsyncRunner(this, DEFAULT_SPEED);

    public AbstractMap() {
        super();
        this.setBounds(new Rectangle(0, 0, CatsTD.WINDOW_WIDTH, CatsTD.WINDOW_HEIGHT));
        this.add(this.player, JLayeredPane.MODAL_LAYER);
        this.add(this.towerPalette, JLayeredPane.MODAL_LAYER);
        this.add(this.tickerButton, JLayeredPane.MODAL_LAYER);
        this.addMouseListener(this.resetMapListener);
    }

    public AbstractProjectile removeProjectile(AbstractProjectile proj) {
        super.remove(proj);
        return proj;
    }

    public AbstractProjectile addProjectile(AbstractProjectile proj) {
        this.projectiles.add(proj);
        super.add(proj, JLayeredPane.PALETTE_LAYER);
        return proj;
    }

    @Override
    public PlacedTowerMenu addPlacedTowerMenu(PlacedTowerMenu menu) {
        this.placedTowerMenu = Optional.of(menu);
        super.add(menu, JLayeredPane.DRAG_LAYER);
        return menu;
    }

    @Override
    public PlacedTowerMenu removePlacedTowerMenu(PlacedTowerMenu menu) {
        this.placedTowerMenu = Optional.empty();
        super.remove(menu);
        this.repaint();
        return menu;
    }

    @Override
    public AbstractTower addDragTower(AbstractTower t) {
        System.out.println("Drag tower done by: " + Thread.currentThread().getName());
        this.dragTower = Optional.of(t);
        this.add(t, JLayeredPane.MODAL_LAYER);
        this.add(t.getAtkRadius(), JLayeredPane.MODAL_LAYER);
        this.repaint();
        return t;
    }

    @Override
    public AbstractTower removeDragTower(AbstractTower t) {
        this.remove(t);
        this.repaint();
        this.dragTower = Optional.empty();
        return t;
    }

    @Override
    public AbstractTower add(AbstractTower t) {
        if (!(this.hasIntersect(t.getBounds()) || t.outsideMap())) {
            t.initProjectile();
            this.placedTowers.add(t);
            super.add(t, JLayeredPane.PALETTE_LAYER);
            super.add(t.getAtkRadius(), JLayeredPane.PALETTE_LAYER);
            System.out.println("AbstractMap placed " + t);
            this.player.payMoney(t.getCost());
            return t;
        } else {
            System.out.println("AbstractMap unable to place " + t);
            System.out.println("Enter adjust mode");
            t.adjustMode();
            this.addDragTower(t);
            return null;
        }
    }

    @Override
    public AbstractTower remove(AbstractTower t) {
        if (this.placedTowers.contains(t)) {
            this.placedTowers.remove(t);
        }
        super.remove(t);
        super.remove(t.getAtkRadius());
        if (this.placedTowerMenu.isPresent()) {
            this.removePlacedTowerMenu(this.placedTowerMenu.get());
        }
        return t;
    }

    @Override
    public boolean upgrade(AbstractTower t) {
        if (!t.upgradeable()) {
            return false;
        }
        this.remove(t);
        AbstractTower upgrade = t.getUpgrade();
        this.add(upgrade);
        upgrade.displayMenu();
        this.repaint();
        return true;
    }

    @Override
    public boolean sell(AbstractTower t) {
        if (!this.placedTowers.contains(t)) {
            return false;
        }
        this.remove(t);
        this.player.getMoney(t.getSellPrice());
        return true;
    }

    @Override
    public AbstractTower getDragTower() {
        return this.dragTower.get();
    }

    @Override
    public boolean hasDragTower() {
        return this.dragTower.isPresent();
    }

    public void resetAndIncreaseLevel() {
        this.currentLevel++;
        this.tick = 0;
        this.tickerButton.setStart();
        this.projectiles.stream().forEach(p -> this.removeProjectile(p));
        this.projectiles.clear();
        System.out.println("AbstractMap de-spawned all mobs, Executor stopped");
    }

    public void cleanUpAndTick() {
        this.spawnedMobs.removeIf(mp -> mp.exited() || mp.dead());
        this.projectiles.removeIf(p -> p.exited() || p.exhausted());
        this.tick++;
    }

    public void givePlayerMoney(MobPackage mobPack) {
        this.player.getMoney(mobPack.getReward());
    }

    public void damagePlayer(MobPackage mobPack) {
        this.player.inflictDamage(mobPack.getAttack());    
    }

    public void despawnMob(MobPackage mobPack) {
        this.remove(mobPack.mob);
    }

    public void spawnMob(MobPackage mobPack) {
        this.add(mobPack.mob, JLayeredPane.POPUP_LAYER);
        this.spawnedMobs.add(mobPack);
    }

    @Override
    public Component[] getComponents() {
        return super.getComponents();
    }

    public int getTick() {
        return this.tick;
    }

    public List<AbstractProjectile> getProjectiles() {
        return this.projectiles;
    }

    public List<MobPackage> getSpawnedMobs() {
        return this.spawnedMobs;
    }

    public List<AbstractTower> getPlacedTowers() {
        return this.placedTowers;
    }

    public LevelPackage getLevelPack() {
        return this.levelPacks.get(this.currentLevel - 1);
    }

    public boolean finishedLevel() {
        return this.spawnedMobs.isEmpty() && this.getLevelPack().finishedSpawning(); 
    }

    @Override
    public boolean playerCanAfford(AbstractTower t) {
        return this.player.enoughMoney(t.getCost());
    }

    @Override
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

    @Override
    public void repaintCanvas() {
        this.repaint();
    }

    @Override
    public boolean isSpedUp() {
        return this.isSpedUp;
    }

    @Override
    public void speedDown() {
        if (!this.isSpedUp) {
            return;
        }
        this.runner.setDelay(DEFAULT_SPEED);
        this.isSpedUp = false;
    }

    @Override
    public void speedUp() {
        if (this.isSpedUp) {
            return;
        }
        this.runner.setDelay(FF_SPEED);
        this.isSpedUp = true;
    }

    @Override
    public void startLevel() {
        if (this.currentLevel <= this.levelPacks.size()) {
            this.executor.submit(this.runner);
        } else {
            System.out.println("AbstractMap no more levels!");       
        }
    }
}
