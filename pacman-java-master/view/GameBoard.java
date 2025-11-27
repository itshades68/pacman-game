package view;

import java.awt.Point;
import java.util.*;
import javax.swing.ImageIcon;

import model.Food;
import model.Ghost;
import model.Player;
import model.Wall;
import strategy.BlueGhostStrategy;
import strategy.OrangeGhostStrategy;
import strategy.PinkGhostStrategy;
import strategy.RedGhostStrategy;

import java.awt.Image;
import java.awt.Color;

public class GameBoard {
	public int rowCount;
	public int columnCount;
	public int tileSize;

	private List<Wall> walls;
	private List<Food> foods;
	private List<Ghost> ghosts;
	private Player player;

	private String[] tileMap;

	// Lưu ghost đỏ để BlueGhostStrategy sử dụng
	private Ghost redGhost;

	public GameBoard(int tileSize, int level) {
		this.tileSize = tileSize;
		walls = new ArrayList<>();
		foods = new ArrayList<>();
		ghosts = new ArrayList<>();
		loadMap(level);
	}

	private void loadMap(int level) {
    	
		switch (level) {
        case 1 -> {
            rowCount = 15;
            columnCount = 15;
            tileMap = new String[]{
            		    "XXXXXXXXXXXXXXX",
            		    "X     X     X X",
            		    "X XXX X XXX X X",
            		    "X X  O    X   X",
            		    "X X XXX X XXX X",
            		    "X X         X X",
            		    "X   XXnnnXX X X",
            		    "XXX  XrnoX    X",
            		    "X X XXXXXXXXX X",
            		    "X     XP    X X",
            		    "X XXX X XXX X X",
            		    "X   X         X",
            		    "X XXX XXX XXX X",
            		    "X        O  X X",
            		    "XXXXXXXXXXXXXXX"
            };
        }
        default -> {
        	 rowCount = 30;
             columnCount = 30;
             tileMap = new String[]{
            		"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
             	    "XX   OXX              XXO   XX",
             	    "XX XX XX XX XXXXXX XX XX XX XX",
             	    "XX XX XX XX XXXXXX XX XX XX XX",
             	    "XX XX    XX   XX   XX    XX XX",
             	    "XX XXXXX XXXX XX XXXX XXXXX XX",
             	    "XX XXXXX XXXX XX XXXX XXXXX XX",
             	    "XX                          XX",
             	    "XX XX XXXX XXXXXXXX XXXX XX XX",
             	    "XX XX XXXX XXXXXXXX XXXX XX XX",
             	    "XX XX XXXX XXXXXXXX XXXX XX XX",
             	    "XX XXXX XX          XX XXXX XX",
             	    "XX XXXX XX XXnnnnXX XX XXXX XX",
             	    "XX         XXrnnpXX         XX",
             	    "XXXX XXXXX XXonnbXX XXXXX XXXX",
             	    "XXXXOXXXXX XXXXXXXX XXXXXOXXXX",
             	    "XX      XX XXXXXXXX XX      XX",
             	    "XX XXXX XX          XX XXXX XX",
             	    "XX XXXX XX XXXXXXXX XX XXXX XX",
             	    "XX            XX            XX",
             	    "XX XX XXXXXXX XX XXXXXXX XX XX",
             	    "XX XX XXXXXXX XX XXXXXXX XX XX",
             	    "XX XX      XX  P XX      XX XX",
             	    "XX XXXX XX XX XX XX XX XXXX XX",
             	    "XX XXXX XX XX XX XX XX XXXX XX",
             	    "XX XX   XX    XX    XX   XX XX",
             	    "XX XX XXXX XXXXXXXX XXXX XX XX",
             	    "XX XX XXXX XXXXXXXX XXXX XX XX",
             	    "XX   O                  O   XX",
             	    "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
             	};
        }
        case 2 -> {
            rowCount = 30;
            columnCount = 30;
            tileMap = new String[]{
            "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    	    "XX   O                  O   XX",
    	    "XX XX XXXX XXXXXXXX XXXX XX XX",
    	    "XX XX XXXX XXXXXXXX XXXX XX XX",
    	    "XX XX XXXX XX    XX XXXX XX XX",
    	    "XX XX      XX XX XX      XX XX",
    	    "XX XXXX XX XX XX XX XX XXXX XX",
    	    "XX      XX    XX    XX      XX",
    	    "XXXX XXXXX XXXXXXXX XXXXX XXXX",
    	    "XXXX XXXXX XXXXXXXX XXXXX XXXX",
    	    "XX                          XX",
    	    "XXXX XXXXX XXnnnnXX XXXXX XXXX",
    	    "XXXX XXXXX XXrnnpXX XXXXX XXXX",
    	    "XX      XX XXonnbXX XX      XX",
    	    "XXXX XX XX XXXXXXXX XX XX XXXX",
    	    "XXXX XXOXX XXXXXXXX XXOXX XXXX",
    	    "XX   XX                XX   XX",
    	    "XX XXXXXXX XXXXXXXX XXXXXXX XX",
    	    "XX XXXXXXX XXXXXXXX XXXXXXX XX",
    	    "XX            XX            XX",
    	    "XXXX XXXXXXXX XX XXXXXXXX XXXX",
    	    "XXXX XXXXXXXX XX XXXXXXXX XXXX",
    	    "XX         XX  P XX         XX",
    	    "XX XXXX XX XX XX XX XX XXXX XX",
    	    "XX XXXX XX XX XX XX XX XXXX XX",
    	    "XX XX   XX    XX    XX   XX XX",
    	    "XX XX XXXX XXXXXXXX XXXX XX XX",
    	    "XX XX XXXX XXXXXXXX XXXX XX XX",
    	    "XX   O                  O   XX",
    	    "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
            };
        }
    }

	for(

	int r = 0;r<rowCount;r++)
	{
		String row = tileMap[r];
		for (int c = 0; c < columnCount; c++) {
			char ch = row.charAt(c);
			int x = c * tileSize;
			int y = r * tileSize;

			if (ch == 'X') {
				walls.add(new Wall(x, y, tileSize));
			} else if (ch == 'b') { // Blue ghost
				Image blueGhostImage = new ImageIcon(getClass().getResource("/images/blueGhost.png")).getImage();
				ghosts.add(new Ghost(x, y, blueGhostImage, tileSize, new BlueGhostStrategy()));
			} else if (ch == 'o') { // Orange ghost
				Image orangeGhostImage = new ImageIcon(getClass().getResource("/images/orangeGhost.png")).getImage();
				ghosts.add(new Ghost(x, y, orangeGhostImage, tileSize, new OrangeGhostStrategy()));
			} else if (ch == 'p') { // Pink ghost
				Image pinkGhostImage = new ImageIcon(getClass().getResource("/images/pinkGhost.png")).getImage();
				ghosts.add(new Ghost(x, y, pinkGhostImage, tileSize, new PinkGhostStrategy()));
			} else if (ch == 'r') { // Red ghost
				Image redGhostImage = new ImageIcon(getClass().getResource("/images/redGhost.png")).getImage();
				redGhost = new Ghost(x, y, redGhostImage, tileSize, new RedGhostStrategy());
				ghosts.add(redGhost);
			} else if (ch == 'P') { // Pac-Man
				player = new Player(x, y, tileSize);
			} else if (ch == ' ' || ch == 'O') {
				int size = (ch == 'O') ? 8 : 4;
				foods.add(new Food(x + (tileSize - size) / 2, y + (tileSize - size) / 2, size));
			}
		}
	}
	}

	public List<Wall> getWalls() { return walls; }

	public List<Food> getFoods() {
		return foods;
	}

	public List<Ghost> getGhosts() {
		return ghosts;
	}

	public Player getPlayer() {
		return player;
	}

	public void resetMap(int level) {
		walls.clear();
		foods.clear();
		ghosts.clear();
		loadMap(level);
	}

	// Phương thức A* ban đầu dùng cho Red Ghost
	public char getNextDirectionAStar(Ghost ghost) {
		int rows = rowCount;
		int cols = columnCount;

		// Xây dựng ma trận lưới: 1 là ô đi được, 0 là tường
		int[][] grid = new int[rows][cols];
		for (int r = 0; r < rows; r++) {
			String rowStr = tileMap[r];
			for (int c = 0; c < cols; c++) {
				grid[r][c] = (rowStr.charAt(c) == 'X') ? 0 : 1;
			}
		}

		// Chuyển đổi vị trí của ghost và Pac-Man từ pixel sang chỉ số ô (row, col)
		int ghostRow = ghost.y / tileSize;
		int ghostCol = ghost.x / tileSize;
		int playerRow = player.y / tileSize;
		int playerCol = player.x / tileSize;

		// Lớp Node dùng cho A*
		class Node implements Comparable<Node> {
			int row, col;
			int g, h, f;
			Node parent;

			Node(int row, int col, int g, int h, Node parent) {
				this.row = row;
				this.col = col;
				this.g = g;
				this.h = h;
				this.f = g + h;
				this.parent = parent;
			}

			@Override
			public int compareTo(Node other) {
				return this.f - other.f;
			}
		}

		boolean[][] visited = new boolean[rows][cols];
		PriorityQueue<Node> openList = new PriorityQueue<>();

		int h = Math.abs(ghostRow - playerRow) + Math.abs(ghostCol - playerCol);
		Node start = new Node(ghostRow, ghostCol, 0, h, null);
		openList.add(start);

		Node destination = null;

		while (!openList.isEmpty()) {
			Node current = openList.poll();
			if (current.row == playerRow && current.col == playerCol) {
				destination = current;
				break;
			}
			if (visited[current.row][current.col])
				continue;
			visited[current.row][current.col] = true;

			// Các hướng di chuyển: trên, dưới, trái, phải
			int[] dr = { -1, 1, 0, 0 };
			int[] dc = { 0, 0, -1, 1 };

			for (int i = 0; i < 4; i++) {
				int newRow = current.row + dr[i];
				int newCol = current.col + dc[i];
				if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol] == 1
						&& !visited[newRow][newCol]) {
					int newG = current.g + 1;
					int newH = Math.abs(newRow - playerRow) + Math.abs(newCol - playerCol);
					Node neighbor = new Node(newRow, newCol, newG, newH, current);
					openList.add(neighbor);
				}
			}
		}

		// Nếu không tìm được đường đi, trả về hướng hiện tại của ghost
		if (destination == null) {
			return ghost.direction;
		}

		// Truy vết đường đi từ Pac-Man về ghost
		List<Point> path = new ArrayList<>();
		Node node = destination;
		while (node != null && !(node.row == ghostRow && node.col == ghostCol)) {
			// Lưu lại tọa độ của node (sử dụng Point: x chứa row, y chứa col)
			path.add(new Point(node.row, node.col));
			node = node.parent;
		}
		Collections.reverse(path);

		// Xác định hướng di chuyển từ ô hiện tại của ghost đến ô kế tiếp
		if (path.size() > 0) {
			Point next = path.get(0);
			if (next.x < ghostRow)
				return 'U';
			else if (next.x > ghostRow)
				return 'D';
			else if (next.y < ghostCol)
				return 'L';
			else if (next.y > ghostCol)
				return 'R';
		}
		return ghost.direction;
	}

	// Hàm hỗ trợ cho BlueGhostStrategy: trả về ghost đỏ
	public Ghost getRedGhost() {
		return redGhost;
	}

	// Hàm tính toán hướng đi từ (startRow, startCol) đến (targetRow, targetCol)
	// dùng cho các chiến lược khác
	public char getDirectionTowards(int startRow, int startCol, int targetRow, int targetCol) {
		// Đảm bảo startRow và startCol nằm trong phạm vi hợp lệ
		startRow = Math.max(0, Math.min(startRow, rowCount - 1));
		startCol = Math.max(0, Math.min(startCol, columnCount - 1));
		// Đảm bảo targetRow và targetCol nằm trong phạm vi hợp lệ
		targetRow = Math.max(0, Math.min(targetRow, rowCount - 1));
		targetCol = Math.max(0, Math.min(targetCol, columnCount - 1));

		int rows = rowCount;
		int cols = columnCount;
		int[][] grid = new int[rows][cols];
		for (int r = 0; r < rows; r++) {
			String rowStr = tileMap[r];
			for (int c = 0; c < cols; c++) {
				grid[r][c] = (rowStr.charAt(c) == 'X') ? 0 : 1;
			}
		}

		class Node implements Comparable<Node> {
			int row, col;
			int g, h, f;
			Node parent;

			Node(int row, int col, int g, int h, Node parent) {
				this.row = row;
				this.col = col;
				this.g = g;
				this.h = h;
				this.f = g + h;
				this.parent = parent;
			}

			public int compareTo(Node other) {
				return this.f - other.f;
			}
		}

		boolean[][] visited = new boolean[rows][cols];
		PriorityQueue<Node> openList = new PriorityQueue<>();
		int h = Math.abs(startRow - targetRow) + Math.abs(startCol - targetCol);
		Node start = new Node(startRow, startCol, 0, h, null);
		openList.add(start);
		Node destination = null;

		while (!openList.isEmpty()) {
			Node current = openList.poll();
			if (current.row == targetRow && current.col == targetCol) {
				destination = current;
				break;
			}
			if (visited[current.row][current.col])
				continue;
			visited[current.row][current.col] = true;

			// Các hướng di chuyển: trên, dưới, trái, phải
			int[] dr = { -1, 1, 0, 0 };
			int[] dc = { 0, 0, -1, 1 };

			for (int i = 0; i < 4; i++) {
				int newRow = current.row + dr[i];
				int newCol = current.col + dc[i];
				if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && grid[newRow][newCol] == 1
						&& !visited[newRow][newCol]) {
					int newG = current.g + 1;
					int newH = Math.abs(newRow - targetRow) + Math.abs(newCol - targetCol);
					Node neighbor = new Node(newRow, newCol, newG, newH, current);
					openList.add(neighbor);
				}
			}
		}

		if (destination == null)
			return 'N';
		List<Point> path = new ArrayList<>();
		Node node = destination;
		while (node != null && !(node.row == startRow && node.col == startCol)) {
			path.add(new Point(node.row, node.col));
			node = node.parent;
		}
		Collections.reverse(path);
		if (path.size() > 0) {
			Point next = path.get(0);
			// Lưu ý: Trong đối tượng Point, x chứa giá trị hàng, y chứa giá trị cột
			if (next.x < startRow)
				return 'U';
			else if (next.x > startRow)
				return 'D';
			else if (next.y < startCol)
				return 'L';
			else if (next.y > startCol)
				return 'R';
		}
		return 'N';
	}

	public boolean isDirectionValid(int x, int y, char direction) {

		int centerX = x + tileSize / 2;
		int centerY = y + tileSize / 2;

		// Tính toán ô tiếp theo dựa trên hướng
		int nextCol = centerX / tileSize;
		int nextRow = centerY / tileSize;

		switch (direction) {
		case 'U':
			nextRow--;
			break;
		case 'D':
			nextRow++;
			break;
		case 'L':
			nextCol--;
			break;
		case 'R':
			nextCol++;
			break;
		}

		// Kiểm tra trong phạm vi map
		if (nextRow < 0 || nextRow >= rowCount || nextCol < 0 || nextCol >= columnCount) {
			return false;
		}

		// Kiểm tra ô đó có phải tường không
		return tileMap[nextRow].charAt(nextCol) != 'X';
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

}
