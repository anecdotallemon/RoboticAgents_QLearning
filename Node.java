
public class Node {
	/* five double arrays - each holding action + state
	 * need 5 arrays (2d) to encode the q table, and reward needs 5 more arrays
	 * playgame method has agent move around randomly and update q table as it moves
	 * 		and one more method that executes the best path from the learned q table
	 * for getting out of the playgame, possibly have some sort of metric that adds 
	 * 		all the boxes together and after each generation, check to see how much it changed
	 * may want to write your q table to a file or something, save it locally, so that 
	 * 		you can reload it later when you want to run the game again so you don't have to 
	 * 		constantly save it
	 */
	
	double[][] qLearningDown = new double[15][10]; // 2
	double[][] qLearningUp = new double[15][10]; // 1
	double[][] qLearningLeft = new double[15][10]; // 3
	double[][] qLearningRight = new double[15][10]; // 4
	double[][] qLearningStay = new double[15][10]; // 0
	int x = 1;
	int y = 5;
	
	State state = new State();
	
	// storing trajectory?
	// reward function
	// value of reward - this would be in state possibly
	// choose a discount factor
	// q learning score = reward for the action + discounted maximum award of the next possible actions
	
	
	/*
	 * Problem set up
	 * Agent spawns in a given location
	 * Agent can move up, down, left, right, or stay
	 * Goal is to maximize reward
	 * Stop when the goal is reached (100)
	 * Given state, take random actions and calculate q value
	 */
	
	public Node() {
		
	}
	
	
	/**
	 * Returns the integer representation of the action with the best Q value
	 * that can be taken from the current node.
	 * @return int the integer representation of the action with the best Q value
	 */
	public int getBestAction() {
		
		// TODO: Pull from possibleActions()
		
		double bestAction = Math.max(Math.max(qLearningDown[this.x][this.y], qLearningUp[this.x][this.y]), Math.max(qLearningLeft[this.x][this.y], Math.max(qLearningRight[this.x][this.y], qLearningStay[this.x][this.y])));
//		System.out.println("Inside getBestAction(), best action is " + bestAction);
		if (bestAction == qLearningUp[this.x][this.y]) {
			return 1;
		}
		else if (bestAction == qLearningDown[this.x][this.y]) {
			return 2;
		}
		else if (bestAction == qLearningLeft[this.x][this.y]) {
			return 3;
		}
		else if (bestAction == qLearningRight[this.x][this.y]) {
			return 4;
		}
		else {
			return 0;
		}
		
	}	
	
	
	public int[] possibleActions() {
		// TODO: Change this to actually just the valid actions
		int[] actions = new int[]{0,1,2,3,4};
		return actions;
	}
	
	
	/**
	 * This method checks if the current node's coordinates
	 * are at the goal.
	 * @return true if the current node's coordinates are at the goal, otherwise false
	 */
	public boolean checkIfGoal() {
		if (state.getStateRewardStay(this.x, this.y) == 100) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * This method calculates the QValue for the given starting coordinate
	 * and given move to be taken.
	 * @param a the integer value for x coordinate
	 * @param b the integer value for y coordinate
	 * @param move an integer representation for which way to be moved
	 * @return qValue the integer value
	 */
	public double calculateQValue(int a, int b, int move) {
		int rewardValue = 0;
		double futureReward = 0;
		int futureX = a;
		int futureY = b;
		
		// stay in place
		if (move == 0) {
			rewardValue = state.getStateRewardStay(a, b);
			futureReward = futureMax(futureX, futureY);
		}
		// go up
		else if (move == 1) {
			rewardValue = state.getStateRewardUp(a, b);
			if (b > 0) {
				futureY = b - 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		// go down
		else if (move == 2) {
			rewardValue = state.getStateRewardDown(a, b);
			if (b < 9) {
				futureY = b + 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		// go left
		else if (move == 3) {
			rewardValue = state.getStateRewardLeft(a, b);
			if (a > 0) {
				futureX = a - 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		// go right
		else {
			rewardValue = state.getStateRewardRight(a, b);
			if (a < 14) {
				futureX = a + 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		double qValue = rewardValue + (0.9 * futureReward);
		updateQValue(x, y, qValue, move);
		return qValue;
	}
	
	
	/**
	 * This method finds the future maximum Q value for given
	 * coordinates.
	 * @param a the x coordinate
	 * @param b the y coordinate
	 * @return double the future maximum Q value
	 */
	public double futureMax(int a, int b) {		
		double[] futureRewards = new double[5];
		Node newNode = new Node();
		newNode.x = a;
		newNode.y = b;
		// TODO: May need to update this when possibleActions() is changed
		int[] move = newNode.possibleActions();
		double futureReward = 0;
		for (int i = 0; i < move.length; i++) {
			if (move[i] == 0) {
				futureRewards[i] = qLearningStay[a][b];
			}
			else if (move[i] == 1) {
				futureRewards[i] = qLearningUp[a][b];
			}
			else if (move[i] == 2) {
				futureRewards[i] = qLearningDown[a][b];
			}
			else if (move[i] == 3) {
				futureRewards[i] = qLearningLeft[a][b];
			}
			else {
				futureRewards[i] = qLearningRight[a][b];
			}
		}
		for (int j = 0; j < move.length; j++) {
			if (futureRewards[j] > futureReward) {
				futureReward = futureRewards[j];
			}
		}
		return futureReward;
	}
	
	
	/**
	 * This method updates the QValue table for the given move,
	 * coordinates, and value.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param value the Q value to insert to the table
	 * @param move the direction to update the Q value for
	 */
	public void updateQValue(int x, int y, double value, int move) {
		if (move == 0) {
			qLearningStay[x][y] = value;
		}
		else if (move == 1) {
			qLearningUp[x][y] = value;
		}
		else if (move == 2) {
			qLearningDown[x][y] = value;
		}
		else if (move == 3) {
			qLearningLeft[x][y] = value;
		}
		else {
			qLearningRight[x][y] = value;
		}
	}
	
	
	/**
	 * This method calculates the total value of all spaces in the
	 * Q arrays, to be used as a means of calculating the change
	 * between iterations of the game to know when to stop searching
	 * @return totalValue the total value of all points in every Q array
	 */
	public double calculateChangeMetric() {
		double totalValue = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				totalValue += qLearningStay[j][i];
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				totalValue += qLearningUp[j][i];
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				totalValue += qLearningDown[j][i];
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				totalValue += qLearningLeft[j][i];
			}
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				totalValue += qLearningRight[j][i];
			}
		}
		return totalValue;
	}
	
	
	public void printQTableStay() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningStay[j][i]));
			}
			System.out.println();
		}
	}
	
	public void printQTableUp() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningUp[j][i]));
			}
			System.out.println();
		}
	}
	
	public void printQTableDown() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningDown[j][i]));
			}
			System.out.println();
		}
	}
	
	public void printQTableLeft() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningLeft[j][i]));
			}
			System.out.println();
		}
	}
	
	public void printQTableRight() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningRight[j][i]));
			}
			System.out.println();
		}
	}
	
	
	
	
}
