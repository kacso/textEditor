package hr.fer.zemris.ooup.lab3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class TextEditor extends JComponent implements CursorObserver,
		TextObserver, KeyListener {
	private TextEditorModel textEditorModel;
	private ClipboardStack stack = ClipboardStack.getClipboardStackInstance();
	private JFrame frame;

	public TextEditor(TextEditorModel model) {
		textEditorModel = model;
		textEditorModel.registerCursorObserver(this);
		textEditorModel.registerTextObserver(this);
		frame = textEditorModel.frame;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font font = new Font("Serif", Font.PLAIN, 16);
		g2.setFont(font);
		FontMetrics myFontMetrics = g2.getFontMetrics(font);

		/*
		 * for(int i = 0; i < textEditorModel.lines.length; i++){
		 * g2.drawString(textEditorModel.lines[i], 30, 20 * (i + 1)); }
		 */

		Iterator<String> iterator = textEditorModel.allLines();

		int i = 0;
		g2.setColor(Color.black);
		int cursorPosition = 0;
		int selectionStart = 0;
		int selectionEnd = 0;
		/** Print text */
		while (iterator.hasNext()) {
			String text = iterator.next();
			if (i == textEditorModel.getSelectionRange().start.y) {
				selectionStart = (int) myFontMetrics.stringWidth(text
						.substring(0,
								textEditorModel.getSelectionRange().start.x));
			}
			if (i == textEditorModel.getSelectionRange().end.y) {
				selectionEnd = (int) myFontMetrics.stringWidth(text.substring(
						0, textEditorModel.getSelectionRange().end.x));
			}
			if (i == textEditorModel.getCursorLocation().y) {
				cursorPosition = (int) myFontMetrics.stringWidth(text
						.substring(0, textEditorModel.getCursorLocation().x));
			}
			g2.drawString(text, 30, 20 * (i + 1));
			i++;

		}
		/** Draw cursor */
		int y = textEditorModel.getCursorLocation().y;
		Line2D lin = new Line2D.Float(cursorPosition + textEditorModel.xSpace,
				textEditorModel.fontHight * y + textEditorModel.ySpace,
				cursorPosition + textEditorModel.xSpace,
				textEditorModel.fontHight * y + textEditorModel.fontHight);
		g2.setColor(Color.red);
		g2.draw(lin);

		/** Selection */
		// g2.setColor(Color.cyan);
		g2.setColor(new Color(255, 200, 10, 128));
		LocationRange locationRange = textEditorModel.getSelectionRange();
		if ((locationRange.start.y == locationRange.end.y)
				&& locationRange.start.x >= 0) {
			g2.fillRect(selectionStart + textEditorModel.xSpace,
					textEditorModel.fontHight * locationRange.start.y
							+ textEditorModel.ySpace, selectionEnd
							- selectionStart, textEditorModel.fontHight);
		} else if (locationRange.start.x >= 0) {
			g2.fillRect(selectionStart + textEditorModel.xSpace,
					textEditorModel.fontHight * locationRange.start.y
							+ textEditorModel.ySpace,
					textEditorModel.editorWidth - selectionStart
							- textEditorModel.xSpace, textEditorModel.fontHight);

			for (i = locationRange.start.y + 1; i < locationRange.end.y; i++) {
				g2.fillRect(textEditorModel.xSpace, textEditorModel.fontHight
						* i + textEditorModel.ySpace,
						textEditorModel.editorWidth - textEditorModel.xSpace,
						textEditorModel.fontHight);
			}
			g2.fillRect(textEditorModel.xSpace, textEditorModel.fontHight
					* locationRange.end.y + textEditorModel.ySpace,
					selectionEnd, textEditorModel.fontHight);
		}

	}

	public void createAndShowGUI() {

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 500);
		frame.setLayout(new BorderLayout());
		/** Menu */
		Menu menu = new Menu(this, textEditorModel, stack);
		menu.createMenu(frame);

		/** Toolbar */
		ToolBar toolBar = new ToolBar(this, menu, textEditorModel);
		toolBar.createToolBar(frame);

		frame.addKeyListener(this);
		Container cp = frame.getContentPane();
		cp.add(new TextEditor(textEditorModel));

		/** Status bar */
		StatusBar statusBar = new StatusBar(textEditorModel);
		statusBar.createStatusBar(frame);

		frame.setVisible(true);
	}

	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) {
		if (!e.isControlDown() && e.getKeyChar() != KeyEvent.VK_DELETE
				&& e.getKeyChar() != KeyEvent.VK_BACK_SPACE && !e.isAltDown()) {
			textEditorModel.insert(e.getKeyChar());
		}
	}

	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		LocationRange locationRange = textEditorModel.getSelectionRange();
		// TODO: if start == end odznaèi
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (e.isShiftDown()) {
				Location location = new Location();
				/**
				 * Ako nije ništa oznaèeno promijeni start i end u locationRange
				 */
				if (locationRange.start.x < 0) {
					location.x = textEditorModel.getCursorLocation().x;
					location.y = textEditorModel.getCursorLocation().y;
					locationRange.end = location;
					textEditorModel.moveCursorUp();
					location = textEditorModel.getCursorLocation();
					locationRange.start = location;
					textEditorModel.setSelectionRange(locationRange);
				}

				/**
				 * Ako je veæ oznaèeno nešto, promijeni samo start u
				 * locationRange
				 */
				else {
					textEditorModel.moveCursorUp();
					location = textEditorModel.getCursorLocation();
					locationRange.start = location;
					textEditorModel.setSelectionRange(locationRange);
				}
			} else {
				unsetLocationRange(locationRange);
				textEditorModel.moveCursorUp();
			}

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (e.isShiftDown()) {
				Location location = new Location();
				/**
				 * Ako nije ništa oznaèeno promijeni start i end u locationRange
				 */
				if (locationRange.start.x < 0) {
					location.x = textEditorModel.getCursorLocation().x;
					location.y = textEditorModel.getCursorLocation().y;
					locationRange.start = location;
					textEditorModel.moveCursorDown();
					location = textEditorModel.getCursorLocation();
					locationRange.end = location;
					textEditorModel.setSelectionRange(locationRange);
				}

				/**
				 * Ako je veæ oznaèeno nešto, promijeni samo start u
				 * locationRange
				 */
				else {
					textEditorModel.moveCursorDown();
					location = textEditorModel.getCursorLocation();
					locationRange.end = location;
					textEditorModel.setSelectionRange(locationRange);
				}
			} else {
				unsetLocationRange(locationRange);
				textEditorModel.moveCursorDown();
			}

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (e.isShiftDown()) {
				Location location = new Location();
				/**
				 * Ako nije ništa oznaèeno promijeni start i end u locationRange
				 */
				if (locationRange.start.x < 0) {
					location.x = textEditorModel.getCursorLocation().x;
					location.y = textEditorModel.getCursorLocation().y;
					locationRange.end = location;
					textEditorModel.moveCursorLeft();
					location = textEditorModel.getCursorLocation();
					locationRange.start = location;
					textEditorModel.setSelectionRange(locationRange);
				}
				/**
				 * Ako je veæ oznaèeno nešto, promijeni samo start ili samo end
				 * u locationRange
				 */
				else if (textEditorModel.getCursorLocation() == locationRange.end) {
					textEditorModel.moveCursorLeft();
					location = textEditorModel.getCursorLocation();
					locationRange.end = location;
					textEditorModel.setSelectionRange(locationRange);
				} else if (textEditorModel.getCursorLocation() == locationRange.start) {
					textEditorModel.moveCursorLeft();
					location = textEditorModel.getCursorLocation();
					locationRange.start = location;
					textEditorModel.setSelectionRange(locationRange);
				}
			} else {
				unsetLocationRange(locationRange);
				textEditorModel.moveCursorLeft();
			}

		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (e.isShiftDown()) {
				Location location = new Location();
				/**
				 * Ako nije ništa oznaèeno promijeni start i end u locationRange
				 */
				if (locationRange.start.x < 0) {
					location.x = textEditorModel.getCursorLocation().x;
					location.y = textEditorModel.getCursorLocation().y;
					locationRange.start = location;
					textEditorModel.moveCursorRight();
					location = textEditorModel.getCursorLocation();
					locationRange.end = location;
					textEditorModel.setSelectionRange(locationRange);
				}
				/**
				 * Ako je veæ oznaèeno nešto, promijeni samo start ili samo end
				 * u locationRange
				 */
				else if (textEditorModel.getCursorLocation() == locationRange.end) {
					textEditorModel.moveCursorRight();
					location = textEditorModel.getCursorLocation();
					locationRange.end = location;
					textEditorModel.setSelectionRange(locationRange);
				} else if (textEditorModel.getCursorLocation() == locationRange.start) {
					textEditorModel.moveCursorRight();
					location = textEditorModel.getCursorLocation();
					locationRange.start = location;
					textEditorModel.setSelectionRange(locationRange);
				}
			} else {
				unsetLocationRange(locationRange);
				textEditorModel.moveCursorRight();
			}

		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {

			if (textEditorModel.getSelectionRange().start.x < 0)
				textEditorModel.deleteBefore();
			else
				textEditorModel
						.deleteRange(textEditorModel.getSelectionRange());

		} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {

			if (textEditorModel.getSelectionRange().start.x < 0)
				textEditorModel.deleteAfter();
			else
				textEditorModel
						.deleteRange(textEditorModel.getSelectionRange());

		} else if ((e.getKeyCode() == KeyEvent.VK_C) && e.isControlDown()) {
			copy();
		} else if ((e.getKeyCode() == KeyEvent.VK_X) && e.isControlDown()) {
			cut();
		} else if ((e.getKeyCode() == KeyEvent.VK_V) && e.isControlDown()
				&& e.isShiftDown()) {
			pasteAndTake();
		} else if ((e.getKeyCode() == KeyEvent.VK_V) && e.isControlDown()) {
			paste();
		} else if ((e.getKeyCode() == KeyEvent.VK_Z) && e.isControlDown()) {
			undo();

		} else if ((e.getKeyCode() == KeyEvent.VK_Y) && e.isControlDown()) {
			redo();
		}
	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {
	}

	private void unsetLocationRange(LocationRange locationRange) {
		if (locationRange.start.x >= 0) {
			Location location = new Location();
			location.x = location.y = -1;
			locationRange.start = locationRange.end = location;
			textEditorModel.setSelectionRange(locationRange);
		}
	}

	// Implements CursorObserver
	public void updateCursorLocation(Location loc) {
		repaint();
	}

	public void updateText() {
		repaint();
	}

	public void copy() {
		if (textEditorModel.getSelectionRange().start.x >= 0) {
			stack.push(textEditorModel.getSelectedText());
		}
	}

	public void save() {
		JFileChooser saveFile = new JFileChooser();
		saveFile.showSaveDialog(null);
		if (saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = saveFile.getSelectedFile();
			String path;
			try {
				path = file.getCanonicalPath();
				String string = "";
				Iterator<String> iterator = textEditorModel.allLines();

				while (iterator.hasNext()) {
					string += iterator.next() + "\r\n";
				}

				// save to file
				BufferedWriter writer = new BufferedWriter(new FileWriter(path));
				writer.write(string);

				// Close writer
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void open() {
		JFileChooser openFile = new JFileChooser();
		int returnVal = openFile.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = openFile.getSelectedFile();
			// This is where a real application would open the file.
			try {
				String path = file.getCanonicalPath();
				BufferedReader in = new BufferedReader(new FileReader(path));
				String line;
				textEditorModel.clearDocument();
				ArrayList<String> lines = new ArrayList<String>();
				while ((line = in.readLine()) != null) {
					lines.add(line);
				}
				textEditorModel.setDocument(new ArrayList<String>(lines));
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void exit() {
		System.exit(0);
	}

	public void cursorToDocumentStart() {
		Location location = new Location();
		location.x = location.y = 0;
		textEditorModel.setCursorLocation(location);
	}

	public void cursorToDocumentEnd() {
		textEditorModel.setCursorLocation(textEditorModel.getEndOfDocument());
	}

	public void cut() {
		if (textEditorModel.getSelectionRange().start.x >= 0) {
			stack.push(textEditorModel.getSelectedText());
			textEditorModel.deleteRange(textEditorModel.getSelectionRange());
		}
	}

	public void paste() {
		if (!stack.isEmpty()) {
			String text = stack.peek();
			textEditorModel.insert(text);
		}
	}

	public void pasteAndTake() {
		if (!stack.isEmpty()) {
			String text = stack.pop();
			textEditorModel.insert(text);
		}
	}

	public void undo() {
		UndoManager.getUndoManagerInstance().undo();
	}

	public void redo() {
		UndoManager.getUndoManagerInstance().redo();
	}

	public void deleteSection() {
		textEditorModel.deleteRange(textEditorModel.getSelectionRange());
	}

	public void clearDocument() {
		textEditorModel.clearDocument();
	}

}
