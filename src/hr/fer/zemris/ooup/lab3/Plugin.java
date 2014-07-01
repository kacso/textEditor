package hr.fer.zemris.ooup.lab3;

public interface Plugin {
	String getName(); // ime plugina (za izbornicku stavku)

	String getDescription(); // kratki opis

	void execute(TextEditorModel model, UndoManager undoManager,
			ClipboardStack clipboardStack);
}
