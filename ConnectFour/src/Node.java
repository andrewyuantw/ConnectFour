import java.util.ArrayList;
import java.util.Random;

// The Node class defines a node for a tree structure. 

public class Node {

    // Each Node also has a parent.
	Node parent;
	
    // The ArrayList contains all possible moves that can be made on this board.
	ArrayList<Node> children;

    // The 2D array that includes the current ConnectFour board
	int [][] board;

    // This int indicate which player just made the last move
	int player;

    // visitCounter counts the amount of times we've visited this node
	int visitCounter;

    // wins counts the amount of times we've won when visiting this node 
	int wins;

    // move includes the move to be played, with the move represented as the column that a marker was dropped
	int move;

    // movesPerformed keeps track of the amount of moves in the game so we know if there has been a draw
	int movesPerformed;

    // Stores end result - 0 means no winner yet, 1 means Player 1 won, 2 means Player 2 won, 3 means ended in a draw
	int endResult = 0;
	
    // Constructor for a node
	public Node(Node parent, int [][] originalBoard, int player, int move, int movesPerformed) {
		this.parent = parent;
		this.children = new ArrayList<Node>();
        this.board = originalBoard;
		this.move = move;
		this.movesPerformed = movesPerformed;
		this.player = player;
	}

    // This function gets a random child node, or in other words, gets a random next move
	public Node getRandomChildNode() {
		Random rand = new Random();
	    Node randomElement = children.get(rand.nextInt(children.size()));
	    return randomElement;
	}
	
    // This function retrieves the child node with the highest win rate
	public Node getMaxChild() {
		Node best = null;
		double bestScore = 0;
		for (Node node: children) {
			double nodeScore = (double)(node.wins) / (double)(node.visitCounter);
            System.out.println();
			System.out.println("Column: " + node.move);
			System.out.println("Num wins: " + node.wins);
			System.out.println("Num visits: " + node.visitCounter);
            System.out.format("Percentage: %.4f\n", (double)(node.wins)/(double)(node.visitCounter));
			if ( nodeScore >= bestScore) {
				bestScore = nodeScore;
				best = node;
			}
		}
		return best; 
	}

    // This function retrieves the child node with the lowest win rate
    // This is useful when we want to assume our player is intelligent and will always
    // make the move that would cause the computer to have the lowest win rate
    public Node getMinChild() {

		Node worst = children.get(0);
		double worstScore = (double)(worst.wins)/ (double)(worst.visitCounter);
		for (Node node: children) {
			double nodeScore = (double)(node.wins) / (double)(node.visitCounter);
			if (worstScore > nodeScore) {
				worstScore = nodeScore;
				worst = node;
			}
		}
		return worst; 
	}

	public void incrementVisit() {
		this.visitCounter ++;
	}

	public void incrementWins() {
		this.wins ++;
	}
}