package hr.fer.zemris.ooup.lab3;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

public class OsnovniGUI extends JComponent {
	JFrame frame = new JFrame();

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Serif", Font.PLAIN, 16);
		g2.setFont(font);
		g2.drawString("Ovdje je neki proizvoljan tekst", 30, 20);
		g2.drawString("koji se ispisuje u dva retka", 30, 35);
		Line2D lin = new Line2D.Float(20, 20, 28, 20);
		g2.setColor(Color.red);
		g2.draw(lin);
		lin = new Line2D.Float(24, 10, 24, 20);
		g2.draw(lin);
	}

	public void createAndShowGUI() {

		Container cp = frame.getContentPane();
		cp.add(new OsnovniGUI());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);

		Action escapeAction = new AbstractAction() {
			// close the frame when the user presses escape
			public void actionPerformed(ActionEvent e) {
				// System.exit(0);
				frame.dispose();
			}
		};
		frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(stroke, "ENTER");
		frame.getRootPane().getActionMap().put("ENTER", escapeAction);
		frame.setVisible(true);
	}
}
