/* MachinePlayer.java */

package player;

/**
 * An implementation of an automatic Network player. Keeps track of moves made
 * by both players. Can select a move for itself.
 */
public class MachinePlayer extends Player {
	private final int color;
	private final int searchDepth;
	private static Board board;

	// Creates a machine player with the given color. Color is either 0 (black)
	// or 1 (white). (White has the first move.)
	public MachinePlayer(int color) {
		this.color = color;
		this.searchDepth = 0;
		this.board = new Board();
	}

	// Creates a machine player with the given color and search depth. Color is
	// either 0 (black) or 1 (white). (White has the first move.)
	public MachinePlayer(int color, int searchDepth) {
		this.color = color;
		this.searchDepth = searchDepth;
		this.board = new Board();
	}

	// Returns a new move by "this" player. Internally records the move (updates
	// the internal game board) as a move by "this" player.
	@Override
	public Move chooseMove() {
		return new Move();
	}

	// If the Move m is legal, records the move as a move by the opponent
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method allows your opponents to inform you of their moves.
	@Override
	public boolean opponentMove(Move m) {
		// Why it is returning doMove?
		return doMove((opponent(color)), m);
		return false;
	}

	// If the Move m is legal, records the move as a move by "this" player
	// (updates the internal game board) and returns true. If the move is
	// illegal, returns false without modifying the internal state of "this"
	// player. This method is used to help set up "Network problems" for your
	// player to solve.
	@Override
	public boolean forceMove(Move m) {
		return false;
	}

	/*
	 * doMove(int color, Move move, Board board) performs the legal move on the
	 * board
	 * 
	 * @param color is the color of the player, black or white
	 * 
	 * @param m is the Move object returned by chooseMove
	 * 
	 * @return true if move is legal and do the move, false if move illegal
	 */

	public static boolean doMove(int color, Move m) {
		int mk = m.moveKind;
		if (isLegalMove(color, m)) {
			if (mk == m.ADD) {
				board.addChip(color, m);
			} else if (mk == m.STEP) {
				board.stepChip(color, m);
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * public Object[][] legalMoves(int color, Board board)
	 * 
	 * @param int color is the color of the player
	 * @return a 2 by maxLMs array with the first item as an array of legal
	 *         moves and the second item as an array of boards resulting from
	 *         the legal moves.
	 **/

	public Object[][] legalMoves(int color, Board board) {
		int maxLMs = SIZE * SIZE - numBC - numWC;
		int counter = 0;
		Move[] lmoves = new Move[maxLMs];
		Board[] lboards = new Board[maxLMs];
		if ((color == BLACK && numBC < 10) || (color == WHITE && numWC < 10)) { // ADD
																				// Moves
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					Move tmp = new Move(i, j);
					if (isLegalMove(color, tmp)) {
						lmoves[counter] = tmp;
						gBoard[i][j] = color;
						lboards[counter] = this;
						gBoard[i][j] = EMPTY;
						counter++;
					}
				}
			}
		} else if ((color == BLACK && numBC == 10)
				|| (color == WHITE && numWC == 10)) { // STEP moves SHOULD I USE
														// ELSE IF?
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					if (gBoard[i][j] == color) { // if contain the player's chip
						for (int m = 0; m < SIZE; m++) { // search for legal
															// moves
							for (int n = 0; n < SIZE; n++) {
								Move tmp = new Move(m, n, i, j);
								if (isLegalMove(color, tmp)) {
									lmoves[counter] = tmp;
									gBoard[m][n] = color;
									gBoard[i][j] = EMPTY;
									lboards[counter] = this;
									gBoard[m][n] = EMPTY;
									gBoard[i][j] = color;
									counter++;
								}
							}
						}
					}
				}
			}
		}
		counter = 0;
		Object[][] muahaha = new Object[maxLMs][2];
		muahaha[0] = lmoves;
		muahaha[1] = lboards;
		return muahaha;
	}

}
