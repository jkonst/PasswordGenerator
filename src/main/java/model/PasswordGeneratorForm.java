package model;

import exceptions.InvalidPasswordLengthException;
import exceptions.InvalidSpecialCharsSizeException;
import exceptions.NotAdequateValidWordsException;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import org.controlsfx.control.CheckComboBox;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGeneratorForm {

	private PasswordField passwordField;
	private ComboBox<? extends Integer> passwordLength;
	private CheckComboBox<? extends String> specialChars;
	private TextArea textArea;
	private List<String> uniqueWords = new ArrayList<>();

	private static final String SPACE_SEPARATOR = " ";
	public static final Integer MIN_PASSWORD_LENGTH = 8;
	public static final Integer MAX_PASSWORD_LENGTH = 20;
	public static final Integer MIN_WORD_LENGTH = 5;
	public static final Integer MIN_SPECIAL_CHARS_SIZE = 3;

	private PasswordGeneratorForm() {
	}

	private void setUniqueWords(String text) {
		this.uniqueWords = Arrays.stream(text.split(SPACE_SEPARATOR))
				.filter(w -> StandardCharsets.US_ASCII.newEncoder().canEncode(w) && w.length() >= MIN_WORD_LENGTH)
				.distinct()
				.collect(Collectors.toList());
	}

	public List<String> getUniqueWords(int passwordLength) {
		return this.uniqueWords.stream().limit(passwordLength).collect(Collectors.toList());
	}

	public void validatePasswordLength() throws InvalidPasswordLengthException {
		if (passwordLength.getValue() == null || passwordLength.getValue() < MIN_PASSWORD_LENGTH ||
				passwordLength.getValue() > MAX_PASSWORD_LENGTH) {
			throw new InvalidPasswordLengthException("Password length must be from " + MIN_PASSWORD_LENGTH + " to "
					+ MAX_PASSWORD_LENGTH);
		}
	}

	public void validateSpecialChars() throws InvalidSpecialCharsSizeException {
		ObservableList<String> list = (ObservableList<String>) specialChars.getCheckModel().getCheckedItems();
		if (specialChars == null || list == null || list.size() < MIN_SPECIAL_CHARS_SIZE ||
				list.size() > passwordLength.getValue() - 4) {
			throw new InvalidSpecialCharsSizeException("Special characters must be at least " + MIN_SPECIAL_CHARS_SIZE
					+ " and maximum " + (passwordLength.getValue() - 4));
		}
	}

	public void validateTextArea() throws NotAdequateValidWordsException {
		this.setUniqueWords(textArea.getText());
		if (uniqueWords.size() < passwordLength.getValue()) {
			throw new NotAdequateValidWordsException("Less than " + passwordLength.getValue() +
					" valid US_ASCII words found in text area.");
		};
	}

	public PasswordField getPasswordField() {
		return passwordField;
	}

	public ComboBox<? extends Integer> getPasswordLength() {
		return passwordLength;
	}

	public CheckComboBox<? extends String> getSpecialChars() {
		return specialChars;
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public static class Builder {

		private PasswordField passwordField;
		private ComboBox<? extends Integer> passwordLength;
		private CheckComboBox<? extends String> specialChars;
		private TextArea textArea;

		public Builder() {
		}

		public Builder withPasswordField(PasswordField passwordField) {
			this.passwordField = passwordField;
			return this;
		}

		public Builder withPasswordLength(ComboBox<? extends Integer> passwordLength) {
			this.passwordLength = passwordLength;
			return this;
		}

		public Builder withSpecialChars(CheckComboBox<? extends String> specialChars) {
			this.specialChars = specialChars;
			return this;
		}

		public Builder withTextArea(TextArea textArea) {
			this.textArea = textArea;
			return this;
		}

		public PasswordGeneratorForm build() {
			PasswordGeneratorForm model = new PasswordGeneratorForm();
			model.passwordField = this.passwordField;
			model.passwordLength = this.passwordLength;
			model.specialChars = this.specialChars;
			model.textArea = this.textArea;
			return model;
		}
	}
}
