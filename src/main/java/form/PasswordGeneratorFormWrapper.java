package form;

import generation.IGenerator;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.ButtonType;
import model.PasswordGeneratorForm;
import org.controlsfx.control.CheckComboBox;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordGeneratorFormWrapper extends FormWrapper {
	private final static int MAX_CHARS = 1000;
	private final static String GITHUB_URL = "https://jkonst.github.io/john.konstas/info/";
	private final IGenerator generator;

	public PasswordGeneratorFormWrapper(IGenerator generator, Stage stage) {
		super(stage);
		this.generator = generator;
		this.getAlert().setHeaderText(null);
	}

	public void styleAlertsDialog(String cssResource) {
		DialogPane dialogPane = this.getAlert().getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource(cssResource).toExternalForm());
		dialogPane.getStyleClass().add("myDialog");
	}

	public void setPadding(double top, double right, double bottom, double left) {
		Insets insets = new Insets(top, right, bottom, left);
		super.setPadding(insets);
	}

	public void addTitle(String title) {
		Text sceneTitle = new Text(title);
		sceneTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
		super.addTitle(sceneTitle, 0, 0, 2, 1);
	}

	@Override
	public void createForm() {
		this.addFormLabels();
		this.addFormFields();
		this.addFooter();
	}

	private void addButtons(PasswordGeneratorForm model) {
		this.addButton(model, "Generate", 8, ButtonType.SUBMIT, "submit");
		this.addButton(model, "Copy to Clipboard", 12, ButtonType.COPY_TO_CLIPBOARD, "submit",
				"copy-clipboard");
		this.addButton(model, "Reset", 14, ButtonType.RESET, "submit", "reset");
	}

	private void addFormLabels() {
		super.addLabel("Password Length: ", 1, 0, "Password Length must be between 8 and 20");
		super.addLabel("Special Characters: ", 3, 0, "Pick at least 3 special characters");
		super.addLabel("Text snippet: ", 5, 0, "It must contain at least 8 US-ASCII words \n" +
				"with at least 5 characters. Maximum number of characters: 1000");
		super.addLabel("Generated Password: ", 12, 0, "This is a non-editable field");
	}

	private void addFormFields() {
		ComboBox<? extends Integer> passwordLength = (ComboBox<? extends Integer>) super.
				addComboBox(1, IntStream.rangeClosed(8, 20).boxed().collect(Collectors.toList()));


		CheckComboBox<? extends String> specialChars = (CheckComboBox<? extends String>) super.
				addCheckComboBox(3, getSpecialCharacters());

		TextArea textArea = super.addTextArea(1, 5, MAX_CHARS, true);
		textArea.setPrefWidth(300);
		textArea.setPromptText("Enter a text of maximum 1000 characters");

		this.addSeparator(0, 10);
		PasswordField generated = this.addGeneratedPasswordField();

		PasswordGeneratorForm model = new PasswordGeneratorForm.Builder()
				.withPasswordLength(passwordLength)
				.withSpecialChars(specialChars)
				.withTextArea(textArea)
				.withPasswordField(generated)
				.build();
		this.addButtons(model);
	}

	private void addButton(PasswordGeneratorForm model, String label, int row, ButtonType type, String... cssClasses) {
		Button button = super.addButton(label, cssClasses);
		switch (type) {
			case COPY_TO_CLIPBOARD:
				super.addBox(button, 2, row, 0, Pos.CENTER_RIGHT);
				ActionController copyToClipboardController = new CopyToClipboardController();
				button.setOnAction((event) -> {
					copyToClipboardController.handleAction(model, this.getAlert());
				});
				break;
			case SUBMIT:
				super.addBox(button, 1, row, 10, Pos.CENTER_LEFT);
				ActionController generatePasswordController = new GeneratePasswordController(generator);
				button.setOnAction((event) -> {
					generatePasswordController.handleAction(model, this.getAlert());
				});
				break;
			case RESET:
				super.addBox(button, 1, row, 10, Pos.CENTER_LEFT);
				ActionController resetAllFieldsController = new ResetAllFieldsController();
				button.setOnAction((event) -> {
					resetAllFieldsController.handleAction(model, this.getAlert());
				});
				break;
		}
	}

	private void addSeparator(int column, int row) {
		Separator separator = super.addSeparator();
		separator.setValignment(VPos.CENTER);
		GridPane.setConstraints(separator, column, row);
		GridPane.setColumnSpan(separator, 7);
	}

	private List<Object> getSpecialCharacters() {
		return new ArrayList<>(Arrays.asList("$", "#", "@", "!", "%", "-", "_"));
	}

	private void addFooter() {
		Optional<Label> label = super.addLabel("© " + Year.now().getValue() + " jkonst",
				16,
				1,
				null,
				"copyright");
		label.ifPresent(value -> value.setOnMouseClicked((event -> {
			HostServices services = (HostServices) this.getStage().getProperties().get("hostServices");
			services.showDocument(GITHUB_URL);
		})));
	}

	private PasswordField addGeneratedPasswordField() {
		Image image = new Image(String.valueOf(PasswordGeneratorFormWrapper.class.getResource("eye-password.png")));
		PasswordField passwordField = super.addPasswordField(false);
		TextField textField = super.addTextField(false);
		passwordField.textProperty().bindBidirectional(textField.textProperty());
		Optional<ImageView> imageViewOptional = super.addImageView(image, "eye-password");
		imageViewOptional.ifPresent(imageView -> {
			imageView.setFitHeight(28);
			imageView.setFitWidth(28);
			StackPane.setMargin(imageView, new Insets(0, 10, 0, 0));
			StackPane.setAlignment(imageView, Pos.CENTER_RIGHT);
			imageView.setOnMousePressed((event) -> {
				textField.toFront();
				imageView.toFront();
			});

			imageView.setOnMouseReleased((event) -> {
				passwordField.toFront();
				imageView.toFront();
			});

			StackPane stackPane = super.addStackPane(1, 12, textField, passwordField, imageView);
			stackPane.getStyleClass().add("password-field");
		});
		return passwordField;
	}
}
