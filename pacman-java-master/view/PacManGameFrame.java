package view;

import java.awt.Dimension;

import javax.swing.*;

import utils.SoundManager;

public class PacManGameFrame extends JFrame {
	private PacMan pacmanGame;
	private MainMenu mainMenu;
	private OptionsMenu optionsMenu;
	private OptionsMenuInGame optionsMenuInGame;
	private PacManGameFrame frame;

	public PacManGameFrame() {
		super("Pac-Man");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setPreferredSize(new Dimension(960, 720));
		// Create start screen
		mainMenu = new MainMenu();
		pacmanGame = new PacMan(this);
		optionsMenu = new OptionsMenu(this);
		optionsMenuInGame = new OptionsMenuInGame(this);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);

		// Setup main menu actions
		mainMenu.setStartAction(e -> {
//            SoundManager.playBackgroundMusic();
			showGameScreen();
		});
		mainMenu.setOptionsAction(e -> showOptionsScreen());
		mainMenu.setExitAction(e -> System.exit(0));

		// Show main menu first
		showMainMenu();
		setLocationRelativeTo(null);

		// Start background music
		SoundManager.playBackgroundMusic("Classic");
	}

	public void startGame() {
		// Remove start screen

		pacmanGame = new PacMan(frame);
		add(pacmanGame);

		// Refresh frame
		revalidate();
		repaint();

		pacmanGame.requestFocus();
	}

	public void showMainMenu() {
		getContentPane().removeAll();
		add(mainMenu);
		revalidate();
		repaint();
	}

	public void showGameScreen() {
		getContentPane().removeAll();
		pacmanGame = new PacMan(this);
		add(pacmanGame);
		pacmanGame.requestFocus();
		revalidate();
		repaint();
		pacmanGame.startGame();
	}

	public void restartGame() {
		pacmanGame.resetGame();
		showGameScreen();
	}

	public void showOptionsScreen() {
		getContentPane().removeAll();
		add(optionsMenu);
		revalidate();
		repaint();

		// Dừng game khi vào options
	}

	public void showOptionsScreenInGame() {
		getContentPane().removeAll();
		add(optionsMenuInGame);
		revalidate();
		repaint();

		// Dừng game khi vào options
		if (pacmanGame != null) {
			pacmanGame.pauseGame();
		}
	}

	public void returnToGame() {
		getContentPane().removeAll();
		add(pacmanGame);
		revalidate();
		repaint();
		pacmanGame.requestFocus();
	}

}
