import java.util.Optional;
import java.awt.Container;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class TPIcon extends JPanel {

    private final AbstractTower towerTemplate;

    private Optional<AbstractTower> dragTower = Optional.empty();

    public TPIcon(AbstractTower towerTemplate) {
        super();
        this.setLayout(null);
        this.towerTemplate = towerTemplate;
        this.setBackground(towerTemplate.getBackground());
        /*
           this.addMouseListener(new MouseListener() {
           @Override
           public void mouseEntered(MouseEvent e) {
           System.out.println("Mouse entered");
           }

           @Override
           public void mouseExited(MouseEvent e) {
           System.out.println("Mouse exited");
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
           */
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX() + CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - 15;
                int y = e.getY();
                if (TPIcon.this.dragTower.isPresent()) {
                    TPIcon.this.dragTower.get().setLocation(x, y);
                } else {
                    TPIcon.this.dragTower = Optional.of(TPIcon.this.towerTemplate.getClone(x, y));
                    Container parent = TPIcon.this.getParent().getParent();
                    parent.add(TPIcon.this.dragTower.get());
                    this.mouseDragged(e);
                }
            }

        @Override
        public void mouseMoved(MouseEvent e) {
            System.out.println("Mouse moved");
        }
        });
    }
}
