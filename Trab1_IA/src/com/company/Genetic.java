package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Genetic {


    private static String[][] labirinto;
    private static String[][] labirintoAux;
    private static String[][] agentes;
    private static String[][] geracaoAux;
    private static int[] posS;
    private static int[] notas;
    private static int[] notasAux;
    private static String concat;
    private static String concatPath;
    private static int entradaIndexX;
    private static int entradaIndexY;
    private static int saidaIndexX;
    private static int saidaIndexY;
    private static int caminhoSize;
    private static int counter;
    private static int dropout;
    private static String filename;
    private static final int DIMENSOES = 10;
    private static final int POPULACAO = 201;
    private static int maiorNota;
    private static int melhorAgente;
    private static int segundoMelhorAgente;
    private static int segundaMaiorNota;
    private static String mascara = "1";

    static int finalx;
    static int finaly;

    public static int[] run(String filename) throws FileNotFoundException {

        for (int i = 1; i < DIMENSOES * DIMENSOES; i++) { // Inicializa mascara
            Random r = new Random();
            if (r.nextInt(2) == 0) {
                mascara += "0";
            } else {
                mascara += "1";
            }
        }
        System.out.println(mascara);


//            filename = args[0];
        File file = new File(filename);
        Scanner in = new Scanner(file);
        counter = 0;
        dropout = 0;
        maiorNota = 0;
        melhorAgente = 0;
        segundaMaiorNota = 0;
        segundoMelhorAgente = 1;
        caminhoSize = DIMENSOES * DIMENSOES;
        agentes = new String[POPULACAO][caminhoSize];
        geracaoAux = new String[POPULACAO][caminhoSize];
        notas = new int[POPULACAO];
        notasAux = new int[POPULACAO];
        posS = new int[POPULACAO];

        agentes = iniciaGeracao(POPULACAO, caminhoSize);

        labirinto = new String[DIMENSOES][DIMENSOES];
        labirintoAux = new String[DIMENSOES][DIMENSOES];
        in.nextLine();
        for (int i = 0; i < DIMENSOES; i++) { // Inicializa o labirinto
            for (int j = 0; j < DIMENSOES; j++) {
                String aux = in.next();
                labirinto[i][j] = aux;
                labirintoAux[i][j] = aux;
            }
        }

        for (int i = 0; i < 200; i++) {
            System.out.println("Calculando aptidao...\n");
            for (int j = 0; j < agentes.length; j++) {
                notas[j] = aptidao(agentes[j]);
                if (notas[j] > maiorNota) {
                    melhorAgente = j;
                    maiorNota = notas[j];
                }
                if(concat.contains("SAIDA")
                        && !concatPath.contains("bateu")
                        && !concatPath.contains("saiu")){
                    break;
                }

                Scanner scanner = new Scanner(file);
                scanner.nextLine();
                for (int k = 0; k < DIMENSOES; k++) {
                    for (int l = 0; l < DIMENSOES; l++) {
                        String aux = scanner.next();
                        labirinto[k][l] = aux;
                    }
                }
                scanner.close();

            }

            geracaoAux[0] = agentes[melhorAgente].clone();
            notasAux[0] = maiorNota;

            System.out.println("Criando geracao numero " + (i + 1) + "...");

            for (int j = 1; j < agentes.length; j++) {
                Random r1 = new Random();
                Random r2 = new Random();

                int p1 = r1.nextInt(POPULACAO);
                int p2 = r1.nextInt(POPULACAO);
                int m1 = r2.nextInt(POPULACAO);
                int m2 = r2.nextInt(POPULACAO);

                String[] pai = notas[p1] > notas[p2] ? agentes[p1].clone() : agentes[p2].clone();
                String[] mae = notas[m1] > notas[m2] ? agentes[m1].clone() : agentes[m2].clone();

                for (int k = 0; k < mascara.length(); k++) {
                    if (mascara.charAt(k) == 1) {
                        geracaoAux[j][k] = pai[k];
                    } else {
                        geracaoAux[j][k] = mae[k];
                    }
                }
                notasAux[j] = 0;

                j++;

                for (int k = 0; k < mascara.length(); k++) {
                    if (mascara.charAt(k) == 1) {
                        geracaoAux[j][k] = mae[k];
                    } else {
                        geracaoAux[j][k] = pai[k];
                    }
                }
                notas[j] = 0;
            }

            agentes = geracaoAux.clone();
            notas = notasAux.clone();



            for (int j = 1; j < agentes.length; j++) {
                Random mutacao = new Random();
                if (mutacao.nextInt(100) >= 10) {
                    agentes[j] = mutacao(agentes[j].length, agentes[j], j);
                }
            }

            System.out.println("Melhor da geracao: ");

            printAgente(agentes[0]);
            System.out.println("Nota : " + notas[0]);
            System.out.println("Posicao : " + melhorAgente);
            System.out.println();
            melhorAgente = 0;
        }

        in.close();
        System.out.println();
        System.out.println("Melhor agente");
        System.out.print("Agente  ");
        printAgente(agentes[melhorAgente]);

        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        for (int k = 0; k < DIMENSOES; k++) {
            for (int l = 0; l < DIMENSOES; l++) {
                String aux = scanner.next();
                labirinto[k][l] = aux;
            }
        }
        scanner.close();
        aptidao(agentes[melhorAgente]);

        System.out.println(concat);
        System.out.println(concatPath);
        System.out.println(saidaIndexX + " " + saidaIndexY);
        System.out.println(finalx + " " + finaly);
        System.out.println("NOTA : " + notas[melhorAgente]);

        int[] saida = {finalx, finaly};
        return saida;
    }

    private static int aptidao(String[] path) {
        int score = 0;
        int currentX = entradaIndexX;
        int currentY = entradaIndexY;
        concat = "Caminho ";
        concatPath = "Celulas ";
        int[] multi = new int[]{0, 1, 0, 0};

        for (int k = 0; k < path.length; k++) {

            int[] feedback = move(path[k], currentX, currentY);
            currentX = feedback[1];
            currentY = feedback[2];
            finalx = feedback[1];
            finaly = feedback[2];

            if (feedback[0] == 0) {
                multi[0] += 10;
                multi[1] = 0;
                multi[2] = 0;
                multi[3] = 0;

                score -= multi[0];
                concat += "| BATEU ";
            } else {
                if (feedback[0] == 1) {
                    if (verificarCiclo(path, k)) { // VERIFICA SE RETORNA DE ONDE VEIO
                        multi[0] = 0;
                        multi[1] = 1;
                        multi[2] += 1;
                        multi[3] = 0;

                        score -= 5;
                        concat += "| CICLO ";
                    } else {
                        multi[0] = 0;
                        multi[1] *= 2;
                        multi[2] = 0;
                        multi[3] = 0;
                        if (multi[1] > 128)
                            multi[1] = 128;

                        score += multi[1];
                        concat += "| ANDOU ";
                    }
                } else {
                    if (feedback[0] == 2) {
                        concat += "| CHEGOU SAIDA \n";
                        score += 1000;
                        break;
                    } else {
                        if (feedback[0] == -1) {
                            concat += "| SAIU ";
                            multi[0] = 0;
                            multi[1] = 1;
                            multi[2] = 0;
                            multi[3] += 1;
                            score -= (250 * multi[3]);
                        }
                    }
                }
            }
        }
        return score;
    }


    private static int[] move(String movement, int positionX, int positionY) {
        int[] statusSaida;
        int x = positionX;
        int y = positionY;
        String newPosition;

        switch (movement) {
            case "U":
                if (!checkMove("U", positionX - 1, positionY)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX - 1][positionY];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX - 1;
                        y = positionY;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";
                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // SAIDA
                                x = positionX - 1;
                                y = positionY;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "UR":
                if (!checkMove("UR", positionX - 1, positionY + 1)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += " | saiu";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX - 1][positionY + 1];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX - 1;
                        y = positionY + 1;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";
                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // 3 chegou na saida!
                                x = positionX - 1;
                                y = positionY + 1;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "R":
                if (!checkMove("R", positionX, positionY + 1)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX][positionY + 1];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX;
                        y = positionY + 1;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";

                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // SAIDA
                                y = positionY + 1;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "DR":
                if (!checkMove("DR", positionX + 1, positionY + 1)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX + 1][positionY + 1];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX + 1;
                        y = positionY + 1;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";

                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // 3 chegou na saida!
                                x = positionX + 1;
                                y = positionY + 1;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "D":
                if (!checkMove("D", positionX + 1, positionY)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX + 1][positionY];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX + 1;
                        y = positionY;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";

                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // SAIDA
                                x = positionX + 1;
                                y = positionY;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "DL":
                if (!checkMove("DL", positionX + 1, positionY - 1)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX + 1][positionY - 1];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX + 1;
                        y = positionY - 1;
                        concatPath += "| " + x + " , " + y + " ";

//                        labirinto[x][y] = "$";
                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";

                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // 3 chegou na saida!
                                x = positionX + 1;
                                y = positionY - 1;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "L":
                if (!checkMove("L", positionX, positionY - 1)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX][positionY - 1];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) { // MOVE VALIDO
                        x = positionX;
                        y = positionY - 1;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";

                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // SAIDA
                                x = positionX;
                                y = positionY - 1;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
            case "UL":
                if (!checkMove("UL", positionX - 1, positionY - 1)) {
                    statusSaida = new int[]{-1, x, y};
                    concatPath += "| saiu ";
                    return statusSaida;
                } else {
                    newPosition = labirinto[positionX - 1][positionY - 1];
                    if (newPosition.equals("0")
                            || newPosition.equals("E")) {
                        x = positionX - 1;
                        y = positionY - 1;
                        concatPath += "| " + x + " , " + y + " ";

                        statusSaida = new int[]{1, x, y};
                        return statusSaida;
                    } else {
                        if (newPosition.equals("B") || newPosition.equals("1")) {
                            statusSaida = new int[]{0, x, y};
                            concatPath += "| bateu ";

                            return statusSaida;
                        } else {
                            if (newPosition.equals("S")) { // SAIDA
                                x = positionX - 1;
                                y = positionY - 1;
                                concatPath += "| " + x + " , " + y + " ";

                                statusSaida = new int[]{2, x, y};
                                return statusSaida;
                            }
                        }
                    }
                }
        }
        return null;
    }

    private static boolean checkMove(String move, int positionX, int positionY) {
        switch (move) {
            case "U":
                if (positionX < 0)
                    return false;
                else
                    return true;
            case "UR":
                if (positionX < 0 || positionY > DIMENSOES - 1)
                    return false;
                else
                    return true;
            case "R":
                if (positionY > DIMENSOES - 1)
                    return false;
                else
                    return true;
            case "DR":
                if (positionX > DIMENSOES - 1 || positionY > DIMENSOES - 1)
                    return false;
                else
                    return true;
            case "D":
                if (positionX > DIMENSOES - 1)
                    return false;
                else
                    return true;
            case "DL":
                if (positionX > DIMENSOES - 1 || positionY < 0)
                    return false;
                else
                    return true;
            case "L":
                if (positionY < 0)
                    return false;
                else
                    return true;
            case "UL":
                if (positionX < 0 || positionY < 0)
                    return false;
                else
                    return true;
        }
        return false;
    }

    private static boolean verificarCiclo(String[] path, int moves) {
        boolean b = false;
        if (moves == 0) {
            return false;
        } else {
            switch (path[moves - 1]) {
                case "U":
                    if (path[moves].equals("D")) b = true;
                    break;
                case "UR":
                    if (path[moves].equals("DL")) b = true;
                    break;
                case "R":
                    if (path[moves].equals("L")) b = true;
                    break;
                case "DR":
                    if (path[moves].equals("UL")) b = true;
                    break;
                case "D":
                    if (path[moves].equals("U")) b = true;
                    break;
                case "DL":
                    if (path[moves].equals("UR")) b = true;
                    break;
                case "L":
                    if (path[moves].equals("R")) b = true;
                    break;
                case "UL":
                    if (path[moves].equals("DR")) b = true;
                    break;
            }
//            System.out.println(" ANTERIOR " + path[moves - 1] + " ATUAL " + path[moves] + " CICLO? "  + b);
            return b;
        }
    }

    private static void printAgente(String[] moves) {
        for (int k = 0; k < moves.length; k++) {
            System.out.print(moves[k] + " ");
        }
        System.out.println();
    }

    private static String[][] iniciaGeracao(int numAgentes, int caminhoSize) {
        Random random = new Random();
        String[][] path = new String[numAgentes][caminhoSize];
        int a;
        for (int i = 0; i < numAgentes; i++) {
            notas[i] = 0;
            for (int j = 0; j < caminhoSize; j++) {
                a = random.nextInt(8) + 1;
                switch (a) {
                    case 1:
                        path[i][j] = "U";
                        break;
                    case 2:
                        path[i][j] = "UR";
                        break;
                    case 3:
                        path[i][j] = "R";
                        break;
                    case 4:
                        path[i][j] = "DR";
                        break;
                    case 5:
                        path[i][j] = "D";
                        break;
                    case 6:
                        path[i][j] = "DL";
                        break;
                    case 7:
                        path[i][j] = "L";
                        break;
                    case 8:
                        path[i][j] = "UL";
                        break;
                }
            }
        }
        return path;
    }

    private static String[] mutacao(int caminhoSize, String[] path, int num) {
        Random mutacao = new Random();
        Random r1 = new Random();
        int percent = 2;
        int a;
        for (int j = 0; j < caminhoSize; j++) {
            if(posS[num] == j)
                percent = 9;
            if (r1.nextInt(10) > percent) {
                a = mutacao.nextInt(8) + 1;
                switch (a) {
                    case 1:
                        path[j] = "U";
                        break;
                    case 2:
                        path[j] = "UR";
                        break;
                    case 3:
                        path[j] = "R";
                        break;
                    case 4:
                        path[j] = "DR";
                        break;
                    case 5:
                        path[j] = "D";
                        break;
                    case 6:
                        path[j] = "DL";
                        break;
                    case 7:
                        path[j] = "L";
                        break;
                    case 8:
                        path[j] = "UL";
                        break;
                }
            }
        }
        return path;
    }


}

