import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * Handles all movements and locations for Kings
 * 
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public class King extends Piece
{

	/**
	 * Constructs a new King object
	 * 
	 * @param myRow
	 *            the row the piece is on
	 * @param myCol
	 *            the column the piece is on
	 * @param myColour
	 *            the colour of the piece
	 * @param amAi
	 *            checks if the king belongs to the AI or a player
	 */
	public King(int myRow, int myCol, int myColour, boolean amAi)
	{
		// Call super
		super(myRow, myCol, myColour);
		isAi = amAi;
		// Select the piece's image depending on its colour
		if (myColour == 1)
			image = new ImageIcon("images\\whiteKing.png").getImage();
		else
			image = new ImageIcon("images\\blackKing.png").getImage();
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
		int minRow = Math.max(-1, -row);
		int maxRow = Math.min(1, 7 - row);
		int minCol = Math.max(-1, -col);
		int maxCol = Math.min(1, 7 - col);

		// Searches all of the positions on the board that are around the king
		ArrayList<Point> moveLocations = new ArrayList<Point>();
		for (int addRow = minRow; addRow <= maxRow; addRow++)
		{
			for (int addCol = minCol; addCol <= maxCol; addCol++)
			{
				if (!(addCol == 0 && addRow == 0))
				{
					if (board[row + addRow][col + addCol] == null
							|| board[row + addRow][col + addCol].getColour() != colour)
					{
						Point currentPoint = new Point(col + addCol, row
								+ addRow);
						moveLocations.add(currentPoint);
					}
				}

			}
		}

		// Check for castle
		if (!isAi && !getHasMoved())
		{
			if (getColour() == 1)
			{
				// White left rook
				if ((board[7][0] != null && !(board[7][0].getHasMoved())))
				{
					// If there are no pieces between them
					boolean pieceBlocking = false;
					boolean underAttack = false;
					for (int col = 1; col < 4; col++)
					{
						if (board[7][col] != null)
						{
							pieceBlocking = true;
						}
						// Check if king would castle through check
						for (int boardRow = 0; boardRow < 8; boardRow++)
							for (int boardCol = 0; boardCol < 8; boardCol++)
							{
								Point betweenCastle = new Point(col, 7);
								if (board[boardRow][boardCol] != null
										&& (boardRow != row || boardCol != col)
										&& board[boardRow][boardCol]
												.getColour() != colour
										&& board[boardRow][boardCol]
												.getMoveLocations(board, false)
												.contains(betweenCastle))
									underAttack = true;
							}
					}

					if (!pieceBlocking && !underAttack)
					{
						Point currentPoint = new Point(2, 7);
						moveLocations.add(currentPoint);
					}
				}
				// White right rook
				if ((board[7][7] != null && !(board[7][7].getHasMoved())))
				{
					// If there are no pieces between them and the area is not
					// under attack
					boolean pieceBlocking = false;
					boolean underAttack = false;
					for (int col = 5; col < 7; col++)
					{
						if (board[7][col] != null)
						{
							pieceBlocking = true;
						}
						for (int boardRow = 0; boardRow < 8; boardRow++)
							for (int boardCol = 0; boardCol < 8; boardCol++)
							{
								// Check if king would castle through check
								Point betweenCastle = new Point(col, 7);
								if (isForced == true
										&& board[boardRow][boardCol] != null
										&& (boardRow != row || boardCol != col)
										&& board[boardRow][boardCol]
												.getColour() != colour
										&& board[boardRow][boardCol]
												.getMoveLocations(board, false)
												.contains(betweenCastle))
									underAttack = true;
							}

					}
					if (!pieceBlocking && !underAttack)
					{
						Point currentPoint = new Point(6, 7);
						moveLocations.add(currentPoint);
					}
				}
			}
			else
			{
				// Black left rook
				if ((board[0][0] != null && !(board[0][0].getHasMoved())))
				{
					// If there are no pieces between them
					boolean pieceBlocking = false;
					boolean underAttack = false;
					for (int col = 1; col < 4; col++)
					{
						if (board[0][col] != null)
						{
							pieceBlocking = true;
						}
						for (int boardRow = 0; boardRow < 8; boardRow++)
							for (int boardCol = 0; boardCol < 8; boardCol++)
							{
								Point betweenCastle = new Point(col, 0);
								// If an enemy piece is attacking
								if (isForced == true
										&& board[boardRow][boardCol] != null
										&& (boardRow != row || boardCol != col)
										&& board[boardRow][boardCol]
												.getColour() != colour
										&& board[boardRow][boardCol]
												.getMoveLocations(board, false)
												.contains(betweenCastle))
									underAttack = true;
							}
					}

					if (!pieceBlocking && !underAttack)
					{
						Point currentPoint = new Point(2, 0);
						moveLocations.add(currentPoint);
					}
				}
				// Black right rook
				if ((board[0][7] != null && !(board[0][7].getHasMoved())))
				{
					// If there are no pieces between them
					boolean pieceBlocking = false;
					boolean underAttack = false;
					for (int col = 5; col < 7; col++)
					{
						if (board[0][col] != null)
						{
							pieceBlocking = true;
						}
						for (int boardRow = 0; boardRow < 8; boardRow++)
							for (int boardCol = 0; boardCol < 8; boardCol++)
							{
								// Check if king would castle through check
								Point betweenCastle = new Point(col, 0);
								if (isForced == true
										&& board[boardRow][boardCol] != null
										&& (boardRow != row || boardCol != col)
										&& board[boardRow][boardCol]
												.getColour() != colour
										&& board[boardRow][boardCol]
												.getMoveLocations(board, false)
												.contains(betweenCastle))
									underAttack = true;
							}
					}

					if (!pieceBlocking && !underAttack)
					{
						Point currentPoint = new Point(6, 0);
						moveLocations.add(currentPoint);
					}
				}
			}
		}

		// If the piece may be forced to move, check for it
		for (Point currLoc : moveLocations)
		{
			// Check each valid location to see if it contains a piece
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
		return moveLocations;
	}
	
	public boolean isAi()
	{
		return isAi;
	}
}
