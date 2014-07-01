package hr.fer.zemris.ooup.lab3;

import java.util.ArrayList;

public class SetDocumentAction implements EditAction {
	TextEditorModel textEditorModel;
	ArrayList<String> lines;
	
	public SetDocumentAction(ArrayList<String> lines, TextEditorModel model){
		textEditorModel = model;
		this.lines = new ArrayList<String>(lines);
	}

	@Override
	public void execute_do() {
		textEditorModel.setDocument(new ArrayList<String>(lines));
	}

	@Override
	public void execute_undo() {
		textEditorModel.clearDocument();
	}

}
