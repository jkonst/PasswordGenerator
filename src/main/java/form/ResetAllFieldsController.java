package form;

import javafx.scene.control.Alert;
import model.PasswordGeneratorForm;

public class ResetAllFieldsController implements ActionController {
	@Override
	public void handleAction(Object model, Alert alert) {
		PasswordGeneratorForm formModel = (PasswordGeneratorForm) model;
		try {
			formModel.getPasswordLength().setValue(null);
			formModel.getTextArea().setText("");
			formModel.getPasswordField().setText("");
			formModel.getSpecialChars().getCheckModel().clearChecks();
		} catch (Exception e) {
			e.printStackTrace();
			alert.setAlertType(Alert.AlertType.ERROR);
			Utils.showAlert(alert, "Unexpected Error while resetting all fields");
		}
	}
}
