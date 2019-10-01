import java.awt.Font;
import javax.swing.JLabel;

public class TDPlayer extends JLabel {
    
    private int hp = 200;

    public TDPlayer() {
        super();
        this.setLayout(null);
        this.setBounds(25, 0, 100, 30);
        this.setText("HP: " + this.hp);
        this.setFont(new Font("Arial", Font.PLAIN, 15));
        this.setForeground(java.awt.Color.YELLOW);
    }

    public void inflictDamage(int damage) {
        this.hp -= damage;
        this.setText("HP: " + this.hp);
    }
}
