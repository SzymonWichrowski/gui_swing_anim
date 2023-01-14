package figury;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class RectFig extends Figure{

    public RectFig(Graphics2D buffer, int delay, int width, int height) {

        super(buffer, delay, width, height);    //wywołanie konstruktora klasy nadrzędnej

        Random random = new Random();
        shape = new Rectangle2D.Double(0, 0, random.nextDouble(50), random.nextDouble(50));
        area = new Area(shape);
        aft = new AffineTransform();
    }

}
