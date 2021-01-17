package form;

import javafx.scene.control.Alert;
import model.PasswordGeneratorForm;

public class CopyToClipboardController implements ActionController {
	@Override
	public void handleAction(Object model, Alert alert) {
		PasswordGeneratorForm formModel = (PasswordGeneratorForm) model;
		try {
			if (formModel.getPasswordField() != null && !formModel.getPasswordField().getText().isEmpty()) {
				alert.setAlertType(Alert.AlertType.INFORMATION);
				Utils.copyToClipboard(formModel.getPasswordField().getText());
				Utils.showAlert(alert, "Password copied to Clipboard!");
			} else {
				alert.setAlertType(Alert.AlertType.WARNING);
				Utils.showAlert(alert, "Nothing to copy");
			}
		} catch (Exception e) {
			e.printStackTrace();
			alert.setAlertType(Alert.AlertType.ERROR);
			Utils.showAlert(alert, "Unexpected Error while copying to Clipboard");
		}
	}
}
