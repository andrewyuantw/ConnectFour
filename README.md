# MonteCarloConnectFour

### About

I decided to create this because I always got beaten in a game of ConnectFour. I was interested in Machine Learning and wanted to learn more about how something like AlphaGo operates. Due to ConnectFour's more simplistic rules, I was able to to use a simpler method to find the best move for a board.

### Background

My initial idea was to code up the rules for ConnectFour, and have the computer play numerous randomized games against itself, tallying up the amount of wins to find the best move. My second iteration improves upon this concept, using the Monte Carlo Tree Search algorithm to make this method of playing out a scenario and tallying the results more efficient. I am now working on making the opponent use this method as well. In other words, instead of predicting our next move against a random player, we are assuming the opponent is as smart as possible and will always make the best move.

### Code Structure

I have a Node class, with the following attributes

* ArrayList<Node>() children - an ArrayList of all the child nodes 
* int [][] board - a 2D array that stores a ConnectFour board
* int player - the player that made the last move
* int visitCounter - the amount of times we've visited this node
* int wins - the amount of time we've won when visiting this node
* Node parent - the parent node of the current node
* int move - the last move that was performed (the column number of the last marker that was placed)
* int movesPerformed - number of moves performed, if we reach 42 then we have a draw
* int endResult - stores the result of the current board (0 for ongoing, 1 for Player 1 won, 2 for Player 2 won, and 3 for a draw)

### Monte Carlo Tree Search
  
1) We first find all possibile next moves of a given board (this is usually seven, but if one or more columns are already completely full, then there will be less than seven)
2) We want to find the next move that would give us the largest percentage of winning. Of course we could visit each possible next move an equal amount and see which move gives us the largest winning percentage. However, we can do something better. We want to visit the possibilities with a higher chance of winning to see if the high win rate holds with more trial runs, but we also want to ensure we thoroughly explore other nodes. This is where the Monte Carlo Tree Search and UCT algorithms comes in. Using the UCT (Upper Confidence bounds applied to Trees) algorithm, we find a balance between choosing unexplored nodes, and nodes with high win rates.
3) After we find a node to explore, we play out the node using recursive calls until we reach a result - either a player wins or we reach a draw.
4) We then back propogate up the tree - incrementing the visitCounter for each node visited, and if the result ends in a win, we increment the winCounter as well.
5) We can now examine the win rates of the children of our initial node, and determine the best move. The win percentage is found by evaluating winCounter/visitCounter.
  
  
