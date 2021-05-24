

public class MonteCarloTree {
	
	public static int findNextMove (Node node) {

        // Sets the node's parent as null for our back propogation
		node.parent = null;

		int i = 0;

        expandNode(node);
        for (Node n: node.children){
            expandNode(n);
            for (Node next: n.children){
                if (next.endResult == 1 && n.move != next.move){
                    return next.move;
                }
            }
            
            
        }

        // We do 100000 trials to find the best node
		while (i < 10000000) {
			selfPlaySimulation(node);
            i++;
		}

		Node winner = node.getMaxChild();
        return winner.move;
	}

    static private int selfPlaySimulation(Node currentBoard){
        
        // Goes down our tree to find an unexplored board
        Node selected = selectNode(currentBoard);

        // We first check if this unexplored node is a winning board
        if (selected.endResult == 0) {
            // If it is ongoing, we expand the node to find all possible next moves
            expandNode(selected);
        }

        if (selected.children.size() > 0) 
            selected = selected.getRandomChildNode();
        

        int playoutResult = 0;
        
        // If the selected board is no longer ongoing, we know the playoutResult
        if (selected.endResult != 0) {
            playoutResult = selected.endResult;
            // With the playoutResult, we back propogate up the tree
            backPropogation(selected, playoutResult);
        }
        else if (selected.player == 1) 
            // If the last move of this unexplored node was made by the user,
            // we make a recursive call on selected
            playoutResult = selfPlaySimulation(selected);
        else {
            // If the last move of this unexplored node was made by the computer,
            // we need to make a move for the player. We assume the player is going
            // to be intelligent and always make the best move in their interest
            expandNode(selected);
            selected = selected.getMinChild();
            playoutResult = selfPlaySimulation(selected);
        }

        
        
        return playoutResult;
    }
	
    // Function that selects the node to visit using UCT
	private static Node selectNode(Node root) {
	    Node node = root;

        // We find an unexplored node 
	    while (node.children.size() != 0) 
	        node = UCT(node);
	    return node;
	}
	
    // UCT (Upper Confident applied to Trees) formula to find the best node to visit
	public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
	        if (nodeVisit == 0) 
	            return Integer.MAX_VALUE;
		    return ((double) nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
	}
	
    // Goes through the children to find the child with the highest uctValue
	public static Node UCT(Node node) {
        int parentVisit = node.visitCounter;
        double max = 0;
        Node best = null;
        for (Node curr: node.children) {
        	double temp = uctValue(parentVisit, curr.wins, curr.visitCounter);
        	if (temp > max) {
        		max = temp;
        		best = curr;
        	}
        }
        return best; 
    }
	
    // Function that expands the node, populating its children ArrayList
	public static void expandNode(Node node) {

        // Goes through each column
	    for (int i = 0; i < 7; i ++) {

            boolean foundChild = false;
            for (Node n: node.children){
                if (n.move == i)
                    foundChild = true;
            }

            // If the column is already full, we do nothing
	    	if (!foundChild && node.board[0][i] == 0) {
	    		int [][] copyboard = new int [6][7];
				for (int r = 0; r < 6; r ++) {
					for (int c = 0; c < 7; c ++) 
						copyboard[r][c] = node.board[r][c];
				}
				int opponent = (node.player == 1) ? 2 : 1;
				ConnectFourGamePlay.addMove(copyboard, i, opponent);
                Node newNode = new Node(node, copyboard, opponent, i, node.movesPerformed + 1);

                // checks the endResult for this new board before adding to the children ArrayList
				if (node.endResult != 0) 
					newNode.endResult = node.endResult;
				else if (ConnectFourGamePlay.checkWin(copyboard, i, opponent)) 
					newNode.endResult = opponent;
				else if (newNode.movesPerformed == 42) 
                    newNode.endResult = 3;
		    	
		    	node.children.add(newNode);
	    	}
	    	
	    }
	}
	
    // Function that goes back up the tree, and increments the visitCounter and winCounter
	private static void backPropogation(Node node, int result) {
	    Node tempNode = node;
	    while (tempNode != null) {
	        tempNode.incrementVisit();
	        if (result == 2) 
	            tempNode.incrementWins();
	        tempNode = tempNode.parent;
	    }
	}

}
