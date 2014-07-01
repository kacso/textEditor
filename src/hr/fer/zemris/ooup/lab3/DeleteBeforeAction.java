package hr.fer.zemris.ooup.lab3;

public class DeleteBeforeAction implements EditAction {
	Location charLocation;
	char deletedChar;
	TextEditorModel textEditorModel;

	public DeleteBeforeAction(char deletedChar, Location charLocation,
			TextEditorModel model) {
		this.charLocation = charLocation;
		this.deletedChar = deletedChar;
		this.textEditorModel = model;
	}

	@Override
	public void execute_do() {
		textEditorModel.setCursorLocation(charLocation);
		textEditorModel.deleteBefore();
	}

	@Override
	public void execute_undo() {
		textEditorModel.setCursorLocation(charLocation);
		textEditorModel.insert(deletedChar);
	}
}
