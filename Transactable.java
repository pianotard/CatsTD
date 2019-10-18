import java.awt.image.BufferedImage;

public interface Transactable {
    
    public void upgrade();
    public void sell();
    public int getUpgradePrice();
    public int getSellPrice();
    public boolean upgradeable();
    public String getDescription();
    public BufferedImage getDefaultBufferedImage();
}
