# MonteCarloConnectFour

### About

I decided to create this because I always got beaten in a game of ConnectFour. I was interested in Machine Learning and how something like AlphaGo operates. I took a similar approach, but due to ConnectFour's more simplistic rules, I was able to create something fairly simply that could still give you the best move for any given board.

### How It Works

My initial idea was to code up the rules for ConnectFour, and have the computer play numerous randomized games against itself. My code improves upon this concept, using the Monte Carlo Tree Search algorithm to make it more efficient.

### Code Structure

I have a Node class, with the following attributes

* ArrayList<Node>() children - an ArrayList of all the child nodes 
* int [][] board - a 2D array that stores a ConnectFour board
* player - the player that made the last move
* visitCounter - the amount of times we've visited this node
* wins - the amount of time we've won when visiting this node
