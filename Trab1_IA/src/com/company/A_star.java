package com.company;
import java.util.ArrayList;
import java.util.Collections;

public class A_star {
    private int[] inicio;
    private int[] objetivo;

    private String[][] maze;

    public A_star( int[] i, int[] o, String[][] maze) {
        this.inicio = i;
        this.objetivo = o;

        this.maze = maze;
    }

    public ArrayList<Node> run() {
        System.out.println("Iniciou Astar");
        ArrayList<Node> open_list = new ArrayList<Node>();
        ArrayList<Node> closed_list = new ArrayList<Node>();
        ArrayList<Node> finalPath = new ArrayList<Node>();

        int[] current;

        // Posicao inicial do agente
        double cg = calculaHeuristica(this.inicio, this.objetivo);
        double cf = 0 + cg;
        Node inicial = new Node(this.inicio, cg, 0, cf, this.inicio);
        open_list.add(inicial);

        int count = 0;

        while (!open_list.isEmpty()) {

            current = open_list.get(0).getPosition();

            // checa se posicao atual = posicao do objetivo
            if ((current[0] == this.objetivo[0]) && (current[1] == this.objetivo[1])) {
                finalPath = constroiCaminho(closed_list);
                break;
            }

            // ignora qnd sao obstaculos
            ArrayList<int[]> vizinhos = getVizinhos(current);

            for (int[] v : vizinhos) {

                int cost_initial = count;
                double cost_goal = calculaHeuristica(current, this.objetivo);
                double cost_final = cost_initial + cost_goal;
                int[] pos_pai = current;

                Node vizinho = new Node(pos_pai, cost_goal, cost_initial, cost_final, v);

                boolean contains = false;

                for (Node s : open_list) {
                    if (s.getPosition()[0] == v[0] && s.getPosition()[1] == v[1]) {
                        contains = true;
                        break;
                    }
                }
                if (!contains)
                    for (Node s : closed_list) {
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
        System.out.println("Achou melhor caminho");
        return finalPath;
    }

    public ArrayList<int[]> getVizinhos(int[] current) {
        ArrayList<int[]> vizinhos = new ArrayList<int[]>();

        int[] position = new int[2];
        try {
            position[0] = current[0] + 1;
            position[1] = current[1];
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }

            position = new int[2];
            position[0] = current[0] - 1;
            position[1] = current[1];
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }
            position = new int[2];
            position[0] = current[0];
            position[1] = current[1] + 1;
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }
            position = new int[2];
            position[0] = current[0];
            position[1] = current[1] - 1;
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }

            position = new int[2];
            position[0] = current[0] + 1;
            position[1] = current[1] + 1;
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }

            position = new int[2];
            position[0] = current[0] + 1;
            position[1] = current[1] - 1;
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }

            position = new int[2];
            position[0] = current[0] - 1;
            position[1] = current[1] + 1;
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }
                }
            }

            position = new int[2];
            position[0] = current[0] - 1;
            position[1] = current[1] - 1;
            if (validaPosicao(position[0], position[1])) {
                if (this.maze[position[0]][position[1]] != null) {

                    if (!this.maze[position[0]][position[1]].contains("1") && !this.maze[position[0]][position[1]].contains("B")) {
                        vizinhos.add(position);
                    }

                }
            }
        } catch (Exception e) {
        }

        return vizinhos;
    }

    private ArrayList<Node> constroiCaminho(ArrayList<Node> path) {
        Node current = path.get(path.size() - 1);

        ArrayList<Node> finalPath = new ArrayList<Node>();
        finalPath.add(current);
        int i = 0, attempts = 0;
        while (i < calculaHeuristica(this.inicio, this.objetivo) && attempts < 50) {
            for (Node s : path) {
                if (s.getPosition()[0] == current.getPos_pai()[0] && s.getPosition()[1] == current.getPos_pai()[1]) {
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

    public boolean validaPosicao(int i, int j) {
        return (i >= 0 && i < maze.length && j >= 0 && j < maze[0].length);
    }

    public int calculaHeuristica(int[] current, int[] goal) {
        int distance = Math.abs(current[0] - goal[0]) + Math.abs(current[1] - goal[1]);
        return distance;
    }
}
