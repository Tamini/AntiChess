import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates the main Frame of the Program Anti-Chess
 * 
 * @author Bellamy, Tristan, and Mark
 * 
 */
public class AntiChessMain extends JFrame implements ActionListener
{

	// Declaring menu options
	private JMenuItem aboutOption;

	// Declaring JPanels
	public Board myBoard;

	public ChessPanel chessArea;

	/**
	 * The main class of the Anti chess game which includes all the images and
	 * JPanels which the user will be using.
	 */
	public AntiChessMain()
	{
		super("AntiChess");
		setResizable(true);

		// Position in the middle of the window
		setLocation(0, 0);

		// Add in an Icon - Black King
		setIconImage(new ImageIcon("images\\blackKing.png").getImage());

		// initializing JPanels
		JPanel rightPanel = new JPanel();
		rightPanel.setBackground(new Color(32, 178, 170));
		rightPanel.setLayout(new BorderLayout());

		setLayout(new BorderLayout());

		// Set up the JPanels on top of each other so we can alternate when
		// needed
		myBoard = new Board(this);
		
		add(myBoard, BorderLayout.WEST);
		myBoard.setVisible(false);

		chessArea = new ChessPanel(this);
		add(chessArea, BorderLayout.CENTER);
		
		

		// Add in the menus
		addMenus();
	}

	/**
	 * Adds in all the menus at the top of the game which the user can use to
	 * look at the about tab or start a new game
	 */
	private void addMenus()
	{
		// Set up all the menus
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');

		JMenu helpMenu = new JMenu("Help");

		aboutOption = new JMenuItem("About");
		aboutOption.addActionListener(this);
		helpMenu.add(aboutOption);
		helpMenu.setMnemonic('H');

		JMenuItem newOption = new JMenuItem("New (vs Computer)");
		newOption.addActionListener(new ActionListener() {
			/**
			 * Responds to the New Menu choice
			 * 
			 * @param event
			 *            The event that selected this menu option
			 */
			public void actionPerformed(ActionEvent event)
			{
				myBoard.newGame(true);
			}
		});
		JMenuItem newOption2 = new JMenuItem("New (vs Player)");
		newOption2.addActionListener(new ActionListener() {
			/**
			 * Responds to the New Menu choice
			 * 
			 * @param event
			 *            The event that selected this menu option
			 */
			public void actionPerformed(ActionEvent event)
			{
				myBoard.newGame(false);
			}
		});
		// Add menu tabs to menu
		menuBar.add(gameMenu);
		gameMenu.add(newOption);
		gameMenu.add(newOption2);
		gameMenu.add(aboutOption);

		setJMenuBar(menuBar);
	}

	/**
	 * Sets the frame to the AntiChess Frame and makes it visible to the user
	 * 
	 * @param args
	 *            Arguments for main method
	 */

	public static void main(String[] args)
	{
		AntiChessMain frame = new AntiChessMain();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * 
	 * Based of which tab the user clicked it will determine which information
	 * to display or if to create a new game
	 * 
	 * @param event
	 *            The action which is performed by the user
	 */
	public void actionPerformed(ActionEvent event)
	{

		// Displays the about tab on the screen
		if (event.getSource() == aboutOption)
		{
			JOptionPane
					.showMessageDialog(
							myBoard,
							"Anti-Chess by Bellamy, Mark and Tristan"
									+ "\n\u00a9 2013"
									+ "\nSpecial thanks and Images:  \nhttp://www.thechessdrum.net/chessacademy/CA_Castling.html "
									+ "\nhttp://en.wikipedia.org/wiki/En_passant"
									+ "\nhttp://www.chessvariants.org/d.chess/chess.html"
									+ "\nhttp://www.cursors-4u.com/cursor/2010/04/18/chess-white-queen.html"
									+ "\nMr. Ridout", "About Anti-Chess",
							JOptionPane.INFORMATION_MESSAGE);
		}

	}
}
