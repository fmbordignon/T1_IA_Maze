import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class A_star {
	private int[] inicio = new int[2];
	private int[] objetivo = new int[2];
	Agent ag = null;

	private String[][] maze = null;

	public A_star(Agent a, int[] i, int[] o, Maze m) {
		this.inicio = i;
		this.objetivo = o;
		this.ag = a;
		this.maze = m.getMaze();
	}

	public ArrayList<State> run() {

		ArrayList<State> open_list = new ArrayList<State>();
		ArrayList<State> closed_list = new ArrayList<State>();
		ArrayList<State> finalPath = new ArrayList<State>();

		int[] current = new int[2];

		// Posicao inicial do agente
		double cg = calcHeuristic(this.inicio, this.objetivo);
		double cf = 0 + cg;
		State inicial = new State(this.inicio, cg, 0, cf, this.inicio);
		open_list.add(inicial);

		int count = 0;
		
		while (!open_list.isEmpty()) {

			// pega primeiro elemento da lista aberta (aquele com menor custo)
			current = open_list.get(0).getPosition();

			// checa se posicao atual = posicao do objetivo (bau ou saida)
			if ((current[0] == this.objetivo[0]) && (current[1] == this.objetivo[1])) {
				finalPath = reconstructPath(closed_list);
				break;
			}

			// ignora qnd sao obstaculos
			ArrayList<int[]> vizinhos = getVizinhos(current);

			for (int[] v : vizinhos) {
				
				int cost_initial = count;
				double cost_goal = calcHeuristic(current, this.objetivo);
				double cost_final = cost_initial + cost_goal;
				int[] pos_pai = current;

				State vizinho = new State(pos_pai, cost_goal, cost_initial, cost_final, v);

				boolean contains = false;

				// testa se vizinho ja esta em open_list ou closed_list
				for (State s : open_list) {
					if (s.getPosition()[0] == v[0] && s.getPosition()[1] == v[1]) {
						contains = true;
						break;
					}
				}
				if (!contains)
					for (State s : closed_list) {
						if (s.getPosition()[0] == v[0] && s.getPosition()[1] == v[1]) {
							contains = true;
							break;
						}
					}

				if (!contains)
					open_list.add(vizinho);
			}

			closed_list.add(open_list.get(0));
			open_list.remove(open_list.get(0));

			Collections.sort(open_list);

			vizinhos.clear();

			count++;
		}

		return finalPath;
	}

	public ArrayList<int[]> getVizinhos(int[] current) {
		ArrayList<int[]> vizinhos = new ArrayList<int[]>();

		// ignora qnd sao obstaculos
		int[] position = new int[2];
		try {
			position[0] = current[0] + 1;
			position[1] = current[1];
			if (ag.validRangePos(position[0], position[1])) {
				if (this.maze[position[0]][position[1]] != null) {
					if (this.maze[position[0]][position[1]].contains("O")) {
						if (ag.validPos(position[0] + 1, position[1])) {
							position[0] = position[0] + 1;
							vizinhos.add(position);
						}
					} else {
						if (!this.maze[position[0]][position[1]].contains("P")) {
							vizinhos.add(position);
						}
					}
				}
			}

			position = new int[2];
			position[0] = current[0] - 1;
			position[1] = current[1];
			if (ag.validRangePos(position[0], position[1])) {
				if (this.maze[position[0]][position[1]] != null) {
					if (this.maze[position[0]][position[1]].contains("O")) {
						if (ag.validPos(position[0] - 1, position[1])) {
							position[0] = position[0] - 1;
							vizinhos.add(position);
						}
					} else {
						if (!this.maze[position[0]][position[1]].contains("P")) {
							vizinhos.add(position);
						}
					}
				}
			}
			position = new int[2];
			position[0] = current[0];
			position[1] = current[1] + 1;
			if (ag.validRangePos(position[0], position[1])) {
				if (this.maze[position[0]][position[1]] != null) {
					if (this.maze[position[0]][position[1]].contains("O")) {
						if (ag.validPos(position[0], position[1] + 1)) {
							position[1] = position[1] + 1;
							vizinhos.add(position);
						}
					} else {
						if (!this.maze[position[0]][position[1]].contains("P")) {
							vizinhos.add(position);
						}
					}
				}
			}
			position = new int[2];
			position[0] = current[0];
			position[1] = current[1] - 1;
			if (ag.validRangePos(position[0], position[1])) {
				if (this.maze[position[0]][position[1]] != null) {
					if (this.maze[position[0]][position[1]].contains("O")) {
						if (ag.validPos(position[0], position[1] - 1)) {
							position[1] = position[1] - 1;
							vizinhos.add(position);
						}
					} else {
						if (!this.maze[position[0]][position[1]].contains("P")) {
							vizinhos.add(position);
						}
					}
				}
			}
		} catch (Exception e) {
		}

		return vizinhos;
	}

	private ArrayList<State> reconstructPath(ArrayList<State> path) {
		// comeca com ultimo elemento do path, que e o state "objetivo"
		State current = path.get(path.size() - 1);

		ArrayList<State> finalPath = new ArrayList<State>();
		finalPath.add(current);
		int i = 0, attempts = 0;
		while (i < calcHeuristic(this.inicio, this.objetivo) && attempts < 50) {
			for (State s : path) {
				if (s.getPosition()[0] == current.getPos_pai()[0] && s.getPosition()[1] == current.getPos_pai()[1]) {
					// current vai pro state anterior o atual, p/ o nodo came_from
					current = s;
					finalPath.add(current);
					i++;
					break;
				}

			}
			attempts++;
			path.remove(current);
		}
		Collections.reverse(finalPath);
		return finalPath;

	}

	/*
	 * Gera a funcao heuristica do estado atual
	 *
	 */
	public int calcHeuristic(int[] current, int[] goal) {
		int distance = Math.abs(current[0] - goal[0]) + Math.abs(current[1] - goal[1]);
		return distance;
	}
}
