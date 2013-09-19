import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Handles the AI and moves
 * @author Tristan Amini
 * @version June 15, 2013
 */
public class AiPlayer extends Player
{
	private int difficulty;

	/**
	 * Constructs the AiPlayer object
	 * @param myName the name of the AI
	 * @param myColour the colour of the AI
	 * @param myDifficulty the AI's difficulty
	 */
	public AiPlayer(String myName, int myColour, int myDifficulty)
	{
		// Call Player constructor
		super(myName, myColour);
		difficulty = myDifficulty;
	}

	/**
	 * Makes the best move found for this turn
	 * 
	 * @param board
	 *            the board the piece is being moved in
	 * @param lastMove
	 *            the last move made
	 */
	public void makeMove(Piece[][] board, Point[] lastMove)
	{
		Move bestMove = selectMove(board);
		Point loc = bestMove.getDestination();
		// Check for tie
		if (bestMove == null)
			return;
		bestMove.getPiece().move(loc, board);
		lastMove[0] = loc;
	}

	/**
	 * Looks through the possible moves for this turn and selects the best one
	 * 
	 * @param board
	 *            the board the pieces exist in
	 * @return the best move for the AI to make
	 */
	public Move selectMove(Piece[][] board)
	{
		PriorityQueue<Move> allMoves = new PriorityQueue<Move>();
		// Score all pieces
		// Look at valid moves of all pieces of current player to check force
		// takes

		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (board[row][col] != null
						&& board[row][col].getColour() == colour)
				{
					// Find the best move for the current piece
					board[row][col].findBestMove(board, difficulty * 4);
					// Add all moves to the list of possible moves
					ArrayList<Move> tempMove = board[row][col].getMoves();
					// Add all possible moves to the queue
					for (Move currMove : tempMove)
					{
						allMoves.add(currMove);
					}

				}
			}
		}
		// Check for tie
		if (allMoves.size() == 0)
			tied = true;
		return allMoves.remove();
	}
}
