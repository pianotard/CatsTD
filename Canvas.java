import java.awt.Rectangle;

public interface Canvas {
    
    public AbstractTower add(AbstractTower t);
    public AbstractTower remove(AbstractTower t);
    public AbstractTower addDragTower(AbstractTower t);
    public AbstractTower removeDragTower(AbstractTower t);
    public AbstractTower getDragTower();
    public boolean hasDragTower();
    public boolean hasIntersect(Rectangle bounds);
    public boolean playerCanAfford(AbstractTower t);
    public void repaintCanvas();
}
