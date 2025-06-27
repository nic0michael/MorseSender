package za.co.morse.morsesender;

import javax.sound.sampled.*;

public class ToneGenerator {
    private final int frequency;
    private final AudioFormat format;
    private SourceDataLine line;
    private volatile boolean keepRunning = false;

    public ToneGenerator(int frequency) {
        this.frequency = frequency;
        this.format = new AudioFormat(44100, 16, 1, true, false);
    }

    // Send tone for a specified duration (used for Morse dots/dashes)
    public void send(int durationMs) {
        try {
            byte[] tone = generateTone(durationMs);
            try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
                line.open(format);
                line.start();
                line.write(tone, 0, tone.length);
                line.drain();
                line.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Send tone continuously until stop() is called (used for # mode)
    public void send() {
        try {
            line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();
            keepRunning = true;

            Thread thread = new Thread(() -> {
                byte[] buffer = generateTone(100); // small looped chunk
                while (keepRunning) {
                    line.write(buffer, 0, buffer.length);
                }
                line.drain();
                line.stop();
                line.close();
            });
            thread.setDaemon(true);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        keepRunning = false;
    }

    private byte[] generateTone(int durationMs) {
        int sampleRate = 44100;
        int samples = (int) (sampleRate * durationMs / 1000.0);
        byte[] buffer = new byte[samples * 2];

        for (int i = 0; i < samples; i++) {
            double angle = 2.0 * Math.PI * frequency * i / sampleRate;
            short sample = (short) (Math.sin(angle) * Short.MAX_VALUE);
            buffer[2 * i] = (byte) (sample & 0xFF);
            buffer[2 * i + 1] = (byte) ((sample >> 8) & 0xFF);
        }

        return buffer;
    }
}
