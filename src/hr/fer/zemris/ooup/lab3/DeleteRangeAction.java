package hr.fer.zemris.ooup.lab3;

public class DeleteRangeAction implements EditAction {
	String deletedString;
	LocationRange stringLocationRange;
	TextEditorModel textEditorModel;

	public DeleteRangeAction(String deletedString,
			LocationRange stringLocationRange, TextEditorModel textEditorModel) {
		this.deletedString = deletedString;
		this.stringLocationRange = stringLocationRange;
		this.textEditorModel = textEditorModel;
	}

	@Override
	public void execute_do() {
		// TODO undo mijenja stringLocationRange
		textEditorModel.setCursorLocation(stringLocationRange.start);
		textEditorModel.setSelectionRange(stringLocationRange);
		textEditorModel.deleteRange(stringLocationRange);
	}

	@Override
	public void execute_undo() {
//		textEditorModel.setCursorLocation(stringLocationRange.start);
		textEditorModel.setCursorLocation(new Location(stringLocationRange.start.x, stringLocationRange.start.y));
		textEditorModel.insert(deletedString);
	}

}
