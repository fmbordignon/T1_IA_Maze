package com.company;


import java.util.Arrays;

public class Node implements Comparable<Node> {

    private double cost_goal = 0;
    private int cost_initial = 0;
    private double cost_final = 0;
    private int[] position = new int[2];
    private int[] pos_pai = new int[2];

    public Node() {
    }

    public Node(int[] pp, double cg, int ci, double cf, int[] p) {
        this.pos_pai = pp;
        this.cost_goal = cg;
        this.cost_initial = ci;
        this.cost_final = cf;
        this.position = p;
    }

    public int[] getPos_pai() {
        return pos_pai;
    }

    }

    public double getCost_final() {
        return cost_final;
    }

    public int[] getPosition() {
        return position;
    }

    @Override
    public int compareTo(Node other_state) {
        if (this.cost_final < other_state.getCost_final()) {
            return -1;
        }
        if (this.cost_final > other_state.getCost_final()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Node{" +
                "position=" + Arrays.toString(position) +
                '}';
    }
}
