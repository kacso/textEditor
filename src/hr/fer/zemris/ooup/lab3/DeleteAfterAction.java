package hr.fer.zemris.ooup.lab3;

public class DeleteAfterAction implements EditAction {
	Location charLocation;
	char deletedChar;
	TextEditorModel textEditorModel;

	public DeleteAfterAction(char deletedChar, Location charLocation, 
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
