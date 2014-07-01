package hr.fer.zemris.ooup.lab3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu implements ActionListener, UndoObserver, ClipboardObserver,
		TextObserver {
	private ClipboardStack stack;

	/** Menu items */
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem menuItemOpen = new JMenuItem();
	private JMenuItem menuItemSave = new JMenuItem();
	private JMenuItem menuItemExit = new JMenuItem();
	private JMenuItem menuItemCopy = new JMenuItem();
	private JMenuItem menuItemCut = new JMenuItem();
	private JMenuItem menuItemPaste = new JMenuItem();
	private JMenuItem menuItemPasteAndTake = new JMenuItem();
	private JMenuItem menuItemUndo = new JMenuItem();
	private JMenuItem menuItemRedo = new JMenuItem();
	private JMenuItem menuItemDeleteSection = new JMenuItem();
	private JMenuItem menuItemClearDocument = new JMenuItem();
	private JMenuItem menuItemCursorToDocumentStart = new JMenuItem();
	private JMenuItem menuItemCursorToDocumentEnd = new JMenuItem();

	private ArrayList<JMenuItem> menuPluginItems = new ArrayList<JMenuItem>();

	private TextEditor editor;
	private TextEditorModel textEditorModel;

	public Menu(TextEditor editor, TextEditorModel model, ClipboardStack stack) {
		this.editor = editor;
		textEditorModel = model;
		this.stack = stack;
		UndoManager.getUndoManagerInstance().registerUndoObserver(this);
		stack.registerClipboardObserver(this);
		textEditorModel.registerTextObserver(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Copy".equals(e.getActionCommand())) {
			editor.copy();
		} else if ("Cut".equals(e.getActionCommand())) {
			editor.cut();
		} else if ("Paste".equals(e.getActionCommand())) {
			editor.paste();
		} else if ("Paste and Take".equals(e.getActionCommand())) {
			editor.pasteAndTake();
		} else if ("Undo".equals(e.getActionCommand())) {
			editor.undo();
		} else if ("Redo".equals(e.getActionCommand())) {
			editor.redo();
		} else if ("Delete section".equals(e.getActionCommand())) {
			editor.deleteSection();
		} else if ("Clear document".equals(e.getActionCommand())) {
			editor.clearDocument();
		} else if ("Save".equals(e.getActionCommand())) {
			editor.save();
		} else if ("Open".equals(e.getActionCommand())) {
			editor.open();
		} else if ("Exit".equals(e.getActionCommand())) {
			editor.exit();
		} else if ("Cursor to document start".equals(e.getActionCommand())) {
			editor.cursorToDocumentStart();
		} else if ("Cursor to document end".equals(e.getActionCommand())) {
			editor.cursorToDocumentEnd();
		} else {
			int index = menuPluginItems.indexOf(e.getSource());
			
			(textEditorModel.plugins.get(index)).execute(textEditorModel,
					UndoManager.getUndoManagerInstance(),
					ClipboardStack.getClipboardStackInstance());
		}
	}

	public void createMenu(JFrame frame) {
		/** Build File menu */
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menuFile);

		/* a group of File menu */
		// Open
		menuItemOpen = new JMenuItem("Open", KeyEvent.VK_O);
		menuItemOpen.addActionListener(this);
		menuFile.add(menuItemOpen);

		// Save
		menuItemSave = new JMenuItem("Save", KeyEvent.VK_S);
		menuItemSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.save();
			}
		});
		menuFile.add(menuItemSave);

		// Exit
		menuItemExit = new JMenuItem("Exit", KeyEvent.VK_X);
		menuItemExit.addActionListener(this);
		menuFile.add(menuItemExit);

		/** Build Edit menu */
		JMenu menuEdit = new JMenu("Edit");
		menuEdit.setMnemonic(KeyEvent.VK_E);
		menuBar.add(menuEdit);

		/* a group of Edit menu */
		// Undo
		menuItemUndo = new JMenuItem("Undo", KeyEvent.VK_U);
		menuItemUndo.addActionListener(this);
		menuItemUndo.setEnabled(false);
		menuEdit.add(menuItemUndo);

		// Redo
		menuItemRedo = new JMenuItem("Redo", KeyEvent.VK_R);
		menuItemRedo.addActionListener(this);
		menuItemRedo.setEnabled(false);
		menuEdit.add(menuItemRedo);

		// Cut
		menuItemCut = new JMenuItem("Cut", KeyEvent.VK_T);
		menuItemCut.addActionListener(this);
		menuItemCut.setEnabled(false);
		menuEdit.add(menuItemCut);

		// Copy
		menuItemCopy = new JMenuItem("Copy", KeyEvent.VK_C);
		menuItemCopy.addActionListener(this);
		menuItemCopy.setEnabled(false);
		menuEdit.add(menuItemCopy);

		// Paste
		menuItemPaste = new JMenuItem("Paste", KeyEvent.VK_P);
		menuItemPaste.addActionListener(this);
		menuItemPaste.setEnabled(false);
		menuEdit.add(menuItemPaste);

		// Paste and Take
		menuItemPasteAndTake = new JMenuItem("Paste and Take", KeyEvent.VK_T);
		menuItemPasteAndTake.addActionListener(this);
		menuItemPasteAndTake.setEnabled(false);
		menuEdit.add(menuItemPasteAndTake);

		// Delete section
		menuItemDeleteSection = new JMenuItem("Delete section", KeyEvent.VK_D);
		menuItemDeleteSection.addActionListener(this);
		menuItemDeleteSection.setEnabled(false);
		menuEdit.add(menuItemDeleteSection);

		// Clear document
		menuItemClearDocument = new JMenuItem("Clear document", KeyEvent.VK_A);
		menuItemClearDocument.addActionListener(this);
		menuEdit.add(menuItemClearDocument);

		/** Build Move menu */
		JMenu menuMove = new JMenu("Move");
		menuMove.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menuMove);

		/* a group of Edit menu */
		// Cursor to document start
		menuItemCursorToDocumentStart = new JMenuItem(
				"Cursor to document start", KeyEvent.VK_S);
		menuItemCursorToDocumentStart.addActionListener(this);
		menuMove.add(menuItemCursorToDocumentStart);

		// Cursor to document end
		menuItemCursorToDocumentEnd = new JMenuItem("Cursor to document end",
				KeyEvent.VK_E);
		menuItemCursorToDocumentEnd.addActionListener(this);
		menuMove.add(menuItemCursorToDocumentEnd);

		/** Build Plugin menu */
		JMenu menuPlugin = new JMenu("Plugin");
		menuMove.setMnemonic(KeyEvent.VK_P);
		menuBar.add(menuPlugin);

		getPlugins();

		/* Create plugin menu items */
		for (int i = 0; i < textEditorModel.plugins.size(); i++) {
			Plugin plugin = textEditorModel.plugins.get(i);
			JMenuItem item = new JMenuItem(plugin.getName());
			item.addActionListener(this);
			menuPlugin.add(item);
			menuPluginItems.add(item);
		}

		frame.setJMenuBar(menuBar);
	}

	public void getPlugins() {
		File[] files = new File(
				"D:\\Moji Dokumenti\\Dropbox\\Danijel\\Eclipse_workspace\\OOUP_lab3_zad2\\src\\hr\\fer\\zemris\\ooup\\lab3\\plugins")
				.listFiles();

		for (File file : files) {
			if (file.isFile() && file.getName().contains(".java")) {
				String name = file.getName();
				Plugin plugin = TextEditorFactory.newInstance(name.substring(0,
						name.indexOf(".java")));
				textEditorModel.plugins.add(plugin);
			}
		}
	}

	@Override
	public void updateClipboardObserver() {
		if (stack.isEmpty()) {
			menuItemPaste.setEnabled(false);
			menuItemPasteAndTake.setEnabled(false);
		} else {
			menuItemPaste.setEnabled(true);
			menuItemPasteAndTake.setEnabled(true);
		}
	}

	@Override
	public void updateUndoObserver() {
		if (UndoManager.getUndoManagerInstance().isUndoStackEmpty()) {
			menuItemUndo.setEnabled(false);

		} else {
			menuItemUndo.setEnabled(true);
		}
		if (UndoManager.getUndoManagerInstance().isRedoStackEmpty()) {
			menuItemRedo.setEnabled(false);

		} else {
			menuItemRedo.setEnabled(true);
		}
	}

	@Override
	public void updateText() {
		if (textEditorModel.getSelectionRange().start.x < 0) {
			menuItemCopy.setEnabled(false);
			menuItemCut.setEnabled(false);
			menuItemDeleteSection.setEnabled(false);
		} else {
			menuItemCopy.setEnabled(true);
			menuItemCut.setEnabled(true);
			menuItemDeleteSection.setEnabled(true);
		}
	}

}
