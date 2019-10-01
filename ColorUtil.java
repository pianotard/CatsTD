import java.awt.Color;

public class ColorUtil {
    
    public static Color transparent(Color c, double alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (alpha * 255));
    }
}
