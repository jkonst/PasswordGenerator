package generation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator implements IGenerator {

	private final static int LAST_CHAR_IDX = 26; // a: 1 z: 26
	private final static int CONVERT_TO_ASCII = 96; // a: 1 + 96 z: 26 + 96 ASCII codes

	@Override
	public char[] generate(int length, char[] specialChars, List<String> uniqueWords) throws Exception {
		char[] password = new char[length];
		int specialCharsWordsNo = uniqueWords.size() - specialChars.length;
		List<String> uniqueWordsForSpecialChars = uniqueWords
				.stream()
				.skip(specialCharsWordsNo)
				.collect(Collectors.toList());
		List<String> remainingUniqueWords = uniqueWords
				.stream()
				.limit(specialCharsWordsNo)
				.collect(Collectors.toList());
		fillPwdWithSpecialChars(password, specialChars, uniqueWordsForSpecialChars);
		fillPwdWithNumbers(password, remainingUniqueWords);
		fillPwdWithChars(password, remainingUniqueWords);
		return password;
	}

	private void fillPwdWithChars(char[] password, List<String> remainingUniqueWords) throws Exception {
		Queue<Character> passwordChars = new LinkedList<>();
		for (String word : remainingUniqueWords) {
			try {
				passwordChars.add(calculatePwdChars(word));
			} catch (Exception e) {
				throw new Exception(e);
			}
		}

		for (int i = 0; i < password.length; i++) {
			if (password[i] == 0 && !passwordChars.isEmpty()) {
				password[i] = passwordChars.peek();
				passwordChars.remove();
			}
		}
	}

	private char calculatePwdChars(String word) {
		int num = calculateSumOfDigits(getSumOfAsciiCodesOfAWord(word));
		while (num > LAST_CHAR_IDX) {
			num = calculateSumOfDigits(num);
		}
		num = convertToValidASCII(num);
		return num % 2 == 0 ? (char) num : Character.toUpperCase((char) num);
	}

	private int convertToValidASCII(int num) {
		return num + CONVERT_TO_ASCII;
	}

	private void fillPwdWithNumbers(char[] password, List<String> remainingUniqueWords) throws Exception {
		List<String> wordsForNumbers = fetchWordsForNumbers(remainingUniqueWords);
		wordsForNumbers.forEach(w -> remainingUniqueWords.remove(w));
		Queue<Integer> passwordNumbers = new LinkedList<>();
		passwordNumbers
				.addAll(wordsForNumbers.stream()
						.map(w -> getSumOfAsciiCodesOfAWord(w))
						.map(n -> n % 10)
						.collect(Collectors.toList()));
		for (int i = 0; i < password.length; i++) {
			if (password[i] == 0 && !passwordNumbers.isEmpty()) {
				password[i] = (char) (passwordNumbers.peek() + '0');
				passwordNumbers.remove();
			}
		}
		if (!passwordNumbers.isEmpty()) {
			throw new Exception("Failed to fill Password with numbers");
		}
	}

	private List<String> fetchWordsForNumbers(List<String> remainingUniqueWords) {
		List<String> wordsForNumbers = new ArrayList<>();
		wordsForNumbers.add(remainingUniqueWords.get(0));
		wordsForNumbers.addAll(
				remainingUniqueWords
						.stream()
						.skip(1)
						.filter(w -> getSumOfAsciiCodesOfAWord(w) % 7 == 0)
						.collect(Collectors.toList())
		);
		return wordsForNumbers;
	}

	private void fillPwdWithSpecialChars(char[] password, char[] specialChars, List<String> words) {
		List<Integer> charIdxs = getSpecialCharsIdxs(words, password.length);
		IntStream
				.range(0, specialChars.length)
				.forEach(i -> password[charIdxs.get(i)] = specialChars[i]);
	}

	private List<Integer> getSpecialCharsIdxs(List<String> words, int passwordLength) {
		List<Integer> charIdxs = new ArrayList<>();
		words
				.stream()
				.map(w -> calculateWordIdx(w, passwordLength))
				.forEach(idx -> fillCharIdxs(charIdxs, idx, passwordLength));
		return charIdxs;
	}

	private void fillCharIdxs(List<Integer> charIdxs, Integer idx, int length) {
		if (charIdxs.stream().anyMatch(i -> idx == i)) {
			fillCharIdxsWithNextAvailable(charIdxs, idx, length);
		} else {
			charIdxs.add(idx);
		}
	}

	private void fillCharIdxsWithNextAvailable(List<Integer> charIdxs, Integer idx, Integer length) {
		boolean foundAvailable = false;
		int count = idx;
		while (!foundAvailable) {
			if (count < length) {
				final int theCount = count;
				if (charIdxs.stream().anyMatch(ci -> ci == theCount)) {
					count++;
				} else {
					charIdxs.add(count);
					foundAvailable = true;
				}
			} else {
				count = 0;
			}
		}
	}

	private int calculateWordIdx(String word, int passwordLength) {
		int sumOfDigits = calculateSumOfDigits(getSumOfAsciiCodesOfAWord(word));
		return sumOfDigits >= passwordLength ? (sumOfDigits % passwordLength) : sumOfDigits;
	}

	private int getSumOfAsciiCodesOfAWord(String word) {
		int sum = 0;
		for (int i = 0; i < word.length(); i++) {
			sum += word.charAt(i);
		}
		return sum;
	}

	private int calculateSumOfDigits(int number) {
		int sum = 0;
		while (number != 0) {
			sum += number % 10;
			number = number / 10;
		}
		return sum;
	}
}
