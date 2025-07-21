package za.co.morse.morsesender;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class LessonMaker {

    public static void makeRandomWords(String filename) {
        try {
            // ✅ Convert to mutable list to avoid UnsupportedOperationException
            List<String> allWords = new ArrayList<>(
                    Files.lines(Paths.get("dictionary.txt"))
                            .filter(line -> !line.isBlank())
                            .toList()
            );

            if (allWords.size() < 10) {
                System.out.println("Not enough words in dictionary to proceed.");
                return;
            }

            Random random = new Random();
            StringBuilder output = new StringBuilder();

            for (int i = 0; i < 10; i++) {
                Collections.shuffle(allWords, random);
                List<String> randomWords = allWords.subList(0, 10);
                output.append(String.join(" ", randomWords)).append(System.lineSeparator());
            }

            Files.write(Paths.get(filename), output.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
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
            Files.write(Paths.get(filename), output.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Random callsigns written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeRandomGroupsOfFive(String filename) {
        Random random = new Random();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            List<String> groups = new ArrayList<>();

            for (int j = 0; j < 5; j++) {
                List<Character> chars = new ArrayList<>();

                for (int k = 0; k < 4; k++) {
                    chars.add((char) ('A' + random.nextInt(26)));
                }

                char digit = (char) ('0' + random.nextInt(10));
                int insertPos = random.nextInt(5);
                chars.add(insertPos, digit);

                StringBuilder groupBuilder = new StringBuilder();
                for (char c : chars) {
                    groupBuilder.append(c).append("  ");
                }

                groups.add(groupBuilder.toString().trim());
            }

            output.append(String.join("    ", groups)).append(System.lineSeparator());
        }

        try {
            Files.write(Paths.get(filename), output.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Random groups written to " + filename);
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

    // ✅ Method to generate all lessons
    public static void makeAllLessons() {
        for (int i = 1; i <= 9; i++) {
            String filename = "lesson" + i + ".txt";

            try {
                // Clear or create the file
                Files.write(Paths.get(filename), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                System.out.println("Error preparing file: " + filename);
                e.printStackTrace();
                continue;
            }

            // Lesson 1–3: groups of five
            if (i >= 1 && i <= 3) {
                makeRandomGroupsOfFive(filename);
            }

            // Lesson 4–6: callsigns
            if (i >= 4 && i <= 6) {
                makeRandomCallsigns(filename);
            }

            // Lesson 7–9: words
            if (i >= 7 && i <= 9) {
                makeRandomWords(filename);
            }

            System.out.println("✅ Finished generating " + filename);
        }

    }
}
