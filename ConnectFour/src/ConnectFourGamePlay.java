
import java.util.Scanner;

// Class for basic rules of ConnectFour
public class ConnectFourGamePlay {

    // Note that unplayed slots are represented as zeroes, while player 1 and player 2
    // are represented with 1s and 2s respectively
	
    // This function simply prints out the board
	public static void showBoard(int [][]board) {
		for (int i = 0; i < 6;  i ++) {
			for (int r = 0; r < 7; r ++) 
				System.out.print(board[i][r] + " ");
			System.out.println();
		}
	}
	
    // This function drops a marker into the desired column
    // The function returns true if the addMove was successful, false if not
	public static boolean addMove(int [][]board, int move, int player) {

        // First check if that column is full
        // Ex. If column 1 has been completely filled up, we cannot put a marker in column 1
		if (board[0][move] == 0) {

            // Move down the column until we either reach a nonzero value, or the end of the board
			for (int i = 0; i < 5;  i ++) {
				if (board[i+1][move]!=0) {
					board[i][move] = player;
					break;
				}
			}
			if (board[5][move] == 0) 
				board[5][move] = player;
			return true;
		}
		return false;
	}
	
    // The following functions check if the newest move has caused someone to win
     
    // For efficiency sake, I only check the column of the move
	public static boolean checkVertical(int [] [] board, int move, int player) {
		int tally = 0;
		for (int i = 0; i < 6;  i ++) {
			if (board[i][move] == player)
                tally ++;
			else 
                tally = 0;
			if (tally == 4) 
                return true;
		}
		return false;
	}
	
    // For efficiency sake, I only check the row of the move
	public static boolean checkHorizontal(int [] [] board, int move, int player) {
		int tally = 0;
		int index = 0;

        // First we have to find the row that the marker ended up on
		for (int i = 0; i < 6;  i ++) {
			if (board[i][move]==player) {
				index = i;
				break;
			}
		}

        // We must check board[index] and see if player has won
		for (int i = 0; i < 7;  i ++) {
			if (board[index][i] == player) 
                tally ++;
			else 
                tally = 0;
			if (tally == 4) 
                return true;
		}
		return false;
	}
	
    // Checks the diagonal axis for win condition
	public static boolean checkTopRight(int [] [] board, int move, int player) {
		int tally = 0;
		int index = 0;
		for (int i = 0; i < 6;  i ++) {
			if (board[i][move]==player) {
				index = i;
				break;
			}
		}

		int base = move;
		int total = index + move;
		
        // Return false because the diagonal axis has less than four spots, making it impossible to win
		if (total < 4 || total > 8) 
            return false;

		int baserow = 0;
		while (index != 0) {
			index --;
			base ++;
		}
		if (base > 6) {
			baserow = base - 6;
			base = 6;
		}
		
		int [] count = {0,0,0,4,5,6,6,5,4};
		for (int i = 0; i < count[total];  i ++) {
			if (board[baserow + i][base - i] == player) 
                tally ++;
			else 
                tally = 0;
			if (tally == 4) 
                return true;
		}
		return false;
	}
	
    // Checks the other diagonal axis for win condition
	public static boolean checkTopLeft(int [] [] board, int move, int player) {
		int tally = 0;
		int index = 0;
		for (int i = 0; i < 6;  i ++) {
			if (board[i][move]==player) {
				index = i;
				break;
			}
		}

		int base = move;
		int total = 6 - move + index;
		if (total < 4 || total > 8) 
            return false;
		int baserow = 0;
		while (index != 0) {
			index --;
			base --;
		}
		if (base < 0) {
			baserow = 0-base;
			base = 0;
		}
		int [] count = {0,0,0,4,5,6,6,5,4};
		for (int i = 0; i < count[total];  i ++) {
			if (board[baserow + i][base + i] == player) 
                tally ++;
			else 
                tally = 0;
			if (tally == 4) 
                return true;
		}
		return false;
	}

	// Checks the whole board for a win condition
	public static boolean checkWin(int [] [] board, int move, int player) {
		if (checkVertical(board,move,player) || checkHorizontal(board,move,player) || checkTopRight(board,move,player) || checkTopLeft(board,move,player)) 
			return true;
		return false;
	}
	
    // Plays against the user
	public static void GameVsComputer() {
		int [] [] board = new int [6][7];
		boolean first = true;
		Scanner input = new Scanner (System.in) ;
		

        // Creates placeholder currentBoard with move 0
        Node currentBoard = new Node (null, board, 1, 1, 0);
		System.out.println();
		
		while (currentBoard.movesPerformed < 42) {
            showBoard(currentBoard.board);
			System.out.println("Please enter your column.");
			int x = input.nextInt();

            // Prompts for input until we get a valid input
			while (! addMove(board,x,1)) {
				System.out.println("Please enter your column.");
				x = input.nextInt();
			}

            // Creates the node if this is the first move
			if (first) {
				currentBoard = new Node(null, board, 1, x, 1);
                first = false;
			} else {
                // otherwise we try to locate the next board from the children
                boolean foundBoard = false;
                for (Node n: currentBoard.children){
                    if (n.move == x){
                        currentBoard = n;
                        foundBoard = true;
                    }
                }
                // If we could not find the next board, we create the next board
                if (!foundBoard){
                    Node newNode = new Node(currentBoard, board, 1, x, currentBoard.movesPerformed + 1);
                    currentBoard = newNode;
                }
            }

            if (currentBoard.endResult != 0){
                if (currentBoard.endResult == 1){
                    System.out.println("You've (Player 1) won!");
                    showBoard(currentBoard.board);
                    return;
                } else if (currentBoard.endResult == 2) {
                    System.out.println("The Computer (Player 2) won! ");
                    showBoard(currentBoard.board);
                    return;
                } else if (currentBoard.endResult == 3) {
                    System.out.println("Ended in a draw!");
                    showBoard(currentBoard.board);
                    return;
                }
            }
        
			int y = MonteCarloTree.findNextMove(currentBoard);
            System.out.println("The best move is " + y);

            

            // Updates the currentBoard
            for (Node n: currentBoard.children){
                if (n.move == y)
                    currentBoard = n;
            }

            Node z = currentBoard.getMinChild();
            double win = (double)(z.wins)/(double)(z.visitCounter);

            

            System.out.println("I predict you do " + z.move);
            System.out.println("Win rate: " + (1 - win));


            if (currentBoard.endResult != 0){
                if (currentBoard.endResult == 1){
                    System.out.println("You've (Player 1) won!");
                    showBoard(currentBoard.board);
                    return;
                } else if (currentBoard.endResult == 2) {
                    System.out.println("The Computer (Player 2) won! ");
                    showBoard(currentBoard.board);
                    return;
                } else if (currentBoard.endResult == 3) {
                    System.out.println("Ended in a draw!");
                    showBoard(currentBoard.board);
                    return;
                }
            }
        }

        System.out.println("Ended in a draw!");
        return;
    }

	
    public static void main(String[ ] args)
	{
		GameVsComputer();
	}
}
