import java.awt.Point;
import java.util.ArrayList;

/**
 * Handles all moves and their scoring
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public class Move implements Comparable
{
	private Piece piece;

	private Point moveTo;

	private int score;

	private int myScore;

	private int opponentScore;

	/**
	 * Constructs the Move object
	 * 
	 * @param myPiece
	 *            the piece the move belongs to
	 * @param location
	 *            the location the piece is moving to
	 * @param otherPiece
	 *            the piece (if any) that exist at the target location
	 */
	public Move(Piece myPiece, Point location, Piece otherPiece)
	{
		piece = myPiece;
		moveTo = location;
		myScore = 0;
		opponentScore = 0;
	}

	/**
	 * Gets the destination of the piece
	 * 
	 * @return the destination of the piece
	 */
	public Point getDestination()
	{
		return moveTo;
	}

	/**
	 * Gets the piece that's making the move
	 * 
	 * @return the piece that's going to move
	 */
	public Piece getPiece()
	{
		return piece;
	}

	/**
	 * Compares to moves based on their scores
	 * 
	 * @param other
	 *            the other move to compare this one
	 * @return the result of the comparison
	 */
	public int compareTo(Object other)
	{
		Move otherMove = (Move) other;
		return otherMove.score - this.score;
	}

	/**
	 * Copies the board to a new board in order to not change the original and
	 * calls the lookAhead() method to start the move searching
	 * 
	 * @param board
	 *            the board the pieces exist on
	 * @param depth
	 *            the depth to search through
	 */
	public void setScore(Piece[][] board, int depth)
	{
		int currColour = piece.getColour();
		Piece[][] copyBoard = new Piece[8][8];
		// Copy over all elements
		for (int row = 0; row < copyBoard.length; row++)
		{
			Piece[] tempPieces = board[row];
			copyBoard[row] = new Piece[tempPieces.length];
			System.arraycopy(tempPieces, 0, copyBoard[row], 0,
					tempPieces.length);
		}
		score = lookAhead(copyBoard, depth, currColour, currColour);
	}

	/**
	 * Calculates the overall score of this move.  Looks ahead and anticipates the opponent's moves in order to score.
	 * @param board the board the piece exists on
	 * @param depth the amount of times to look ahead
	 * @param currColour the colour currently being scored
	 * @param playerColour the colour the AI is playing
	 * @return the score of the move
	 */
	public int lookAhead(Piece[][] board, int depth, int currColour,
			int playerColour)
	{
		// Base case
		if (depth == 0)
			return 0;
		int scoreSoFar = 0;
		Piece[][] copyBoard = new Piece[8][8];
		// Copy over all elements
		for (int row = 0; row < copyBoard.length; row++)
		{
			Piece[] tempPieces = board[row];
			copyBoard[row] = new Piece[tempPieces.length];
			System.arraycopy(tempPieces, 0, copyBoard[row], 0,
					tempPieces.length);
		}
		// Score the move based on pieces taken and pieces lost
		if (board[moveTo.y][moveTo.x] != null)
			if (currColour == playerColour)
				scoreSoFar += takeScore(board[moveTo.y][moveTo.x]);
			else
				scoreSoFar -= takeScore(board[moveTo.y][moveTo.x]);
		// Store old location
		Point oldLocation = new Point(piece.getLocation().x,
				piece.getLocation().y);
		// Only store best next move
		int nextTopScore = Integer.MIN_VALUE;
		Piece copiedPiece = piece.clonePiece();
		copiedPiece.move(moveTo, copyBoard);
		// Run move locations to set up force take
		for (int row = 0; row < 8 && !copiedPiece.canOneTake(); row++)
		{
			for (int col = 0; col < 8; col++)
			{

				if (copyBoard[row][col] != null
						&& copyBoard[row][col].getColour() == currColour)
				{
					copyBoard[row][col].getMoveLocations(copyBoard, true);
				}

			}
		}

		// Do the same recursively for possible moves for both players if this move were to be made
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (copyBoard[row][col] != null
						&& copyBoard[row][col].getColour() == currColour)
				{
					ArrayList<Point> nextMoves = copyBoard[row][col]
							.getMoveLocations(copyBoard, true);
					// Create new set of moves
					for (int movesSoFar = 0; movesSoFar < nextMoves.size(); movesSoFar++)
					{
						// Store the next move to be scored
						Move currMove = new Move(
								copiedPiece,
								nextMoves.get(movesSoFar),
								copyBoard[nextMoves.get(movesSoFar).y][nextMoves
										.get(movesSoFar).x]);
						if (currColour == 1)
							currColour = 2;
						else if (currColour == 2)
							currColour = 1;
						// Recursion, recall lookAhead()
						int thisScore = currMove.lookAhead(copyBoard,
								depth - 1, currColour, playerColour);
						// Compare current score to current best outcome
						if (thisScore > nextTopScore)
							nextTopScore = thisScore;
					}
				}
			}
		}
		// Add score of top outcome to this moves score
		scoreSoFar += nextTopScore;
		// Move pieces back
		copiedPiece.move(oldLocation, copyBoard);
		return scoreSoFar;
	}

	/**
	 * Gives the score for the piece that is being taken
	 * @param toTake the piece being taken
	 * @return the score of the taken piece
	 */
	public int takeScore(Piece toTake)
	{
		int takeScore = 0;
		// Check if piece is being taken
		if (toTake != null)
		{
			// Check score for taking piece
			// Higher scores are better
			if (toTake instanceof Pawn)
			{
				takeScore = 6;
			}
			else if (toTake instanceof King)
			{
				takeScore = 8;
			}
			else if (toTake instanceof Bishop)
			{
				takeScore = 4;
			}
			else if (toTake instanceof Knight)
			{
				takeScore = 6;
			}
			else if (toTake instanceof Queen)
			{
				takeScore = -3;
			}
			else if (toTake instanceof Rook)
			{
				takeScore = 4;
			}
		}
		return takeScore;
	}

	/**
	 * Gets the score of this move
	 * @return the score of the move
	 */
	public int getScore()
	{
		return score;
	}

}
