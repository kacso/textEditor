package hr.fer.zemris.ooup.lab3;

import java.util.ArrayList;
import java.util.Stack;

public class ClipboardStack {
	private Stack<String> texts = new Stack<String>();
	private ArrayList<ClipboardObserver> clipboardObservers = new ArrayList<ClipboardObserver>();
	private static ClipboardStack stackInstance = new ClipboardStack();

	private ClipboardStack() {

	}

	public static ClipboardStack getClipboardStackInstance() {
		return stackInstance;
	}

	public void push(String text) {
		texts.push(text);
		notifyClipboardObservers();
	}

	public String pop() {
		String text = texts.pop();
		notifyClipboardObservers();
		return text;
	}

	public boolean isEmpty() {
		return texts.empty();
	}

	public String peek() {
		String text = texts.peek();
		notifyClipboardObservers();
		return text;
	}

	public void emptyStack() {
		texts = new Stack<String>();
		notifyClipboardObservers();
	}

	public void registerClipboardObserver(ClipboardObserver observer) {
		clipboardObservers.add(observer);
	}

	public void removeClipboardObserver(ClipboardObserver observer) {
		int i = clipboardObservers.indexOf(observer);
		if (i >= 0)
			clipboardObservers.remove(i);
	}

	public void notifyClipboardObservers() {
		for (int i = 0; i < clipboardObservers.size(); i++) {
			ClipboardObserver observer = clipboardObservers.get(i);
			observer.updateClipboardObserver();
		}
	}
}
