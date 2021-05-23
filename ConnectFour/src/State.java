
// This class defines the State of a node. The state holds large amounts of detail relevant to the current state of the game.

public class State {

    // The 2D array that includes the current ConnectFour board
	int [][] board;

    // This int indicate which player just made the last move
	int player;

    // visitCounter counts the amount of times we've visited this node
	int visitCounter;

    // wins counts the amount of times we've won when visiting this node 
	int wins;

    // move includes the move to be played, with the move represented as the column to drop a marker
	int move;
	int movesPerformed;
	int resultAlready = 0;
	

    // Constructor for State
	public State(int [][] originalBoard, int move, int player, int movesPerformed) {
		this.board = originalBoard;
		this.move = move;
		this.movesPerformed = movesPerformed;
		this.player = player;
	}
	
	public boolean addMove(int [][]board, int move, int player) {
		if (board[0][move] == 0) {
			for (int i = 0; i < 5;  i ++) {
				if (board[i+1][move]!=0) {
					board[i][move] = player;
					break;
				}
			}
			if (board[5][move] == 0) {
				board[5][move] = player;
			}
			return true;
		}
		return false;
	}
	
	
	
	public void incrementVisit() {
		this.visitCounter ++;
	}
    
	public void incrementWins() {
		this.wins ++;
		
	}
}
