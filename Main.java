import java.util.Random;

/**
 * A Java program that implements a Q-learning algorithm to find the best path
 * towards the goal from either a given or random coordinate pair.
 * 
 * @author AJ Moore
 * for Robotic Agents
 */
public class Main {

	public static void main(String[] args) {
		State state = new State();
		Node node = new Node();
//		state.printState();
		
		
		// =============
		// Instructions:
		// =============
		
		// Generate Q Tables
		generateQTables(state, node);
		
		// Print them if you want (uncomment me!)
		printQTables(node);
		
		// Get a random solution
//		getRandomSolution(state, node);
		
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
		System.out.println("============== Q Table: Stay Action ================");
		node.printQTableStay();
		System.out.println("============== Q Table: Up Action ================");
		node.printQTableUp();		
		System.out.println("============== Q Table: Down Action ================");
		node.printQTableDown();		
		System.out.println("============== Q Table: Left Action ================");
		node.printQTableLeft();		
		System.out.println("============== Q Table: Right Action ================");
		node.printQTableRight();
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
//		int i = 0;
		// grab best direction from q table until goal is found
		while (!node.checkIfGoal()) {
//			i++;
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
			// implemented because my discount was too high and the top left corner was getting stuck
//			if (i > 30) {
//				System.out.println("Stuck, couldn't find solution!");
//				solution.printSolution();
//				return;
//			}
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
			int dir = random.nextInt(node.possibleActions().length);
//			System.out.println(dir);
			double qValue = node.calculateQValue(node.x, node.y, dir);
//			System.out.println(qValue);
			node.updateQValue(node.x, node.y, qValue, dir);
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
//			System.out.println("Node coordinates: " + node.x + ", " + node.y);
		}
	}

}
