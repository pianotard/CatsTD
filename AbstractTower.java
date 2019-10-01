import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Container;
import java.awt.LayoutManager;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;


public abstract class AbstractTower extends JPanel {

    protected String name;
    
    protected Point centre;
    protected JPanel atkRadius;
    protected int atkRange;
    protected int msAtkDelay;
    protected int atkCoolDown;
    protected int atkDamage;

    public AbstractTower(LayoutManager lm) {
        super(lm);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                AbstractTower.this.showAtkRadius();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                AbstractTower.this.hideAtkRadius();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Mouse clicked");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Mouse released");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse pressed");
            }
        });
    }

    public abstract AbstractTower getClone(int x, int y);

    public void showAtkRadius() {
        Container parent = this.getParent();
        parent.remove(this);
        parent.add(this.atkRadius);
        parent.add(this);
        parent.repaint();
    }

    public void hideAtkRadius() {
        Container parent = this.getParent();
        parent.remove(this.atkRadius);
        parent.repaint();
    }

    public void tick() {
        if (this.atkCoolDown < this.msAtkDelay) {
            this.atkCoolDown++;
        }
    }

    public MobPackage findMob(List<MobPackage> spawnedMobs) {
        for (MobPackage mobPack : spawnedMobs) {
            if (this.atkRadius.getBounds().intersects(mobPack.mob.getBounds())) {
                return mobPack;
            }
        }
        return null;
    }

    public MobPackage attemptAttack(MobPackage mobPack) {
        if (this.atkCoolDown < this.msAtkDelay) {
            return mobPack;
        }
        if (this.atkRadius.getBounds().intersects(mobPack.mob.getBounds())) {
            mobPack.inflictDamage(this.atkDamage);
            this.atkCoolDown = 0;
        }
        return mobPack;
    }

    public boolean hasIntersect(Rectangle r) {
        return this.getBounds().intersects(r);
    }

    protected void initRadius() {
        int rx = this.centre.x - this.atkRange / 2;
        int ry = this.centre.y - this.atkRange / 2;
        Rectangle radiusRect = new Rectangle(rx, ry, this.atkRange, this.atkRange);
        
        this.atkRadius = new JPanel();
        this.atkRadius.setLayout(null);
        this.atkRadius.setBounds(radiusRect);
        this.atkRadius.setBackground(ColorUtil.transparent(java.awt.Color.YELLOW, 0.3));
    }

    @Override
    public String toString() {
        return this.name + " @ (" + this.getLocation().x + ", " + this.getLocation().y + ")";
    }
}
