package form;

import javafx.scene.control.Alert;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class Utils {

	static void showAlert(Alert alert, String message) {
		alert.setContentText(message);
		alert.show();
	}

	static void copyToClipboard(String s) {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();

		content.putString(s);
		clipboard.setContent(content);
	}
}
