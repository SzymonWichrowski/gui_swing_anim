/**
 * 
 */
package figury;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tb
 *
 */
public abstract class Figure implements Runnable, ActionListener/*, Shape*/ {

	// wspolny bufor
	protected Graphics2D buffer;
	protected Area area;
	// do wykreslania
	protected Shape shape;
	// przeksztalcenie obiektu
	protected AffineTransform aft;

	// przesuniecie
	private int dx, dy;
	// rozciaganie
	private double sf;
	// kat obrotu
	private double an;
	private AtomicInteger delay = new AtomicInteger();	//klasa dzięki której int może być automatycznie updatowany
	private int width;
	private int height;
	private Color clr;
	private static boolean paused = false;	//pauza(nieaktywna)
	private boolean freeze;
	protected static final Random rand = new Random();

	public Figure(Graphics2D buf, int del, int w, int h) {
		delay.set(del);
		buffer = buf;
		width = w;
		height = h;

		dx = 1 + rand.nextInt(5);
		dy = 1 + rand.nextInt(5);
		sf = 1 + 0.05 * rand.nextDouble();
		an = 0.1 * rand.nextDouble();
		clr = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
		freeze = false;
		// reszta musi być zawarta w realizacji klasy Figure
		// (tworzenie figury i przygotowanie transformacji)

	}

	@Override
	public void run() {
		// przesuniecie na srodek
		aft.translate(100, 100);
		area.transform(aft);
		shape = area;
		while (true) {
			// przygotowanie nastepnego kadru
			shape = nextFrame();
			try {
				Thread.sleep(delay.get());
			} catch (InterruptedException e) {
			}
		}
	}

	protected Shape nextFrame() {
		// zapamietanie na zmiennej tymczasowej
		// aby nie przeszkadzalo w wykreslaniu
		area = new Area(area);
		aft = new AffineTransform();
		Rectangle bounds = area.getBounds();
		int cx = bounds.x + bounds.width / 2;
		int cy = bounds.y + bounds.height / 2;

		if(!paused) {	//poruszanie tylko wtedy kiedy pauza jest nieaktywna (paused == false)
			if (!freeze){	//każda figura ma przypisane pole freeze

				// odbicie
				if (cx + bounds.width/2 <= bounds.width) dx = Math.abs(dx);

				if (cx - bounds.width/2 >= width - bounds.width && dx > 0) dx = -dx;

				if (cy + bounds.height/2 <= bounds.height) dy = Math.abs(dy);

				if (cy - bounds.height/2 >= height - bounds.height && dy > 0) dy = -dy;

				// zwiekszenie lub zmniejszenie
				if (bounds.height > height / 3 || bounds.height < 10)
					sf = 1 / sf;

				// konstrukcja przeksztalcenia
				aft.translate(cx, cy);
				aft.scale(sf, sf);				//skalowanie po zwiekszeniu/zmniejszeniu
				aft.rotate(an);					//rotacja na podstawie kąta an
				aft.translate(-cx, -cy);
				aft.translate(dx, dy);
				// przeksztalcenie obiektu
				area.transform(aft);
			}
		}
		return area;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// wypelnienie obiektu
		buffer.setColor(clr.brighter());
		buffer.fill(shape);
		// wykreslenie ramki
		buffer.setColor(clr.darker());
		buffer.draw(shape);
	}

	public static void setPaused(boolean paused) {
		Figure.paused = paused;
	}

	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}

	public boolean getFreeze() {
		return this.freeze;
	}

	public AtomicInteger getDelay() {
		return delay;
	}

	public void setClr(Color clr) {
		this.clr = clr;
	}

	public void setAn(double an) {
		this.an = an;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public double getAn() {
		return an;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}
}
