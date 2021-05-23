

public class MonteCarloTree {
	
	public static int findNextMove (Node node) {

		node.parent = null;

		System.out.println("Initial root info ......");
		for (Node n: node.children) {
			System.out.println("Move: " + n.move);
			System.out.println("Num wins: " + n.wins);
			System.out.println("Num visits: " + n.visitCounter);
		}
		
		int i = 0;

        // We do 100000 trials to find the best node
		while (i < 100000) {
			selfPlaySimulation(node);
            i++;
		}

		Node winner = node.getMaxChild();
        return winner.move;
		
	}

    static private int selfPlaySimulation(Node currentBoard){
        Node selected = selectNode(currentBoard);

        // We first check if this unexplored node is a winning board
        if (selected.endResult == 0) {
            expandNode(selected);
        }

        if (selected.children.size() > 0) 
            selected = selected.getRandomChildNode();
        
        
        
        int playoutResult = 0;
        
        if (selected.endResult != 0) {
            playoutResult = selected.endResult;
        }
        else if (selected.player == 1) 
            playoutResult = selfPlaySimulation(selected);
        else {
            expandNode(selected);
            
            selected = selected.getRandomChildNode();
            playoutResult = selfPlaySimulation(selected);
        }
        backPropogation(selected, playoutResult);
        
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
	
    // UCT formula to find the best node to visit
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

                
				if (node.endResult != 0) {
					newNode.endResult = node.endResult;
				} else if (ConnectFourGamePlay.checkWin(copyboard, i, opponent)) {
					newNode.endResult = opponent;
				} else if (newNode.movesPerformed == 42) {
                    newNode.endResult = 3;
                }
		    	
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
