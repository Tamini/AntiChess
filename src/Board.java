import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Handles all movements and player actions during the game
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public class Board extends JPanel
{
	private Piece[][] board;

	private int turn;

	private Point[] lastMove;

	private static Image boardBackground = new ImageIcon(
			"images\\chessboardPlainBig.png").getImage();

	private static Image panelBackground = new ImageIcon("images/boardMenu.png")
			.getImage();

	// p = pawn, r = rook, n = knight, b = bishop, k = king, q = queen
	private static final char[] pieceSet = { 'p', 'p', 'p', 'p', 'p', 'p', 'p',
			'p', 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r' };

	private Piece selectedPiece;

	private Player[] players;

	private boolean[] areAI;

	private boolean gameOver;

	private Image menuImage;

	private Image main, instructions1, instructions2, instructions3,
			instructions4, selectedMenu;

	private Image chessPiece;

	private Point firstPoint;

	private Point mousePoint;

	private String selectedMenuName;

	private AntiChessMain parentFrame;

	private boolean aiOn = false;

	public boolean instructionsCalled = false;

	/**
	 * Constructs a new board object to manage the game
	 * 
	 * @param parent
	 *            the frame the board exists in
	 */
	public Board(AntiChessMain parent)
	{
		setPreferredSize(new Dimension(1024, 740));
		requestFocusInWindow();

		// Declares the frames to utilize the Main Class
		parentFrame = parent;
		this.parentFrame = parent;

		// Declaring images
		instructions1 = new ImageIcon("images/Instructions1.png").getImage();
		instructions2 = new ImageIcon("images/Instructions2.png").getImage();
		instructions3 = new ImageIcon("images/Instructions3.png").getImage();
		instructions4 = new ImageIcon("images/Instructions4(inGame).png")
				.getImage();

		// Setting the cursor image
		chessPiece = new ImageIcon("images/customCursor.gif").getImage();

		// Setting the menu names to show which menus to draw
		selectedMenu = main;
		selectedMenuName = "main";

		// Keeps track of the mouse position
		firstPoint = new Point();
		mousePoint = new Point();

		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		parent.getContentPane().setCursor(blankCursor);

		newGame(!aiOn);
		// Add mouse listeners
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseMotionHandler());

		aiOn = false;
	}

	/**
	 * Resets all variables that change over the course of the game
	 * 
	 * @param whether
	 *            an AI is playing or not
	 */
	public void newGame(boolean aiOn)
	{
		// Set up all required variables for a new game
		gameOver = false;
		board = new Piece[8][8];
		areAI = new boolean[2];

		// Record if ai is playing
		if (aiOn)
		{
			areAI[0] = false;
			areAI[1] = true;
		}
		else
		{
			areAI[0] = false;
			areAI[1] = false;
		}
		lastMove = new Point[1];
		turn = 1;
		selectedPiece = null;
		instructionsCalled = false;
		// Set up players
		players = new Player[2];
		// Check if players are AI
		if (aiOn)
		{
			if (areAI[0])
				players[0] = new AiPlayer("White (You)", 1, 1);
			else
				players[0] = new Player("White (You)", 1);
			if (areAI[1])
				players[1] = new AiPlayer("Computer", 2, 1);
			else
				players[1] = new Player("Computer", 2);
		}
		else
		{
			players[0] = new Player("White Player", 1);
			players[1] = new Player("Black Player", 2);
		}
		// Set up black pieces
		int pieceCount = 0;
		for (int row = 1; row > -1; row--)
		{
			for (int col = 0; col < 8; col++)
			{
				if (pieceSet[pieceCount] == 'p')
				{
					board[row][col] = new Pawn(row, col, 2);
				}
				else if (pieceSet[pieceCount] == 'r')
				{
					board[row][col] = new Rook(row, col, 2);
				}
				else if (pieceSet[pieceCount] == 'n')
				{
					board[row][col] = new Knight(row, col, 2);
				}
				else if (pieceSet[pieceCount] == 'b')
				{
					board[row][col] = new Bishop(row, col, 2);
				}
				else if (pieceSet[pieceCount] == 'k')
				{
					board[row][col] = new King(row, col, 2, areAI[1]);
				}
				else
				{
					board[row][col] = new Queen(row, col, 2);
				}
				pieceCount++;
			}
		}
		pieceCount = 0;
		// Set up white pieces
		for (int row = 6; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (pieceSet[pieceCount] == 'p')
				{
					board[row][col] = new Pawn(row, col, 1);
				}
				else if (pieceSet[pieceCount] == 'r')
				{
					board[row][col] = new Rook(row, col, 1);
				}
				else if (pieceSet[pieceCount] == 'n')
				{
					board[row][col] = new Knight(row, col, 1);
				}
				else if (pieceSet[pieceCount] == 'b')
				{
					board[row][col] = new Bishop(row, col, 1);
				}
				else if (pieceSet[pieceCount] == 'k')
				{
					board[row][col] = new King(row, col, 1, areAI[0]);
				}
				else
				{
					board[row][col] = new Queen(row, col, 1);
				}
				pieceCount++;
			}
		}
		repaint();
	}

	/**
	 * Changes the current player and makes the AI move
	 */
	public void changePlayer()
	{
		selectedPiece = null;
		// Reset can takes
		if (turn == 1)
			turn++;
		else
			turn--;

		// Repaint the board before AI moves
		paintImmediately(new Rectangle(0, 0, 600, 600));
		// Counts for number of pieces remaining (for win purposes)
		int[] pieceCounter = new int[3];
		pieceCounter[1] = 0;
		pieceCounter[2] = 0;
		boolean hasMoves = false;
		// Look at valid moves of all pieces of current player to check force
		// takes also count number of pieces remaining
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (board[row][col] != null)
				{
					if (board[row][col].getColour() == turn)
					{
						pieceCounter[turn]++;
						ArrayList<Point> tempArray = board[row][col]
								.getMoveLocations(board, true);
						if (tempArray.size() > 0)
							hasMoves = true;
					}
					else
					{
						if (turn == 1)
							pieceCounter[2]++;
						else
							pieceCounter[1]++;
					}

				}
			}
		}
		// One side is out of pieces
		if (pieceCounter[1] == 0)
			win(0);
		else if (pieceCounter[2] == 0)
			win(1);
		// Make AI move
		else if (!hasMoves)
			tie();
		else if (areAI[turn - 1] && !gameOver)
		{
			players[turn - 1].makeMove(board, lastMove);
			changePlayer();
		}
	}

	/**
	 * The paint component that draws the graphics on the screen
	 * 
	 * @param g
	 *            the Graphics to draw in
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(panelBackground, 0, 0, null);
		g.drawImage(boardBackground, 0, 0, null);
		g.setFont(new Font("BIG", Font.BOLD, 19));

		// Draw box to display who's turn it is
		if (turn == 1)
		{
			g.setColor(Color.BLACK);
			g.fillRect(700, 80, 225, 25);
			g.setColor(Color.WHITE);
		}
		else
		{
			g.setColor(Color.BLACK);
			g.fillRect(700, 80, 225, 25);
			g.setColor(Color.WHITE);
		}
		g.drawString("Current Player: " + players[turn - 1].getName(), 700, 100);
		// Highlight force takes
		// Run through board and highlight all pieces forced to move
		g.setColor(Color.RED);
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (board[row][col] != null
						&& board[row][col].getColour() == turn
						&& board[row][col].forcedMove())
				{
					g.fillRect(board[row][col].getLocation().x * 71 + 15,
							board[row][col].getLocation().y * 71 + 19, 70, 70);
				}
			}
		}
		// Draw pink highlights to display last move
		if (lastMove[0] != null)
		{
			g.setColor(Color.pink);
			g.fillRect(lastMove[0].x * 71 + 15, lastMove[0].y * 71 + 19, 70, 70);
		}
		// Show valid moves for selected piece
		if (selectedPiece != null)
		{
			showMoves(g);
		}

		// Draw pieces
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				if (board[row][col] != null)
					board[row][col].draw(g);
			}
		}
		// If they want to see the instructions it will draw and make everything
		// else unable to be clicked
		if (instructionsCalled == true)
		{

			g.drawImage(menuImage, 0, 0, this);
			g.drawImage(selectedMenu, 0, 0, this);

		}
		// Draws the cursor based on where the mouse is
		g.drawImage(chessPiece, mousePoint.x - chessPiece.getWidth(this) / 3,
				mousePoint.y - chessPiece.getHeight(this) / 3, this);
	}

	/**
	 * Highlights in yellow the valid moves for the current piece
	 * 
	 * @param g
	 *            the Graphics to draw in
	 */
	public void showMoves(Graphics g)
	{
		// Get the valid squares to move to
		ArrayList<Point> validLocations = selectedPiece.getMoveLocations(board,
				true);
		if (validLocations == null)
			return;
		// Highlight all valid squares
		for (Point currPoint : validLocations)
		{
			g.setColor(Color.YELLOW);
			g.fillRect(currPoint.x * 71 + 15, currPoint.y * 71 + 19, 70, 70);
		}
		// Fill in square where selected piece is
		g.setColor(Color.ORANGE);
		Point selectedLoc = selectedPiece.getLocation();
		g.fillRect(selectedLoc.x * 71 + 15, selectedLoc.y * 71 + 19, 70, 70);

	}

	/**
	 * Displays tie game message and options to quit or restart
	 */
	public void tie()
	{
		// Display tie message
		int reply = JOptionPane.showConfirmDialog(null, "It's a tie!"
				+ "\nWould you like to play again?", "Game Over!",
				JOptionPane.YES_NO_OPTION);

		if (reply == JOptionPane.YES_OPTION)
		{
			// Ask if they want to play with AI again
			int secondReply = JOptionPane.showConfirmDialog(null,
					"Would you like to play with AI on?", "New Game",
					JOptionPane.YES_NO_OPTION);
			if (secondReply == JOptionPane.YES_OPTION)
			{
				aiOn = true;
			}
			else
				aiOn = false;
			newGame(aiOn);
		}

		else
		{
			System.exit(0);
		}
	}

	/**
	 * Displays winning message (AI or player)
	 * 
	 * @param winner
	 *            the player/AI that won
	 */
	public void win(int winner)
	{
		// Display win message
		int reply = JOptionPane.showConfirmDialog(null,
				"The winner is: " + players[winner].getName()
						+ "\nWould you like to play again?", "Game Over!",
				JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION)
		{

			// Ask if they want to play with AI again
			int secondReply = JOptionPane.showConfirmDialog(null,
					"Would you like to play with AI on?", "New Game",
					JOptionPane.YES_NO_OPTION);
			if (secondReply == JOptionPane.YES_OPTION)
			{
				aiOn = true;
			}
			else
				aiOn = false;
			newGame(aiOn);
		}
		else
		{
			System.exit(0);
		}
	}

	// Inner class to handle mouse events
	private class MouseHandler extends MouseAdapter
	{
		public void mousePressed(MouseEvent event)
		{
			Point currentPoint = event.getPoint();

			firstPoint = currentPoint;
			repaint();
		}

		public void mouseReleased(MouseEvent event)
		{
			Point selectedPoint = event.getPoint();

			// Deciding which menu to send the user to based on where they
			// clicked
			// on the image
			if (selectedPoint.getX() > 660 && selectedPoint.getY() > 415
					&& selectedPoint.getX() < 780 && selectedPoint.getY() < 482
					&& instructionsCalled == false)

			{
				// Sets the correct JPanel visible for the user to see based on
				// what they clicked
				parentFrame.chessArea.setVisible(true);

				parentFrame.myBoard.setVisible(false);
				newGame(aiOn);
				repaint();

			}

			// Exits if the player wants to quit
			else if (selectedPoint.getX() > 870 && selectedPoint.getY() > 509
					&& selectedPoint.getX() < 994 && selectedPoint.getY() < 569
					&& instructionsCalled == false)
			{
				System.exit(0);
			}
			// Links all the instruction pages together using the selected menu
			// variables
			else if (selectedPoint.getX() > 660 && selectedPoint.getY() > 506
					&& selectedPoint.getX() < 782 && selectedPoint.getY() < 569)
			{
				selectedMenu = instructions1;
				selectedMenuName = "instructions1";
				instructionsCalled = true;

				repaint();
			}
			if (selectedMenuName.equals("instructions1"))
			{

				if (selectedPoint.getX() > 865 && selectedPoint.getY() > 585
						&& selectedPoint.getX() < 956
						&& selectedPoint.getY() < 653)
				{
					selectedMenu = instructions2;
					selectedMenuName = "instructions2";
					repaint();

				}

			}

			else if (selectedMenuName.equals("instructions2"))
			{

				if (selectedPoint.getX() > 865 && selectedPoint.getY() > 585
						&& selectedPoint.getX() < 956
						&& selectedPoint.getY() < 653)
				{
					selectedMenu = instructions3;
					selectedMenuName = "instructions3";
					repaint();

				}

			}

			else if (selectedMenuName.equals("instructions3"))
			{

				if (selectedPoint.getX() > 865 && selectedPoint.getY() > 585
						&& selectedPoint.getX() < 956
						&& selectedPoint.getY() < 653)
				{
					selectedMenu = instructions4;
					selectedMenuName = "instructions4(inGame)";
					repaint();

				}

			}
			else if (selectedMenuName.equals("instructions4(inGame)"))
			{

				if (selectedPoint.getX() > 865 && selectedPoint.getY() > 585
						&& selectedPoint.getX() < 956
						&& selectedPoint.getY() < 653)
				{
					selectedMenu = main;
					parentFrame.myBoard.setVisible(true);
					instructionsCalled = false;
					repaint();

				}

			}
			// If they resign, it asks if they want to play again
			else if (selectedPoint.getX() > 865 && selectedPoint.getY() > 421
					&& selectedPoint.getX() < 987 && selectedPoint.getY() < 487
					&& instructionsCalled == false && aiOn)
			{

				int reply = JOptionPane.showConfirmDialog(null,
						"The winner is: The Computer!"
								+ "\nWould you like to play again?",
						"Game Over!", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION)
				{
					// Ask if they want to play with AI again
					int secondReply = JOptionPane.showConfirmDialog(null,
							"Would you like to play with AI on?", "New Game",
							JOptionPane.YES_NO_OPTION);
					if (secondReply == JOptionPane.YES_OPTION)
					{
						aiOn = true;
					}
					else
						aiOn = false;
					newGame(aiOn);
				}
				else
				{
					System.exit(0);
				}
			}
			// Resign for player vs player
			else if (selectedPoint.getX() > 865 && selectedPoint.getY() > 421
					&& selectedPoint.getX() < 987 && selectedPoint.getY() < 487
					&& instructionsCalled == false && !aiOn)
			{
				int reply = JOptionPane.showConfirmDialog(null,
						"The winner is: " + players[(turn) % 2].getName()
								+ "\nWould you like to play again?",
						"Game Over!", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION)
				{
					// Ask if they want to play with AI again
					int secondReply = JOptionPane.showConfirmDialog(null,
							"Would you like to play with AI on?", "New Game",
							JOptionPane.YES_NO_OPTION);
					if (secondReply == JOptionPane.YES_OPTION)
					{
						aiOn = true;
					}
					else
						aiOn = false;
					newGame(aiOn);
				}
				else
				{
					System.exit(0);
				}
			}
			// Make sure point is on board and it's not AI's turn and only if
			// the instructions aren't showing
			if (selectedPoint.getX() < 600 && selectedPoint.getY() < 600
					&& !(players[turn - 1] instanceof AiPlayer)
					&& instructionsCalled == false)
			{

				selectedPoint.x /= 75;
				selectedPoint.y /= 75;
				// No piece currently selected or not on the area
				if (selectedPiece == null
						|| selectedPiece.getMoveLocations(board, true) != null
						&& !selectedPiece.getMoveLocations(board, true)
								.contains(selectedPoint))
				{
					// Figure out which square/piece was clicked on
					selectedPiece = board[selectedPoint.y][selectedPoint.x];
					// Selected opponents piece
					if (selectedPiece != null
							&& selectedPiece.getColour() != turn)
						selectedPiece = null;
				}
				else
				{
					// Deduct number of pieces left for player
					if (board[selectedPoint.y][selectedPoint.x] != null)
					{
						selectedPiece.move(selectedPoint, board);
						// Other player's turn
						changePlayer();
					}
					else
					{
						selectedPiece.move(selectedPoint, board);
						lastMove[0] = selectedPoint;
						// Other player's turn
						changePlayer();
					}

				}
			}
			repaint();
		}
	}

	// Inner Class to handle mouse movements
	private class MouseMotionHandler implements MouseMotionListener
	{
		public void mouseMoved(MouseEvent event)
		{
			Point currentPoint = event.getPoint();

			mousePoint = currentPoint;
			repaint();

		}

		public void mouseDragged(MouseEvent event)
		{
			Point currentPoint = event.getPoint();

			mousePoint = currentPoint;
			repaint();
		}
	}
}
