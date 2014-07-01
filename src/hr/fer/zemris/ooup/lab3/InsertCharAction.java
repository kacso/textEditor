package hr.fer.zemris.ooup.lab3;

public class InsertCharAction implements EditAction {
	char insertedChar;
	TextEditorModel textEditorModel;

	public InsertCharAction(char insertedChar, TextEditorModel model) {
		this.insertedChar = insertedChar;
		this.textEditorModel = model;
	}

	@Override
	public void execute_do() {
		textEditorModel.insert(insertedChar);
	}

	@Override
	public void execute_undo() {
		textEditorModel.deleteBefore();
	}

}
