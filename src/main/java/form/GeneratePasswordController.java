package form;

import exceptions.InvalidPasswordLengthException;
import exceptions.InvalidSpecialCharsSizeException;
import exceptions.NotAdequateValidWordsException;
import generation.IGenerator;
import javafx.scene.control.Alert;
import model.PasswordGeneratorForm;

public class GeneratePasswordController implements ActionController {
	private final IGenerator generator;

	public GeneratePasswordController(IGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void handleAction(Object model, Alert alert) {
		PasswordGeneratorForm formModel = (PasswordGeneratorForm) model;
		if (isModelValid(formModel, alert)) {
			try {
				Integer passwordLength = formModel.getPasswordLength().getValue();
				char[] chars = String.join("", formModel.getSpecialChars().getCheckModel().getCheckedItems())
						.toCharArray();
				char[] generated = this.generator.generate(passwordLength, chars, formModel.getUniqueWords(passwordLength));
				if (generated != null && generated.length == passwordLength) {
					alert.setAlertType(Alert.AlertType.INFORMATION);
					Utils.showAlert(alert, "Password generated successfully!");
					formModel.getPasswordField().setText(new String(generated));
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				e.printStackTrace();
				alert.setAlertType(Alert.AlertType.ERROR);
				Utils.showAlert(alert, "Unexpected Error while generating the Password");
			}
		}
	}

	private boolean isModelValid(PasswordGeneratorForm model, Alert alert) {
		try {
			model.validatePasswordLength();
			model.validateSpecialChars();
			model.validateTextArea();
		} catch (InvalidPasswordLengthException | NotAdequateValidWordsException | InvalidSpecialCharsSizeException e) {
			alert.setAlertType(Alert.AlertType.ERROR);
			Utils.showAlert(alert, e.getMessage());
			return false;
		}
		return true;
	}
}