package app;

import javax.swing.SwingUtilities;

import view.PacManGameFrame;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new PacManGameFrame());
	}

}
