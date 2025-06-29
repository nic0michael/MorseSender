package za.co.morse.morsesender;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MorseSender {
    private final ToneGenerator toneGen;
    private int wpm;
    private final int freq; // New field to store frequency

    private static final Map<Character, String> MORSE = new HashMap<>();

    static {
        String[][] data = {
            {"A", ".-"}, {"B", "-..."}, {"C", "-.-."}, {"D", "-.."}, {"E", "."},
            {"F", "..-."}, {"G", "--."}, {"H", "...."}, {"I", ".."}, {"J", ".---"},
            {"K", "-.-"}, {"L", ".-.."}, {"M", "--"}, {"N", "-."}, {"O", "---"},
            {"P", ".--."}, {"Q", "--.-"}, {"R", ".-."}, {"S", "..."}, {"T", "-"},
            {"U", "..-"}, {"V", "...-"}, {"W", ".--"}, {"X", "-..-"}, {"Y", "-.--"},
            {"Z", "--.."}, {"0", "-----"}, {"1", ".----"}, {"2", "..---"},
            {"3", "...--"}, {"4", "....-"}, {"5", "....."}, {"6", "-...."},
            {"7", "--..."}, {"8", "---.."}, {"9", "----."}, {".", ".-.-.-"},
            {",", "--..--"}, {"?", "..--.."}, {"=", "-...-"}, {" ", " "}
        };
        for (String[] pair : data) MORSE.put(pair[0].charAt(0), pair[1]);
    }

    public MorseSender(int freq, int wpm) {
        this.freq = freq;
        this.wpm = wpm;
        this.toneGen = new ToneGenerator(freq);
    }

    public void sender() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nZS6BVR Morse Code Sender");
        System.out.println("Speed: " + wpm + " WPM    Tone: " + freq + " Hz");
        System.out.println("Type message or special key and press ENTER");
        System.out.println("Special keys: * = quit, + = faster, - = slower, @ = dots (scope), # = continuous tone\n");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().toUpperCase().trim();

            switch (input) {
                case "*":
                    return;
                case "+":
                case "++":
                    wpm += 5;
                    System.out.println("WPM: " + wpm);
                    continue;
                case "-":
                case "--":
                    wpm = Math.max(1, wpm - 5);
                    System.out.println("WPM: " + wpm);
                    continue;
                case "@":
                    scopeMode();
                    continue;
                case "#":
                    continuousToneMode();
                    continue;
            }

            int unit = 1200 / wpm;
            System.out.println("Text: " + input);

            for (char ch : input.toCharArray()) {
                String code = MORSE.getOrDefault(ch, "");
                for (char sym : code.toCharArray()) {
                    playSymbol(sym, unit);
                    pause(unit); // Between symbols
                }
                pause(unit * 2); // Between letters
            }
            pause(unit * 4); // Between words
            System.out.println();
        }
    }

    private void playSymbol(char symbol, int unit) {
        if (symbol == '.') toneGen.send(unit);
        else if (symbol == '-') toneGen.send(unit * 3);
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    private void scopeMode() {
        int unit = 1200 / wpm;
        System.out.println("Scope mode: Sending continuous dots. Press any key to stop.");
        try {
            while (true) {
                playSymbol('.', unit);
                pause(unit);
                if (System.in.available() > 0) {
                    System.in.read(); // Consume key
                    break;
                }
            }
        } catch (Exception ignored) {}
    }

    private void continuousToneMode() {
        System.out.println("Continuous tone mode: Press any key to stop.");
        toneGen.send();
        try {
            while (true) {
                if (System.in.available() > 0) {
                    System.in.read(); // Consume key
                    break;
                }
            }
        } catch (Exception ignored) {}
        toneGen.stop();
    }
}
