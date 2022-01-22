
public class State {
	int[][] stateRewardDown = new int[15][10];
	int[][] stateRewardUp = new int[15][10];
	int[][] stateRewardLeft = new int[15][10];
	int[][] stateRewardRight = new int[15][10];
	int[][] stateRewardStay = new int[15][10];
	
	public State() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 10; j++) {
				stateRewardDown[i][j] = -1;
				stateRewardUp[i][j] = -1;
				stateRewardLeft[i][j] = -1;
				stateRewardRight[i][j] = -1;
				stateRewardStay[i][j] = 0;
			}
		}
		stateRewardStay[12][7] = 100;
		stateRewardDown[12][6] = 100;
		stateRewardUp[12][8] = 100;
		stateRewardLeft[13][7] = 100;
		stateRewardRight[11][7] = 100;
	}

	public int getStateRewardDown(int a, int b) {
		return stateRewardDown[a][b];
	}

	public int getStateRewardUp(int a, int b) {
		return stateRewardUp[a][b];
	}
	
	public int getStateRewardLeft(int a, int b) {
		return stateRewardLeft[a][b];
	}
	
	public int getStateRewardRight(int a, int b) {
		return stateRewardRight[a][b];
	}

	public int getStateRewardStay(int a, int b) {
		return stateRewardStay[a][b];
	}
	
	/**
	 * This method prints the stateRewardStay array to the console.
	 */
	public void printState() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 15; j++) {
				if (stateRewardStay[j][i] == 100) {
					System.out.print(stateRewardStay[j][i] + " ");
				}
				else {
					System.out.print(stateRewardStay[j][i] + "   ");
				}
			}
			System.out.println();
		}
	}
}
