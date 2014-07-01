package hr.fer.zemris.ooup.lab3;

public class InsertStringAction implements EditAction {
	String insertedString;
	TextEditorModel textEditorModel;
	LocationRange stringRange = new LocationRange();

	public InsertStringAction(String insertedString, TextEditorModel model,
			Location stringStart) {
		this.insertedString = insertedString;
		textEditorModel = model;
		stringRange.start = stringStart;
		stringRange.end.y = stringStart.y + insertedString.split("\\r?\\n").length - 1;
		stringRange.end.x = stringStart.x + insertedString.length();
	}

	@Override
	public void execute_do() {
		// TODO Auto-generated method stub
		textEditorModel.setCursorLocation(stringRange.start);
		textEditorModel.insert(insertedString);
	}

	@Override
	public void execute_undo() {
		textEditorModel.setSelectionRange(new LocationRange(new Location(
				stringRange.start.x, stringRange.start.y), new Location(
				stringRange.end.x, stringRange.end.y)));
		textEditorModel.deleteRange(stringRange);

	}

}
