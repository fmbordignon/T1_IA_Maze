package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {
        String filename = "teste01";
        Scanner in = new Scanner(new File(filename));
        String aux;
        int tam = in.nextInt();
        String[][] mazeIn = new String[tam][tam];
        int i=0;
        in.nextLine();
        while (in.hasNext()) {
            aux = in.nextLine();
            aux = aux.replaceAll(" ", "");
            for(int j=0; j<10;j++){
                mazeIn[i][j] = String.valueOf(aux.charAt(j));
            }
            i++;
        }
        in.close();

        int[] fim = Genetic.run(filename);

        System.out.println("\n \n \n ---- Astar vai iniciar em 10 segundos ----");

        int[] inicio = {0,0};
        Thread.sleep(10000);
        A_star a = new A_star(inicio,fim, mazeIn);
        ArrayList<Node> path = a.run();
        System.out.println(path.toString());
        for(int j=0;j<path.size();j++){
            int x = path.get(j).getPosition()[0];
            int y = path.get(j).getPosition()[1];
            mazeIn[x][y] = "A";
        }
        System.out.println("\n (A) = Caminho até a saída\n");
        for (int k = 0; k < mazeIn.length; k++) {
            for (int l = 0; l < mazeIn[0].length; l++) {
                System.out.print(mazeIn[k][l]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
