import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlacedTowerMenu extends JPanel {
    
    private static final int PADDING = 10;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 100;
    private static final int X = (AbstractMap.MAP_BOUNDS.width - PlacedTowerMenu.WIDTH) / 2;
    private static final int Y = AbstractMap.MAP_BOUNDS.height - PlacedTowerMenu.HEIGHT;
    private static final int I_WIDTH = 50;
    private static final int I_HEIGHT = 50;
    private static final int B_WIDTH = 100;
    private static final int B_HEIGHT = HEIGHT - I_HEIGHT - PADDING * 3;
    private static final int T_WIDTH = WIDTH - I_WIDTH - PADDING * 3;
    private static final int T_HEIGHT = HEIGHT - B_HEIGHT - PADDING * 3;

    private static final Parser PARSER = new Parser(T_WIDTH, T_HEIGHT);

    private Transactable placedTower;

    public PlacedTowerMenu(Transactable t) {
        super();
        this.setLayout(null);
        this.placedTower = t;
        this.setBounds(X, Y, WIDTH, HEIGHT);
        this.setBackground(java.awt.Color.GREEN);

        JLabel icon = new JLabel();
        icon.setLayout(null);
        icon.setBounds(PADDING, PADDING, I_WIDTH, I_HEIGHT);
        icon.setIcon(new ImageIcon(UIUtil.resize(t.getDefaultBufferedImage(), I_WIDTH, I_HEIGHT)));
        this.add(icon);

        JLabel description = new JLabel();
        description.setLayout(null);
        description.setBounds(I_WIDTH + PADDING * 2, PADDING, T_WIDTH, T_HEIGHT);
        description.setVerticalAlignment(JLabel.TOP);
        description.setText(PARSER.parse(t.getDescription()));
        this.add(description);

        JLabel sellButton = new JLabel();
        sellButton.setLayout(null);
        sellButton.setBounds(WIDTH / 4 - B_WIDTH / 2, I_HEIGHT + PADDING * 2, B_WIDTH, B_HEIGHT);
        sellButton.setHorizontalAlignment(JLabel.CENTER);
        sellButton.setText("SELL CAT $" + t.getSellPrice());
        sellButton.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {
                PlacedTowerMenu.this.placedTower.sell();    
            }
        });
        this.add(sellButton);

        JLabel upgradeButton = new JLabel();
        upgradeButton.setLayout(null);
        upgradeButton.setBounds(WIDTH * 3 / 4 - B_WIDTH / 2, I_HEIGHT + PADDING * 2, B_WIDTH, B_HEIGHT);
        upgradeButton.setHorizontalAlignment(JLabel.CENTER);
        if (t.upgradeable()) {
            upgradeButton.setText("UPGRADE $" + t.getUpgradePrice());
            upgradeButton.addMouseListener(new MouseListener() {
                public void mouseEntered(MouseEvent e) {}
                public void mouseExited(MouseEvent e) {}
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
                public void mouseClicked(MouseEvent e) {
                    PlacedTowerMenu.this.placedTower.upgrade();    
                }
            });
        } else {
            upgradeButton.setText("MAX UPGRADE");
        }
        this.add(upgradeButton);
    }
}
