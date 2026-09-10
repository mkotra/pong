import javax.sound.sampled.*;

public class SoundEffect {

    public static void playPaddleHit() {
        playTone(520, 50);
    }

    public static void playWallBounce() {
        playTone(330, 35);
    }

    public static void playScore() {
        playTone(180, 180);
    }

    public static void playVictory() {
        new Thread(() -> {
            int[] notes = {440, 554, 659, 880};
            for (int note : notes) {
                playTone(note, 90);
                try {
                    Thread.sleep(95);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public static void playGameOver() {
        new Thread(() -> {
            int[] notes = {350, 300, 250, 200};
            for (int note : notes) {
                playTone(note, 120);
                try {
                    Thread.sleep(130);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    public static void playTone(int hz, int msecs) {
        new Thread(() -> {
            try {
                float sampleRate = 8000f;
                int samples = (int) (sampleRate * msecs / 1000);
                byte[] buf = new byte[samples];
                for (int i = 0; i < buf.length; i++) {
                    double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                    double decay = 1.0 - ((double) i / buf.length);
                    buf[i] = (byte) (Math.sin(angle) * 100.0 * decay);
                }
                AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
                SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                sdl.open(af);
                sdl.start();
                sdl.write(buf, 0, buf.length);
                sdl.drain();
                sdl.close();
            } catch (Exception ignored) {
                // Audio unavailable or headless environment
            }
        }).start();
    }
}
