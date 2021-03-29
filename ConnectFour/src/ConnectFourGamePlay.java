import java.util.List;
import java.util.Random;
import java.util.Scanner;



public class ConnectFourGamePlay {
	
	public static void showBoard(int [][]board) {
		for (int i = 0; i < 6;  i ++) {
			for (int r = 0; r < 7; r ++) {
				System.out.print(board[i][r] + " ");
			}
			System.out.println();
		}
	}
	
	public static boolean addMove(int [][]board, int move, int player) {
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
	
	public static boolean checkVertical(int [] [] board, int move, int player) {
		int tally = 0;
		for (int i = 0; i < 6;  i ++) {
			if (board[i][move] == player) {tally ++;}
			else {tally = 0;}
			if (tally == 4) {return true;}
		}
		return false;
	}
	
	public static boolean checkHorizontal(int [] [] board, int move, int player) {
		int tally = 0;
		int index = 0;
		for (int i = 0; i < 6;  i ++) {
			if (board[i][move]==player) {
				index = i;
				break;
			}
		}
		for (int i = 0; i < 7;  i ++) {
			if (board[index][i] == player) {tally ++;}
			else {tally = 0;}
			if (tally == 4) {return true;}
		}
		return false;
	}
	
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
		
		if (total < 4 || total > 8) {return false;}
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
			
			if (board[baserow + i][base - i] == player) {tally ++;}
			else {tally = 0;}
			if (tally == 4) {return true;}
		}
		return false;
	}
	
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
		if (total < 4 || total > 8) {return false;}
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
			if (board[baserow + i][base + i] == player) {tally ++;}
			else {tally = 0;}
			if (tally == 4) {return true;}
		}
		return false;
	}
	
	public static boolean checkWin(int [] [] board, int move, int player) {
		if (checkVertical(board,move,player) || checkHorizontal(board,move,player) || checkTopRight(board,move,player) || checkTopLeft(board,move,player)) {
			return true;
		}
		return false;
	}
	
	public static void GameVsComputer() {
		int [] [] board = new int [6][7];
		int moves = 0;
		boolean first = true;
		Scanner input = new Scanner (System.in) ;
		showBoard(board);
		Tree gameTree = new Tree();
		
		System.out.println();
		
		while (moves < 42) {
			System.out.println("Please enter your column.");
			int x = input.nextInt();
			while (! addMove(board,x,1)) {
				System.out.println("Please enter your column.");
				x = input.nextInt();
			};
			if (first) {
				State rootState = new State(board, x, 1, 0);
				Node root = new Node(rootState, null);
				gameTree.root = root;
				
				
			}
			moves ++;
			showBoard(board);
			boolean winStatus = checkWin(board, x, 1);
			System.out.println(winStatus);
			if (winStatus) {
                System.out.println("You win!");
                input.close();
                break;
            }
			int y = bestMoveWithMCTS(board, gameTree, x, first);
			if (first) {
				first = false;
			}
			System.out.println("Computer played: " + y);
			addMove(board,y,2);
			moves ++;
			showBoard(board);
			winStatus = checkWin(board, y, 2);
			System.out.println(winStatus);
			if (winStatus) {
                System.out.println("You lose!");
                input.close();
				break;
			}
		}
		if (moves == 42) {
            System.out.println("It's a draw");
            input.close();
		}
		
		
    }
	
	public static int ComputervsComputer(int [][]board, int moves) {
		while (moves < 42) {
			
			int x = (int) (Math.random() * 7);
			int counter = 0;
			while (!addMove(board,x,2)) {
				counter ++;
				x = (int) (Math.random() * 7);
				if (counter >= 10) {
					//showBoard(board);
					//System.out.println("We broke it");
					return 3;
				}
			}
			moves ++;
			boolean winStatus = checkWin(board, x, 2);
			if (winStatus) {
				//System.out.println("Computer won in this simulation");
				//showBoard(board);
				return 2;
			}
			if (moves == 42) {
				return 3;
			}
			int y = (int) (Math.random() * 7);
			
			counter = 0;
			while (! addMove(board, y, 1)) {
				counter ++;
				y = (int) (Math.random() * 7);
				if (counter >= 10) {
					//showBoard(board);
					//System.out.println("We broke it");
					return 3;
				}
			}
			moves ++;
			winStatus = checkWin(board, y, 1);
			if (winStatus) {
				//System.out.println("Computer lost in this simulation");
				//showBoard(board);
				return 1;
			}
		}
		return 3;
	}
	
	public static int ComputervsComputerMCTS(int [][]board, int moves) {
		while (moves < 42) {
			
			int x = (int) (Math.random() * 7);
			int counter = 0;
			while (!addMove(board,x,1)) {
				counter ++;
				x = (int) (Math.random() * 7);
				if (counter >= 10) {
					//showBoard(board);
					//System.out.println("We broke it");
					return 3;
				}
			}
			moves ++;
			boolean winStatus = checkWin(board, x, 1);
			if (winStatus) {
				//System.out.println("Computer lost in this simulation");
				//showBoard(board);
				return 1;
			}
			if (moves == 42) {
				return 3;
			}
			int y = (int) (Math.random() * 7);
			
			counter = 0;
			while (! addMove(board, y, 2)) {
				counter ++;
				y = (int) (Math.random() * 7);
				if (counter >= 10) {
					//showBoard(board);
					//System.out.println("We broke it");
					return 3;
				}
			}
			moves ++;
			winStatus = checkWin(board, y, 2);
			if (winStatus) {
				//System.out.println("Computer won in this simulation");
				//showBoard(board);
				return 2;
			}
		}
		return 3;
	}
	public static int bestMove(int [][] originalBoard, int moves) {
		int max = 0;
		int bestMove = 0;
		
		for (int i = 0; i < 7; i ++) {
			int wins = 0;
			for (int j = 0; j < 1000; j ++) {
				int [][] board = new int [6][7];
				for (int r = 0; r < 6; r ++) {
					for (int c = 0; c < 7; c ++) {
						board[r][c] = originalBoard[r][c];
					}
				}
				
				if (!addMove(board, i, 1)) {
					continue;
				}
				
				int x = ComputervsComputer(board, moves + 1);
				
				if (x == 1)
					wins ++;
			}
			System.out.println("Wins: "+ wins);
			if (wins > max) {
				max = wins;
				bestMove = i;
			}
		}
		System.out.println("Best move is " + bestMove);
		System.out.println("You won " + max + " times");
		return bestMove;
		
	}
	
	public static int bestMoveWithMCTS (int[][] originalBoard, Tree tree, int opMove, boolean first) {
		return MonteCarloTree.findNextMove(originalBoard, tree, 1, opMove, first);
	}
	
	
	
    public static void main(String[ ] args)
	{
		GameVsComputer();
	}
}
