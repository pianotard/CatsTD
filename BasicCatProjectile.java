import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class BasicCatProjectile extends AbstractProjectile {

    public BasicCatProjectile() {
        super();
        this.img = UIUtil.readBufferedImage("test_projectile.png");
        this.setIcon(new ImageIcon(this.img));
        this.setBounds(new Rectangle(0, 0, this.img.getWidth() * 2, this.img.getHeight() * 2));
    }

    private BasicCatProjectile(Point start) {
        super();
        this.img = UIUtil.readBufferedImage("test_projectile.png");
        this.setStartPoint(start);
        this.setIcon(new ImageIcon(this.img));
        this.setBounds(new Rectangle(0, 0, this.img.getWidth() * 2, this.img.getHeight() * 2));
    }

    @Override
    public AbstractProjectile clone() {
        return new BasicCatProjectile(this.startPoint);
    }
}
