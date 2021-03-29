
public class State {
	int [][] board;
	int player;
	int visitCounter;
	int wins;
	int move;
	int movesPerformed;
	int resultAlready = 0;
	
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
