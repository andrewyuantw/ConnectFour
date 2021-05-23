import java.util.ArrayList;
import java.util.Random;

// The Node class defines a node for a tree structure. 

public class Node {

    // Each tree will hold a state, with further details in the State class
	State state;

    // Each Node also has a parent. It will contain information of the board prior to a move
	Node parent;
	
    // The ArrayList contains all possible moves that can be made on this board.
	ArrayList<Node> children;
	
    // Constructor for a node
	public Node(State state, Node parent) {
		this.state = state;
		this.parent = parent;
		this.children = new ArrayList<Node>();
	}

    // This function gets a random child node, or in other words, gets a random next move
	public Node getRandomChildNode() {
		Random rand = new Random();
	    Node randomElement = children.get(rand.nextInt(children.size()));
	    return randomElement;
	}
	
    
	public Node getMaxChild() {
		Node best = null;
		double bestScore = 0;
		for (Node node: children) {
			double nodeScore = (double)(node.state.wins) / (double)(node.state.visitCounter);
			System.out.println("Move: " + node.state.move);
			System.out.println("Num wins: " + node.state.wins);
			System.out.println("Num visits: " + node.state.visitCounter);
			if ( nodeScore >= bestScore) {
				bestScore = nodeScore;
				best = node;
			}
		}
		
		return best; 
	}
}