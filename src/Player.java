import java.awt.Point;

/**
 * Handles all player related variables and ties
 * @author Tristan, Bellamy and Mark
 * @version June 15, 2013
 */
public class Player
{
        private String name;
        protected int numPieces;
        protected int colour;
        static protected boolean tied;
        
        /**
         * Constructs a new player object
         * @param myName
         * @param myColour
         */
        public Player(String myName, int myColour)
        {
                // Default number of pieces
                numPieces = 16;
                name = myName;
                colour = myColour;
                tied = false;
        }
        
        /**
         * Returns the name of the player
         * @return the name of this player
         */
        public String getName()
        {
                return name;
        }

        /**
         * Used by the AI to move its pieces
         * @param board the board to move on
         * @param lastMove the last move that was made
         */
        public void makeMove(Piece[][] board, Point[] lastMove)
        {
                // Only hear so AI player can use makeMove
                
        }
}
