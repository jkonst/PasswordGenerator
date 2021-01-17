package generation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class GeneratorTest {
	private static final int WORD_MIN_SIZE = 5;
	private int passwordLength;
	private char[] specialCharacters;
	private String textInput;
	private static IGenerator passwordGenerator;
	private static final String BASE_PATH = "src/test/resources";
	private static final String SPACE_SEPARATOR = " ";

	@BeforeAll
	public static void setUp() {
		passwordGenerator = new Generator();
	}

	@Test
	public void generateValidPWD1() throws Exception{
		passwordLength = 8;
		specialCharacters = new char[]{'$', '#', '@', '!'};
		textInput = readFile(BASE_PATH + "/athex.txt", StandardCharsets.UTF_8);
		List<String> words = Arrays.asList(textInput.split(SPACE_SEPARATOR));

		List<String> uniqueWords = words.stream()
				.distinct()
				.filter(w -> w.length() >= WORD_MIN_SIZE)
				.limit(passwordLength)
				.collect(Collectors.toList());
		char[] actual = passwordGenerator.generate(passwordLength, specialCharacters, uniqueWords);
		char[] expected = {'2','K','v','@','K','!','$','#'};
		assertEquals(new String(expected), new String(actual));
	}

	@Test
	public void generateValidPWD2() throws Exception{
		passwordLength = 12;
		specialCharacters = new char[]{'$', '#', '@', '!'};
		textInput = readFile(BASE_PATH + "/covid-19.txt", StandardCharsets.UTF_8);
		List<String> words = Arrays.asList(textInput.split(SPACE_SEPARATOR));

		List<String> uniqueWords = words.stream()
				.distinct()
				.filter(w -> w.length() >= WORD_MIN_SIZE)
				.limit(passwordLength)
				.collect(Collectors.toList());
		char[] actual = passwordGenerator.generate(passwordLength, specialCharacters, uniqueWords);
		char[] expected = {'1','5','4','#','!','M','@','$', 'r', 'r', 'S', 't'};
		assertEquals(new String(expected), new String(actual));
	}

	@Test
	public void generateValidPWD3() throws Exception{
		passwordLength = 12;
		specialCharacters = new char[]{'$', '#', '@', '!'};
		textInput = readFile(BASE_PATH + "/spotify.txt", StandardCharsets.UTF_8);
		List<String> words = Arrays.asList(textInput.split(SPACE_SEPARATOR));

		List<String> uniqueWords = words.stream()
				.distinct()
				.filter(w -> w.length() >= WORD_MIN_SIZE)
				.limit(passwordLength)
				.collect(Collectors.toList());
		char[] actual = passwordGenerator.generate(passwordLength, specialCharacters, uniqueWords);
		char[] expected = {'5','8','#','2','8','6','$','!', 'l', '@', 'l', 'U'};
		assertEquals(new String(expected), new String(actual));
	}

	private String readFile(String path, Charset encoding) throws Exception{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
