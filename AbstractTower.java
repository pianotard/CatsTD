import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class AbstractTower extends JLabel implements Transactable {

    protected String name;
    protected String description;
    protected int cost;
    protected int sellPrice;
    protected Optional<Supplier<AbstractTower>> upgradePath = Optional.empty();

    protected Point centre;
    protected AbstractAttack attack;

    protected BufferedImage defaultBufferedImage;
    protected double angle = 0;

    protected PlacedTowerMenu menu;

    private Point clicked;
    protected boolean adjustMode = false;
    protected MouseListener placedTowerListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {
            AbstractTower.this.showAtkRadius();
        }
        public void mouseExited(MouseEvent e) {
            AbstractTower.this.hideAtkRadius();
        }
        public void mouseClicked(MouseEvent e) {
            Canvas parent = (Canvas) AbstractTower.this.getParent();
            if (Arrays.asList(parent.getComponents()).contains(AbstractTower.this.menu)) {
                return;
            }
            parent.addPlacedTowerMenu(AbstractTower.this.menu);
        }
        public void mouseReleased(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
    };
    protected MouseListener unplaceableListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {
            AbstractTower self = AbstractTower.this;
            self.exitAdjustMode();
            Canvas parent = (Canvas) self.getParent();
            parent.removeDragTower(self);
            Point loc = self.getLocation();
            parent.add(self.getClone(loc.x, loc.y));
        }
        public void mousePressed(MouseEvent e) {
            AbstractTower.this.clicked = new Point(e.getX(), e.getY());        
        }
    };
    protected MouseMotionListener adjustListener = new MouseMotionListener() {
        public void mouseDragged(MouseEvent e) {
            int dx = e.getX() - AbstractTower.this.clicked.x;
            int dy = e.getY() - AbstractTower.this.clicked.y;
            Point oldLoc = AbstractTower.this.getLocation();
            AbstractTower.this.setLocation(oldLoc.x + dx, oldLoc.y + dy);
            
            Canvas parent = (Canvas) AbstractTower.this.getParent();
            if (parent.hasIntersect(AbstractTower.this.getBounds())) {
                AbstractTower.this.showRedRadius();
            } else {
                AbstractTower.this.showAtkRadius();
            } 
        }
        public void mouseMoved(MouseEvent e) {}
    };

    public AbstractTower() {
        super();
        this.setLayout(null);
        this.addMouseListener(this.placedTowerListener);
    }

    public abstract AbstractTower getClone(int x, int y);
    public abstract Icon getRotatedIcon(int degree);

    @Override
    public void upgrade() {
        if (!this.upgradePath.isPresent()) {
            return;
        }
        Canvas parent = (Canvas) this.getParent();
        parent.upgrade(this);
    }

    @Override
    public void sell() {
        Canvas parent = (Canvas) this.getParent();
        parent.sell(this);
    }

    @Override
    public void setLocation(int x, int y) {
        Point oldLoc = this.getLocation();
        super.setLocation(x, y);
        this.centre = new Point(this.centre.x + x - oldLoc.x, this.centre.y + y - oldLoc.y);
        this.attack.setLocation(this.centre);
    }

    public void setRotation(int degree) {
        degree = degree > 360 ? degree - 360 : degree;
        degree = degree < 0 ? degree + 360 : degree;
        Icon image = this.getRotatedIcon(degree);
        int width = image.getIconWidth();
        int height = image.getIconHeight();
        this.setBounds(this.centre.x - width / 2, this.centre.y - height / 2, width, height);
        this.setIcon(image); 
    }

    public void adjustMode() {
        if (this.adjustMode) {
            return;
        }
        Rectangle oldBounds = this.getBounds();

        this.removeMouseListener(this.placedTowerListener);
        this.addMouseListener(this.unplaceableListener);
        this.addMouseMotionListener(this.adjustListener);
        this.attack.showRedRadius();
        this.adjustMode = true;
    }

    public void exitAdjustMode() {
        if (!this.adjustMode) {
            return;
        }
        this.removeMouseListener(this.unplaceableListener);
        this.removeMouseMotionListener(this.adjustListener);
        this.addMouseListener(this.placedTowerListener);
        this.adjustMode = false;        
    }

    public void showRedRadius() {
        this.attack.showRedRadius();
    }

    public void showAtkRadius() {
        this.attack.showAtkRadius();
    }

    public void hideAtkRadius() {
        this.attack.hideAtkRadius();
    }

    public void tick() {
        this.attack.tick();
    }

    public List<MobPackage> findTargets(List<MobPackage> spawnedMobs) {
        return this.attack.findTargets(spawnedMobs);
    }

    public boolean attemptAttack(List<MobPackage> targets) {
        return this.attack.attemptAttack(targets);
    }

    public boolean outsideMap() {
        return !AbstractMap.MAP_BOUNDS.contains(this.centre);
    }

    @Override
    public boolean upgradeable() {
        return this.upgradePath.isPresent();
    }

    public boolean hasIntersect(Rectangle r) {
        return this.getBounds().intersects(r);
    }

    protected void initMenu() {
        this.menu = new PlacedTowerMenu(this);
    }
/*
    protected void initRadius(int atkRange) {
        this.atkRadius = new TowerAttackRadius(this.centre, atkRange);
        this.atkRadius.setTransparent();
    }
*/
    public void initProjectile() {
        this.attack.initProjectile();
    }

    public AbstractTower getUpgrade() {
        return this.upgradePath.get().get();
    }

    @Override
    public int getUpgradePrice() {
        return this.upgradePath.get().get().cost;
    }

    @Override
    public int getSellPrice() {
        return this.sellPrice;
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public BufferedImage getDefaultBufferedImage() {
        return this.defaultBufferedImage;
    }
    
    public TowerAttackRadius getAtkRadius() {
        return this.attack.getAtkRadius();
    }

    public AbstractProjectile getProjectile() {
        return this.attack.getProjectile();
    }

    @Override
    public String toString() {
        return this.name + " @ (" + this.getLocation().x + ", " + this.getLocation().y + ")";
    }
}
