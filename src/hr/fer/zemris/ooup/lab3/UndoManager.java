package hr.fer.zemris.ooup.lab3;

import java.util.ArrayList;
import java.util.Stack;

public class UndoManager {
	private static UndoManager undoManager = new UndoManager();
	private Stack<EditAction> undoStack = new Stack<EditAction>();
	private Stack<EditAction> redoStack = new Stack<EditAction>();
	private ArrayList<UndoObserver> undoObservers = new ArrayList<UndoObserver>();

	private UndoManager() {

	}

	public static UndoManager getUndoManagerInstance() {
		return undoManager;
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			EditAction action = undoStack.pop();
			action.execute_undo();
			redoStack.push(action);
			undoStack.pop();
			notifyUndoObservers();
		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			EditAction action = redoStack.pop();
			action.execute_do();
			notifyUndoObservers();
		}
	}

	public void push(EditAction c) {
		redoStack = new Stack<EditAction>();
		undoStack.push(c);
		notifyUndoObservers();
	}

	public boolean isUndoStackEmpty() {
		return undoStack.isEmpty();
	}

	public boolean isRedoStackEmpty() {
		return redoStack.isEmpty();
	}

	// UndoObservers
	public void registerUndoObserver(UndoObserver o) {
		undoObservers.add(o);
	}

	public void removeUndoObserver(UndoObserver o) {
		int i = undoObservers.indexOf(o);
		if (i >= 0)
			undoObservers.remove(i);
	}

	public void notifyUndoObservers() {
		for (int i = 0; i < undoObservers.size(); i++) {
			UndoObserver observer = undoObservers.get(i);
			observer.updateUndoObserver();
		}
	}
}
