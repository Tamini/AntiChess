import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Handles all movements and locations for Pawns
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public class Pawn extends Piece
{

	/**
	 * Constructs a new Pawn object
	 * 
	 * @param myRow
	 *            the row the piece is on
	 * @param myCol
	 *            the column the piece is on
	 * @param myColour
	 *            the colour of the piece
	 */
	public Pawn(int myRow, int myCol, int myColour)
	{
		// Call super
		super(myRow, myCol, myColour);
		// Select the piece's image depending on its colour
		if (myColour == 1)
			image = new ImageIcon("images\\whitePawn.png").getImage();
		else
			image = new ImageIcon("images\\blackPawn.png").getImage();

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
	public ArrayList<Point> getMoveLocations(Piece[][] board, boolean isForced)
	{
		canTake = false;
		// Handles out of bounds
		int maxRow = Math.min(1, Math.min(7 - row, row));
		ArrayList<Point> moveLocations = new ArrayList<Point>();

		int addRow;
		// Handles both black and white pawns
		if (colour == 1)
			addRow = -1;
		else
			addRow = 1;
		// If there's no piece in front of the pawn, it can move 1 space up
		if (maxRow != 0 && board[row + addRow][col] == null)
		{
			Point currentPoint = new Point(col, row + addRow);
			moveLocations.add(currentPoint);
		}

		// Can move two spaces ahead if first move
		if (row + 2 * addRow < 8 && row + 2 * addRow > -1 && !hasMoved
				&& board[row + 2 * addRow][col] == null
				&& board[row + addRow][col] == null)
		{
			Point currentPoint = new Point(col, row + 2 * addRow);
			moveLocations.add(currentPoint);
		}

		// Handles eating diagonally
		if (col <= 6 && row != 0 && row != 7)
		{
			if (board[row + addRow][col + 1] != null)
			{
				if (board[row + addRow][col + 1] == null
						|| board[row + addRow][col + 1].getColour() != colour)
				{
					Point currentPoint = new Point(col + 1, row + addRow);
					moveLocations.add(currentPoint);
				}
			}
		}
		if (col >= 1 && row != 0 && row != 7)
		{
			if (board[row + addRow][col - 1] != null)
			{
				if (board[row + addRow][col - 1] == null
						|| board[row + addRow][col - 1].getColour() != colour)
				{
					Point currentPoint = new Point(col - 1, row + addRow);
					moveLocations.add(currentPoint);
				}
			}
		}

		// If the piece may be forced to move, check for it
		if (isForced)
		{
			// Check each valid location to see if it contains a piece
			for (Point currLoc : moveLocations)
			{
				if (board[currLoc.y][currLoc.x] != null)
				{
					// Modify valid moves
					this.canTake = true;
					ensureForcedTake(board, moveLocations);
					return moveLocations;
				}
			}
			// Check if another piece must take
			if (oneCanTake)
				moveLocations.clear();
		}
		return moveLocations;
	}

}
