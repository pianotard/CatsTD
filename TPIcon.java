import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.ImageIcon;

public class TPIcon extends JLayeredPane {

    private final AbstractTower towerTemplate;
    private final JLabel towerIcon = new JLabel();
    private final JLabel priceTag = new JLabel();
    private BufferedImage image;

    public TPIcon(AbstractTower towerTemplate) {
        super();
        this.setLayout(null);
        this.setBackground(null);
        this.towerTemplate = towerTemplate;
        this.image = towerTemplate.getDefaultBufferedImage();
       
        this.towerIcon.setIcon(new ImageIcon(this.image));

        this.priceTag.setText("$" + towerTemplate.getCost());
        this.priceTag.setFont(new Font("Arial", Font.PLAIN, 15));
        this.priceTag.setForeground(java.awt.Color.RED);

        this.add(this.towerIcon, JLayeredPane.DEFAULT_LAYER);
        this.add(this.priceTag, JLayeredPane.MODAL_LAYER);
 
        this.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {
                Canvas parent = (Canvas) TPIcon.this.getParent().getParent();
                if (!parent.hasDragTower()) {
                    return;
                }
                AbstractTower dragTower = parent.getDragTower();
                dragTower.hideAtkRadius();
                parent.removeDragTower(dragTower);

                int x = e.getX() + CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - 15;
                int y = e.getY();

                AbstractTower placeTower = TPIcon.this.towerTemplate.getClone(x, y);
                if (placeTower.outsideMap()) {
                    parent.repaintCanvas();
                    return;
                }
                parent.add(placeTower);
            }
            public void mousePressed(MouseEvent e) {
                int x = e.getX() + CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - 15;
                int y = e.getY();
                AbstractTower t = TPIcon.this.towerTemplate.getClone(x, y);
                Canvas parent = (Canvas) TPIcon.this.getParent().getParent();
                if (!parent.playerCanAfford(t)) {
                    return;
                }
                parent.addDragTower(t);
            }          
        });
           
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                Canvas parent = (Canvas) TPIcon.this.getParent().getParent();
                if (!parent.hasDragTower()) {
                    return;
                }
                AbstractTower tower = parent.getDragTower();
                int x = e.getX() + CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - 15;
                int y = e.getY();
                tower.setLocation(x, y);
                if (parent.hasIntersect(tower.getBounds()) || tower.outsideMap()) {
                    tower.showRedRadius();
                } else {
                    tower.showAtkRadius();
                }
            }
            public void mouseMoved(MouseEvent e) {}
        });
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.towerIcon.setIcon(new ImageIcon(UIUtil.resize(this.image, width, height)));
        this.towerIcon.setBounds(0, 0, width, height);
        this.priceTag.setBounds(width / 4, height * 3 / 4, width / 2, height / 4);
    }
}
