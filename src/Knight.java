import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Handles all movements and locations for Knights
 * 
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public class Knight extends Piece
{

	/**
	 * Constructs a new Knight object
	 * 
	 * @param myRow
	 *            the row the piece is on
	 * @param myCol
	 *            the column the piece is on
	 * @param myColour
	 *            the colour of the piece
	 */
	public Knight(int myRow, int myCol, int myColour)
	{
		// Call super
		super(myRow, myCol, myColour);
		// Select the piece's image depending on its colour
		if (myColour == 1)
			image = new ImageIcon("images\\whiteKnight.png").getImage();
		else
			image = new ImageIcon("images\\blackKnight.png").getImage();
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
		ArrayList<Point> moveLocations = new ArrayList<Point>();

		// Check all possible directions for conflicts
		if (col <= 5)
		{
			if (row >= 1)
			{
				if (board[row - 1][col + 2] == null
						|| board[row - 1][col + 2].getColour() != colour)
				{
					Point currentPoint = new Point(col + 2, row - 1);
					moveLocations.add(currentPoint);
				}
			}
			if (row <= 6)
			{
				if (board[row + 1][col + 2] == null
						|| board[row + 1][col + 2].getColour() != colour)
				{
					Point currentPoint = new Point(col + 2, row + 1);
					moveLocations.add(currentPoint);
				}
			}
		}

		if (col >= 2)
		{
			if (row >= 1)
			{
				if (board[row - 1][col - 2] == null
						|| board[row - 1][col - 2].getColour() != colour)
				{
					Point currentPoint = new Point(col - 2, row - 1);
					moveLocations.add(currentPoint);
				}
			}
			if (row <= 6)
			{
				if (board[row + 1][col - 2] == null
						|| board[row + 1][col - 2].getColour() != colour)
				{
					Point currentPoint = new Point(col - 2, row + 1);
					moveLocations.add(currentPoint);
				}
			}
		}

		if (row <= 5)
		{
			if (col >= 1)
			{
				if (board[row + 2][col - 1] == null
						|| board[row + 2][col - 1].getColour() != colour)
				{
					Point currentPoint = new Point(col - 1, row + 2);
					moveLocations.add(currentPoint);
				}
			}
			if (col <= 6)
			{
				if (board[row + 2][col + 1] == null
						|| board[row + 2][col + 1].getColour() != colour)
				{
					Point currentPoint = new Point(col + 1, row + 2);
					moveLocations.add(currentPoint);
				}
			}
		}
		if (row >= 2)
		{
			if (col >= 1)
			{
				if (board[row - 2][col - 1] == null
						|| board[row - 2][col - 1].getColour() != colour)
				{
					Point currentPoint = new Point(col - 1, row - 2);
					moveLocations.add(currentPoint);
				}
			}
			if (col <= 6)
			{
				if (board[row - 2][col + 1] == null
						|| board[row - 2][col + 1].getColour() != colour)
				{
					Point currentPoint = new Point(col + 1, row - 2);
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
