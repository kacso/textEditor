package hr.fer.zemris.ooup.lab3.plugins;

import hr.fer.zemris.ooup.lab3.ClipboardStack;
import hr.fer.zemris.ooup.lab3.Plugin;
import hr.fer.zemris.ooup.lab3.TextEditorModel;
import hr.fer.zemris.ooup.lab3.UndoManager;

import java.util.Iterator;

import javax.swing.JOptionPane;

public class Statistic implements Plugin {
	private String name;
	private String description;

	public Statistic() {
		name = "Statistika";
		description = "Statistika dokumenta";
	}

	public String getName() {
		// ime plugina (za izbornicku stavku)
		return name;
	}

	public String getDescription() {
		// kratki opis
		return description;
	}

	public void execute(TextEditorModel model, UndoManager undoManager,
			ClipboardStack clipboardStack) {
		Iterator<String> iterator = model.allLines();
		int brRedaka = 0;
		int brRijeci = 0;
		int brSlova = 0;
		while (iterator.hasNext()) {
			String line = iterator.next();
			brSlova += line.length();
			brRijeci += line.split(" ").length;
			brRedaka++;
		}
		JOptionPane.showMessageDialog(model.getFrame(), "Broj redaka = "
				+ brRedaka + ", broj rijeèi = " + brRijeci + ", broj slova = "
				+ brSlova);

	}
}
