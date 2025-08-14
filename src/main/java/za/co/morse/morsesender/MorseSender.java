package za.co.morse.morsesender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MorseSender {

    private final ToneGenerator toneGen;
    private int wpm;
    private final int freq;

    // Memory variables mem1 to mem6
    private final String[] mem = new String[7]; // index 1 to 6 used

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
        for (String[] pair : data) {
            MORSE.put(pair[0].charAt(0), pair[1]);
        }
    }

    public MorseSender(int freq, int wpm) {
        this.freq = freq;
        this.wpm = wpm;
        this.toneGen = new ToneGenerator(freq);
        for (int i = 1; i <= 6; i++) {
            mem[i] = "";
        }
    }

    public void sender() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nZS6BVR Morse Code Sender");
        System.out.println("Speed: " + wpm + " WPM    Tone: " + freq + " Hz");
        System.out.println("Type message or special key and press ENTER");
        System.out.println("Special keys: * = quit, + = faster, - = slower, @ = dots (scope), # = tone, #ML = make lessons, #L1–#L9 = play lesson\n");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            // Store memory slot: !1 Hello
            if (input.length() > 2 && input.charAt(0) == '!' && input.charAt(1) >= '1' && input.charAt(1) <= '6' && input.charAt(2) == ' ') {
                int slot = input.charAt(1) - '0';
                mem[slot] = input.substring(3);
                System.out.println("Mem" + slot + " stored.");
                continue;
            }

            // Replace memory: $1
            for (int i = 1; i <= 6; i++) {
                String key = "$" + i;
                if (!mem[i].isEmpty() && input.contains(key)) {
                    input = input.replace(key, mem[i]);
                }
            }

            switch (input.toUpperCase()) {
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
                case "#ML":
                    LessonMaker.makeAllLessons();
                    System.out.println("All lessons created.");
                    continue;
                default:
                    // Handle #L1 to #L9
                    if (input.toUpperCase().matches("#L[1-9]")) {
                        int lessonNumber = Integer.parseInt(input.substring(2));
                        String filename = "lesson" + lessonNumber + ".txt";

                        try {
                            String content = Files.readString(Paths.get(filename));
                            System.out.println("Sending contents of " + filename + " as Morse:\n");
                            playText(content);
                        } catch (IOException e) {
                            System.out.println("⚠️ Could not read " + filename);
                            e.printStackTrace();
                        }
                        continue;
                    }
            }

            // Handle bracket repeater [text]
            input = expandBrackets(input).toUpperCase();

            // Random group: {10}
            if (input.startsWith("{") && input.endsWith("}")) {
                String numberPart = input.substring(1, input.length() - 1);
                if (numberPart.chars().allMatch(Character::isDigit)) {
                    int rows = Integer.parseInt(numberPart);
                    RandomGroups rg = new RandomGroups(rows);
                    input = "\n" + rg.getMessage();
                    System.out.println("Generated:\n" + input);
                }
            }

            int unit = 1200 / wpm;
            System.out.println("Text: " + input);

            for (char ch : input.toCharArray()) {
                if (ch == '\u241E' || ch == '\n' || ch == '\r') {
                    pause(unit * 7);
                    continue;
                }

                String code = MORSE.getOrDefault(ch, "");
                for (char sym : code.toCharArray()) {
                    playSymbol(sym, unit);
                    pause(unit);
                }
                pause(unit * 2);
            }

            pause(unit * 4); // final pause
            System.out.println();
        }
    }

    public int getWpm() {
        return wpm;
    }

    public void playText(String input) {
        int unit = 1200 / wpm;
        input = input.toUpperCase();

        for (char ch : input.toCharArray()) {
            if (ch == '\u241E' || ch == '\n' || ch == '\r') {
                System.out.print("\n");
                pause(unit * 7);
                continue;
            }

            String code = MORSE.getOrDefault(ch, "");
            if (!code.isEmpty()) {
                System.out.print(ch + " "); // 👈 print character being sent
            }

            for (char sym : code.toCharArray()) {
                playSymbol(sym, unit);
                pause(unit);
            }
            pause(unit * 2);
        }

        System.out.println(); // newline after sending
    }

    private void playSymbol(char symbol, int unit) {
        if (symbol == '.') {
            toneGen.send(unit);
        } else if (symbol == '-') {
            toneGen.send(unit * 3);
        }
    }

    private void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    private void scopeMode() {
        int unit = 1200 / wpm;
        System.out.println("Scope mode: Sending continuous dots. Press any key to stop.");
        try {
            while (true) {
                playSymbol('.', unit);
                pause(unit);
                if (System.in.available() > 0) {
                    System.in.read(); // consume key
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void continuousToneMode() {
        System.out.println("Continuous tone mode: Press any key to stop.");
        toneGen.send();
        try {
            while (true) {
                if (System.in.available() > 0) {
                    System.in.read(); // consume key
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        toneGen.stop();
    }

    private String expandBrackets(String input) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) == '[') {
                int end = input.indexOf(']', i);
                if (end > i) {
                    String inside = input.substring(i + 1, end);
                    for (int j = 0; j < 3; j++) {
                        result.append(inside);
                        if (j < 2) {
                            result.append('\u241E');
                        }
                    }
                    i = end + 1;
                } else {
                    result.append('[');
                    i++;
                }
            } else {
                result.append(input.charAt(i));
                i++;
            }
        }
        return result.toString();
    }
}
