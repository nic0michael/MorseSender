package za.co.morse.morsesender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LessonMaker {

    public static void makeRandomWords(String filename) {
        try {
            // Load all dictionary words into a list
            List<String> allWords = Files.lines(Paths.get("dictionary.txt"))
                                         .filter(line -> !line.isBlank())
                                         .toList();

            if (allWords.size() < 10) {
                System.out.println("Not enough words in dictionary to proceed.");
                return;
            }

            Random random = new Random();
            StringBuilder output = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                // Pick 10 random words
                Collections.shuffle(allWords, random);
                List<String> randomWords = allWords.subList(0, 10);
                output.append(String.join(" ", randomWords)).append(System.lineSeparator());
            }

            Files.write(Paths.get(filename), output.toString().getBytes());
            System.out.println("Random words written to " + filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeRandomCallsigns(String filename) {
        Random random = new Random();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            List<String> callsigns = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                String prefix = randomLetters(1 + random.nextInt(2), random);
                String number = randomDigits(1 + random.nextInt(2), random);
                String suffix = randomLetters(1 + random.nextInt(3), random);
                callsigns.add(prefix + number + suffix);
            }
            output.append(String.join(" ", callsigns)).append(System.lineSeparator());
        }

        try {
            Files.write(Paths.get(filename), output.toString().getBytes());
            System.out.println("Random callsigns written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String randomLetters(int count, Random random) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            char letter = (char) ('A' + random.nextInt(26));
            sb.append(letter);
        }
        return sb.toString();
    }

    private static String randomDigits(int count, Random random) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
