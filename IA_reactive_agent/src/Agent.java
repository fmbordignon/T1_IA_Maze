import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.event.TreeWillExpandListener;

public class Agent {
	private Maze maze;
	private int chestsPositions[] = new int[8];
	int chestsPositionsCont;
	private int exitPosition[] = new int[2];
	private ArrayList coin = new ArrayList<>();
	private String currentPositionContent = "  -  ";
	private String currentDirection;
	private Random random = new Random();
	private int points = 0;
	private int moviements=0;
	private String log="";
	private int [] coinDistribution= new int[17];
	private ArrayList<Integer> chestA = new ArrayList<Integer>();
	private ArrayList<Integer> chestB = new ArrayList<Integer>();
	private ArrayList<Integer> chestC = new ArrayList<Integer>();
	private ArrayList<Integer> chestD = new ArrayList<Integer>();

	public ArrayList getCoin() {
		return coin;
	}

	public Agent(Maze maze) {
		this.maze = maze;
		sortDirection();
	}

	public void explore() throws InterruptedException, IOException {
		while ( ! maze.isExitFree()) {
			for (int j = 0; j < 50; j++) {
				System.out.println();
			}
			if(coin.size() != maze.getCoins().length || chestsPositionsCont!=8) {
				scan();
				move();
				explorePos();
				printData();
			} else {
				geneticAlg();
				distributeCoins();
			}
			if(moviements%20==0 && moviements!=0) {
				sortDirection();
				move();
				explorePos();
				printData();
			}
			if(moviements>=100) {
				gameOver("O agente não econtrou moedas na sua área possivel de exploracao após 100 movimentos de busca.");
				return;
			}			
			Thread.sleep(500);
		}
	}
	
	public void explorePos() {
		if (currentPositionContent.replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]")) {
			int c = Integer.parseInt(currentPositionContent.replaceAll(" ", ""));
			coin.add(c);
			points = points + (c*10);
			moviements = 0;
			log = log+"\nMoedas coletadas! + "+points+" pontos";
			currentPositionContent = "  -  ";
		}
	}

	public void sortDirection() {
		int n;
		if (currentDirection == null) {
			n = random.nextInt(4);
		} else {
			if (currentDirection.contains("up") || currentDirection.contains("down")) {
				n = random.nextInt(2) + 2;
			} else {
				n = random.nextInt(2);
			}
		}

		switch (n) {
		case 0:
			currentDirection = "up";
			break;
		case 1:
			currentDirection = "down";
			break;
		case 2:
			currentDirection = "left";
			break;
		case 3:
			currentDirection = "right";
			break;
		}
	}

	public void move() {
		int[] position = maze.getAgentPosition();
		int[] before = new int[2];
		before[0] = position[0];
		before[1] = position[1];
		int x, y;

		switch (currentDirection) {
		case "up":
			x = position[0] - 1;
			y = position[1];
			if (scanPos(x, y).contains("O") && validPos(x - 1, y)) {
				position[0] = x - 1;
				currentPositionContent = maze.getMaze()[position[0]][position[1]];
				points = points+30;
				moviements++;
				log = log+"\nPulou buraco! +30 pontos";
				break;

			} else {
				if (validPos(x, y)) {
					position[0] = x;
					moviements++;
					currentPositionContent = maze.getMaze()[position[0]][position[1]];
				} else {
					sortDirection();
					// move();

				}
			}
			break;

		case "down":
			x = position[0] + 1;
			y = position[1];
			if (scanPos(x, y).contains("O") && validPos(x + 1, y)) {
				position[0] = x + 1;
				currentPositionContent = maze.getMaze()[position[0]][position[1]];
				points = points+30;
				moviements++;
				log = log+"\nPulou buraco! +30 pontos";
				break;

			} else {
				if (validPos(x, y)) {
					position[0] = x;
					moviements++;
					currentPositionContent = maze.getMaze()[position[0]][position[1]];
				} else {
					sortDirection();
					// move();
				}
			}
			break;

		case "left":
			x = position[0];
			y = position[1] - 1;
			if (scanPos(x, y).contains("O") && validPos(x, y - 1)) {
				position[1] = y - 1;
				currentPositionContent = maze.getMaze()[position[0]][position[1]];
				points = points+30;
				moviements++;
				log = log+"\nPulou buraco! +30 pontos";
				break;

			} else {
				if (validPos(x, y)) {
					position[1] = y;
					moviements++;
					currentPositionContent = maze.getMaze()[position[0]][position[1]];
				} else {
					sortDirection();
					// move();
				}
			}
			break;

		case "right":
			x = position[0];
			y = position[1] + 1;
			if (scanPos(x, y).contains("O") && validPos(x, y + 1)) {
				position[1] = y + 1;
				currentPositionContent = maze.getMaze()[position[0]][position[1]];
				points = points+30;
				moviements++;
				log = log+"\nPulou buraco! +30 pontos";
				break;

			} else {
				if (validPos(x, y)) {
					position[1] = y;
					moviements++;
					currentPositionContent = maze.getMaze()[position[0]][position[1]];
				} else {
					sortDirection();
					// move();
				}

			}
		}

		maze.updateAgentPosition(position, before);
	}

	public boolean validPos(int x, int y) {
		return validRangePos(x, y) && !maze.getMaze()[x][y].contains("P") && !maze.getMaze()[x][y].contains("O");
	}

	// Percepcao: verifica ate 2 casas nas 4 direcoes
	public void scan() {
		int x = maze.getAgentPosition()[0] - 1;
		int y = maze.getAgentPosition()[1];

		if (validRangePos(x, y) || validRangePos(x - 1, y)) {
			if (scanPos(x, y).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]")
					|| (scanPos(x - 1, y).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]") && validPos(x, y))) {
				currentDirection = "up";

			}

		}

		x = maze.getAgentPosition()[0];

		y = maze.getAgentPosition()[1] + 1;
		if (validRangePos(x, y) || validRangePos(x, y + 1)) {
			if (scanPos(x, y).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]")
					|| (scanPos(x, y + 1).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]") && validPos(x, y))) {

				currentDirection = "right";

			}

		}

		x = maze.getAgentPosition()[0] + 1;
		y = maze.getAgentPosition()[1];

		if (validRangePos(x, y) || validRangePos(x + 1, y)) {
			if (scanPos(x, y).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]")
					|| (scanPos(x + 1, y).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]") && validPos(x, y))) {

				currentDirection = "down";

			}

		}

		x = maze.getAgentPosition()[0];

		y = maze.getAgentPosition()[1] - 1;
		if (validRangePos(x, y) || validRangePos(x, y - 1)) {
			if (scanPos(x, y).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]")
					|| (scanPos(x, y - 1).replaceAll(" ", "").matches("^[0-9]{2}|^[0-9]") && validPos(x, y))) {

				currentDirection = "left";

			}

		}

	}

	public String scanPos(int x, int y) {
		if (!validRangePos(x, y))
			return "invalid position";
		if (maze.getMaze()[x][y].contains("B")) {	
			saveChest(x, y);
		}
		if (maze.getMaze()[x][y].contains("S")) {
			exitPosition[0] = x;
			exitPosition[1] = y;
		}
		return maze.getMaze()[x][y];
	}
	
	public void distributeCoins() throws InterruptedException {
		for(int i=0; i<7; i=i+2) {
			int [] chestsPos= {chestsPositions[i], chestsPositions[i+1]};
			A_star a = new A_star(this, maze.getAgentPosition(), chestsPos, maze);
			ArrayList<State> path = a.run();
			for(State s: path) {
				currentPositionContent = maze.getMaze()[s.getPosition()[0]] [s.getPosition()[1]];
				maze.updateAgentPosition(s.getPosition(), maze.getAgentPosition());
				for (int j = 0; j < 50; j++) {
					System.out.println();
				}
				printData();
				System.out.print("\nDistribuicao moedas: ");
				
				System.out.print(" Baú A: ");
				chestA.forEach(n->{System.out.print(n+" ");});
				int A=0;
				for(Integer n: chestA) {A=A+n;}
				System.out.print(" (total:" +A+" ) |");
				
				System.out.print("  Baú B: ");
				chestB.forEach(n->{System.out.print(n+" ");});
				int B=0;
				for(Integer n: chestB) {B=B+n;}
				System.out.print(" (total:" +B+" ) |");
				
				
				System.out.print("  Baú C: ");
				chestC.forEach(n->{System.out.print(n+" ");});
				int C=0;
				for(Integer n: chestC) {C=C+n;}
				System.out.print(" (total:" +C+" ) |");
				
				
				System.out.print("  Baú D: ");
				chestD.forEach(n->{System.out.print(n+" ");});
				int D=0;
				for(Integer n: chestD) {D=D+n;}
				System.out.print(" (total:" +D+" ) |");
				
				System.out.print("\nDiferenca média total entre baús: "+(coinDistribution[coinDistribution.length-1]*1.0)/4);
				Thread.sleep(500);
			}
			
		}
		maze.setExitFree(true);
	}
	
	public void gameOver(String reason) {
		for (int j = 0; j < 50; j++) {System.out.println();}
		System.out.println(log+"      <------- log"+"\n_________________________________________________________");
		System.out.println("------GAME OVER------");
		System.out.println(reason);
		System.out.println("Pontuacao: "+points);
	}
	
	public void geneticAlg () {
		int [] coinArray = toArray(this.coin);
		Genetic g = new Genetic(coinArray);
//		while(true) {p(coinArray);}
		coinDistribution = g.run();
		for(int k=0; k<coinDistribution.length-1; k++) {
			switch (coinDistribution[k]) {
			case 0:
				chestA.add(coinArray[k]);
				break;
			case 1:
				chestB.add(coinArray[k]);
				break;
			case 2:
				chestC.add(coinArray[k]);
				break;
			case 3:
				chestD.add(coinArray[k]);
			}
		}
		
	}
	
	public int [] toArray(ArrayList a) {
		int [] result = new int[a.size()+1];
		for(int i=0; i<result.length-1; i++) {
			result[i] = Integer.parseInt(a.get(i).toString());
		}
		return result;
	}
	
	public void p(int [] v) {
		for(int i=0; i<v.length; i++) {
			System.out.print(v[i]+" ");
		}
	}
	
	public void printData() {
		System.out.println(log+"      <------- log"+"\n_________________________________________________________");
		System.out.print("Posicao dos baus: ");
		for (int j = 0; j < 7; j = j + 2) {
			System.out.print(" " + chestsPositions[j] + chestsPositions[j + 1]);
		}
		System.out.println("\nPosicao da saída: " + exitPosition[0] + exitPosition[1]);
		System.out.print("Moedas coletadas: ");
		coin.stream().forEach(c -> {
			System.out.print("  " + c);
		});
		System.out.print("\nPosicao atual   X: " + maze.getAgentPosition()[0]);
		System.out.println("  Y: " + maze.getAgentPosition()[1]);
		System.out.println("Direcao atual: " + currentDirection);
		System.out.println("Pontuacao: "+points);

		maze.printMaze();
	}
	
	public boolean validRangePos(int i, int j) {
		return (i >= 0 && i < maze.getMaze().length && j >= 0 && j < maze.getMaze()[0].length);
	}

	public void saveChest(int x, int y) {
		for (int i = 0; i < 7; i = i + 2) {
			if (chestsPositions[i] == x && chestsPositions[i + 1] == y) {
				return;
			}
		}
		if (chestsPositionsCont < 8) {
			chestsPositions[chestsPositionsCont] = x;
			chestsPositionsCont++;
			chestsPositions[chestsPositionsCont] = y;
			chestsPositionsCont++;
		}
		moviements = 0;
	}
}
