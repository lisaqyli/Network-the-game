package player;

/** Chip class is a class that describes the chips on the board. */

public class Chip {
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	private final int color;
	private int x;
	private int y;

	/**
	 * true if the chip has been visited, false if not.
	 */
	protected boolean visited;

	public Chip(int col, int xx, int yy) {
		color = col;
		x = xx;
		y = yy;
		visited = false;
	}

	public boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean a) {
		visited = a;
	}

	public int getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
