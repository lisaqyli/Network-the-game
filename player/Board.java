package player;

//if there are a number of equally ok move in the beginning?
//

/**
 * An implementation of board objects. Keep track of the situation of each board
 * after every move.
 */

public class Board {
	/**
	 * gBoard is a two-dimensional array that stores the locations of all the
	 * chips on the board. The value of the array is always 0/1/2, which
	 * represents blank, white, black.
	 */
	protected Chip[][] gBoard;

	/**
	 * type is an integer that represents the type of that game board. The value
	 * is always 0/1/2, which representing referee, white, black boards. ？？？？？
	 * need change
	 */
	public final static int WHITE = 1;
	public final static int BLACK = 2;
	public final static int EMPTY = 0;
	public final static int MAXCHIP = 10;
	public final static int MINCHIP = 6;
	public final static int SIZE = 8;

	/**
	 * numBC and numWC represents the number of black chips and white chips.
	 */
	protected int numBC;

	protected int numWC;

	protected Object[][] bNetworks;
	protected Object[][] wNetworks;

	/**
	 * The constructor of board class.
	 */
	public Board() {
		// Do I need to throw error here if wrong type?????
		gBoard = new Chip[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				gBoard[i][j] = null;
			}
		}
	}

	/**
	 * get the number of black chips.
	 * 
	 * @return the number of black chips
	 */
	public int getNumBC() {
		return numBC;
	}

	/**
	 * get the number of white chips.
	 * 
	 * @return the number of white chips.
	 */
	public int getNumWC() {
		return numWC;
	}

	/**
	 * addChip adds a chip to a designated location on the board, throws
	 * InvalidCoordinatesException if the input coordinates are invalid. and it
	 * also inceases the number of counts of white chips and black chips.
	 * 
	 * @param color
	 *            is an integer that presents the color of the chip being added.
	 * @param m
	 *            is a Move object
	 */

	public void addChip(int color, Move m) {
		// What if the color is invalid input??????????
		int x = m.x1;
		int y = m.y1;
		gBoard[x][y] = new Chip(color, x, y);
		if (color == WHITE) {
			numWC++;
		} else if (color == BLACK) {
			numBC++;
		}
	}

	/**
	 * removeChip is a method that removes a chip from one cell of the board.
	 * 
	 * @param x
	 *            is the horizontal coordinate of the board
	 * @param y
	 *            is the vertical coordinate of the board
	 **/

	public void removeChip(int color, Move m) {
		// what if there is no chip on that location??????
		int x = m.x2;
		int y = m.y2;
		if (gBoard[x][y].getColor() == WHITE) {
			numWC--;
		} else if (gBoard[x][y].getColor() == BLACK) {
			numBC--;
		}
		gBoard[x][y] = null;
	}

	/**
	 * stepChip is a method that moves a chip from one coordinates of the board
	 * to another location.
	 * 
	 * @param x1
	 *            the original horizontal coordinate of the board
	 * @param y1
	 *            the original vertical coordinate of the board
	 * @param x2
	 *            the new horizontal coordinate of the board
	 * @param y2
	 *            the new vertical coordinate of the board
	 **/

	public void stepChip(int color, Move m) {
		removeChip(color, m);
		addChip(color, m);
	}

	/**
	 * clusterThree(int color, Move m) is a boolean that checks if a move is
	 * legal according to the cluster rule.
	 * 
	 * @param color
	 *            is the color of the player, black or white
	 * @param m
	 *            is the Move object returned by chooseMove
	 * @return true if there is a cluster of more than 3 same color chips, false
	 *         if there isn't.
	 **/

	public boolean clusterThree(int color, Move m) {
		int x1 = m.x1;
		int y1 = m.y1;
		int xLBound = x1 - 1;
		int xUBound = x1 + 1;
		int yLBound = y1 - 1;
		int yUBound = y1 + 1;
		if (x1 < 1) {
			xLBound = 0;
		} else if (x1 > (SIZE - 2)) {
			xUBound = SIZE - 1;
		}
		if (y1 < 1) {
			yLBound = 0;
		} else if (y1 > (SIZE - 2)) {
			yUBound = SIZE - 1;
		}
		for (int i = xLBound; i <= xUBound; i++) {
			for (int j = yLBound; j <= yUBound; j++) {
				if (gBoard[i][j].getColor() == color) {
					Move secondary = new Move(i, j);
					return clusterThree(color, secondary);
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * isLegalMove(int color, Move m) is a boolean checking if the chooseMove
	 * return is legal
	 * 
	 * @param color
	 *            is the color of the player, black or white
	 * @param Move
	 *            m is the Move object returned by chooseMove
	 * @return true if legal, false if illegal
	 **/

	public boolean isLegalMove(int color, Move m) {
		int x1 = m.x1;
		int y1 = m.y1;
		if (color == WHITE) { // White
			if (numWC >= 10 || y1 == 0 || y1 == SIZE - 1 || // not more than 10,
															// no opponent goal
															// or corners;
					gBoard[x1][y1] != null) {
				return false;
			} else if (clusterThree(color, m)) {
				return false;
			} else {
				return true;
			}
		} else if (color == BLACK) { // Black
			if (numBC >= 10 || x1 == 0 || x1 == SIZE - 1 || // not more than 10,
															// no opponent goal
															// or corners;
					gBoard[x1][y1] != null) {
				return false;
			} else if (clusterThree(color, m)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * public undoMove(int color, Move m) undoes a move done
	 * 
	 * @param int color is the color of the player, black or white
	 * @param Move
	 *            m is the move we want to undo on the board
	 **/

	public void undoMove(int color, Move m) {
		int x1 = m.x1;
		int y1 = m.y1;
		gBoard[x1][y1] = null;
		if (m.moveKind == m.STEP) {
			int x2 = m.x2;
			int y2 = m.y2;
			gBoard[x2][y2] = color;
		} else {
			if (color == WHITE) {
				numWC--;
			} else if (color == BLACK) {
				numBC--;
			}
		}
	}

	/**
	 * findConnections() finds all the connections the input chip has on the
	 * board connections[][] is a two-dimentional array that stores all the
	 * connections that specified chip has. The first dimention is the chip that
	 * connects to this chip and the second dimention is the length of the
	 * chain.
	 * 
	 * @param x
	 * @return an array of possible connections
	 */
	public Chip[] findConnections(Chip x) {
		Chip[] connections = new Chip[8];
		int position = 0;
		int xc = x.getX();
		int yc = x.getY();
		// check horizontal left
		if (x.getX() != 0) {
			for (int i = x.getX() - 1; i > 0; i--) {
				if (gBoard[i][yc] != null) {
					if (gBoard[i][yc].getColor() == x.getColor()) {
						// Here gBoard should have a chip in it instead of an
						// int.
						connections[position] = gBoard[i][x.getY()];
						position++;
					} else if (gBoard[i][yc].getColor() != x.getColor()) {
						break;
					}
				}
			}
		}
		// check horizontal right
		if (x.getX() != SIZE - 1) {
			for (int i = xc + 1; i < SIZE; i++) {
				if (gBoard[i][yc] != null) {
					if (gBoard[i][yc].getColor() == x.getColor()) {
						connections[position] = gBoard[i][yc];
						position++;
					} else if (gBoard[i][yc].getColor() != x.getColor()) {
						break;
					}
				}
			}
		}
		// check vertical up
		if (x.getY() != 0) {
			for (int i = yc - 1; i > 0; i--) {
				if (gBoard[xc][i] != null) {
					if (gBoard[xc][i].getColor() == x.getColor()) {
						connections[position] = gBoard[xc][i];
						position++;
					} else if (gBoard[xc][i].getColor() != x.getColor()) {
						break;
					}
				}
			}
		}
		// check vertical down
		if (x.getY() != SIZE - 1) {
			for (int i = yc + 1; i > 0; i++) {
				if (gBoard[xc][i] != null) {
					if (gBoard[xc][i].getColor() == x.getColor()) {
						connections[position] = gBoard[xc][i];
						position++;
					} else if (gBoard[xc][i].getColor() != x.getColor()) {
						break;
					}
				}
			}
		}
		int minxy;
		if (xc < yc) {
			minxy = xc;
		} else {
			minxy = yc;
		}

		// check diag1
		if (xc != 0 && yc != 0) {
			for (int i = 1; i < minxy; i++) {
				if (gBoard[xc - i][yc - i] != null) {
					if (gBoard[xc - i][yc - i].getColor() == x.getColor()) {
						connections[position] = gBoard[xc - i][yc - i];
						position++;
					} else if (gBoard[xc - i][yc - i].getColor() != x
							.getColor()) {
						break;
					}
				}
			}
		}
		// check diag2
		if (xc != SIZE - 1 && yc != 0) {
			for (int i = 1; i < minxy; i++) {
				if (gBoard[xc + i][yc - i] != null) {
					if (gBoard[xc + i][yc - i].getColor() == x.getColor()) {
						connections[position] = gBoard[xc + i][yc - i];
						position++;
					} else if (gBoard[xc + i][yc - i].getColor() != x
							.getColor()) {
						break;
					}
				}
			}
		}
		// check diag3
		if (xc != 0 && yc != SIZE - 1) {
			for (int i = 1; i < minxy; i++) {
				if (gBoard[xc - i][yc + i] != null) {
					if (gBoard[xc - i][yc - i].getColor() == x.getColor()) {
						connections[position] = gBoard[xc - i][yc - i];
						position++;
					} else if (gBoard[xc - i][yc - i].getColor() != x
							.getColor()) {
						break;
					}
				}
			}
		}
		// check diag4
		if (xc != SIZE - 1 && yc != SIZE - 1) {
			for (int i = 1; i < minxy; i++) {
				if (gBoard[xc + i][yc + i] != null) {
					if (gBoard[xc + i][yc + i].getColor() == x.getColor()) {
						connections[position] = gBoard[xc + i][yc + 1];
						position++;
					} else if (gBoard[xc + i][yc + i].getColor() != x
							.getColor()) {
						break;
					}
				}
			}
		}
		return connections;
	}

	/**
	 * searchBoard takes in a color and searches in its goal area for possible
	 * chips to start off a network.
	 * 
	 * @param color
	 * @return a two-dimensional array, first dimension is the last chip in the
	 *         network and the second dimension is the number of chips in the
	 *         chain so far.
	 */
	public Object[][] searchBoard(int color) {
		if (color == WHITE) {
			int p1 = 0;
			for (int i = 1; i < SIZE - 1; i++)
				if (gBoard[0][i] != null && gBoard[0][i].getColor() == WHITE) {
					wNetworks[p1][0] = DFS(gBoard[0][i], 1)[0];
					wNetworks[p1][1] = DFS(gBoard[0][i], 1)[1];
					gBoard[0][i].setVisited(true);
					p1++;
				}
			// search the other side of the board (x = 7)
			for (int i = 1; i < SIZE - 1; i++)
				if (gBoard[SIZE - 1][i] != null
						&& gBoard[SIZE - 1][i].getColor() == WHITE) {
					wNetworks[p1][0] = DFS(gBoard[SIZE - 1][i], 1)[0];
					wNetworks[p1][1] = DFS(gBoard[SIZE - 1][i], 1)[1];
					gBoard[SIZE - 1][i].setVisited(true);
					p1++;
				}
			return wNetworks;
		} else {
			int p2 = 0;
			for (int i = 1; i < SIZE - 1; i++)
				if (gBoard[i][0] != null && gBoard[i][0].getColor() == BLACK) {
					DFS(gBoard[i][0], 1);
					bNetworks[p2][0] = DFS(gBoard[i][0], 1)[0];
					bNetworks[p2][1] = DFS(gBoard[i][0], 1)[1];
					p2++;
					gBoard[i][0].setVisited(true);
				}
			for (int i = 1; i < SIZE - 1; i++)
				if (gBoard[i][SIZE - 1] != null
						&& gBoard[i][SIZE - 1].getColor() == BLACK) {
					DFS(gBoard[i][SIZE - 1], 1);
					bNetworks[p2][0] = DFS(gBoard[i][SIZE - 1], 1)[0];
					bNetworks[p2][1] = DFS(gBoard[i][SIZE - 1], 1)[1];
					p2++;
					gBoard[i][SIZE - 1].setVisited(true);
				}
			return bNetworks;
		}
	}

	/**
	 * DFS conducts Depth-First search in the board
	 * 
	 * @param a
	 *            is the chip pass in
	 * @return an array storing the last chip and number of chips of the longest
	 *         chain the input chip can form.
	 */
	Chip current;
	Chip lastChip;
	int maxCount;
	int counter = 1;

	// we call
	public Object[] DFS(Chip a, int counter) {
		Object[] result = new Object[2];
		int localCount = counter;
		current = a;

		Chip[] allConnections = findConnections(a);
		for (int i = 0; i < 8; i++) {
			Chip[] traversed = new Chip[10];
			int t1 = 0;
			if (allConnections[i] != null) {
				if (allConnections[i].visited == true) {
				} else {
					current = allConnections[i];
					localCount++;
					current.setVisited(true);
					traversed[t1] = current;
					t1++;
				}
				if (localCount > maxCount) {
					maxCount = localCount;
					lastChip = current;
				}
				if (findConnections(allConnections[i])[0] != null) {
					DFS(allConnections[i], localCount);
				}
				localCount--;
				for (int o = 0; o < 10; o++) {
					if (traversed[o] != null) {
						traversed[o].setVisited(false);
					} else {
						break;
					}
				}
			} else {
				// How to exit??????????
				break;
			}
		}
		result[0] = lastChip;
		result[1] = maxCount;
		return result;
	}

	/**
	 * reachGoalArea checks if the chip is in the goal area of its color
	 * 
	 * @param x
	 * @return true if it does reach its goal area, false if it does not
	 */

	public boolean reachGoalArea(Chip x) {
		if (x.getColor() == WHITE) {
			if (x.getX() == 0 | x.getX() == SIZE - 1) {
				return true;
			} else {
				return false;
			}
		} else {
			if (x.getY() == 0 | x.getY() == SIZE - 1) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * eval evaluates the likeliness to win and assigns a score to the player.
	 * The score is the difference between number of chips in a chain for your
	 * color and your opponent's color.
	 * 
	 * @param color
	 * @return the score
	 */
	// Max score if win?
	public int eval(int color) {
		int diff;
		int wScore = 0;
		int bScore = 0;
		Object[][] out = searchBoard(color);
		if (color == WHITE) {
			for (int i = 0; i < 20; i++) {
				if (isWin(color)) {
					return 10;
				}
				if (out[i][1] != null) {
					wScore = wScore + ((Integer) out[i][1]);
				} else {
					break;
				}
			}
			diff = wScore - bScore;
		} else {
			for (int i = 0; i < 20; i++) {
				if (isWin(color)) {
					return 10;
				}
				if (bNetworks[i] != null) {
					int bb = ((Integer) out[i][1]);
					bScore = bScore + bb;
				} else {
					break;
				}
			}
			diff = bScore - wScore;
		}
		return diff;
	}

	/**
	 * isWin checks if the player wins the game (has more than 6 chips in a
	 * chain and connects both goal areas.
	 * 
	 * @param color
	 * @return true if he does, false if not.
	 */
	public boolean isWin(int color) {
		// two dimensional array length?????
		// in opposite goal area instead of the same one
		for (int n = 0; n < 10; n++) {
			if (color == WHITE) {
				if (wNetworks[n] != null) {
					if ((Integer) wNetworks[n][1] >= 6
							&& reachGoalArea((Chip) wNetworks[n][0])) {
						return true;
					}
				}
			} else {
				if (bNetworks[n] != null) {
					if ((Integer) bNetworks[n][1] >= 6
							&& reachGoalArea((Chip) bNetworks[n][0])) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
