package hr.fer.zemris.ooup.lab3;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class StatusBar implements CursorObserver {
	JLabel statusLabel;
	TextEditorModel textEditorModel;

	public StatusBar(TextEditorModel model) {
		textEditorModel = model;
		textEditorModel.registerCursorObserver(this);
	}

	@Override
	public void updateCursorLocation(Location loc) {
		statusLabel.setText("Position: "
				+ (textEditorModel.getCursorLocation().x + 1) + ":"
				+ (textEditorModel.getCursorLocation().y + 1) + "Lines: "
				+ (textEditorModel.getEndOfDocument().y + 1));
	}

	public void createStatusBar(JFrame frame) {
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		frame.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("Position: "
				+ (textEditorModel.getCursorLocation().x + 1) + ":"
				+ (textEditorModel.getCursorLocation().y + 1) + "Lines: "
				+ (textEditorModel.getEndOfDocument().y + 1));
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
	}

}
