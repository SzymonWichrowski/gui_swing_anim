package figury;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class AnimatorApp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnInfo, btnAdd, btnAnimate, btnReset, 
					btnRectColor, btnEllipColor, btnColor, 
					btnRectFreeze, btnEllipFreeze,
					btnSpinOn, btnSpinOff;
	private JLabel ltitle, ldelay, lcolors, lFreeze, lSpin;
	private JComboBox speedBox, spinBox;
	private static Color colorOfBtn = new Color(100, 137, 160),	//kolor przycisków (ewentualnie innych komponentów)
				   colorOfLabels = new Color(180, 91, 149),	//kolor etykiet
				   chosenColor;
	private Font fontOfBtn = new Font("Dialog", Font.ITALIC, 12),		//czcionka ogólna
				 fontOfLabels = new Font("Dialog", Font.ITALIC, 16);
	private static String speed;	//wybór użytkownika

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				AnimatorApp frame = new AnimatorApp();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AnimatorApp() {
		//pobieramy rozmiary ekranu aby wyświetlić okno na jego środku
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int windowWidth = 450, windowHeight = 620;
		setBounds((screen.width-windowWidth)/2, (screen.height-windowHeight)/2, windowWidth, windowHeight);
		setTitle("Animator App");

		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(52, 60, 89, 255));	//kolor okna
		setBackground(Color.WHITE);

		AnimPanel canva = new AnimPanel();
		canva.setBounds(10, 50, 422, 219);
		contentPane.add(canva);
		SwingUtilities.invokeLater(() -> canva.initialize());

		ltitle = new JLabel("Simple Animation Creator");	//tytuł
		ltitle.setBounds(125, 10,250 , 30);
		ltitle.setFont(fontOfLabels);
		ltitle.setForeground(colorOfLabels);
		contentPane.add(ltitle);


		btnInfo = new JButton("Info");			//dodanie przycisku info
		btnInfo.setBounds(10, 280, 80, 23);
		btnInfo.setBackground(colorOfBtn);
		btnInfo.setFont(fontOfBtn);
		btnInfo.setToolTipText("Useful instructions");
		contentPane.add(btnInfo);
		btnInfo.addActionListener(e -> {
			JOptionPane.showMessageDialog(null,
										  message(),
									 "How to use this app?",
								          JOptionPane.INFORMATION_MESSAGE);
		});

		btnAdd = new JButton("Add");	//dodanie przycisku add
		btnAdd.setBounds(260, 280, 80, 23);
		btnAdd.setBackground(colorOfBtn);
		btnAdd.setFont(fontOfBtn);
		btnAdd.setEnabled(false);	//blokada przycisku add
		contentPane.add(btnAdd);
		btnAdd.addActionListener(e -> canva.addFigure());	//actionListener poprzez wyrażenie lambda

		btnAnimate = new JButton("Animate/Stop");	//dodanie przycisku animacji
		btnAnimate.setBounds(100, 280, 150, 23);
		btnAnimate.setBackground(colorOfBtn);
		btnAnimate.setFont(fontOfBtn);
		contentPane.add(btnAnimate);
		btnAnimate.addActionListener(e -> {
			canva.animate();
			if(!btnAdd.isEnabled()) btnAdd.setEnabled(true);	//odblokowanie przycisku add
			else btnAdd.setEnabled(false);						//zablokowanie przycisku add
		});

		btnReset = new JButton("Reset");		//przycisk resetu
		btnReset.setBounds(350, 280, 80, 23);
		btnReset.setBackground(colorOfBtn);
		btnReset.setFont(fontOfBtn);
		btnReset.setToolTipText("Reset app");
		contentPane.add(btnReset);
		btnReset.addActionListener(e -> {
			dispose();
			AnimatorApp frame = new AnimatorApp();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			AnimPanel.setNumber(0);		//musimy przypisać number wartość początkową
		});

		ldelay = new JLabel("Speed");
		ldelay.setBounds(50, 300, 100, 50);
		ldelay.setFont(fontOfLabels);
		ldelay.setForeground(colorOfLabels);
		contentPane.add(ldelay);

		speedBox = new JComboBox();					//pole wyboru prędkości
		speedBox.setBounds(30, 340, 100, 30);
		speedBox.setFont(fontOfBtn);
		speedBox.setBackground(colorOfBtn);
		speedBox.setToolTipText("Choose speed");
		speedBox.addItem("Medium");
		speedBox.addItem("Fast");
		speedBox.addItem("Slow");
		contentPane.add(speedBox);
		speedBox.addActionListener(e -> {
			speed = Objects.requireNonNull(speedBox.getSelectedItem()).toString();
			canva.changeSpeed();
		});

		lcolors = new JLabel("Colors");		//etykieta kolorów
		lcolors.setBounds(250, 300, 100, 50);
		lcolors.setFont(fontOfLabels);
		lcolors.setForeground(colorOfLabels);
		contentPane.add(lcolors);

		btnRectColor = new JButton("Rectangles");		//przycisk wyboru koloru prostokątów
		btnRectColor.setBounds(170, 340, 100, 30);
		btnRectColor.setFont(fontOfBtn);
		btnRectColor.setBackground(colorOfBtn);
		btnRectColor.setToolTipText("Change color of all rectangles");
		contentPane.add(btnRectColor);
		btnRectColor.addActionListener(e -> {
			chooseColor();
			canva.changeRectColor();
		});

		btnEllipColor = new JButton("Ellipses");		//przycisk wyboru koloru elips
		btnEllipColor.setBounds(280, 340, 100, 30);
		btnEllipColor.setFont(fontOfBtn);
		btnEllipColor.setBackground(colorOfBtn);
		btnEllipColor.setToolTipText("Change color of all ellipses");
		contentPane.add(btnEllipColor);
		btnEllipColor.addActionListener(e -> {
			chooseColor();
			canva.changeEllipColor();
		});

		btnColor = new JButton("All Figures");			//wybor koloru wszystkich figur
		btnColor.setBounds(230, 380, 100, 30);
		btnColor.setFont(fontOfBtn);
		btnColor.setBackground(colorOfBtn);
		btnColor.setToolTipText("Change color of all figures");
		contentPane.add(btnColor);
		btnColor.addActionListener(e -> {
			chooseColor();
			canva.changeFigColor();
		});


		lFreeze = new JLabel("Freeze only ->");		//etykieta zamrażania
		lFreeze.setBounds(50, 440, 150, 30);
		lFreeze.setFont(fontOfLabels);
		lFreeze.setForeground(colorOfLabels);
		contentPane.add(lFreeze);

		btnRectFreeze = new JButton("Rectangles");		//przycisk zamrożenia prostokątów
		btnRectFreeze.setBounds(170, 440, 100, 30);
		btnRectFreeze.setFont(fontOfBtn);
		btnRectFreeze.setBackground(colorOfBtn);
		btnRectFreeze.setToolTipText("Freeze all rectangles");
		contentPane.add(btnRectFreeze);
		btnRectFreeze.addActionListener(e -> {
			canva.freezeRect();
		});

		btnEllipFreeze = new JButton("Ellipses");		//przycisk zamrożenia elips
		btnEllipFreeze.setBounds(280, 440, 100, 30);
		btnEllipFreeze.setFont(fontOfBtn);
		btnEllipFreeze.setBackground(colorOfBtn);
		btnEllipFreeze.setToolTipText("Freeze all ellipses");
		contentPane.add(btnEllipFreeze);
		btnEllipFreeze.addActionListener(e -> {
			canva.freezeEllip();
		});

		lSpin = new JLabel("Spinning");		//etykieta obrotów
		lSpin.setBounds(175, 490, 100, 30);
		lSpin.setFont(fontOfLabels);
		lSpin.setForeground(colorOfLabels);
		contentPane.add(lSpin);

		btnSpinOn = new JButton("spinning ON");		//aktywacja obrotów
		btnSpinOn.setBounds(20, 530, 110, 30);
		btnSpinOn.setFont(fontOfBtn);
		btnSpinOn.setBackground(colorOfBtn);
		contentPane.add(btnSpinOn);
		btnSpinOn.addActionListener(e -> {
			btnSpinOff.setEnabled(true);
			switch (Objects.requireNonNull(spinBox.getSelectedItem()).toString()){
				case "All figures": {
					canva.figuresSpin();
				}
				break;
				case "Rectangles": {
					canva.rectSpin();
				}
				break;
				case "Ellipses" : {
					canva.ellipSpin();
				}
				break;
			}
		});

		spinBox = new JComboBox();		//wybór figur do obrotów
		spinBox.setBounds(140, 530, 100, 30);
		spinBox.setFont(fontOfBtn);
		spinBox.setBackground(colorOfBtn);
		spinBox.addItem("All figures");
		spinBox.addItem("Rectangles");
		spinBox.addItem("Ellipses");
		contentPane.add(spinBox);

		btnSpinOff = new JButton("spinning OFF");		//wyłączenie obrotów
		btnSpinOff.setBounds(280, 530, 115, 30);
		btnSpinOff.setFont(fontOfBtn);
		btnSpinOff.setBackground(colorOfBtn);
		btnSpinOff.setEnabled(false);
		contentPane.add(btnSpinOff);
		btnSpinOff.addActionListener(e -> {
			canva.spinEnd();
			btnSpinOff.setEnabled(false);
		});
	}

	public String message() {		//instrukcja do programu
		return 	"1) Click on 'Animate/Stop' button to start animation or pause it.\n" +
				"   'Add' button should be available from now on.\n" +
				"2) Click on 'Add' button to add new figures to animation.\n" +
				"3) You can change speed of animation. (3 modes)\n" +
				"4) You can also change color of both rectangles and ellipses drawn on the screen at your discretion.\n" +
				"5) Speaking of colors. All figures on the screen will randomly change them as long as you press the mouse.\n" +
				"6) Next interesting function is freezing only one of two available shapes.\n" +
				"7)	The last but definitely not least is possibility of making figures spin in place. (very fast) \n" +
				"8) Of course button 'Reset' will reset whole app at any moment.\n" +
				"That's all. Have fun :)";
	}

	public static String getSpeed() {	//getter zmiennej speed
		return speed;
	}

	public static Color getChosenColor() {		//getter zmiennej chosenColor
		return chosenColor;
	}

	public static void chooseColor() {
		chosenColor = Objects.requireNonNull(JColorChooser.showDialog(null, "Choose color", Color.PINK),
									 "Color cannot be a null");
	}
}
