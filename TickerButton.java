import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TickerButton extends JLabel {

    private static final int B_WIDTH = 30;
    private static final int B_HEIGHT = 20;
    
    private static final Icon START = new ImageIcon(UIUtil.resize(
            UIUtil.readBufferedImage("buttons/start_button.png"), B_WIDTH, B_HEIGHT));
    private static final Icon FF = new ImageIcon(UIUtil.resize(
            UIUtil.readBufferedImage("buttons/ff_button.png"), B_WIDTH, B_HEIGHT));

    private MouseListener startListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {
            if (TickerButton.this.controllable.isSpedUp()) {
                TickerButton.this.setFF();
                TickerButton.this.controllable.startLevel();
            } else {
                TickerButton.this.setSlow();
                TickerButton.this.controllable.startLevel();
            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
    };
    private MouseListener ffListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {
            TickerButton.this.setSlow();
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {} 
    };
    private MouseListener slowDownListener = new MouseListener() {
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {
            TickerButton.this.setFF();
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {} 
    };

    private Controllable controllable;

    public TickerButton(Controllable c) {
        super();
        this.controllable = c;
        this.setLayout(null);
        int x = CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - TickerButton.B_WIDTH - 27;
        int y = 9;
        this.setBounds(x, y, TickerButton.B_WIDTH, TickerButton.B_HEIGHT);
        this.addMouseListener(this.ffListener);
        this.setStart();
    }

    public void setStart() {
        this.setIcon(TickerButton.START);
        this.removeAllListeners();
        this.addMouseListener(this.startListener);
    }

    public void setFF() {
        this.setIcon(TickerButton.FF);
        this.removeAllListeners();
        this.addMouseListener(this.ffListener);
        this.controllable.speedUp();
    }

    public void setSlow() {
        this.setIcon(TickerButton.START);
        this.removeAllListeners();
        this.addMouseListener(this.slowDownListener);
        this.controllable.speedDown();
    }

    private void removeAllListeners() {
        MouseListener[] listeners = this.getMouseListeners();
        for (MouseListener m : listeners) {
            this.removeMouseListener(m);
        }
    }
}
