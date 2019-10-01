import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
import javax.swing.JPanel;

public class TowerPalette extends JPanel {
   
    static final int P_WIDTH = 100;
    static final int P_HEIGHT = 600;
    static final int ICON_DIM = 35;
    static final int ICON_GAP = 10;

    private List<TPIcon> towerIcons = new ArrayList<>();

    public TowerPalette() {
        super();
        this.setLayout(null);
        this.setBounds(CatsTD.WINDOW_WIDTH - TowerPalette.P_WIDTH - 15, 
                CatsTD.WINDOW_HEIGHT - TowerPalette.P_HEIGHT, 
                TowerPalette.P_WIDTH, TowerPalette.P_HEIGHT);
        this.setBackground(new Color(196, 111, 0));
        this.towerIcons.add(new TPIcon(new BasicCatTower(0, 0)));
        this.initIcons();
    }

    private void initIcons() {
        for (int i = 0; i < this.towerIcons.size(); i++) {
            int x = (i % 2) * TowerPalette.ICON_DIM + (i % 2 + 1) * TowerPalette.ICON_GAP;
            int y = (i / 2) * TowerPalette.ICON_DIM + (i / 2 + 1) * TowerPalette.ICON_GAP;
            TPIcon icon = this.towerIcons.get(i);;
            icon.setBounds(x, y, TowerPalette.ICON_DIM, TowerPalette.ICON_DIM);
            this.add(icon);
        }
    }
}
