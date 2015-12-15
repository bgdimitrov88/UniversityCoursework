package Game;

/**
 *The Game class holds the game grid and all the methods used to manipulate it.
 *
 * @author HUD
 */
public class Game {

    private String message;
    private String move;
    private boolean sendMsg = false;
    private boolean sendMove = false;
    private char[] grid;
    private boolean myTurn = false;
    private boolean iAmX = false;

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
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Initializes the turn variable
     *
     * @param myTurn is it the user's turn
     */
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    /**
     * Checks if the Networking module has to send a chat message to the opponent
     *
     * @return should a message be send
     */
    public boolean isSendMsg() {
        return sendMsg;
    }

    /**
     * Sets if there is a pending chat message to be send
     *
     * @param sendMsg is a message waiting to be send
     */
    public void setSendMsg(boolean sendMsg) {
        this.sendMsg = sendMsg;
    }

    /**
     * Gets the chat message to be send
     *
     * @return the message to be send
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the chat message to be send
     *
     * @param message the message to be send
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the player's move
     *
     * @return player's move
     */
    public String getMove() {
        return move;
    }

    /**
     * Sets the player's move
     *
     * @param move player's move
     */
    public void setMove(String move) {
        this.move = move;
    }

    /**
     * Checks if the Networking module has to send the player's move to the opponent
     *
     * @return should a move be send
     */
    public boolean isSendMove() {
        return sendMove;
    }

    /**
     * Sets if there is a pending player's move to be send to the opponent
     *
     * @param sendMove is a move waiting to be send
     */
    public void setSendMove(boolean sendMove) {
        this.sendMove = sendMove;
    }

    /**
     * Changes the game board according to the move made
     *
     * @param i grid cell to be changed
     * @param value value to which the grid cell will be changed
     */
    public void changeBoard(int i, char value) {
        grid[i] = value;
    }

    /**
     * Gets the game grid
     *
     * @return the game grid
     */
    public char[] getGrid() {
        return grid;
    }

    /**
     * Checks what sign the player is using
     *
     * @return is the player an O or a X
     */
    public boolean isiAmX() {
        return iAmX;
    }

    /**
     * Sets the sign the player is using
     *
     * @param iAmX is the player using X
     */
    public void setiAmX(boolean iAmX) {
        this.iAmX = iAmX;
    }

    /**
     * Clears the game grid
     */
    public void clearGrid() {
        for(int i = 0; i < 9; i++){
            grid[i] = 'E';
        }
    }
}
