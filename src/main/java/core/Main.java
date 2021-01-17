package core;

import form.PasswordGeneratorFormWrapper;
import generation.Generator;
import generation.IGenerator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		IGenerator generator = new Generator();
		primaryStage.getProperties().put("hostServices", this.getHostServices());
		PasswordGeneratorFormWrapper form = new PasswordGeneratorFormWrapper(generator, primaryStage);
		createPasswordGeneratorForm(form);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void createPasswordGeneratorForm(PasswordGeneratorFormWrapper form) {
		form.setAlignment();
		form.setGapBetweenControls(10, 10);
		form.setPadding(25, 25, 25, 25);
		form.initializeScene(840, 600, "form.css");
		form.styleAlertsDialog("alertsDialog.css");
		form.setFormTitle("Password Generator");
		form.createForm();
		form.show();
	}
}
