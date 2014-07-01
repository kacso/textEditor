package hr.fer.zemris.ooup.lab3.plugins;

import hr.fer.zemris.ooup.lab3.ClipboardStack;
import hr.fer.zemris.ooup.lab3.Location;
import hr.fer.zemris.ooup.lab3.Plugin;
import hr.fer.zemris.ooup.lab3.TextEditorModel;
import hr.fer.zemris.ooup.lab3.UndoManager;

import java.util.Iterator;

public class VelikoSlovo implements Plugin{
	private String name;
	private String description;

	public VelikoSlovo() {
		name = "Veliko slovo";
		description = "Pretvori u velika slova";
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
		// TODO
		Iterator<String> iterator = model.allLines();
		int redak = 0;
		while (iterator.hasNext()) {
			String line = iterator.next();
			String splited[] = line.split(" ");
			int stupac = 0;
			for (int i = 0; i < splited.length; i++) {
				if(splited[i].length() <= 0){
					continue;
				}
				char slovo = splited[i].charAt(0);
				Location location = new Location(stupac, redak);
				model.setCursorLocation(location);
				model.deleteAfter();
				model.insert(Character.toUpperCase(slovo));
				stupac += splited[i].length() + 1;
			}
			redak++;
		}
	}
}
