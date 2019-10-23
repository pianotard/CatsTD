import java.awt.Dimension;
import java.awt.Component;
import javax.swing.JFrame;

public class CatsTD extends JFrame {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    public CatsTD() {
        super("Cats TD");
        this.setLocation(75, 75);
        this.setSize(new Dimension(CatsTD.WINDOW_WIDTH, CatsTD.WINDOW_HEIGHT));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        BasicCatTower.initImages();
        BasicCatProjectile.initImages();
        IntermediateCatTower.initImages();
    }

    public void startLevel() {
        AbstractMap map = (AbstractMap) this.getContentPane();
        map.startLevel();
    }

    public AbstractMap add(AbstractMap m) {
        this.getContentPane().removeAll();
        super.setContentPane(m);
        return m;
    }

    public AbstractTower add(AbstractTower t) {
        AbstractMap map = (AbstractMap) this.getContentPane();
        AbstractTower afterAdd = map.add(t);
        if (afterAdd == null) {
            return null;
        }
        this.repaint();
        return t;
    }
}
