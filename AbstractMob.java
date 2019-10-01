import java.awt.Point;
import java.awt.Rectangle;
import java.awt.LayoutManager;
import javax.swing.JPanel;

public abstract class AbstractMob extends JPanel {

    protected String name;
    protected int msSpeedTick;
    protected int hp;
    protected int atk;

    public AbstractMob(LayoutManager lm) {
        super(lm);
    }

    public abstract AbstractMob clone();

    public boolean dead() {
        return this.hp <= 0;
    }

    public int getAttack() {
        return this.atk;
    }

    public void inflictDamage(int dmg) {
        this.hp -= dmg;
    }

    public void setSpawn(Point spawn) {
        Rectangle bounds = this.getBounds();
        this.setBounds(spawn.x, spawn.y, bounds.width, bounds.height);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractMob) {
            AbstractMob a = (AbstractMob) o;
            return a.toString().equals(this.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name + ", HP: " + this.hp;
    }
}
