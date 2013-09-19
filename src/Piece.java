import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Handles all piece movement and searching for valid moves
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public abstract class Piece extends Rectangle
{
	protected int row;

	protected int col;

	protected int colour;

	protected Image image;

	protected boolean hasMoved;

	protected ArrayList<Point> allMoveLocations;

	protected boolean canTake;

	protected static boolean oneCanTake;

	private ArrayList<Move> myMoves;
	
	protected boolean isAi;

	/**
	 * Constructs the piece object
	 * 
	 * @param myRow
	 *            the row the piece is on the board
	 * @param myCol
	 *            the column the piece is on the board
	 * @param myColour
	 *            the colour of the piece on the board
	 */
	public Piece(int myRow, int myCol, int myColour)
	{
		row = myRow;
		col = myCol;
		colour = myColour;
		hasMoved = false;
		canTake = false;
		allMoveLocations = new ArrayList<Point>();
		oneCanTake = false;
		myMoves = new ArrayList<Move>();
	}

	/**
	 * Draws the graphical representation of the piece
	 * 
	 * @param g
	 *            the Graphics to draw the piece in
	 */
	public void draw(Graphics g)
	{
		g.drawImage(image, col * 71 + 25, row * 71 + 25, null);
	}

	/**
	 * Finds all valid moves for this piece
	 * 
	 * @param board
	 *            the board this piece exists on
	 * @param isForced
	 *            whether the piece should be checking for forced moves or not
	 * @return the locations the piece can legally move to
	 */
	public abstract ArrayList<Point> getMoveLocations(Piece[][] board,
			boolean isForced);

	/**
	 * Moves the piece to the given location on the board
	 * 
	 * @param loc
	 *            the location to move the piece to
	 * @param board
	 *            the board the piece exists on
	 */
	public void move(Point loc, Piece[][] board)
	{
		int originalCol = col;
		board[row][col] = null;
		row = loc.y;
		col = loc.x;
		board[row][col] = this;
		hasMoved = true;
		// Check for castle
		if (this instanceof King && !isAi())
		{
			if (Math.abs(col - originalCol) > 1)
			{
				castle(loc, originalCol, board);
			}
		}
		// Check for promote
		if (this instanceof Pawn)
			if (row == 0 || row == 7)
			{
				promote(board);
			}
		oneCanTake = false;
		myMoves.clear();
	}

	/**
	 * Returns if the King is an AI or not for castling purposes
	 * @return if the King is an AI
	 */
	private boolean isAi()
	{
		if (this instanceof King)
			return isAi;
		else
			return false;
	}

	/**
	 * Removes valid locations from the move list if this piece or another can take a piece
	 * @param board the board the piece exists on
	 * @param moveLocations all valid move locations before removing any
	 */
	public void ensureForcedTake(Piece[][] board, ArrayList<Point> moveLocations)
	{
		for (int currLoc = moveLocations.size() - 1; currLoc > -1; currLoc--)
		{
			if (board[moveLocations.get(currLoc).y][moveLocations.get(currLoc).x] == null)
			{
				moveLocations.remove(currLoc);
			}
		}
		oneCanTake = true;
	}

	/**
	 * Gets the colour of the piece
	 * @return the colour of the piece
	 */
	public int getColour()
	{
		return colour;
	}

	/**
	 * Gets if the piece has moved yet
	 * @return if the piece has moved yet
	 */
	public boolean getHasMoved()
	{
		return hasMoved;
	}

	/**
	 * Changes the value that stores if any piece on the board can take
	 * @param newValue the value to set the variable to
	 */
	public void changeCanTake(boolean newValue)
	{
		oneCanTake = newValue;
	}

	/**
	 * Checks if one of the pieces is forced to take
	 * @return whether or not a piece must take
	 */
	public boolean canOneTake()
	{
		return oneCanTake;
	}

	/**
	 * Checks if this current piece must take
	 * @return whether or not this piece must take
	 */
	public boolean forcedMove()
	{
		return canTake;
	}

	/**
	 * Gets the location the piece is at on the board
	 * @return the location of the piece
	 */
	public Point getLocation()
	{
		return new Point(col, row);
	}

	/**
	 * Calculates the best move for this piece based on the score of each move
	 * @param board the board the piece is moving in 
	 * @param depthCount the number of times to look ahead for scoring
	 */
	public void findBestMove(Piece[][] board, int depthCount)
	{
		oneCanTake = false;
		// Run through moves to check for force take
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (board[row][col] != null
						&& board[row][col].getColour() == colour)
				{
					board[row][col].getMoveLocations(board, true);
				}
			}
		}
		ArrayList<Point> myDestinations = getMoveLocations(board, true);
		myMoves.clear();
		// Create and score all possible moves
		// Change all valid destinations into move objects
		for (int movesChecked = 0; movesChecked < myDestinations.size(); movesChecked++)
		{
			myMoves.add(new Move(this, myDestinations.get(movesChecked),
					board[myDestinations.get(movesChecked).y][myDestinations
							.get(movesChecked).x]));
		}
		// Score each possible move
		for (int toCheck = 0; toCheck < myMoves.size(); toCheck++)
		{
			myMoves.get(toCheck).setScore(board, depthCount);
		}
	}

	/**
	 * Returns the moves available to the piece
	 * @return the moves for the piece
	 */
	public ArrayList<Move> getMoves()
	{
		return myMoves;
	}

	/**
	 * Copies over the piece and it's location to a new piece object
	 * @return the newly created piece
	 */
	public Piece clonePiece()
	{
		Piece tempPiece;
		if (this instanceof Pawn)
			tempPiece = new Pawn(row, col, colour);
		else if (this instanceof Rook)
			tempPiece = new Rook(row, col, colour);
		else if (this instanceof Knight)
			tempPiece = new Knight(row, col, colour);
		else if (this instanceof Bishop)
			tempPiece = new Bishop(row, col, colour);
		else if (this instanceof Queen)
			tempPiece = new Queen(row, col, colour);
		else if (this instanceof King)
			tempPiece = new King(row, col, colour, true);
		else
			return null;
		tempPiece.changeCanTake(oneCanTake);
		return tempPiece;
	}

	/**
	 * Moves the rook and king in order to castle
	 * @param loc the location to move to
	 * @param originalCol the starting column of the rook
	 * @param board the board the pieces are moving in
	 */
	public void castle(Point loc, int originalCol, Piece[][] board)
	{
		// Move the rook to the correct location
		if (colour == 1)
		{
			// Left white rook
			if (col - originalCol == -2)
			{
				Point castleLoc = new Point(3, 7);
				board[7][0].rookCastleMove(castleLoc, board);
			}
			// Right white rook
			else
			{
				Point castleLoc = new Point(5, 7);
				board[7][7].rookCastleMove(castleLoc, board);
			}
		}
		else
		{
			// Left black rook
			if (col - originalCol == -2)
			{
				Point castleLoc = new Point(3, 0);
				board[0][0].rookCastleMove(castleLoc, board);
			}
			// Right black rook
			else
			{
				Point castleLoc = new Point(5, 0);
				board[0][7].rookCastleMove(castleLoc, board);
			}
		}

	}

	/**
	 * Castles the rook
	 * @param loc where the rook is moving to
	 * @param board where the rook is moving on
	 */
	public void rookCastleMove(Point loc, Piece[][] board)
	{
		board[row][col] = null;
		row = loc.y;
		col = loc.x;
		board[row][col] = this;
		hasMoved = true;
	}

	/**
	 * Promotes the pawn to a queen
	 * @param board the board the pieces are moving in
	 */
	public void promote(Piece[][] board)
	{
		board[row][col] = new Queen(row, col, colour);
	}

}
