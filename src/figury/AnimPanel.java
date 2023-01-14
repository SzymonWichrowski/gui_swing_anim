package figury;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// bufor
	Image image;
	// wykreslacz ekranowy
	Graphics2D device;
	// wykreslacz bufora
	Graphics2D buffer;
	ArrayList<Figure> figures = new ArrayList<>();	//lista figur
	private int delay = 70, width, height;

	private Timer timer;

	private static int number = 0;

	public AnimPanel() {
		super();
		setBackground(Color.WHITE);
		timer = new Timer(delay, this);

	}

	public void initialize() {
		width = getWidth();
		height = getHeight();

		image = createImage(width, height);		//tworzy off-screen image potrzebny do podwójnego buforowania (bufor)
		buffer = (Graphics2D) image.getGraphics();		//przypisujemy kontekst graficzny bufora do zmiennej buffer
		buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		device = (Graphics2D) getGraphics();	//przypisujemy kontekst graficzny naszego okna do zmiennej device
		device.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}

	void addFigure() {
		Figure fig = (number++ % 2 == 0) ? new RectFig(buffer, delay, getWidth(), getHeight())
				: new EllipFig(buffer, delay, getWidth(), getHeight());
		timer.addActionListener(fig);
		new Thread(fig).start();
		figures.add(fig);		//dodanie nowo utworzonej figury do listy figur
	}

	void animate() {
		if (timer.isRunning()) {
			Figure.setPaused(true);		//pauza
			timer.stop();
		} else {
			timer.start();
			Figure.setPaused(false);	//koniec pauzy
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if(source == timer) {
			device.drawImage(image, 0, 0, null);
			buffer.clearRect(0, 0, getWidth(), getHeight());
		}
	}

	public void changeSpeed() {		//zmiana prędkości figur
		switch(AnimatorApp.getSpeed()) {
			case "Medium": {
				delay = 70;
			}
			break;
			case "Fast": {
				delay = 10;
			}
			break;
			case "Slow": {
				delay = 500;
			}
			break;
		}
		for (Figure figure : figures) {		//dla wszystkich elementów listy
			figure.getDelay().set(delay);
		}
	}

	public void changeRectColor() {		//zmiana koloru prostokątów
		for (int i = 0; i < figures.size(); i = i + 2) {
			figures.get(i).setClr(AnimatorApp.getChosenColor());
		}
	}

	public void changeEllipColor() {	//zmiana koloru elips
		for (int i= 1; i < figures.size(); i = i + 2) {
			figures.get(i).setClr(AnimatorApp.getChosenColor());
		}
	}

	public void freezeRect() {			//zamrażanie prostokątów
		for (int i = 0; i < figures.size(); i = i + 2) {
			if (!figures.get(i).getFreeze()) {
				figures.get(i).setFreeze(true);		//zamrożenie
			}
			else figures.get(i).setFreeze(false);		//odmrożenie
		}
	}

	public void freezeEllip() {			//zamrażanie elips
		for (int i = 1; i < figures.size(); i = i + 2) {
			if (!figures.get(i).getFreeze()) {
				figures.get(i).setFreeze(true);		//zamrożenie
			}
			else figures.get(i).setFreeze(false);	//odmrożenie
		}
	}
}
