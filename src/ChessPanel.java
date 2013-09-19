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

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main menu of the game which includes all clickable menus and allows the
 * player to start a new game, read instructions, and quit
 * 
 * @author Bellamy Too
 * 
 */
public class ChessPanel extends JPanel
{

	// Declaring needed variables such as images and frames
	private Image menuImage;

	private Image main, instructions1, instructions2, instructions3,
			instructions4, selectedMenu;

	private Image chessPiece;

	private Point mousePoint;

	private String selectedMenuName;

	private Point firstPoint;

	private AntiChessMain parentFrame;

	/**
	 * Creates the main menu of the game which includes all clickable menus and
	 * allows the player to start a new game, read instructions, and quit
	 * 
	 * @param parent
	 *            the frame it is using
	 */
	public ChessPanel(AntiChessMain parent)
	{

		// Setting up variables and making this Panel the one the user can use
		parentFrame = parent;

		setPreferredSize(new Dimension(1024, 740));
		setBackground(new Color(0, 0, 0));
		setFocusable(true);
		requestFocusInWindow();
		repaint();
		this.parentFrame = parent;

		// Setting up variables for the menus and the cursor
		menuImage = new ImageIcon("images/MainMenu.png").getImage();

		instructions1 = new ImageIcon("images/Instructions1.png").getImage();
		instructions2 = new ImageIcon("images/Instructions2.png").getImage();
		instructions3 = new ImageIcon("images/Instructions3.png").getImage();
		instructions4 = new ImageIcon("images/Instructions4.png").getImage();

		chessPiece = new ImageIcon("images/customCursor.gif").getImage();

		selectedMenu = main;
		selectedMenuName = "main";
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

		// Implementing mouse usage in the program
		this.addMouseListener(new MouseHandler());
		this.addMouseMotionListener(new MouseMotionHandler());

	}

	/**
	 * Will draw all images and menus in the game
	 * 
	 * @param g
	 *            The place where we will be drawing images
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// Draws the menus
		g.drawImage(menuImage, 0, 0, this);
		g.drawImage(selectedMenu, 0, 0, this);

		// Draws the custom cursor wherever it goes
		g.drawImage(chessPiece, mousePoint.x - chessPiece.getWidth(this) / 3,
				mousePoint.y - chessPiece.getHeight(this) / 3, this);

	}

	/**
	 * Decides which menu the user has clicked and will accordingly send the
	 * user to whichever page they want
	 * 
	 * @param x
	 *            The last clicked X position of the mouse
	 * @param y
	 *            The last clicked Y position of the mouse
	 * @param x2
	 *            The current X Position of the mouse
	 * @param y2
	 *            The current Y position of the mouse
	 */
	private void decideMenu(int x, int y, int x2, int y2)
	{

		// Deciding which menu to send the user to based on where they clicked
		// on the image
		if (selectedMenuName.equals("main"))
		{

			// Links all the instruction pages together using the selected menu
			// variables
			if (x > 19 && y > 216 && x2 < 307 && y2 < 254)
			{

				selectedMenu = instructions1;
				selectedMenuName = "instructions1";

				repaint();
			}

			// Quits if the exit button is pressed
			if (x > 20 && y > 390 && x2 < 105 && y2 < 432)
			{
				System.exit(0);
			}
		}

		// The chain of instructions
		if (selectedMenuName.equals("instructions1"))
		{

			if (x > 865 && y > 585 && x2 < 956 && y2 < 653)
			{
				selectedMenu = instructions2;
				selectedMenuName = "instructions2";
				repaint();

			}

		}

		else if (selectedMenuName.equals("instructions2"))
		{

			if (x > 865 && y > 585 && x2 < 956 && y2 < 653)
			{
				selectedMenu = instructions3;
				selectedMenuName = "instructions3";
				repaint();

			}

		}

		else if (selectedMenuName.equals("instructions3"))
		{

			if (x > 865 && y > 585 && x2 < 956 && y2 < 653)
			{
				selectedMenu = instructions4;
				selectedMenuName = "instructions4";
				repaint();

			}

		}
		// returns to the menu after you have finished looking at the
		// instructions
		else if (selectedMenuName.equals("instructions4"))
		{

			if (x > 865 && y > 585 && x2 < 956 && y2 < 653)
			{
				selectedMenu = main;
				selectedMenuName = "main";
				repaint();

			}

		}

	}

	/**
	 * Based on the movement of the mouse we will redraw the cursor on the point
	 * 
	 * @param event
	 *            The movement of the mouse by the user
	 */
	public void mouseMoved(MouseEvent event)
	{

		// Sets the current point every time the mouse is moved
		Point currentPoint = event.getPoint();

		mousePoint = currentPoint;
		repaint();

	}

	/**
	 * A private inner class to handle the mouse movements and clicks
	 * 
	 * @author Bellamy Too
	 * 
	 */
	private class MouseHandler extends MouseAdapter
	{

		/**
		 * Determines what to do when the mouse is clicked
		 * 
		 * @param event
		 *            The click down on the mouse
		 */

		public void mousePressed(MouseEvent event)
		{

			// Redefines the position of the mouse for the custom cursor
			Point currentPoint = event.getPoint();

			firstPoint = currentPoint;
			repaint();

		}

		/**
		 * Checks the release of the mouse to decide what to do
		 * 
		 * @param event
		 *            The release of the click
		 */
		public void mouseReleased(MouseEvent event)
		{

			// Redefines the position of the mouse for the custom cursor
			Point currentPoint = event.getPoint();

			// Uses the decide menu method to decide which menu to show the user
			decideMenu(firstPoint.x, firstPoint.y, currentPoint.x,
					currentPoint.y);

			// If it is the new game button the user clicked it will switch the
			// JPanels and make the board usable to the user and starts a new
			// game
			if (firstPoint.x > 20 && firstPoint.y > 149 && currentPoint.x < 288
					&& currentPoint.y < 191)

			{
				parentFrame.chessArea.setVisible(false);

				parentFrame.myBoard.setVisible(true);

				repaint();

			}

		}
	}

	/**
	 * An inner class to handle the mouse movement
	 * 
	 * @author Bellamy Too
	 * 
	 */
	class MouseMotionHandler implements MouseMotionListener
	{
		/**
		 * Based on the movement of the mouse we will redraw the cursor on the
		 * point
		 * 
		 * @param event
		 *            The movement of the mouse by the user
		 */
		public void mouseMoved(MouseEvent event)
		{

			// Sets the current point every time the mouse is moved
			Point currentPoint = event.getPoint();

			mousePoint = currentPoint;
			repaint();
		}

		@Override
		/**
		 * If the mouse is clicked and held and then the mouse is moved then will update the position for the custom cursor
		 *
		 * @param event
		 * 			The drag of the mouse
		 */
		public void mouseDragged(MouseEvent event)
		{
			// Sets the current point every time the mouse is moved
			Point currentPoint = event.getPoint();

			mousePoint = currentPoint;
			repaint();

		}

	}

}
