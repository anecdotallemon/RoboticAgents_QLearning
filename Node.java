import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Node {
	private double[][] qLearningDown = new double[15][10]; // 2
	private double[][] qLearningUp = new double[15][10]; // 1
	private double[][] qLearningLeft = new double[15][10]; // 3
	private double[][] qLearningRight = new double[15][10]; // 4
	private double[][] qLearningStay = new double[15][10]; // 0
	int x = 1;
	int y = 5;
	
	private State state = new State();
	
	/**
	 * Constructor for the Node class.
	 */
	public Node() {
		
	}
	
	
	/**
	 * Returns the integer representation of the action with the best Q value
	 * that can be taken from the current node.
	 * @return int the integer representation of the action with the best Q value
	 */
	public int getBestAction() {
		double bestAction = Math.max(Math.max(qLearningDown[this.x][this.y], qLearningUp[this.x][this.y]), Math.max(qLearningLeft[this.x][this.y], Math.max(qLearningRight[this.x][this.y], qLearningStay[this.x][this.y])));
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
	
	
	/**
	 * This method gets an array of the possible actions that can be taken
	 * from the current node's coordinates.
	 * @return an integer array of possible actions where 0 indicates stay, 1 indicates up,
	 * 2 indicates down, 3 indicates left, and 4 indicates right.
	 */
	public int[] possibleActions() {
		ArrayList<Integer> validNums = new ArrayList<>();
		validNums.add(0);
		if (this.y > 0) {
			validNums.add(1);
		}
		if (this.y < 9) {
			validNums.add(2);
		}
		if (this.x > 0) {
			validNums.add(3);
		}
		if (this.x < 14) {
			validNums.add(4);
		}
		int numValid = validNums.size();
		int[] actions = new int[numValid];
		for (int i = 0; i < validNums.size(); i++) {
			actions[i] = validNums.get(i);
		}
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
		if (move == 0) {
			rewardValue = state.getStateRewardStay(a, b);
			futureReward = futureMax(futureX, futureY);
		}
		else if (move == 1) {
			rewardValue = state.getStateRewardUp(a, b);
			if (b > 0) {
				futureY = b - 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		else if (move == 2) {
			rewardValue = state.getStateRewardDown(a, b);
			if (b < 9) {
				futureY = b + 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		else if (move == 3) {
			rewardValue = state.getStateRewardLeft(a, b);
			if (a > 0) {
				futureX = a - 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		else {
			rewardValue = state.getStateRewardRight(a, b);
			if (a < 14) {
				futureX = a + 1;
			}
			futureReward = futureMax(futureX, futureY);
		}
		double qValue = rewardValue + (0.85 * futureReward);
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
	private double futureMax(int a, int b) {		
		double[] futureRewards = new double[5];
		Node newNode = new Node();
		newNode.x = a;
		newNode.y = b;
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
	
	
	/**
	 * Prints the Q table of values for the Stay action.
	 */
	public void printQTableStay() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningStay[j][i]));
			}
			System.out.println();
		}
	}
	
	/**
	 * Prints the Q table of values for the Up action.
	 */
	public void printQTableUp() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningUp[j][i]));
			}
			System.out.println();
		}
	}
	
	/**
	 * Prints the Q table of values for the Down action.
	 */
	public void printQTableDown() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningDown[j][i]));
			}
			System.out.println();
		}
	}
	
	/**
	 * Prints the Q table of values for the Left action.
	 */
	public void printQTableLeft() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningLeft[j][i]));
			}
			System.out.println();
		}
	}
	
	/**
	 * Prints the Q table of values for the Right action.
	 */
	public void printQTableRight() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%7.2f", qLearningRight[j][i]));
			}
			System.out.println();
		}
	}
	
	
	/**
	 * This method writes the current Q Tables to a new file.
	 * @param filename the name of the file to be created - should not include .txt
	 * @throws FileNotFoundException if the file cannot be created
	 */
	public void writeQTablesToFile(String filename) throws FileNotFoundException {
		File file = null;
		try {
			file = new File(filename + ".txt");
			if (file.createNewFile()) {
		        System.out.println("File created: " + file.getName());
		      } else {
		        System.out.println("File already exists, would you like to overwrite the file, yes or no?");
		        Scanner scan = new Scanner(System.in);
		        String input = scan.nextLine().toLowerCase();
		        scan.close();
		        System.out.println("Your input was: " + input);
		        if (input.contentEquals("yes") || input.contentEquals("yeah") || input.contentEquals("yep") || input.contentEquals("yup") || input.contentEquals("just write to the file already")) {
		        	System.out.println("I understand that you wish to overwrite the file.");
		        }
		        else {
		        	System.out.println("I understand you do not wish to overwrite the file.");
		        	return;
		        }
		        
		      }
		} catch (Exception e) {
			System.out.println("There was an error creating the file.");
			e.printStackTrace();
		}
		PrintWriter writer = new PrintWriter(file);
		StringBuilder text = new StringBuilder();
		text.append("Stay\n");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				text.append(String.format("%.2f", qLearningStay[j][i]) + " ");
			}
			text.setLength(text.length() - 1);
			text.append("\n");
		}
		text.append("Up\n");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				text.append(String.format("%.2f", qLearningUp[j][i]) + " ");
			}
			text.setLength(text.length() - 1);
			text.append("\n");
		}
		text.append("Down\n");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				text.append(String.format("%.2f", qLearningDown[j][i]) + " ");
			}
			text.setLength(text.length() - 1);
			text.append("\n");
		}
		text.append("Left\n");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				text.append(String.format("%.2f", qLearningLeft[j][i]) + " ");
			}
			text.setLength(text.length() - 1);
			text.append("\n");
		}
		text.append("Right\n");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				text.append(String.format("%.2f", qLearningRight[j][i]) + " ");
			}
			text.setLength(text.length() - 1);
			text.append("\n");
		}
		writer.write(text.toString());
		writer.close();
	}
}
