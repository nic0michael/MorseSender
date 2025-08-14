package za.co.morse.morsesender;

import java.util.Random;

public class RandomGroups {
    private final StringBuilder message = new StringBuilder();

    public RandomGroups(int rows) {
        for (int i = 0; i < rows; i++) {
            message.append(generateGroup()).append("\n");
        }
    }

    /**
     * Generates a 5-character string with exactly one digit (0-9) placed at a random position,
     * other positions filled with random uppercase letters.
     */
    private String generateGroup() {
        Random rand = new Random();
        char[] group = new char[5];

        // Pick random position for digit (0..4)
        int digitPos = rand.nextInt(5);
        // Generate random digit
        char digit = (char) ('0' + rand.nextInt(10));

        for (int i = 0; i < 5; i++) {
            if (i == digitPos) {
                group[i] = digit;
            } else {
                group[i] = (char) ('A' + rand.nextInt(26));
            }
        }

        return new String(group);
    }

    public String getMessage() {
        return message.toString();
    }
}
