import java.awt.Rectangle;
import java.awt.Component;

public interface Canvas {
    
    public PlacedTowerMenu addPlacedTowerMenu(PlacedTowerMenu menu);
    public PlacedTowerMenu removePlacedTowerMenu(PlacedTowerMenu menu);
    public AbstractTower add(AbstractTower t);
    public AbstractTower remove(AbstractTower t);
    public AbstractTower addDragTower(AbstractTower t);
    public AbstractTower removeDragTower(AbstractTower t);
    public AbstractTower getDragTower();
    public boolean sell(AbstractTower t);
    public boolean upgrade(AbstractTower t);
    public boolean hasDragTower();
    public boolean hasIntersect(Rectangle bounds);
    public boolean playerCanAfford(AbstractTower t);
    public Component[] getComponents();
    public void repaintCanvas();
}
