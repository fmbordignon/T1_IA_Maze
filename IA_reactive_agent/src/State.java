
public class State implements Comparable<State> {
	/*
	 * para cada vizinha, vê custo da vizinha até o objetivo, custo da vizinha até o
	 * inicio(anterior+1), e soma desses dois q gera o custo final. Custo = número
	 * de casas
	 */

	private double cost_goal = 0;
	private int cost_initial = 0;
	private double cost_final = 0;
	private int[] position = new int[2];
	private int[] pos_pai = new int[2];

	public State() {
	}

	public State(int[] pp, double cg, int ci, double cf, int[] p) {
		this.pos_pai = pp;
		this.cost_goal = cg;
		this.cost_initial = ci;
		this.cost_final = cf;
		this.position = p;
	}

	public int[] getPos_pai() {
		return pos_pai;
	}

	public double getCost_goal() {
		return cost_goal;
	}

	public int getCost_initial() {
		return cost_initial;
	}

	public double getCost_final() {
		return cost_final;
	}

	public int[] getPosition() {
		return position;
	}

	@Override
	public int compareTo(State other_state) {
		if (this.cost_final < other_state.getCost_final()) {
			return -1;
		}
		if (this.cost_final > other_state.getCost_final()) {
			return 1;
		}
		return 0;
	}

}
