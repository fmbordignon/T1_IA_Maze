import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		Scanner in = new Scanner(new File("IA_reactive_agent/teste01"));
		String aux;
		int tam = in.nextInt();
		String[][] mazeIn = new String[tam][tam];
		int i=0;
		in.nextLine();
		while (in.hasNext()) {
			aux = in.nextLine();
			aux = aux.replaceAll(" ", "");
			for(int j=0; j<10;j++){
				if(String.valueOf(aux.charAt(j)).equals("E")){
					mazeIn[i][j] = "A";
				}else{
					mazeIn[i][j] = String.valueOf(aux.charAt(j));
				}
			}
			i++;
		}
		in.close();
		System.out.println("\nSIMULACAO COMEÇA EM 15 SEGUNDOS\n"
				+ "Agente: A\n" +
				"Parede: 1\n" +
				"Buraco: B\n" +
				"Saida: S\n" +
				"Vazio: 0\n\n");
		Maze maze = new Maze(mazeIn);
//		Agent agent = new Agent(maze);
//		Thread.sleep(15000);
//		agent.explore();
	}
}
