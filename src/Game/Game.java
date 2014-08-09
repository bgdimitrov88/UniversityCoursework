package Game;

/**
 *The Game class holds the game grid and all the methods used to manipulate it.
 *
 * @author HUD
 */
public class Game {

    private static String message;
    private static String move;
    private static boolean sendMsg = false;
    private static boolean sendMove = false;
    private static char[] grid;
    private static boolean myTurn = false;
    private static boolean iAmX = false;

    private final int[][] winCombinations = new int[][]{
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //horizontal wins
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //virticle wins
        {0, 4, 8}, {2, 4, 6} //diagonal wins };
    };

    /**
     * Constructs a new instance of Game and initializes the game grid
     */
    public Game() {
        grid = new char[9];
        for(int i = 0; i < 9; i++)
            grid[i] = 'E';
    }

    /**
     * Checks if the game is won by a player
     *
     * @return is the game won
     */
    public boolean checkWin() {
       
        for(int i=0; i<=7; i++){
			if( grid[winCombinations[i][0]] == grid[winCombinations[i][1]]
			&& grid[winCombinations[i][1]] == grid[winCombinations[i][2]]
			&& !(grid[winCombinations[i][0]] == 'E')) {
                                return true;
			}
        }
        
        return false;
    }

    /**
     * Checks who has to make the next move
     *
     * @return whose turn it is
     */
    public static boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Initializes the turn variable
     *
     * @param myTurn is it the user's turn
     */
    public static void setMyTurn(boolean myTurn) {
        Game.myTurn = myTurn;
    }

    /**
     * Checks if the Networking module has to send a chat message to the opponent
     *
     * @return should a message be send
     */
    public static boolean isSendMsg() {
        return sendMsg;
    }

    /**
     * Sets if there is a pending chat message to be send
     *
     * @param sendMsg is a message waiting to be send
     */
    public static void setSendMsg(boolean sendMsg) {
        Game.sendMsg = sendMsg;
    }

    /**
     * Gets the chat message to be send
     *
     * @return the message to be send
     */
    public static String getMessage() {
        return message;
    }

    /**
     * Sets the chat message to be send
     *
     * @param message the message to be send
     */
    public static void setMessage(String message) {
        Game.message = message;
    }

    /**
     * Gets the player's move
     *
     * @return player's move
     */
    public static String getMove() {
        return move;
    }

    /**
     * Sets the player's move
     *
     * @param move player's move
     */
    public static void setMove(String move) {
        Game.move = move;
    }

    /**
     * Checks if the Networking module has to send the player's move to the opponent
     *
     * @return should a move be send
     */
    public static boolean isSendMove() {
        return sendMove;
    }

    /**
     * Sets if there is a pending player's move to be send to the opponent
     *
     * @param sendMove is a move waiting to be send
     */
    public static void setSendMove(boolean sendMove) {
        Game.sendMove = sendMove;
    }

    /**
     * Changes the game board according to the move made
     *
     * @param i grid cell to be changed
     * @param value value to which the grid cell will be changed
     */
    public static void changeBoard(int i, char value) {
        grid[i] = value;
    }

    /**
     * Gets the game grid
     *
     * @return the game grid
     */
    public static char[] getGrid() {
        return grid;
    }

    /**
     * Checks what sign the player is using
     *
     * @return is the player an O or a X
     */
    public static boolean isiAmX() {
        return iAmX;
    }

    /**
     * Sets the sign the player is using
     *
     * @param iAmX is the player using X
     */
    public static void setiAmX(boolean iAmX) {
        Game.iAmX = iAmX;
    }

    /**
     * Clears the game grid
     */
    public static void clearGrid() {
        for(int i = 0; i < 9; i++){
            grid[i] = 'E';
        }
    }
}
