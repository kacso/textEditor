package hr.fer.zemris.ooup.lab3;

import java.util.ArrayList;

public class ClearDocumentAction implements EditAction {

	ArrayList<String> lines;
	TextEditorModel textEditorModel;
	
	public ClearDocumentAction(ArrayList<String> lines, TextEditorModel model){
		this.lines = new ArrayList<String>(lines);
		textEditorModel = model;
	}
	
	@Override
	public void execute_do() {
		textEditorModel.clearDocument();
	}

	@Override
	public void execute_undo() {
		textEditorModel.setDocument(lines);
	}

}
