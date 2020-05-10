import java.util.ArrayList;
import java.util.Random;

public class Maze {

	private String[][] maze = new String[10][10];
	private int[] coins = { 10, 90, 3, 96, 92, 2, 90, 50, 50, 33, 20, 51, 84, 96, 96, 11 };
//	private int[] coins = { 10, 10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
	private int [] coinsPositions = new int[32];
	private int [] holesPositions= new int[10];
	private int [] chestsPositions = new int[8];
	int cont;
	boolean exitFree = false;
	
	public void setExitFree(boolean exitFree) {
		this.exitFree = exitFree;
	}

	public boolean isExitFree() {
		return exitFree;
	}

	public int[] getCoinsPositions() {
		return coinsPositions;
	}

	public int[] getHolesPositions() {
		return holesPositions;
	}

	public int[] getChestsPositions() {
		return chestsPositions;
	}


	private Random random = new Random();
	private String wallSide = "";
	private int[] agent_position=null;
	private String lastPositionContent= "  A  ";
	
	// temporario. Algoritmo de busca vai ter q achar saida no labirinto depois.
	private int[] saida = new int[2];

	public int[] getCoins() {
		return coins;
	}

	// Agente: A
	// Parede: P
	// Buraco: O
	// Baus: B
	// Saida: S
	// Moedas: Numeros
	// vazio: -
	public Maze() {
		init();
		generateWallsAndChests();
		generateHoles();
		generateCoins();
		this.agent_position = generateAgent();
		printMaze();
	}

	public void init() {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				maze[i][j] = "  -  ";
			}
		}
	}

	public void generateWallsAndChests() {
		// Decide o lado do paredao:
		int side = random.nextInt(4);
		// Decide a posicao da porta de saida no paredao:
		int exit = random.nextInt(10);
		// Decide as posicoes dos baus vizinhos ao paredao:
		ArrayList chests = new ArrayList<>();
		int n;
		for (int i = 0; i < 4; i++) {
			do {
				n = random.nextInt(10);
			} while (chests.contains(n));
			chests.add(n);
		}

		switch (side) {
		case 0:
			wallSide = "ACIMA";
			for (int i = 0; i < maze[0].length; i++) {
				if (exit == i) {
					maze[0][i] = "  S  ";
					this.saida[0]=0;
					this.saida[1]=i;}
				else
					maze[0][i] = "  P  ";
				if (chests.contains(i)) {
					maze[1][i] = "  B  ";
					chestsPositions[cont]=1;
					chestsPositions[cont+1]=i;
					cont=cont+2;
				}
			}
			break;
		case 1:
			wallSide = "ESQUERDA";
			for (int i = 0; i < maze.length; i++) {
				if (exit == i) {
					maze[i][0] = "  S  ";
					this.saida[0]=i;
					this.saida[1]=0;}
				else
					maze[i][0] = "  P  ";
				if (chests.contains(i)) {
					maze[i][1] = "  B  ";
					chestsPositions[cont]=i;
					chestsPositions[cont+1]=1;
					cont=cont+2;
					
				}
			}
			break;
		case 2:
			wallSide = "ABAIXO";
			for (int i = 0; i < maze[0].length; i++) {
				if (exit == i) {
					maze[maze[0].length - 1][i] = "  S  ";
					this.saida[0]=maze[0].length - 1;
					this.saida[1]=i;}
				else
					maze[maze[0].length - 1][i] = "  P  ";
				if (chests.contains(i)) {
					maze[maze[0].length - 2][i] = "  B  ";
					chestsPositions[cont]=maze[0].length - 2;
					chestsPositions[cont+1]=i;
					cont=cont+2;
				}
			}

			break;
		case 3:
			wallSide = "DIREITA";
			for (int i = 0; i < maze.length; i++) {
				if (exit == i) {
					maze[i][maze[0].length - 1] = "  S  ";
					this.saida[0]=i;
					this.saida[1]=maze[0].length - 1;}
				else
					maze[i][maze[0].length - 1] = "  P  ";
				if (chests.contains(i)) {
					maze[i][maze[0].length - 2] = "  B  ";
					chestsPositions[cont]=i;
					chestsPositions[cont+1]=maze[0].length - 2;
					cont=cont+2;
				}
			}
			break;
		}
		cont = 0;
		// Constroi os 4 muros internos:
		int direction;
		int initialLine, initialColumn;
		for (int i = 0; i < 4; i++) {
			direction = random.nextInt(2);

			switch (direction) {
			case (0):
				do {
					initialLine = random.nextInt(10);
					initialColumn = random.nextInt(6);
				} while (!(maze[initialLine][initialColumn].equals("  -  ")
						&& maze[initialLine][initialColumn + 1].equals("  -  ")
						&& maze[initialLine][initialColumn + 2].equals("  -  ")
						&& maze[initialLine][initialColumn + 3].equals("  -  ")
						&& maze[initialLine][initialColumn + 4].equals("  -  ")));
				for (int j = 0; j < 3; j++) {
					maze[initialLine][initialColumn + j] = "  P  ";
				}
				break;
			case (1):
				do {
					initialLine = random.nextInt(6);
					initialColumn = random.nextInt(10);
				} while (!(maze[initialLine][initialColumn].equals("  -  ")
						&& maze[initialLine + 1][initialColumn].equals("  -  ")
						&& maze[initialLine + 2][initialColumn].equals("  -  ")
						&& maze[initialLine + 3][initialColumn].equals("  -  ")
						&& maze[initialLine + 4][initialColumn].equals("  -  ")));
				for (int j = 0; j < 3; j++) {
					maze[initialLine + j][initialColumn] = "  P  ";
				}
				break;
			}

		}
	}

	public void generateHoles() {
		int line;
		int column;
		boolean validation;
		for (int i = 0; i < 5; i++) {
			do {
				line = random.nextInt(10);
				column = random.nextInt(10);
				if (line == 0) {
					validation = validateHolePosition("UpperEnd", line, column);
				} else if (column == 0) {
					validation = validateHolePosition("LeftEnd", line, column);
				} else if (column == maze[0].length - 1) {
					validation = validateHolePosition("RightEnd", line, column);
				} else if (line == maze.length - 1) {
					validation = validateHolePosition("BottomEnd", line, column);
				} else {
					validation = validateHolePosition("Central", line, column);
				}
			} while (!validation);
			maze[line][column] = "  O  ";
			holesPositions[cont]=line;
			holesPositions[cont+1]=column;
			cont=cont+2;
		}
		cont=0;
	}

	public boolean validateHolePosition(String holeArea, int line, int column) {
		
		switch(wallSide) {
			case "ACIMA":
				if(saida[0]+1==line && saida[1]==column) {
					return false;
				}
			case "ESQUERDA":
				if(saida[0]==line && saida[1]+1==column) {
					return false;
				}
			case "DIREITA":
				if(saida[0]==line && saida[1]-1==column) {
					return false;
				}
			case "ABAIXO":
				if(saida[0]-1==line && saida[1]==column) {
					return false;
				}
		}
			
		
		if (line == 0 && column == 0) {
			if (!maze[line][column].equals("  -  ") || maze[line + 1][column].equals("  O  ")
					|| maze[line][column + 1].equals("  O  ")) {
				return false;
			} else
				return true;
		} else if (line == maze.length - 1 && column == 0) {
			if (!maze[line][column].equals("  -  ") || maze[line - 1][column].equals("  O  ")
					|| maze[line][column + 1].equals("  O  ")) {
				return false;
			} else
				return true;
		} else if (line == 0 && column == maze[0].length - 1) {
			if (!maze[line][column].equals("  -  ") || maze[line + 1][column].equals("  O  ")
					|| maze[line][column - 1].equals("  O  ")) {
				return false;
			} else
				return true;
		} else if (line == maze.length - 1 && column == maze[0].length - 1) {
			if (!maze[line][column].equals("  -  ") || maze[line - 1][column].equals("  O  ")
					|| maze[line][column - 1].equals("  O  ")) {
				return false;
			} else
				return true;
		}

		switch (holeArea) {
		case ("Central"):
			if (!maze[line][column].equals("  -  ") || maze[line + 1][column].equals("  O  ")
					|| maze[line][column + 1].equals("  O  ") || maze[line - 1][column].equals("  O  ")
					|| maze[line][column - 1].equals("  O  ")) {
				return false;
			} else
				return true;
		case ("LeftEnd"):
			if (!maze[line][column].equals("  -  ") || maze[line + 1][column].equals("  O  ")
					|| maze[line][column + 1].equals("  O  ") || maze[line - 1][column].equals("  O  ")) {
				return false;
			} else
				return true;
		case ("RightEnd"):
			if (!maze[line][column].equals("  -  ") || maze[line + 1][column].equals("  O  ")
					|| maze[line - 1][column].equals("  O  ") || maze[line][column - 1].equals("  O  ")) {
				return false;
			} else
				return true;
		case ("UpperEnd"):
			if (!maze[line][column].equals("  -  ") || maze[line + 1][column].equals("  O  ")
					|| maze[line][column + 1].equals("  O  ") || maze[line][column - 1].equals("  O  ")) {
				return false;
			} else
				return true;
		case ("BottomEnd"):
			if (!maze[line][column].equals("  -  ") || maze[line][column + 1].equals("  O  ")
					|| maze[line - 1][column].equals("  O  ") || maze[line][column - 1].equals("  O  ")) {
				return false;
			} else
				return true;
		}

		return false;
	}

	public void generateCoins() {
		int line, column;
		for (int i = 0; i < coins.length; i++) {
			do {
				line = random.nextInt(10);
				column = random.nextInt(10);

			} while (!maze[line][column].equals("  -  "));
			if (coins[i] < 10)
				maze[line][column] = "  " + coins[i] + "  ";
			else {
				maze[line][column] = " " + coins[i] + "  ";
			}
			coinsPositions[cont]=line;
			coinsPositions[cont+1]=column;
			cont=cont+2;
		}
		cont=0;
	}
	// Gera agente no labirinto. Previne que agente fique preso entre paredes. Retorna array com posi��o atual do agente.
	public int[] generateAgent() {
		int[] position = new int[2];
		int i = 0;
		for (int j = 0; j < maze[0].length; j++) {
			if (maze[i][j].contains("-")) {
				if ((!maze[i][j + 1].contains("P")) || (!maze[i + 1][j].contains("P"))
						|| (!maze[i - 1][j].contains("P")) || (!maze[i][j - 1].contains("P"))) {
					maze[i][j] = "  A  ";
					position[0]=i;
					position[1]=j;
					return position;
				}
			} else if (maze[j][i].contains("-")) {
				if ((!maze[j][i + 1].contains("P")) || (!maze[j + 1][i].contains("P"))
						|| (!maze[j - 1][i].contains("P")) || (!maze[j][i - 1].contains("P"))) {
					maze[j][i] = "  A  ";
					return position;
				}
			}
		}
		return null;
	}

	public void printMaze() {
		System.out.println("    |0|  |1|  |2|  |3|  |4|  |5|  |6|  |7|  |8|  |9|");
		for (int i = 0; i < maze.length; i++) {
			System.out.print("|"+i+"|");
			for (int j = 0; j < maze[0].length; j++) {
				System.out.print(maze[i][j]);
			}
			System.out.println();
		}
	}

	public String[][] getMaze() {
		return maze;
	}
	public int[] getAgentPosition() {
		return agent_position;
	}
	public int[] getSaidaPosition() {
		return this.saida;
	}
	
	
	public void updateAgentPosition(int [] position, int [] before) {
		if(lastPositionContent.contains("B") || lastPositionContent.contains("S")) {
			maze[before[0]][before[1]] = lastPositionContent;
		} else {
			maze[before[0]][before[1]] = "  -  ";
		}
	
		this.agent_position[0] = position[0];
		this.agent_position[1] = position[1];
		lastPositionContent = maze[position[0]][position[1]];
		maze[position[0]][position[1]] = "  A  ";
	}
	
	
}
