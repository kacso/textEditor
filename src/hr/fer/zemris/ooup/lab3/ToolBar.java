package hr.fer.zemris.ooup.lab3;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class ToolBar implements ActionListener, UndoObserver,
		ClipboardObserver, TextObserver {
	TextEditor editor;
	Menu menu;
	TextEditorModel textEditorModel;
	JButton undoButton, redoButton, cutButton, copyButton, pasteButton;

	public ToolBar(TextEditor editor, Menu menu, TextEditorModel model) {
		this.editor = editor;
		this.menu = menu;
		this.textEditorModel = model;
		UndoManager.getUndoManagerInstance().registerUndoObserver(this);
		textEditorModel.registerTextObserver(this);
		ClipboardStack.getClipboardStackInstance().registerClipboardObserver(
				this);
	}

	public void createToolBar(JFrame frame) {

		JToolBar toolbar = new JToolBar();

		/** Undo*/
		ImageIcon icon = new ImageIcon(
				"D:\\Moji Dokumenti\\Dropbox\\Danijel\\Eclipse_workspace\\OOUP_lab3_zad2\\src\\hr\\fer\\zemris\\ooup\\lab3\\undo.png");

		icon = scaleImage(icon);

		undoButton = new JButton(icon);
		undoButton.setEnabled(false);
		undoButton.addActionListener(this);
		toolbar.add(undoButton);
		
		/** Redo*/
		icon = new ImageIcon(
				"D:\\Moji Dokumenti\\Dropbox\\Danijel\\Eclipse_workspace\\OOUP_lab3_zad2\\src\\hr\\fer\\zemris\\ooup\\lab3\\redo.png");

		icon = scaleImage(icon);

		redoButton = new JButton(icon);
		redoButton.setEnabled(false);
		redoButton.addActionListener(this);
		toolbar.add(redoButton);
		
		/** Cut*/
		icon = new ImageIcon(
				"D:\\Moji Dokumenti\\Dropbox\\Danijel\\Eclipse_workspace\\OOUP_lab3_zad2\\src\\hr\\fer\\zemris\\ooup\\lab3\\cut.png");

		icon = scaleImage(icon);

		cutButton = new JButton(icon);
		cutButton.setEnabled(false);
		cutButton.addActionListener(this);
		toolbar.add(cutButton);
		
		/** Copy*/
		icon = new ImageIcon(
				"D:\\Moji Dokumenti\\Dropbox\\Danijel\\Eclipse_workspace\\OOUP_lab3_zad2\\src\\hr\\fer\\zemris\\ooup\\lab3\\copy.png");

		icon = scaleImage(icon);

		copyButton = new JButton(icon);
		copyButton.setEnabled(false);
		copyButton.addActionListener(this);
		toolbar.add(copyButton);
		
		/** Paste*/
		icon = new ImageIcon(
				"D:\\Moji Dokumenti\\Dropbox\\Danijel\\Eclipse_workspace\\OOUP_lab3_zad2\\src\\hr\\fer\\zemris\\ooup\\lab3\\paste.png");

		icon = scaleImage(icon);

		pasteButton = new JButton(icon);
		pasteButton.setEnabled(false);
		pasteButton.addActionListener(this);
		toolbar.add(pasteButton);
		
		frame.add(toolbar, BorderLayout.NORTH);

	}

	public ImageIcon scaleImage(ImageIcon icon) {
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(16, 16,
				java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);
		return icon;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == undoButton){
			editor.undo();
		} else if(e.getSource() == redoButton){
			editor.redo();
		} else if(e.getSource() == cutButton){
			editor.cut();
		} else if(e.getSource() == copyButton){
			editor.copy();
		} else if(e.getSource() == pasteButton){
			editor.paste();
		}
		
	}

	@Override
	public void updateClipboardObserver() {
		if (ClipboardStack.getClipboardStackInstance().isEmpty()) {
			pasteButton.setEnabled(false);
		} else {
			pasteButton.setEnabled(true);
		}
	}

	@Override
	public void updateUndoObserver() {
		if (UndoManager.getUndoManagerInstance().isUndoStackEmpty()) {
			undoButton.setEnabled(false);

		} else {
			undoButton.setEnabled(true);
		}
		if (UndoManager.getUndoManagerInstance().isRedoStackEmpty()) {
			redoButton.setEnabled(false);

		} else {
			redoButton.setEnabled(true);
		}
	}

	@Override
	public void updateText() {
		if (textEditorModel.getSelectionRange().start.x < 0) {
			copyButton.setEnabled(false);
			cutButton.setEnabled(false);
		} else {
			copyButton.setEnabled(true);
			cutButton.setEnabled(true);
		}
	}

}
