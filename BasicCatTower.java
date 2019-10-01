import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class BasicCatTower extends AbstractTower {
    
    private static final int TOWER_WIDTH = 20;
    private static final int TOWER_HEIGHT = 20;

    public BasicCatTower(int x, int y) {
        super(null);
        this.name = "Basic Cat Tower";
        this.centre = new Point(x + TOWER_WIDTH / 2, y + TOWER_HEIGHT / 2);
        this.atkRange = 150;
        this.msAtkDelay = 60;
        this.atkCoolDown = this.msAtkDelay;
        this.atkDamage = 1;
        this.setBounds(x, y, TOWER_WIDTH, TOWER_HEIGHT);
        this.setBackground(java.awt.Color.GREEN);
        this.initRadius();
    }

    @Override
    public AbstractTower getClone(int x, int y) {
        return new BasicCatTower(x, y);
    }
}
