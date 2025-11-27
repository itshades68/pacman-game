package model;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

import view.GameBoard;

public class Player {
	public int x, y;
	public int velocityX = 0, velocityY = 0;
	public int tileSize;
	public Image pacmanUpImage, pacmanDownImage, pacmanLeftImage, pacmanRightImage;
	public Image image; // Hình ảnh hiện tại
	public int startX, startY;
	private char currentDirection = 'R';

	public Player(int startX, int startY, int tileSize) {
		this.startX = startX;
		this.startY = startY;
		this.x = startX;
		this.y = startY;
		this.tileSize = tileSize;
		pacmanUpImage = new ImageIcon(getClass().getResource("/images/pacmanUp.png")).getImage();
		pacmanDownImage = new ImageIcon(getClass().getResource("/images/pacmanDown.png")).getImage();
		pacmanLeftImage = new ImageIcon(getClass().getResource("/images/pacmanLeft.png")).getImage();
		pacmanRightImage = new ImageIcon(getClass().getResource("/images/pacmanRight.png")).getImage();
		image = pacmanRightImage;
	}

	public void updateDirection(char direction) {
		int speed = tileSize / 6;
		currentDirection = direction;
		if (direction == 'U') {
			velocityX = 0;
			velocityY = -speed;
			image = pacmanUpImage;
		} else if (direction == 'D') {
			velocityX = 0;
			velocityY = speed;
			image = pacmanDownImage;
		} else if (direction == 'L') {
			velocityX = -speed;
			velocityY = 0;
			image = pacmanLeftImage;
		} else if (direction == 'R') {
			velocityX = speed;
			velocityY = 0;
			image = pacmanRightImage;
		}
	}

	public char getDirection() {
		return currentDirection;
	}

	public void move() {
		x += velocityX;
		y += velocityY;
	}

	public void reset() {
		x = startX;
		y = startY;
		velocityX = 0;
		velocityY = 0;
		image = pacmanRightImage;
	}

	public void draw(Graphics g) {
		g.drawImage(image, x, y, tileSize, tileSize, null);
	}

	private char pendingDirection;

	public void setPendingDirection(char direction) {
		this.pendingDirection = direction;
	}

	public void tryApplyPendingDirection(GameBoard board) {
		if (pendingDirection != ' ' && board.isDirectionValid(x, y, pendingDirection)) {
			updateDirection(pendingDirection);
			pendingDirection = ' '; // Reset sau khi áp dụng
		}
	}

	public boolean isAtCenter() {
		// Kiểm tra xem Pac-Man có ở chính giữa ô không
		return (x % tileSize == tileSize / 2) && (y % tileSize == tileSize / 2);
	}
}
