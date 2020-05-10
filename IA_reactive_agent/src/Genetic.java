import java.util.Random;

public class Genetic {
	// nova popula��o gerada
	// selecao dos mais aptos
	// cruzamento
	// mutacao aleatoria
	// nova populacao ...

	// Elementos importantes:
	// operador de selecao: da preferencia p/ melhores individuos
	// operador de cruzamento: local escolhido aleatoriamente
	// operador de mutacao: realiza mutacoes, evita converg�ncia prematura

	// quando execu�oes param de dar solu�oes novas, ent�o convergiu = fim da
	// execucao do AG

	
	
	
	public Genetic() {}


	static int[] carga = new int[16];
	Agent agente = null;

	public Genetic(int [] carga) {
		this.carga = carga;
	}

	public int [] run() {

		int[][] populacao = new int[5][17];
		int[][] intermediaria = new int[5][17];

		System.out.println("Popula�ao: ");
		popular(populacao);
		printPopulacao(populacao, 17);

		for (int i=0;i<2000;i++) {
//			System.out.println("\nGeracao " + i);
			aptidar(populacao);
			//printPopulacao(populacao, 17);
			
			for(int j=0; j<populacao.length; j++) {
				if(populacao[j][16]==0) {
					return populacao[j];
				}
			}
			
			elitizar(populacao, intermediaria);
			// printPopulacao(populacao,17);
			gerar(populacao, intermediaria);
			// printPopulacao(populacao,17);
		}
		return populacao[0];
		
	}

	static void popular(int[][] populacao) {
		Random r = new Random();
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 16; j++) {
				populacao[i][j] = r.nextInt(4);
			}

	}

	static void printPopulacao(int[][] populacao, int limite) {
		System.out.println();
		for (int i = 0; i < 5; i++) {
			System.out.print("P: ");
			for (int j = 0; j < limite - 1; j++) {
				System.out.print(populacao[i][j] + " ");
			}
			System.out.print("   S: ");
			System.out.print(populacao[i][limite - 1] + " ");
			System.out.println("");
		}
	}

	static void elitizar(int[][] populacao, int[][] intermediaria) {
		int indexMenor = 0;
		for (int i = 0; i < 5; i++) {
			if (populacao[i][16] < populacao[indexMenor][16]) {
				indexMenor = i;
			}
		}
		clonar(intermediaria[0], populacao[indexMenor]);
	}

	static void gerar(int[][] populacao, int[][] intermediaria) {
		int linha = 0;
		for (int i = 0; i < 2; i++) {
			int pai = torner(populacao);
			int mae = torner(populacao);
			linha++;
			for (int j=0; j<4; j++) {
				intermediaria[linha][j] = populacao[pai][j];
				intermediaria[linha + 1][j] = populacao[mae][j];
			}
			for (int j=4; j<8; j++) {
				intermediaria[linha][j] = populacao[mae][j];
				intermediaria[linha+1][j] = populacao[pai][j];
			}
			for (int j=8; j<12; j++) {
				intermediaria[linha][j] = populacao[pai][j];
				intermediaria[linha+1][j] = populacao[mae][j];
			}
			for (int j=12; j<16; j++) {
				intermediaria[linha][j] = populacao[mae][j];
				intermediaria[linha + 1][j] = populacao[pai][j];
			}
			linha++;
			for(int k=0; k<5; k++) {mutate(populacao, linha);}
		}
		clonar(populacao, intermediaria);
	}

	
	static void mutate(int [][]populacao, int i) {
		Random r = new Random();
		int bau = r.nextInt(4);
		int posicao = r.nextInt(16);
		populacao[i][posicao] = bau;
	}

	static int torner(int[][] populacao) {

		Random r = new Random();
		int primeiro = r.nextInt(5);
		int segundo = r.nextInt(5);
		return (populacao[primeiro][16] < populacao[segundo][16]) ? primeiro : segundo;
	}

	static void clonar(int[] destino, int[] origem) {
		for (int j = 0; j < 17; j++) {
			destino[j] = origem[j];
		}
	}

	static void clonar(int[][] destino, int[][] origem) {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 17; j++) {
				destino[i][j] = origem[i][j];
			}
		}
	}

	static void aptidar(int[][] populacao) {
		// combinacoes: ab, ac, ad, bc, bd, cd
		// somar diferenca entre elas
		int a = 0, b = 0, c = 0, d = 0;
		for (int i = 0; i < 5; i++) {
			// populacao[i][16] = 0;
			for (int j = 0; j < 16; j++) {
				// populacao[i][16] += (populacao[i][j] == 1) ? carga[j] : -carga[j];
				switch (populacao[i][j]) {
				case 0:
					a = a + carga[j];
					break;
				case 1:
					b = b + carga[j];
					break;
				case 2:
					c = c + carga[j];
					break;
				case 3:
					d = d + carga[j];

				}

			}
			int ab, ac, ad, bc, bd, cd;
			ab = Math.abs(a - b);
			ac = Math.abs(a - c);
			ad = Math.abs(a - d);
			bc = Math.abs(b - c);
			bd = Math.abs(b - d);
			cd = Math.abs(c - d);
			populacao[i][16] = ab + ac + ad + bc + bd + cd;
			a = 0;
			b = 0;
			c = 0;
			d = 0;
		}
	}
}
