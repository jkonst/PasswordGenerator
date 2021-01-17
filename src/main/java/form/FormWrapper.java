package form;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class FormWrapper {
	private final GridPane grid;
	private final Stage stage;
	private Alert alert;

	public FormWrapper(Stage stage) {
		this.grid = new GridPane();
		this.stage = stage;
		this.alert = new Alert(Alert.AlertType.NONE);
	}

	public void setAlignment() {
		this.grid.setAlignment(Pos.CENTER);
	}

	public void setGapBetweenControls(double hGap, double vGap) {
		this.grid.setHgap(hGap);
		this.grid.setVgap(vGap);
	}

	protected void setPadding(Insets value) {
		this.grid.setPadding(value);
	}

	protected Separator addSeparator() {
		Separator separator = new Separator();
		this.grid.getChildren().add(separator);
		return separator;
	}

	public void initializeScene(double width, double height, String cssResource) {
		Scene scene = new Scene(grid, width, height);
		if (cssResource != null && cssResource.length() > 0) {
			scene.getStylesheets().add(getClass().getResource(cssResource).toExternalForm());
		}
		this.stage.setScene(scene);
	}

	public void setFormTitle(String title) {
		this.stage.setTitle(title);
	}

	protected void addTitle(Text title, int columnIdx, int rowIdx, int colSpan, int rowSpan) {
		this.grid.add(title, columnIdx, rowIdx, colSpan, rowSpan);
	}

	abstract protected void createForm();

	protected Optional<Label> addLabel(String text, int row, int column, String tooltipText, String... cssClasses) {
		Optional<Label> label = (text != null && !text.isEmpty())
				? Optional.of(new Label(text))
				: Optional.empty();
		if (label.isPresent()) {
			this.applyCssClasses(label.get(), cssClasses);
			label.get().getStyleClass().add("label");
			if (tooltipText != null && tooltipText.length() > 0) {
				label.get().setTooltip(new Tooltip(tooltipText));
			}
			this.grid.add(label.get(), column, row);
		}
		return label;
	}

	protected TextField addTextField(boolean isEditable, String... cssClasses) {
		return this.addTextField(-1, isEditable, cssClasses);
	}

	protected TextField addTextField(int row, boolean isEditable, String... cssClasses) {
		TextField textField = new TextField();
		textField.setEditable(isEditable);
		this.applyCssClasses(textField, cssClasses);
		if (row >= 0) {
			this.grid.add(textField, 1, row);
		}
		return textField;
	}

	protected PasswordField addPasswordField(boolean isEditable, String... cssClasses) {
		return this.addPasswordField(-1, isEditable, cssClasses);
	}

	protected PasswordField addPasswordField(int row, boolean isEditable, String... cssClasses) {
		PasswordField passwordField = new PasswordField();
		passwordField.setEditable(isEditable);
		this.applyCssClasses(passwordField, cssClasses);
		if (row >= 0) {
			this.grid.add(passwordField, 1, row);
		}
		return passwordField;
	}

	protected ComboBox<?> addComboBox(int row, List<Object> values) {
		ComboBox<Object> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(values);
		this.grid.add(comboBox, 1, row);
		return comboBox;
	}

	protected CheckComboBox<?> addCheckComboBox(int row, List<Object> values) {
		CheckComboBox<Object> checkComboBox = new CheckComboBox<>();
		checkComboBox.getItems().addAll(values);
		this.grid.add(checkComboBox, 1, row);
		return checkComboBox;
	}

	protected TextArea addTextArea(int column, int row, int maxChars, boolean wrapText) {
		TextArea textSnippetField = new TextArea();
		textSnippetField.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= maxChars ? change : null)
		);
		textSnippetField.setWrapText(wrapText);
		VBox box = new VBox(textSnippetField);
		this.grid.add(box, column, row);
		return textSnippetField;
	}

	protected Optional<ImageView> addImageView(Image image, String... cssClasses) {
		Optional<ImageView> imageViewOptional = image != null
				? Optional.of(new ImageView(image)) : Optional.empty();
		imageViewOptional.ifPresent(imageView -> this.applyCssClasses(imageView, cssClasses));
		return imageViewOptional;
	}

	protected StackPane addStackPane(int column, int row, Node... nodes) {
		StackPane stackPane = new StackPane(nodes);
		this.grid.add(stackPane, column, row);
		return stackPane;
	}

	protected void addBox(Node node, int column, int row, double spacing, Pos position) {
		HBox hbox = new HBox(spacing);
		hbox.setAlignment(position);
		hbox.getChildren().add(node);
		this.getGrid().add(hbox, column, row);
	}

	public void show() {
		this.stage.show();
	}

	protected Button addButton(String label, String... cssClasses) {
		Button button = new Button(label);
		this.applyCssClasses(button, cssClasses);
		return button;
	}

	public GridPane getGrid() {
		return grid;
	}

	public Stage getStage() {
		return stage;
	}

	public Alert getAlert() {
		return alert;
	}

	private void applyCssClasses(Node node, String... cssClasses) {
		if (cssClasses != null && cssClasses.length > 0) {
			Arrays.stream(cssClasses).forEach(cssClass -> node.getStyleClass().add(cssClass));
		}
	}
}
