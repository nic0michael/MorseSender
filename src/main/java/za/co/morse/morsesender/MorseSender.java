package za.co.morse.morsesender;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MorseSender {

    private final ToneGenerator toneGen;
    private int wpm;
    private int freq;   // 🔹 not final anymore

    private String callsign = "";
    private final String VERSION = "2.1";
    private final int measuredFreq = 1000;
    private final int measuredSpeedmS = 92;
    private double freqCalibrationFactor;
    private double speedCalibrationFactor;
    private int beaconIntervalMs = 60000 / 4;

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
        this.toneGen = new ToneGenerator(freq);
        this.speedCalibrationFactor = (double) 100 / measuredSpeedmS; // 12 wpm test 100mS
        this.freqCalibrationFactor = (double) 1000 / measuredFreq; // 🔹1000Hz initialize calibration        
        this.wpm = wpm;
        System.out.println("Initial WPM: " + wpm);
        for (int i = 1; i <= 6; i++) {
            mem[i] = "";
        }
    }

    
    int calculateMorseSpeed() {
        return (int) (wpm * speedCalibrationFactor);
    }

    public void sender() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n\nZS6BVR Morse Code Sender Version " + VERSION);
        System.out.println("Speed: " + wpm + " WPM    Tone: " + freq + " Hz");
        System.out.println("Calibration Factor: " + freqCalibrationFactor);
        System.out.println("Type message or special key and press ENTER");
        System.out.println("Special keys: * = quit, + = faster, - = slower, @ = dots (scope), # = tone, #ML = make lessons, #L1–#L9 = play lesson");
        System.out.println("Type #H for help\n");

        while (true) {
            // 🔹 Prompt now shows WPM
            System.out.print(wpm + " WPM > ");
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
                case "#H":
                    showHelp();
                    continue;
                case "+":
                    wpm++;
                    System.out.println("WPM: " + wpm);
                    continue;
                case "++":
                    wpm += 5;
                    System.out.println("WPM: " + wpm);
                    continue;
                case "-":
                    wpm--;
                    if (wpm < 1) {
                        wpm = 1;
                    }
                    System.out.println("WPM: " + wpm);
                    continue;
                case "--":
                    wpm -= 5;
                    if (wpm < 1) {
                        wpm = 1;
                    }
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

                case "#B": // Beacon mode
                    if (!callsign.isEmpty()) {
                        System.out.println("Beacon mode: Sending callsign '" + callsign + "' every "
                                + (beaconIntervalMs / 1000) + " seconds.");
                        System.out.println("Press Enter to stop.");
                        try {
                            while (true) {
                                playText(callsign + "  ");
                                pause(beaconIntervalMs);
                                if (System.in.available() > 0) {
                                    System.in.read(); // consume key
                                    break;
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    } else {
                        System.out.println("⚠️ No callsign stored. Use #C first.");
                    }
                    continue;

                case "#C":
                    System.out.print("Enter callsign: ");
                    callsign = scanner.nextLine().trim().toUpperCase();
                    if (!callsign.isEmpty()) {
                        System.out.print("Enter beacon interval in seconds (default 60): ");
                        String inputInterval = scanner.nextLine().trim();
                        if (!inputInterval.isEmpty() && inputInterval.chars().allMatch(Character::isDigit)) {
                            int seconds = Integer.parseInt(inputInterval);
                            if (seconds > 0) {
                                beaconIntervalMs = seconds * 1000;
                            }
                        }
                        System.out.println("Callsign stored: " + callsign
                                + " | Beacon interval: " + (beaconIntervalMs / 1000) + " sec");
                    } else {
                        System.out.println("⚠️ Callsign not stored (empty input).");
                    }
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

            int unit = 1200 / calculateMorseSpeed();
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
        int unit = 1200 / calculateMorseSpeed();
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
        int unit = 1200 / calculateMorseSpeed();
        System.out.println("Scope mode: Sending continuous dots. Speed:" + wpm + " WPM");
        System.out.println("12 WPM → 100.0 ms per dot");
//        System.out.println("24 WPM → 50.0 ms per dot");
//        System.out.println("48 WPM → 25.0 ms per dot");
        System.out.println("Press Enter to stop.");
        try {
            while (true) {
                playSymbol('.', unit);
                pause(unit);
                if (System.in.available() > 0) {
                    System.in.read(); // consume key
                    freq = 800;
                    break;
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void continuousToneMode() {
        // 🔹 Override frequency with measuredFreq
        this.freq = 1000;
        System.out.println("Continuous tone mode: Using " + freq + " Hz (calibrated). Press any key to stop.");
        ToneGenerator tone = new ToneGenerator(freq); // fresh generator with new freq        
        System.out.println("Press Enter to stop.");
        tone.send();
        try {
            while (true) {
                if (System.in.available() > 0) {
                    System.in.read(); // consume key
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        tone.stop();
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

    // 🔹 Help method
    private void showHelp() {
        System.out.println("\nHelp — Command Reference:\n");
        System.out.println("\n\nZS6BVR Morse Code Sender Version " + VERSION);
        System.out.println("  *           Exit the program");
        System.out.println("  + / ++      Increase speed (WPM)");
        System.out.println("  - / --      Decrease speed (WPM)");
        System.out.println("  @           Send continuous dots (scope calibration)");
        System.out.println("  #           Continuous tone (tone calibration, 1000 Hz)");
        System.out.println("  #H          Show this help message");
        System.out.println("  #C          Save your callsign and Beacon delay");
        System.out.println("  #B          Beacon mode — send your callsign every minute");
        System.out.println("  #ML         Regenerate lesson files");
        System.out.println("  #L1–#L9     Play lessons 1–9");
        System.out.println("  [text]      Repeat 'text' 3 times with pauses");
        System.out.println("  {N}         Send N lines of random 5-character groups");
        System.out.println("  !1–!6 text  Store text in memory slot 1–6");
        System.out.println("  $1–$6       Insert stored text from memory slot 1–6");
        System.out.println("  Any other text — Sent as Morse code\n");
    }

}
