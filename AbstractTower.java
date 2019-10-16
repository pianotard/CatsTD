import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public abstract class AbstractTower extends JLabel {

    protected String name;
    protected int cost;

    protected Point centre;
    protected TowerAttackRadius atkRadius;
    protected AbstractProjectile projectile;
    protected int msAtkDelay;
    protected int atkCoolDown;
    protected int atkDamage;

    protected BufferedImage defaultBufferedImage;
    protected double angle = 0;

    private Point clicked;
    protected boolean adjustMode = false;
    protected MouseListener placedTowerListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {
            AbstractTower.this.showAtkRadius();
        }
        public void mouseExited(MouseEvent e) {
            AbstractTower.this.hideAtkRadius();
        }
        public void mouseClicked(MouseEvent e) {}
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
            
            AbstractMap parent = (AbstractMap) AbstractTower.this.getParent();
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
    public void setLocation(int x, int y) {
        Point oldLoc = this.getLocation();
        super.setLocation(x, y);
        this.centre = new Point(this.centre.x + x - oldLoc.x, this.centre.y + y - oldLoc.y);
        this.atkRadius.setLocation(this.centre);
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
        this.atkRadius.setRed();
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

    public MobPackage findMob(List<MobPackage> spawnedMobs) {
        for (MobPackage mobPack : spawnedMobs) {
            Point mob = mobPack.getCentre();
            if (this.centre.distance(mob) <= this.atkRadius.getRadius()) {
                return mobPack;
            }
        }
        return null;
    }

    public boolean attemptAttack(MobPackage mobPack) {
        if (this.atkCoolDown < this.msAtkDelay) {
            return false;
        }
        Point mob = mobPack.getCentre();
        if (this.centre.distance(mob) <= this.atkRadius.getRadius()) {
            mobPack.inflictDamage(this.atkDamage);
            this.atkCoolDown = 0;
            return true;
        }
        return false;
    }

    public boolean outsideMap() {
        return !AbstractMap.MAP_BOUNDS.contains(this.centre);
    }

    public boolean hasIntersect(Rectangle r) {
        return this.getBounds().intersects(r);
    }

    protected void initRadius(int atkRange) {
        this.atkRadius = new TowerAttackRadius(this.centre, atkRange);
        this.atkRadius.setTransparent();
    }

    public void initProjectile() {
        this.projectile.setStartPoint(this.centre);
    }

    public int getCost() {
        return this.cost;
    }

    public BufferedImage getDefaultBufferedImage() {
        return this.defaultBufferedImage;
    }
    
    public TowerAttackRadius getAtkRadius() {
        return this.atkRadius;
    }

    public AbstractProjectile getProjectile() {
        return this.projectile;
    }

    @Override
    public String toString() {
        return this.name + " @ (" + this.getLocation().x + ", " + this.getLocation().y + ")";
    }
}
