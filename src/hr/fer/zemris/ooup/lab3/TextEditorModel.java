package hr.fer.zemris.ooup.lab3;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;

public class TextEditorModel {
	protected final int xSpace = 30;
	protected final int ySpace = 5;
	protected final int fontHight = 20;
	protected final int editorWidth = 750;
	protected JFrame frame = new JFrame();
	protected ArrayList<Plugin> plugins = new ArrayList<Plugin>();

	private ArrayList<String> lines = new ArrayList<String>();
	private LocationRange selectionRange = new LocationRange();
	private Location cursorLocation = new Location();
	private ArrayList<CursorObserver> cursorObservers = new ArrayList<CursorObserver>();
	private ArrayList<TextObserver> textObservers = new ArrayList<TextObserver>();

	public TextEditorModel(String text) {
		String splitedText[] = text.split("\\r?\\n");
		for (int i = 0; i < splitedText.length; i++) {
			lines.add(splitedText[i]);
		}
		cursorLocation.x = 0;
		cursorLocation.y = 0;
		Location l = new Location();
		l.x = -1;
		l.y = -1;
		selectionRange.start = selectionRange.end = l;
	}

	public Location getCursorLocation() {
		return cursorLocation;
	}

	public void setCursorLocation(Location location) {
		cursorLocation = location;
		cursorPositionChange();
	}

	// get iterator
	public Iterator<String> allLines() {
		return linesRange(0, lines.size());
	}

	public Iterator<String> linesRange(int index1, int index2) {
		ArrayList<String> trimedLines = new ArrayList<String>();
		for (int i = index1; i < index2; i++) {
			trimedLines.add(lines.get(i));
		}
		Iterator<String> iterator = trimedLines.iterator();
		return iterator;
	}

	// CursorObservers
	public void registerCursorObserver(CursorObserver o) {
		cursorObservers.add(o);
	}

	public void removeCursorObserver(CursorObserver o) {
		int i = cursorObservers.indexOf(o);
		if (i >= 0)
			cursorObservers.remove(i);
	}

	public void notifyCursorObservers() {
		for (int i = 0; i < cursorObservers.size(); i++) {
			CursorObserver observer = cursorObservers.get(i);
			observer.updateCursorLocation(cursorLocation);
		}
	}

	public void cursorPositionChange() {
		notifyCursorObservers();
	}

	// Change cursor location
	public void moveCursorLeft() {
		if (cursorLocation.x > 0) {
			cursorLocation.x -= 1;
			cursorPositionChange();
		} else if (cursorLocation.y > 0) {
			cursorLocation.y--;
			cursorLocation.x = lines.get(cursorLocation.y).length();
			cursorPositionChange();
		}
	}

	public void moveCursorRight() {
		if (cursorLocation.x < lines.get(cursorLocation.y).length()) {
			cursorLocation.x += 1;
			cursorPositionChange();
		} else if (cursorLocation.y < lines.size() - 1) {
			cursorLocation.x = 0;
			cursorLocation.y++;
		}
	}

	public void moveCursorUp() {
		if (cursorLocation.y > 0) {
			cursorLocation.y -= 1;
			if (cursorLocation.x > lines.get(cursorLocation.y).length()) {
				cursorLocation.x = lines.get(cursorLocation.y).length();
			}
			cursorPositionChange();
		}
	}

	public void moveCursorDown() {
		if (cursorLocation.y < lines.size() - 1) {
			cursorLocation.y += 1;
			if (cursorLocation.x > lines.get(cursorLocation.y).length()) {
				cursorLocation.x = lines.get(cursorLocation.y).length();
			}
			cursorPositionChange();
		}
	}

	public void moveCursorToBegin(int y) {
		cursorLocation.y = y;
		cursorLocation.x = 0;
	}

	// TextObservers
	public void registerTextObserver(TextObserver o) {
		textObservers.add(o);
	}

	public void removeTextObserver(TextObserver o) {
		int i = textObservers.indexOf(o);
		if (i >= 0)
			textObservers.remove(i);
	}

	public void notifyTextObservers() {
		for (int i = 0; i < textObservers.size(); i++) {
			TextObserver observer = textObservers.get(i);
			observer.updateText();
		}
	}

	public void textChanged() {
		notifyTextObservers();
	}

	// delete text
	// crusorLocation.y = redak
	// cursorLocation.x = slovo u nizu
	public void deleteBefore() {
		if (cursorLocation.y < lines.size() && cursorLocation.x > 0
				&& cursorLocation.x <= lines.get(cursorLocation.y).length()) {
			EditAction action = new DeleteBeforeAction(lines.get(
					cursorLocation.y).charAt(cursorLocation.x - 1),
					cursorLocation, this);
			UndoManager.getUndoManagerInstance().push(action);
			StringBuilder sb = new StringBuilder(lines.get(cursorLocation.y));
			sb.deleteCharAt(cursorLocation.x - 1);
			lines.set(cursorLocation.y, sb.toString());
			moveCursorLeft();
			textChanged();
		} else if (cursorLocation.y > 0 && cursorLocation.y < lines.size()
				&& cursorLocation.x == 0) {
			EditAction action = new DeleteBeforeAction('\n', cursorLocation,
					this);
			UndoManager.getUndoManagerInstance().push(action);
			StringBuilder sb = new StringBuilder(
					lines.get(cursorLocation.y - 1)
							+ lines.get(cursorLocation.y));
			lines.set(cursorLocation.y - 1, sb.toString());
			lines.remove(cursorLocation.y);
			moveCursorLeft();
			textChanged();
		}
	}

	public void deleteAfter() {
		if (cursorLocation.y < lines.size() && cursorLocation.x >= 0
				&& cursorLocation.x < lines.get(cursorLocation.y).length()) {
			EditAction action = new DeleteAfterAction(lines.get(
					cursorLocation.y).charAt(cursorLocation.x), cursorLocation,
					this);
			UndoManager.getUndoManagerInstance().push(action);
			StringBuilder sb = new StringBuilder(lines.get(cursorLocation.y));
			sb.deleteCharAt(cursorLocation.x);
			lines.set(cursorLocation.y, sb.toString());
			textChanged();
		} else if (cursorLocation.y < lines.size() - 1
				&& cursorLocation.x == lines.get(cursorLocation.y).length()) {
			EditAction action = new DeleteAfterAction('\n', cursorLocation,
					this);
			UndoManager.getUndoManagerInstance().push(action);
			StringBuilder sb = new StringBuilder(lines.get(cursorLocation.y)
					+ lines.get(cursorLocation.y + 1));
			lines.set(cursorLocation.y, sb.toString());
			lines.remove(cursorLocation.y + 1);
			textChanged();
		}
	}

	public void deleteRange(LocationRange r) {
		String selectedText = getSelectedText();
		if (selectionRange.start.y == selectionRange.end.y) {
			StringBuilder sb = new StringBuilder(
					lines.get(selectionRange.start.y));
			sb.delete(selectionRange.start.x, selectionRange.end.x);
			lines.set(cursorLocation.y, sb.toString());
			if (lines.get(cursorLocation.y).length() == 0) {
				lines.remove(cursorLocation.y);
				selectionRange.start.y--;
				selectionRange.end.y--;
			}
		} else {
			StringBuilder sb = new StringBuilder(
					lines.get(selectionRange.start.y));
			sb.delete(selectionRange.start.x, lines.get(selectionRange.start.y)
					.length());
			lines.set(selectionRange.start.y, sb.toString());
			if (lines.get(selectionRange.start.y).length() == 0) {
				lines.remove(selectionRange.start.y);
				selectionRange.start.y--;
				selectionRange.end.y--;
			}
			for (int i = selectionRange.start.y + 1; i <= selectionRange.end.y; i++) {
				sb = new StringBuilder(lines.get(i));
				if (i == selectionRange.end.y) {
					sb.delete(0, selectionRange.end.x);
					lines.set(selectionRange.end.y, sb.toString());
					if (lines.get(i).length() == 0) {
						lines.remove(i);
						selectionRange.end.y--;
						selectionRange.start.x--;
					}
					break;
				} else {
					sb.delete(0, lines.get(i).length());
					lines.set(i, sb.toString());
					if (lines.get(i).length() == 0) {
						lines.remove(i);
						selectionRange.end.y--;
					}
				}
			}
		}
		Location location = new Location();

		if (selectionRange.start.y >= 0) {
			cursorLocation.y = selectionRange.start.y;
		} else {
			cursorLocation.y = 0;
		}
		if (selectionRange.start.x >= 0) {
			cursorLocation.x = selectionRange.start.x;
		} else {
			cursorLocation.x = lines.get(cursorLocation.y).length();
		}
		location.x = location.y = -1;
		selectionRange.start = selectionRange.end = location;
		LocationRange range = new LocationRange(new Location(cursorLocation.x,
				cursorLocation.y), new Location(cursorLocation.x,
				cursorLocation.y));

		EditAction action = new DeleteRangeAction(selectedText, range, this);
		UndoManager.getUndoManagerInstance().push(action);
		textChanged();
	}

	// Use selectionRange variable
	public LocationRange getSelectionRange() {
		return selectionRange;
	}

	public void setSelectionRange(LocationRange range) {
		selectionRange = range;
		textChanged();
	}

	// Insert character
	public void insert(char c) {
		EditAction action = new InsertCharAction(c, this);
		UndoManager.getUndoManagerInstance().push(action);
		if (selectionRange.start.x >= 0) {
			deleteRange(selectionRange);
		}
		if (c == '\n') {
			if (lines.get(cursorLocation.y).length() > cursorLocation.x) {
				StringBuilder sb = new StringBuilder(
						lines.get(cursorLocation.y));
				sb.insert(cursorLocation.x, c);
				lines.set(cursorLocation.y, sb.toString());
				String s[] = lines.get(cursorLocation.y).split("\\r?\\n");
				lines.set(cursorLocation.y, s[0]);
				if (lines.size() > cursorLocation.y) {
					lines.add(cursorLocation.y + 1, s[1]);
				} else {
					lines.add(cursorLocation.y, s[1]);
				}
			} else {
				if (lines.size() > cursorLocation.y) {
					lines.add(cursorLocation.y + 1, "");
				} else {
					lines.add(cursorLocation.y, "");
				}
			}
			moveCursorDown();
			moveCursorToBegin(cursorLocation.y);
			textChanged();
		} else {
			StringBuilder sb = new StringBuilder(lines.get(cursorLocation.y));
			sb.insert(cursorLocation.x, c);
			lines.set(cursorLocation.y, sb.toString());
			moveCursorRight();
			textChanged();
		}
	}

	// Insert string
	public void insert(String text) {
		EditAction action = new InsertStringAction(text, this, new Location(
				cursorLocation.x, cursorLocation.y));
		UndoManager.getUndoManagerInstance().push(action);

		if (selectionRange.start.x >= 0) {
			deleteRange(selectionRange);
		}
		String s[] = text.split("\\r?\\n");
		if (s.length > 1) {
			String endSubstring = lines.get(cursorLocation.y).substring(
					cursorLocation.x);
			String startSubstring = lines.get(cursorLocation.y).substring(0,
					cursorLocation.x);
			lines.set(cursorLocation.y, startSubstring + s[0]);
			for (int i = 1; i < s.length; i++) {
				if (lines.size() > cursorLocation.y + i) {
					lines.add(cursorLocation.y + i, s[i]);
				} else {
					lines.add(s[i]);
				}
			}
			lines.add(cursorLocation.y + s.length, endSubstring);
		} else {
			StringBuilder sb = new StringBuilder(lines.get(cursorLocation.y));
			sb.insert(cursorLocation.x, text);
			lines.set(cursorLocation.y, sb.toString());
		}
		for (int i = 0; i < text.length(); i++) {
			moveCursorRight();
		}
		textChanged();
	}

	public String getSelectedText() {
		LocationRange range = getSelectionRange();
		Iterator<String> iterator = linesRange(range.start.y, range.end.y + 1);
		String text = "";
		int i = 0;
		if (iterator.hasNext()) {
			text += iterator.next();
			if (range.start.y == range.end.y) {
				text = text.substring(range.start.x, range.end.x) + '\n';
			} else {
				text = text.substring(range.start.x, text.length()) + '\n';
			}
			i++;
			while (iterator.hasNext()) {
				if (i == range.end.y) {
					text += iterator.next().substring(0, range.end.x);
				} else {
					text += iterator.next() + '\n';
				}
				i++;
			}
		}
		return text;
	}

	public void clearDocument() {
		EditAction action = new ClearDocumentAction(
				new ArrayList<String>(lines), this);
		UndoManager.getUndoManagerInstance().push(action);

		lines = new ArrayList<String>();
		lines.add("");
		cursorLocation.x = 0;
		cursorLocation.y = 0;
		textChanged();
	}

	public void setDocument(ArrayList<String> l) {
		EditAction action = new SetDocumentAction(new ArrayList<String>(lines),
				this);
		UndoManager.getUndoManagerInstance().push(action);
		lines = new ArrayList<String>(l);
		textChanged();
	}

	public Location getEndOfDocument() {
		Location location = new Location();
		location.y = lines.size() - 1;
		location.x = lines.get(location.y).length();
		return location;
	}

	public JFrame getFrame() {
		return frame;
	}
}
