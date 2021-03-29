import java.util.List;




public class MonteCarloTree {
	
	
	
	public static int findNextMove(int [][] board, Tree tree, int player, int opMove, boolean first) {
		
		
		/*
		int opponent = (player == 1) ? 2 : 1;
		
		State rootState = new State(board, opMove, opponent, 0);
		Node root = new Node(rootState, null);
		tree.root = root;
		*/
		Node root = tree.root;
		//System.out.println("root node straight out the gate");
		//ConnectFourGamePlay.showBoard(root.state.board);
		boolean gotRoot = false;
		for (Node n: root.children) {
			if (n.state.move == opMove) {
				root = n;
				gotRoot = true;
			}
		}
		if (!first && !gotRoot) {
			int [][] copyboard = new int [6][7];
			for (int r = 0; r < 6; r ++) {
				for (int c = 0; c < 7; c ++) {
					copyboard[r][c] = root.state.board[r][c];
				}
			}
			
			ConnectFourGamePlay.addMove(copyboard, opMove, 1);
    		State currState = new State(copyboard, opMove, 1, root.state.movesPerformed + 1);
	    	root = new Node(currState, root);
		}
		System.out.println(opMove);
		//ConnectFourGamePlay.showBoard(root.state.board);
		System.out.println("Initial root info ......");
		for (Node node: root.children) {
			System.out.println("Move: " + node.state.move);
			System.out.println("Num wins: " + node.state.wins);
			System.out.println("Num visits: " + node.state.visitCounter);
		}
		/*
		long t= System.currentTimeMillis();
		long end = t+15000;
		
		while(System.currentTimeMillis() < end) {
		*/
		int i = 0;
		while (i < 100000) {
			//System.out.println("iteration " + i);
			Node selected = selectNode(root);
			//System.out.println("Got a selected node...");
			//ConnectFourGamePlay.showBoard(selected.state.board);
            if (!ConnectFourGamePlay.checkWin(root.state.board, selected.state.move, selected.state.player)) {
                expandNode(selected);
            }
            Node nodeExpand = selected;
            if (selected.children.size() > 0) {
                nodeExpand = selected.getRandomChildNode();
            }
            
            
            //System.out.println("Got a node to expand!");
            //ConnectFourGamePlay.showBoard(nodeExpand.state.board);
            int [][] copyboard = new int [6][7];
			for (int r = 0; r < 6; r ++) {
				for (int c = 0; c < 7; c ++) {
					copyboard[r][c] = nodeExpand.state.board[r][c];
				}
			}
			//System.out.println("Playing itself ...");
			//System.out.println("The move is " + nodeExpand.state.move);
			int playoutResult = 0;
			if (nodeExpand.state.resultAlready != 0) {
				playoutResult = nodeExpand.state.resultAlready;
			}else if (nodeExpand.state.player == 1) {
				playoutResult = ConnectFourGamePlay.ComputervsComputer(copyboard, nodeExpand.state.movesPerformed);
				
				
			}else {
				playoutResult = ConnectFourGamePlay.ComputervsComputerMCTS(copyboard, nodeExpand.state.movesPerformed);
				
				
			}
            
            //System.out.println("Move: " + nodeExpand.state.move);
            //System.out.println("playoutResult:" + playoutResult);
            if (playoutResult == 2) {
            	//System.out.println("Computer won!");
            }
            else {
            	//System.out.println("Computer lost!");
            }
            backPropogation(nodeExpand, playoutResult);
            i++;
		}
		Node winner = root.getMaxChild();
        tree.setRoot(winner);
        //System.out.println("We just set this to root");
        //ConnectFourGamePlay.showBoard(winner.state.board);
        return winner.state.move;
		
	}
	
	private static Node selectNode(Node root) {
	    Node node = root;
	    while (node.children.size() != 0) {
	    	for (Node n: node.children) {
	    		//System.out.println("This is a child");
	    		//ConnectFourGamePlay.showBoard(n.state.board);
	    		//System.out.println();
	    	}
	        node = UCT(node);
	    }
	    return node;
	}
	
	public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
	        if (nodeVisit == 0) {
	            return Integer.MAX_VALUE;
	        }
		    return ((double) nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
	}
	
	public static Node UCT(Node node) {
        int parentVisit = node.state.visitCounter;
        double max = 0;
        Node best = null;
        for (Node curr: node.children) {
        	double temp = uctValue(parentVisit, curr.state.wins, curr.state.visitCounter);
        	if (temp > max) {
        		max = temp;
        		best = curr;
        	}
        }
        return best; 
    }
	
	private static void expandNode(Node node) {
	    for (int i = 0; i < 7; i ++) {
	    	if (node.state.board[0][i] == 0) {
	    		int [][] copyboard = new int [6][7];
				for (int r = 0; r < 6; r ++) {
					for (int c = 0; c < 7; c ++) {
						copyboard[r][c] = node.state.board[r][c];
					}
				}
				int opponent = (node.state.player == 1) ? 2 : 1;
				ConnectFourGamePlay.addMove(copyboard, i, opponent);
				State currState = new State(copyboard, i, opponent, node.state.movesPerformed + 1);
				if (node.state.resultAlready != 0) {
					currState.resultAlready = node.state.resultAlready;
				}else if (ConnectFourGamePlay.checkWin(copyboard, i, opponent)) {
					currState.resultAlready = opponent;
				}
	    		
		    	Node newNode = new Node(currState, node);
		    	node.children.add(newNode);
	    	}
	    	
	    }
	    for (Node n: node.children) {
	    	//System.out.println("One of the children in expandNode");
	    	//ConnectFourGamePlay.showBoard(n.state.board);
	    }
	}
	
	private static void backPropogation(Node node, int result) {
	    Node tempNode = node;
	    while (tempNode != null) {
	        tempNode.state.incrementVisit();
	        if (result == 2) {
	            tempNode.state.incrementWins();
	        }
	        tempNode = tempNode.parent;
	    }
	}

}
