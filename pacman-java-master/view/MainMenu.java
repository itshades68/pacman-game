package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
	private JButton startButton;
	private JButton optionsButton;
	private JButton exitButton;
	private Image backgroundImage;
	private Image logoImage;
	private static final int MAX_LOGO_WIDTH = 700; // Chiều rộng tối đa
	private static final int MAX_LOGO_HEIGHT = 300; // Chiều cao tối đa

	public MainMenu() {
		setLayout(new GridBagLayout());
		backgroundImage = new ImageIcon(getClass().getResource("/images/map_background.jpg")).getImage();
		Image originalImage = new ImageIcon(getClass().getResource("/images/pacman_logo.png")).getImage();

		// Tính tỷ lệ scale
		double widthRatio = (double) MAX_LOGO_WIDTH / originalImage.getWidth(null);
		double heightRatio = (double) MAX_LOGO_HEIGHT / originalImage.getHeight(null);
		double scaleRatio = Math.min(widthRatio, heightRatio);

		// Tính kích thước mới
		int newWidth = (int) (originalImage.getWidth(null) * scaleRatio);
		int newHeight = (int) (originalImage.getHeight(null) * scaleRatio);

		// Scale ảnh
		logoImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
		initUI();
	}

	private void initUI() {
		setLayout(new GridBagLayout());

		// Panel chứa nội dung chính
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setOpaque(false);

		// Thêm logo thay cho chữ
		if (logoImage != null) {
			JLabel logoLabel = new JLabel(new ImageIcon(logoImage));
			logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			contentPanel.add(logoLabel);
		} else {
			// Fallback nếu không load được ảnh
			JLabel title = new JLabel("PAC-MAN", SwingConstants.CENTER);
			title.setFont(new Font("Arial", Font.BOLD, 72));
			title.setForeground(Color.YELLOW);
			contentPanel.add(title);
		}

		// Thêm khoảng cách
		contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

		// Thêm các nút như bình thường
		startButton = createMenuButton("START GAME");
		optionsButton = createMenuButton("OPTIONS");
		exitButton = createMenuButton("EXIT");

		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		contentPanel.add(startButton);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		contentPanel.add(optionsButton);
		contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		contentPanel.add(exitButton);

		// Thêm contentPanel vào giữa màn hình
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		add(contentPanel, gbc);
	}

	private JButton createMenuButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Arial", Font.BOLD, 28));
		button.setForeground(Color.BLACK);
		button.setBackground(Color.YELLOW);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(250, 60));

		// Hover effect
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(255, 255, 150));
				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(Color.YELLOW);
				button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		});

		return button;
	}

	public void setStartAction(ActionListener action) {
		startButton.addActionListener(action);
	}

	public void setOptionsAction(ActionListener action) {
		optionsButton.addActionListener(action);
	}

	public void setExitAction(ActionListener action) {
		exitButton.addActionListener(action);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Vẽ background
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		} else {
			// Fallback nếu không load được ảnh
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		// Lớp phủ đen mờ để làm nổi bật menu
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}