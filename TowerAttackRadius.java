import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TowerAttackRadius extends JLabel {

    private final AlphaImageIcon empty;
    private final AlphaImageIcon defaultRadius;
    private final AlphaImageIcon redRadius;

    private Point centre;
    private final int radius;

    public TowerAttackRadius(Point centre, int range) {
        super();
        super.setLayout(null);

        this.centre = centre;
        this.radius = range;
        int rx = this.centre.x - this.radius;
        int ry = this.centre.y - this.radius;
        Rectangle bounds = new Rectangle(rx, ry, this.radius * 2, this.radius * 2);

        super.setBounds(bounds);
       
        BufferedImage emptyImg = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage defaultImg = UIUtil.readBufferedImage("atk_radius_2.png");
        BufferedImage redImg = UIUtil.readBufferedImage("atk_radius_red_2.png");

        Icon empt = new ImageIcon(emptyImg);
        Icon def = new ImageIcon(UIUtil.resize(defaultImg, bounds.width, bounds.height));
        Icon red = new ImageIcon(UIUtil.resize(redImg, bounds.width, bounds.height));

        this.empty = new AlphaImageIcon(empt, 0.0f);
        this.defaultRadius = new AlphaImageIcon(def, 0.3f);
        this.redRadius = new AlphaImageIcon(red, 0.3f);

        this.setIcon(this.defaultRadius);
    }

    @Override
    public void setLocation(Point centre) {
        this.centre = centre;
        int rx = this.centre.x - this.radius;
        int ry = this.centre.y - this.radius;
        super.setLocation(rx, ry);
    }

    public void setTransparent() {
        this.setIcon(this.empty);
    }

    public void setRed() {
        super.setIcon(this.redRadius);
    }

    public void setDefaultColor() {
        super.setIcon(this.defaultRadius);
    }

    public int getRadius() {
        return this.radius;
    }
}
