package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utils.SoundManager;

public class OptionsMenuInGame extends JPanel {
	private JComboBox<String> musicSelector;
	private JSlider volumeSlider;
	private Image backgroundImage;

	public OptionsMenuInGame(PacManGameFrame frame) {
		setLayout(new GridBagLayout());
		// Load hình nền với chất lượng cao hơn
		backgroundImage = new ImageIcon(getClass().getResource("/images/map_background.jpg")).getImage();
		initUI(frame);
	}

	private void initUI(PacManGameFrame frame) {
		// Panel chính chứa nội dung (để dễ căn chỉnh)
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setOpaque(false); // Trong suốt để hiện background
		contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 50, 15, 50); // Tăng padding

		// Title - Làm nổi bật hơn
		JLabel title = new JLabel("GAME OPTIONS", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 56)); // Tăng kích thước font
		title.setForeground(Color.YELLOW);
		title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		gbc.insets = new Insets(0, 50, 30, 50);
		contentPanel.add(title, gbc);

		// Music Selection - Làm rõ ràng hơn
		JPanel musicPanel = new JPanel(new BorderLayout(10, 5));
		musicPanel.setOpaque(false);

		JLabel musicLabel = new JLabel("BACKGROUND MUSIC:");
		musicLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Font lớn hơn
		musicLabel.setForeground(Color.WHITE);
		musicPanel.add(musicLabel, BorderLayout.NORTH);

		musicSelector = new JComboBox<>(SoundManager.getAvailableTracks());
		musicSelector.setSelectedItem(SoundManager.getCurrentTrack());
		musicSelector.setFont(new Font("Arial", Font.PLAIN, 20)); // Font lớn
		musicSelector.setPreferredSize(new Dimension(300, 40)); // Kích thước lớn
		musicSelector.addActionListener(e -> {
			String selected = (String) musicSelector.getSelectedItem();
			SoundManager.playBackgroundMusic(selected);
		});
		musicPanel.add(musicSelector, BorderLayout.CENTER);
		gbc.insets = new Insets(10, 50, 30, 50); // Tăng khoảng cách dưới
		contentPanel.add(musicPanel, gbc);

		// Volume Control - Chi tiết hơn
		JPanel volumePanel = new JPanel(new BorderLayout(10, 5));
		volumePanel.setOpaque(false);

		JLabel volumeLabel = new JLabel("VOLUME LEVEL:");
		volumeLabel.setFont(new Font("Arial", Font.BOLD, 22));
		volumeLabel.setForeground(Color.WHITE);
		volumePanel.add(volumeLabel, BorderLayout.NORTH);

		volumeSlider = new JSlider(0, 100, (int) (SoundManager.getVolume() * 100));
		volumeSlider.setMajorTickSpacing(25); // Thêm các vạch chia
		volumeSlider.setMinorTickSpacing(5);
		volumeSlider.setPaintTicks(true);
		volumeSlider.setPaintLabels(true);
		volumeSlider.setFont(new Font("Arial", Font.PLAIN, 16));
		volumeSlider.setPreferredSize(new Dimension(400, 60)); // Thanh trượt lớn
		volumeSlider.addChangeListener(e -> {
			float volume = volumeSlider.getValue() / 100f;
			SoundManager.setVolume(volume);
		});
		volumePanel.add(volumeSlider, BorderLayout.CENTER);
		contentPanel.add(volumePanel, gbc);

		// Back Button - Làm nổi bật hơn
		JButton backButton = new JButton("BACK TO GAME");
		backButton.setFont(new Font("Arial", Font.BOLD, 24));
		backButton.setForeground(Color.BLACK);
		backButton.setBackground(Color.YELLOW);
		backButton.setFocusPainted(false);
		backButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
				BorderFactory.createEmptyBorder(10, 30, 10, 30)));
		backButton.addActionListener(e -> frame.returnToGame());

		// Hiệu ứng hover
		backButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				backButton.setBackground(new Color(255, 255, 150));
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				backButton.setBackground(Color.YELLOW);
			}
		});

		gbc.insets = new Insets(40, 50, 0, 50); // Khoảng cách lớn phía trên
		contentPanel.add(backButton, gbc);

		// Thêm contentPanel vào chính giữa
		GridBagConstraints mainGbc = new GridBagConstraints();
		mainGbc.gridx = 0;
		mainGbc.gridy = 0;
		mainGbc.weightx = 1;
		mainGbc.weighty = 1;
		mainGbc.fill = GridBagConstraints.CENTER;
		add(contentPanel, mainGbc);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Vẽ background chất lượng cao
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		// Lớp phủ đen mờ (giảm độ mờ để background rõ hơn)
		g.setColor(new Color(0, 0, 0, 120)); // Giảm alpha từ 150 xuống 120
		g.fillRect(0, 0, getWidth(), getHeight());

		// Thêm viền trang trí
		g.setColor(Color.YELLOW);
		((Graphics2D) g).setStroke(new BasicStroke(5));
		g.drawRoundRect(50, 50, getWidth() - 100, getHeight() - 100, 20, 20);
	}
}