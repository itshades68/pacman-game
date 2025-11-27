package utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;

public class SoundManager {
	private static Clip backgroundMusic;
	private static float volume = 0.5f;
	private static String currentTrack = "";
	private static final LinkedHashMap<String, String> musicTracks = new LinkedHashMap<>();

	static {
		// Khởi tạo danh sách bài hát
		musicTracks.put("Classic", "/sounds/original.wav");
		musicTracks.put("Modern", "/sounds/remix.wav");
		musicTracks.put("xD", "/sounds/alor.wav");
		musicTracks.put("xDD", "/sounds/payphone.wav");
		musicTracks.put("None", "");

	}

	public static void playBackgroundMusic(String trackName) {
		stopBackgroundMusic();

		if (trackName.equals("None")) {
			currentTrack = "";
			return;
		}

		try {
			String path = musicTracks.get(trackName);
			if (path == null)
				path = musicTracks.get("Classic");

			URL url = SoundManager.class.getResource(path);
			if (url != null) {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
				backgroundMusic = AudioSystem.getClip();
				backgroundMusic.open(audioIn);
				backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
				setVolume(volume);
				currentTrack = trackName;
			}
		} catch (Exception e) {
			System.err.println("Lỗi khi phát nhạc: " + e.getMessage());
		}
	}

	public static void stopBackgroundMusic() {
		if (backgroundMusic != null && backgroundMusic.isRunning()) {
			backgroundMusic.stop();
		}
	}

	public static void setVolume(float volume) {
		SoundManager.volume = Math.max(0, Math.min(1, volume));
		if (backgroundMusic != null && backgroundMusic.isOpen()) {
			FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
			float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0f);
			gainControl.setValue(dB);
		}
	}

	public static float getVolume() {
		return volume;
	}

	public static String[] getAvailableTracks() {
		return musicTracks.keySet().toArray(new String[0]);
	}

	public static String getCurrentTrack() {
		return currentTrack;
	}
}