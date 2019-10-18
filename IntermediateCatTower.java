import java.util.List;
import java.util.ArrayList;
import java.awt.Point;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IntermediateCatTower extends AbstractTower {

    private static final int TOWER_WIDTH = 40;
    private static final int TOWER_HEIGHT = 40;
    private static final String IMAGE_PATH = "int_cat_tower/int_cat_tower";
    private static final List<Icon> IMAGES = new ArrayList<>();

    public IntermediateCatTower(int x, int y) {
        super();
        this.name = "Intermediate Cat Toewr";
        this.description = "Tom has matured and sharpened his senses. He can now defend Jerry from the monsters above his bed.";
        this.cost = 150;
        this.sellPrice = 250;
        this.centre = new Point(x + TOWER_WIDTH / 2, y + TOWER_HEIGHT / 2);
        this.setBounds(x, y, TOWER_WIDTH, TOWER_HEIGHT);
        this.msAtkDelay = 40;
        this.atkCoolDown = this.msAtkDelay;
        this.atkDamage = 2;
        this.initRadius(150);
        this.defaultBufferedImage = UIUtil.readBufferedImage("int_cat_tower/int_cat_tower_1.png");

        this.projectile = new BasicCatProjectile();
        this.setRotation(0);

        this.initMenu();
    }

    @Override
    public Icon getRotatedIcon(int degree) {
        return IntermediateCatTower.IMAGES.get(degree);
    }

    @Override
    public AbstractTower getClone(int x, int y) {
        return new IntermediateCatTower(x, y);
    }

    public static void initImages() {
        for (int i = 0; i < 360; i++) {
            IntermediateCatTower.IMAGES.add(
                    new ImageIcon(UIUtil.readBufferedImage(IMAGE_PATH + "_" + i + ".png")));
        }
    }
}
