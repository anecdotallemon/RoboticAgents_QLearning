
public class Solution {
	private String[][] solution = new String[15][10];
	
	/**
	 * Constructor for Solution class, initializes all values to "." and
	 * the goal at 12, 7.
	 */
	public Solution() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 10; j++) {
				solution[i][j] = ".";
			}
		}
		solution[12][7] = "G";
	}
	
	
	/**
	 * This method sets the starting position on the solution board.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setStart(int x, int y) {
		solution[x][y] = "S";
	}
	
	
	/**
	 * This method updates a given point and direction to visualize the next
	 * step on the path towards the goal.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param move the direction travelled from these coordinates
	 */
	public void updateSolution(int x, int y, int move) {
		if (solution[x][y] == "S") {
			if (move == 1) {
				solution[x][y] = "S^";
			}
			else if (move == 2) {
				solution[x][y] = "Sv";
			}
			else if (move == 3) {
				solution[x][y] = "<-S";
			}
			else if (move == 4) {
				solution[x][y] = "S->";
			}
		}
		else if (move == 1) {
			solution[x][y] = "^";
		}
		else if (move == 2) {
			solution[x][y] = "v";
		}
		else if (move == 3) {
			solution[x][y] = "<-";					
		}
		else if (move == 4) {
			solution[x][y] = "->";
		}
	}
	
	/**
	 * This method prints the solution board to the console.
	 */
	public void printSolution() {
		System.out.println("Here is an optimal path to take to get to the reward. Multiple paths may exist.");
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(String.format("%4s", solution[j][i]));
			}
			System.out.println();
		}
	}
	
}
