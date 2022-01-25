import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * A Java program that implements a Q-learning algorithm to find the best path
 * towards the goal from either a given or random coordinate pair.
 * 
 * This program also implements both writing the Q Tables to a file (existing or
 * not existing, and will ask before overwriting) and reading the Q Tables in from
 * a pre-existing file.
 * 
 * @author AJ Moore
 * for Robotic Agents
 */
public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		State state = new State();
		Node node = new Node();		
		
		// =============
		// Instructions:
		// Comment or uncomment whichever lines you wish to see.
		// =============
		
		// Generate Q Tables
		generateQTables(state, node);
		
		// Or generate Q Tables from pre-existing file
//		generateQTablesFromFile(node, "qtables");
		
		// Print the Q Tables to verify accuracy
		printQTables(node);
		
		// Write the Q Tables to a file to access later
		writeQTables(node, "qtables");
		
		// Get a random solution
		getRandomSolution(state, node);
		
		// Get a solution for given input x and y
//		int x = ;
//		int y = ;
//		getSolution(state, node, x, y);
		
		
	}
	
	
	/**
	 * Method to print out all of the QTables.
	 * @param node the Node object
	 */
	public static void printQTables(Node node) {
		System.out.println("========================================= Q Table: Stay Action ==========================================");
		node.printQTableStay();
		System.out.println("========================================== Q Table: Up Action ===========================================");
		node.printQTableUp();		
		System.out.println("========================================= Q Table: Down Action ==========================================");
		node.printQTableDown();		
		System.out.println("========================================= Q Table: Left Action ==========================================");
		node.printQTableLeft();		
		System.out.println("========================================= Q Table: Right Action =========================================");
		node.printQTableRight();
	}
	
	
	/**
	 * Helper method to write Q Tables to a file
	 * @param node the node with the Q Tables
	 * @param filename the name of the file to be included - should not include .txt at the end
	 * @throws FileNotFoundException if the file is not created properly
	 */
	public static void writeQTables(Node node, String filename) throws FileNotFoundException {
		node.writeQTablesToFile(filename);
	}
	
	
	/**
	 * This method will generate Q Tables from a saved file.
	 * @param node the node object to generate the Q Tables for
	 * @param filename the name of the file to be accessed - without the .txt
	 * @throws FileNotFoundException if the file is not available
	 */
	public static void generateQTablesFromFile(Node node, String filename) throws FileNotFoundException {
		File file = null;
		try {
			file = new File(filename + ".txt");
		} catch (Exception e) {
			System.out.println("An error occurred getting the Q Tables file.");
			e.printStackTrace();
		}
		Scanner scan = new Scanner(file);
		scan.nextLine(); // tossing first line
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				node.updateQValue(j, i, scan.nextDouble(), 0);
			}
		}
		while(!scan.hasNextDouble()) {
			scan.nextLine();
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				node.updateQValue(j, i, scan.nextDouble(), 1);
			}
		}
		while(!scan.hasNextDouble()) {
			scan.nextLine();
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				node.updateQValue(j, i, scan.nextDouble(), 2);
			}
		}
		while(!scan.hasNextDouble()) {
			scan.nextLine();
		}		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				node.updateQValue(j, i, scan.nextDouble(), 3);
			}
		}
		while(!scan.hasNextDouble()) {
			scan.nextLine();
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				node.updateQValue(j, i, scan.nextDouble(), 4);
			}
		}
		scan.close();
	}
	

	/**
	 * Method to generate the QTables by using playGame() randomly until
	 * the QTables are complete. It decides when to stop based on the 8th time
	 * the QTables full value (all the QTables' values added) hasn't changed from
	 * before the game was last run. This number limit was imposed due to the
	 * chance of a game essentially repeating itself, therefore not updating the
	 * tables much, so showing minimal change, even if the QTables are not complete yet.
	 * @param state
	 * @param node
	 */
	public static void generateQTables(State state, Node node) {
		Random random = new Random();
		int continueGame = 0;
		while (continueGame < 8) {
			double startQ = node.calculateChangeMetric();
			playGame(state, node);
			node.x = random.nextInt(15);
			node.y = random.nextInt(10);
			double endQ = node.calculateChangeMetric();
			if (endQ - startQ == 0) {
				continueGame++;
			}
		}
	}
	
	
	/**
	 * This method will get and print a solution for a random starting point.
	 * @param state the State object
	 * @param node the Node object
	 */
	public static void getRandomSolution(State state, Node node) {
		Random random = new Random();
		Solution solution = new Solution();
		node.x = random.nextInt(15);
		node.y = random.nextInt(10);
		
		if (node.checkIfGoal()) {
			System.out.println("Starting point is the solution.");
			return;
		}
		
		solution.setStart(node.x, node.y);
		while (!node.checkIfGoal()) {
			int goNext = node.getBestAction();
			if (goNext == 1) {
				solution.updateSolution(node.x, node.y, goNext);
				node.y--;
			}
			else if (goNext == 2) {
				solution.updateSolution(node.x, node.y, goNext);
				node.y++;
			}
			else if (goNext == 3) {
				solution.updateSolution(node.x, node.y, goNext);
				node.x--;
			}
			else if (goNext == 4) {
				solution.updateSolution(node.x, node.y, goNext);
				node.x++;
			}
		}
		solution.printSolution();
	}
	
	
	/**
	 * This method will get and print a solution for a given starting point.
	 * @param state the State object
	 * @param node the Node object
	 * @param x the starting x coordinate
	 * @param y the starting y coordinate
	 */
	public static void getSolution(State state, Node node, int x, int y) {
		Solution solution = new Solution();
		node.x = x;
		node.y = y;
		if (node.checkIfGoal()) {
			System.out.println("Starting point is the solution.");
			return;
		}
		solution.setStart(node.x, node.y);
		while (!node.checkIfGoal()) {
			int goNext = node.getBestAction();
			if (goNext == 1) {
				solution.updateSolution(node.x, node.y, goNext);
				node.y--;
			}
			else if (goNext == 2) {
				solution.updateSolution(node.x, node.y, goNext);
				node.y++;
			}
			else if (goNext == 3) {
				solution.updateSolution(node.x, node.y, goNext);
				node.x--;
			}
			else if (goNext == 4) {
				solution.updateSolution(node.x, node.y, goNext);
				node.x++;
			}
		}
		solution.printSolution();
	}
	
	
	/**
	 * This method plays the game, taking random directions and updating the Q Table.
	 * @param state the State object
	 * @param node the Node object
	 */
	public static void playGame(State state, Node node) {
		Random random = new Random();	
		while (!node.checkIfGoal()) {
			int[] possible = node.possibleActions();
			int dir = possible[random.nextInt(possible.length)];
			node.calculateQValue(node.x, node.y, dir);
			if (dir == 1) {
				if (node.y > 0) { node.y--;	}
			}
			if (dir == 2) {
				if (node.y < 9) { node.y++;	}
			}
			if (dir == 3) {
				if (node.x > 0) { node.x--;	}
			}
			if (dir == 4) {
				if (node.x < 14) { node.x++;	}
			}
		}
	}

}
