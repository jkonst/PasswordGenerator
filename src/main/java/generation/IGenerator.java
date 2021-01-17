package generation;

import java.util.List;

@FunctionalInterface
public interface IGenerator {
	char[] generate(int length, char[] specialChars, List<String> uniqueWords) throws Exception;
}
