import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class BasicCatTower extends AbstractTower {
    
    private static final int TOWER_WIDTH = 40;
    private static final int TOWER_HEIGHT = 40;
    private static final String IMAGE_PATH = "basic_cat_tower/basic_cat_tower";
    private static final List<Icon> IMAGES = new ArrayList<>();

    public BasicCatTower(int x, int y) {
        super();
        this.name = "Basic Cat Tower";
        this.cost = 200;
        this.centre = new Point(x + TOWER_WIDTH / 2, y + TOWER_HEIGHT / 2);
        this.setBounds(x, y, TOWER_WIDTH, TOWER_HEIGHT);
        this.msAtkDelay = 60;
        this.atkCoolDown = this.msAtkDelay;
        this.atkDamage = 1;
        this.initRadius(75);
 
        this.defaultBufferedImage = UIUtil.readBufferedImage("basic_cat_tower/basic_cat_tower_1.png");
        this.projectile = new BasicCatProjectile();
        this.setRotation(0);
    }

    @Override
    public Icon getRotatedIcon(int degree) {
        return BasicCatTower.IMAGES.get(degree);
    }      

    @Override
    public AbstractTower getClone(int x, int y) {
        return new BasicCatTower(x, y);
    }

    public static void initImages() {
        for (int i = 0; i < 360; i++) {
            BasicCatTower.IMAGES.add(
                    new ImageIcon(UIUtil.readBufferedImage(IMAGE_PATH + "_" + i + ".png")));
        } 
    }
}
