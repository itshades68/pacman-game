package view;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

import model.Food;
import model.Ghost;
import model.Player;
import model.Wall;
import utils.HighScoreManager;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class PacMan extends JPanel implements ActionListener, KeyListener {
	private int tileSize = 22;
	private GameBoard gameBoard;
	private Timer gameLoop;
	private int score = 0;
	private int lives = 3;
	private boolean gameOver = false;

	private JButton pauseButton;
	private JButton restartButton;
	private JButton optionsButton;
	private JButton exitButton;
	private boolean gamePaused = false;
	private PacManGameFrame parentFrame;

	private HighScoreManager highScoreManager;

	private int currentLevel = 1;
	private int startLevel = 1;
	private final int MAX_LEVEL = 3;
	private boolean gameWon = false;
	private JPanel pauseOverlayPanel;
	
	private List<Integer> getHighScores() {
	    return highScoreManager.getHighScores();
	}

	public PacMan(PacManGameFrame frame) {
		this.parentFrame = frame;
		setPreferredSize(new Dimension(960, 720));
		setBackground(Color.BLACK);
		addKeyListener(this);
		setFocusable(true);

		gameBoard = new GameBoard(tileSize, currentLevel);

		// Khởi chạy game loop với khoảng 50ms (20 FPS)
		gameLoop = new Timer(50, this);
		gameLoop.start();
		initInGameMenu();
		highScoreManager = new HighScoreManager();
	}

	private void checkHighScore() {
		if (score > 0) {
			List<Integer> currentHighs = highScoreManager.getHighScores();
			if (currentHighs.size() < 3 || score > currentHighs.get(currentHighs.size() - 1)) {
				highScoreManager.saveHighScore(score);
			}
		}
	}

	private void initInGameMenu() {
		// Panel chính chứa logo và menu
		JPanel mainMenuPanel = new JPanel();
		mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));
		mainMenuPanel.setOpaque(false);
		mainMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Thêm logo
		JLabel logoLabel = new JLabel();
		try {
			Image logoImage = new ImageIcon(getClass().getResource("/images/pacman_logo.png")).getImage();
			logoImage = logoImage.getScaledInstance(250, 100, Image.SCALE_SMOOTH);
			logoLabel.setIcon(new ImageIcon(logoImage));
		} catch (Exception e) {
			// Fallback nếu không có logo
			logoLabel.setText("PAC-MAN");
			logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
			logoLabel.setForeground(Color.YELLOW);
		}
		logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		mainMenuPanel.add(logoLabel);

		// Panel chứa các nút menu (1 hàng ngang)
		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
		menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
		menuPanel.setOpaque(false);

		// Tạo các nút với style đẹp
		pauseButton = createTextButton("PAUSE");
		restartButton = createTextButton("RESTART");
		optionsButton = createTextButton("OPTIONS");
		exitButton = createTextButton("EXIT");

		// Căn giữa tất cả các nút
		pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm sự kiện
		pauseButton.addActionListener(e -> togglePause());
		restartButton.addActionListener(e -> restartGame());
		optionsButton.addActionListener(e -> parentFrame.showOptionsScreenInGame());
		exitButton.addActionListener(e -> checkHighScore());
		exitButton.addActionListener(e -> System.exit(0));

		// Thêm vào panel với khoảng cách
		menuPanel.add(pauseButton);
		menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		menuPanel.add(restartButton);
		menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		menuPanel.add(optionsButton);
		menuPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		menuPanel.add(exitButton);

		mainMenuPanel.add(menuPanel);

		// Thiết lập layout overlay
		setLayout(new OverlayLayout(this));
		add(mainMenuPanel);

		// Căn chỉnh vị trí
		mainMenuPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		mainMenuPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
	}

	private JButton createTextButton(String text) {
		JButton button = new JButton(text);

		// Thiết lập style
		button.setFont(new Font("Arial", Font.BOLD, 14));
		button.setForeground(Color.BLACK);
		button.setBackground(Color.YELLOW);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		// Hiệu ứng hover
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(new Color(255, 255, 150));
				button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(Color.YELLOW);
				button.setCursor(Cursor.getDefaultCursor());
			}
		});

		return button;
	}

	private void togglePause() {
		gamePaused = !gamePaused;

		if (gamePaused) {
			gameLoop.stop();
			pauseButton.setText("RESUME");
			showPauseOverlay();
		} else {
			gameLoop.start();
			pauseButton.setText("PAUSE");
			hidePauseOverlay();
			requestFocusInWindow();
		}
	}

	private void showPauseOverlay() {
		pauseOverlayPanel = new JPanel(new GridBagLayout());
		pauseOverlayPanel.setOpaque(false);
		pauseOverlayPanel.setName("pauseOverlay");

		JLabel pauseLabel = new JLabel("GAME PAUSED");
		pauseLabel.setFont(new Font("Arial", Font.BOLD, 48));
		pauseLabel.setForeground(Color.YELLOW);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.SOUTH;
		pauseOverlayPanel.add(pauseLabel, gbc);

		add(pauseOverlayPanel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	private void hidePauseOverlay() {
		// Xóa lớp phủ pause
		Component[] components = getComponents();
		for (Component comp : components) {
			if (comp instanceof JPanel && "pauseOverlay".equals(comp.getName())) {
				remove(comp);
				break;
			}
		}
		revalidate();
		repaint();
	}

	private void restartGame() {
		// Xóa panel kết thúc game nếu có
		checkHighScore();
		Component[] components = getComponents();
		for (Component comp : components) {
			if (comp instanceof JPanel && "endGamePanel".equals(comp.getName())) {
				remove(comp);
				break;
			}
		}

		// Reset game state
		gamePaused = false;
		gameOver = false;
		gameWon = false;
		score = 0;
		lives = 3;
		currentLevel = 1;

		// Khởi tạo lại UI
		initInGameMenu();
		gameBoard.resetMap(startLevel);
		resetPositions();

		if (!gameLoop.isRunning()) {
			gameLoop.start();
		}

		requestFocusInWindow();
		revalidate();
		repaint();
	}

	public void startGame() {
		gameOver = false;
		score = 0;
		lives = 3;
		gameBoard.resetMap(startLevel);
		resetPositions();
		gameLoop.start();
	}

	public void resetGame() {
		// Xóa panel kết thúc game nếu có
		Component[] components = getComponents();
		for (Component comp : components) {
			if (comp instanceof JPanel && "endGamePanel".equals(comp.getName())) {
				remove(comp);
				break;
			}
		}

		// Reset game state
		score = 0;
		lives = 3;
		currentLevel = 1;
		gameWon = false;
		gameOver = false;

		// Khởi tạo lại UI
		initInGameMenu();
		gameBoard.resetMap(startLevel);
		resetPositions();

		if (!gameLoop.isRunning()) {
			gameLoop.start();
		}

		requestFocusInWindow();
		revalidate();
		repaint();
	}

	private void loadLevel(int level) {
		currentLevel = level;
//        tileSize = calculateTileSize(level);
		gameBoard = new GameBoard(tileSize, level);
		resetPositions();
		repaint(); // Vẽ lại giao diện ngay lập tức
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Vẽ tường
		for (Wall wall : gameBoard.getWalls()) {
			wall.draw(g);
		}
		// Vẽ thức ăn
		for (Food food : gameBoard.getFoods()) {
			food.draw(g);
		}
		// Vẽ ghost
		for (Ghost ghost : gameBoard.getGhosts()) {
			ghost.draw(g);
		}
		// Vẽ Pac-Man
		gameBoard.getPlayer().draw(g);

		drawGameInfo(g);
		// Hiển thị score và số mạng
	}

	private void drawGameInfo(Graphics g) {
		// Tạo panel thông tin
		int panelHeight = tileSize; // Chiều cao bằng 1 ô
		int panelY = getHeight() - panelHeight;

		// Vẽ nền panel
		g.setColor(new Color(0, 0, 0, 180)); // Màu đen trong suốt
		g.fillRect(0, panelY, getWidth(), panelHeight);

		// Vẽ viền
		g.setColor(Color.YELLOW);
		g.drawLine(0, panelY, getWidth(), panelY);

		// Chuẩn bị thông tin
		String livesText = "Lives: " + lives;
		String scoreText = "Score: " + score;
		String levelText = "Level: " + currentLevel;
//        String highScoreText = "High: " + getHighScore(); // Giả sử có phương thức này

		// Thiết lập font
		Font infoFont = new Font("Press Start 2P", Font.PLAIN, 16);
		g.setFont(infoFont);

		// Vẽ lives (bên trái)
		g.setColor(Color.WHITE);
		g.drawString(livesText, tileSize, panelY + panelHeight / 2 + 6);

		// Vẽ các biểu tượng Pac-Man nhỏ đại diện cho số mạng
		int pacmanSize = tileSize / 2;
		for (int i = 0; i < lives; i++) {
			int x = tileSize + g.getFontMetrics().stringWidth(livesText) + 20 + i * (pacmanSize + 5);
			g.setColor(Color.YELLOW);
			g.fillArc(x, panelY + panelHeight / 2 - pacmanSize / 2, pacmanSize, pacmanSize, 30, 300);
		}

		// Vẽ score (giữa)
		FontMetrics fm = g.getFontMetrics();
		int centerX = getWidth() / 2 - fm.stringWidth(scoreText) / 2;
		g.setColor(Color.WHITE);
		g.drawString(scoreText, centerX, panelY + panelHeight / 2 + 6);

		// Vẽ high score (bên phải)
//        int rightX = getWidth() - tileSize - fm.stringWidth(highScoreText);
//        g.setColor(new Color(255, 215, 0)); // Màu vàng gold
//        g.drawString(highScoreText, rightX, panelY + panelHeight/2 + 6);

//		g.setColor(Color.WHITE);
//		int levelX = getWidth() - levelText.length() - tileSize * 3; // canh phải, cách 1 tileSize từ lề
//		g.drawString(levelText, levelX, panelY + panelHeight / 2 + 6);
		levelText = "Level: " + currentLevel;
		int levelX = getWidth() - fm.stringWidth(levelText) - 20;
		g.drawString(levelText, levelX, panelY + panelHeight/2 + 6);
		drawHighScores(g);

		// Thêm thông báo Victory
		if (gameOver || gameWon) {
//			showEndGameButtons(); // Hiển thị nút khi kết thúc game
			// Vẽ thông báo
			String message = gameWon ? "VICTORY!" : "GAME OVER";
			Color color = gameWon ? Color.GREEN : new Color(255, 50, 50);

			g.setColor(color);
			g.setFont(new Font("Arial", Font.BOLD, 72));

			fm = g.getFontMetrics();
			int messageWidth = fm.stringWidth(message);
			int x = (getWidth() - messageWidth) / 2;
			int y = getHeight() / 2 - 50;

			g.drawString(message, x, y);

			// Vẽ điểm số

//			g.setFont(new Font("Arial", Font.BOLD, 36));
//			fm = g.getFontMetrics();
//			int scoreX = (getWidth() - fm.stringWidth(scoreText)) / 2;
//			g.drawString(scoreText, scoreX, y + 60);
			
			int scoreX = getWidth()/2 - fm.stringWidth(scoreText)/2;
			int scoreY = panelY + panelHeight/2 + 6;

			// Vẽ score
			g.setColor(Color.WHITE);
			g.drawString(scoreText, scoreX, scoreY);

		}
	}
	
	private void drawHighScores(Graphics g) {
	    List<Integer> highs = getHighScores();
	    String highScoreText = "Highscore: ";
	    
	    int panelHeight = tileSize; // Chiều cao bằng 1 ô
		int panelY = getHeight() - panelHeight;
	    
	    if (highs.isEmpty()) {
	        highScoreText += "N/A";
	    } else {
	        for (int i = 0; i < Math.min(3, highs.size()); i++) {
	            if (i > 0) highScoreText += " | ";
	            highScoreText += highs.get(i);
	        }
	    }

	    // Thiết lập font và màu
	    Font highScoreFont = new Font("Press Start 2P", Font.PLAIN, 20);
	    g.setFont(highScoreFont);
	    g.setColor(new Color(255, 215, 0)); // Màu vàng gold

	    // Tính toán vị trí
	    FontMetrics fm = g.getFontMetrics();
	    int x = getWidth() - fm.stringWidth(highScoreText) - 20; // Cách lề phải 20px
	    int y = panelY + panelHeight/2 - 20;

	    // Vẽ nền
	    g.setColor(new Color(0, 0, 0, 150));
	    g.fillRect(x - 5, y - fm.getAscent(), 
	              fm.stringWidth(highScoreText) + 10, 
	              fm.getHeight());

	    // Vẽ text
	    g.setColor(new Color(255, 215, 0));
	    g.drawString(highScoreText, x, y);
	}

	// Phương thức kiểm tra va chạm giữa 2 hình chữ nhật
	public boolean collision(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
	}

	public void moveGame() {
		Player player = gameBoard.getPlayer();
		player.tryApplyPendingDirection(gameBoard);
		int prevX = player.x;
		int prevY = player.y;

		player.move();

		// Kiểm tra va chạm Pac-Man với tường
		for (Wall wall : gameBoard.getWalls()) {
			if (collision(player.x, player.y, player.tileSize, player.tileSize, wall.x, wall.y, wall.tileSize,
					wall.tileSize)) {
				player.x = prevX;
				player.y = prevY;

				// Căn chỉnh vị trí khi va chạm
				if (player.getDirection() == 'U' || player.getDirection() == 'D') {
					player.x = ((player.x + player.tileSize / 2) / player.tileSize) * player.tileSize;
				} else {
					player.y = ((player.y + player.tileSize / 2) / player.tileSize) * player.tileSize;
				}
				break;
			}
		}

		// Di chuyển ghost
		long currentTime = System.currentTimeMillis();
		for (Ghost ghost : gameBoard.getGhosts()) {
			int ghostPrevX = ghost.x;
			int ghostPrevY = ghost.y;

			if (ghost.x % tileSize == 0 && ghost.y % tileSize == 0) {
				// Kiểm tra thời gian trì hoãn nếu cần (nếu bạn muốn ghost bắt đầu đuổi sau 3-4
				// giây)
				long currenTime = System.currentTimeMillis();
				if (currenTime - ghost.getSpawnTime() >= ghost.getChaseDelay()) {
					char nextDir = ghost.strategy.calculateNextDirection(ghost, gameBoard.getPlayer(), gameBoard);
					ghost.updateDirection(nextDir);
				}
			}

			ghost.move();

			// Kiểm tra va chạm ghost với tường
			for (Wall wall : gameBoard.getWalls()) {
				if (collision(ghost.x, ghost.y, tileSize, tileSize, wall.x, wall.y, tileSize, tileSize)) {
					ghost.x = ghostPrevX;
					ghost.y = ghostPrevY;
					char[] directions = { 'U', 'D', 'L', 'R' };
					ghost.updateDirection(directions[new java.util.Random().nextInt(4)]);
				}
			}
		}

		// Kiểm tra va chạm giữa Pac-Man và ghost
		for (Ghost ghost : gameBoard.getGhosts()) {
			if (collision(player.x, player.y, tileSize, tileSize, ghost.x, ghost.y, tileSize, tileSize)) {
				if (ghost.isVulnerable) {
					ghost.reset(); // Quay lại vị trí spawn
					score += 200;
				} else {
					lives--;
					if (lives == 0) {
						gameOver = true;
						gameLoop.stop();
						showEndGameButtons();
						return;
					}
					resetPositions();
				}
				break;
			}
		}

		// Kiểm tra ăn thức ăn
		Food foodEaten = null;
		for (Food food : gameBoard.getFoods()) {
			if (collision(player.x, player.y, tileSize, tileSize, food.x, food.y, food.size, food.size)) {
				foodEaten = food;
				score += food.isPowerPellet ? 50 : 10;
				if (food.isPowerPellet) {
					for (Ghost ghost : gameBoard.getGhosts()) {
						ghost.setVulnerable(true);
					}
					new javax.swing.Timer(5000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent evt) {
							for (Ghost ghost : gameBoard.getGhosts()) {
								ghost.setVulnerable(false);
							}
							((javax.swing.Timer) evt.getSource()).stop();
						}
					}).start();
				}
				break;
			}
		}
		if (foodEaten != null) {
			gameBoard.getFoods().remove(foodEaten);
		}

		// Nếu hết thức ăn, reset bản đồ
		if (gameBoard.getFoods().isEmpty()) {
			if (currentLevel < MAX_LEVEL) {
				currentLevel++;
				gameBoard = new GameBoard(tileSize, currentLevel);
				resetPositions();
			} else {
				gameWon = true;
				gameLoop.stop();
				showEndGameButtons();
			}
		}
	}

	public void resetPositions() {
		gameBoard.getPlayer().reset();
		for (Ghost ghost : gameBoard.getGhosts()) {
			ghost.reset();
			ghost.tileSize = this.tileSize; // Cập nhật tileSize cho ghost
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!gameOver) {
			moveGame();
			repaint();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Khi game over, nhấn phím sẽ reset lại game

		if (gameOver || gameWon || gamePaused) {
			return; // Không xử lý input nữa
		}

		if (gameOver) {
			resetPositions();
			lives = 3;
			score = 0;
			gameOver = false;
			gameLoop.start();
			Container parent = getParent();
			parent.remove(this);
			parent.revalidate();
			parent.repaint();
			return;
		}
		Player player = gameBoard.getPlayer();
		char newDirection = player.getDirection(); // Giữ nguyên hướng hiện tại mặc định

		// Chỉ cập nhật hướng nếu hợp lệ
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			player.setPendingDirection('U');
			if (gameBoard.isDirectionValid(player.x, player.y, 'U')) {
				newDirection = 'U';
			}
			break;
		case KeyEvent.VK_DOWN:
			player.setPendingDirection('D');
			if (gameBoard.isDirectionValid(player.x, player.y, 'D')) {
				newDirection = 'D';
			}
			break;
		case KeyEvent.VK_LEFT:
			player.setPendingDirection('L');
			if (gameBoard.isDirectionValid(player.x, player.y, 'L')) {
				newDirection = 'L';
			}
			break;
		case KeyEvent.VK_RIGHT:
			player.setPendingDirection('R');
			if (gameBoard.isDirectionValid(player.x, player.y, 'R')) {
				newDirection = 'R';
			}
			break;
		}
		if (newDirection != player.getDirection()) {
			player.updateDirection(newDirection);
		}
	}

	private void showEndGameButtons() {
		// Xóa tất cả các component trừ pause overlay
		Component[] components = getComponents();
		for (Component comp : components) {
			if (!(comp instanceof JPanel && "pauseOverlay".equals(comp.getName()))) {
				remove(comp);
			}
		}

		// Tạo panel chứa nút
		JPanel endGamePanel = new JPanel();
		endGamePanel.setLayout(new BoxLayout(endGamePanel, BoxLayout.Y_AXIS));
		endGamePanel.setOpaque(false);
		endGamePanel.setName("endGamePanel");

		// Tạo nút
		restartButton = createTextButton("RESTART");
		exitButton = createTextButton("EXIT");

		// Gán sự kiện
		restartButton.addActionListener(e -> restartGame());
		exitButton.addActionListener(e -> System.exit(0));

		// Căn giữa nút
		restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Thêm nút vào panel với khoảng cách
		endGamePanel.add(restartButton);
		endGamePanel.add(Box.createRigidArea(new Dimension(0, 15)));
		endGamePanel.add(exitButton);

		// Sử dụng OverlayLayout
		setLayout(new OverlayLayout(this));
		add(endGamePanel);

		// Đặt vị trí khoảng 1/3 dưới màn hình
		endGamePanel.setAlignmentX(0.5f); // center
		endGamePanel.setAlignmentY(0.66f); // 2/3 chiều cao

		revalidate();
		repaint();
	}

	public void pauseGame() {
		if (!gamePaused) {
			togglePause();
		}
	}

	public void resumeGame() {
		if (gamePaused) {
			togglePause();
			requestFocusInWindow();
		}
	}

	public boolean isGamePaused() {
		return gamePaused;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}
