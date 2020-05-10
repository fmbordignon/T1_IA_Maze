import java.io.IOException;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		for (int j = 0; j < 50; j++) {
			System.out.println();
		}
		System.out.println("LOCALIZE A POSICAO DO AGENTE (A)     SIMULACAO COMEÇA EM 15 SEGUNDOS\n"
				+ "Agente: A\n" + 
				"Parede: P\n" + 
				"Buraco: O\n" + 
				"Baus: B\n" + 
				"Saida: S\n" + 
				"Moedas: Numeros\n" + 
				"Vazio: -\n\n");
		Maze maze = new Maze();
		Agent agent = new Agent(maze);
		Thread.sleep(15000);
		agent.explore();
	}
}
