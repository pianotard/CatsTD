import java.awt.Rectangle;

public class MouseMob extends AbstractMob {
    
    public MouseMob() {
        super(null);
        this.setBounds(new Rectangle(0, 0, 30, 30));
        this.setBackground(java.awt.Color.RED);
        this.name = "Mouse mob";
        this.tickDelay = 2;
        this.hp = 1;
        this.atk = 1;

        this.reward = 1;
    }

    @Override
    public AbstractMob clone() {
        return new MouseMob();
    }
}
