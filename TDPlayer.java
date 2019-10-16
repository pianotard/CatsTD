import java.awt.Font;
import javax.swing.JPanel;
import javax.swing.JLabel;

public final class TDPlayer extends JPanel {

    private int hp = 200;
    private final JLabel hpLabel = new JLabel();

    private int money = 500;
    private final JLabel moneyLabel = new JLabel();

    public TDPlayer() {
        super();
        this.setLayout(null);
        this.setBounds(25, 0, 180, 30);
        this.setBackground(null);

        this.hpLabel.setText("HP: " + this.hp);
        this.hpLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        this.hpLabel.setForeground(java.awt.Color.YELLOW);
        this.hpLabel.setBounds(0, 0, 90, 30);

        this.moneyLabel.setText("$: " + this.money);
        this.moneyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        this.moneyLabel.setForeground(java.awt.Color.YELLOW);
        this.moneyLabel.setBounds(90, 0, 90, 30);

        this.add(this.hpLabel);
        this.add(this.moneyLabel);
    }

    public void getMoney(int money) {
        this.money += money;
        this.moneyLabel.setText("$: " + this.money);
    }

    // Assumes already have enough money
    public void payMoney(int cost) {
        this.money -= cost;
        this.moneyLabel.setText("$: " + this.money);
    }

    public boolean enoughMoney(int cost) {
        return cost <= this.money;
    }

    public void inflictDamage(int damage) {
        this.hp -= damage;
        this.hpLabel.setText("HP: " + this.hp);
    }

    @Override
    public String toString() {
        return "Player. HP: " + this.hp + ", $: " + this.money;
    }
}
