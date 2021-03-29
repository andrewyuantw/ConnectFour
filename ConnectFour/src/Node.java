import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Node {
	State state;
	Node parent;
	
	ArrayList<Node> children;
	
	public Node(State state, Node parent) {
		this.state = state;
		this.parent = parent;
		this.children = new ArrayList<Node>();
	}
	
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
