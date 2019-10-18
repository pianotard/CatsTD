import java.awt.Dimension;

public class Parser {

    private static final double CHAR_SIZE = 3.2;

    private final Dimension frame;

    public Parser(int width, int height) {
        this.frame = new Dimension(width, height);
    }

    public String parse(String string) {
        return "<html>" + string + "</html>";
    }
}
