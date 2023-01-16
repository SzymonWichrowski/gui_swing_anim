package figury;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimPanel extends JPanel implements ActionListener, MouseListener {
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
	private Timer timer, colorTimer;
	private static int number = 0;

	public AnimPanel() {
		super();
		setBackground(Color.WHITE);
		timer = new Timer(delay, this);
		addMouseListener(this);
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

		if (source == timer) {
			device.drawImage(image, 0, 0, null);
			buffer.clearRect(0, 0, getWidth(), getHeight());
		}

		if (source == colorTimer) {
			for (Figure figure : figures) {
				Random random = new Random();
				figure.setClr(new Color(random.nextInt(255), random.nextInt(255),
										random.nextInt(255), random.nextInt(255)));
			}
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
		for (int i = 1; i < figures.size(); i = i + 2) {
			figures.get(i).setClr(AnimatorApp.getChosenColor());
		}
	}

	public void changeFigColor() {		//zmiana koloru figur
		for (Figure figure : figures) {
			figure.setClr(AnimatorApp.getChosenColor());
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

	public void figuresSpin() {
		for (Figure figure : figures) {
			spinSetting(figure);
		}
	}

	public void rectSpin() {
		for (int i = 0; i < figures.size(); i = i + 2) {
			spinSetting(figures.get(i));
		}
	}

	public void ellipSpin() {
		for (int i = 1; i < figures.size(); i = i + 2) {
			spinSetting(figures.get(i));
		}
	}

	public void spinSetting(Figure f) {
		f.setDx(0);
		f.setDy(0);
		f.setAn(1);
	}

	public void spinEnd() {
		Random random = new Random();
		int dx, dy;
		double an;
		dx = 1 + random.nextInt(5);
		dy = 1 + random.nextInt(5);
		an = 0.1 * random.nextDouble();
		for (Figure figure : figures) {
			if(figure.getDx() == 0 && figure.getDy() == 0 && figure.getAn() == 1) {
				figure.setDx(dx);
				figure.setDy(dy);
				figure.setAn(an);
			}
		}
	}

	public static void setNumber(int number) {
		AnimPanel.number = number;
	}

	@Override
	public void mouseClicked(MouseEvent e) {


	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (Figure figure : figures) {
			figure.setClrToRemember(figure.getClr());
		}
		colorTimer = new Timer(100, this);
		colorTimer.start();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		colorTimer.stop();
		for (Figure figure : figures) {
			figure.setClr(figure.getClrToRemember());
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
