import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class BasicCatProjectile extends AbstractProjectile {

    private static final int TICK_DELAY = 1;
    private static final int DIMENSION = 15;
    private static final String IMAGE_PATH = "test_projectile/test_projectile";
    private static final List<ImageIcon> IMAGES = new ArrayList<>();

    public BasicCatProjectile() {
        super();
        this.dimension = BasicCatProjectile.DIMENSION;
        this.setBounds(0, 0, DIMENSION, DIMENSION);
        this.tickDelay = BasicCatProjectile.TICK_DELAY;
        this.moveCD = this.tickDelay;
    }

    private BasicCatProjectile(Point start) {
        this();
        this.setStartPoint(start);
    }

    @Override
    public void setRotation(int degree) {
        this.setIcon(BasicCatProjectile.IMAGES.get(degree));
    }

    @Override
    public AbstractProjectile clone() {
        return new BasicCatProjectile(this.startPoint);
    }

    public static void initImages() {
        for (int i = 0; i < 360; i++) {
            BasicCatProjectile.IMAGES.add(
                    new ImageIcon(UIUtil.readBufferedImage(IMAGE_PATH + "_" + i + ".png")));
        } 
    }
}
