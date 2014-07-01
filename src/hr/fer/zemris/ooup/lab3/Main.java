package hr.fer.zemris.ooup.lab3;

public class Main {
	public static void main(String[] args) {
		OsnovniGUI osnovniGUI = new OsnovniGUI();
		osnovniGUI.createAndShowGUI();
		TextEditorModel model = new TextEditorModel(
				"Ovo je tekst\r\nkoji je u više redova\n"
						+ "i ima hrvatske znakove!");
		TextEditor editor = new TextEditor(model);
		editor.createAndShowGUI();
	}
}
