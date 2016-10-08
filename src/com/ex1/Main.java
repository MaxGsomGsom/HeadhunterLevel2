package com.ex1;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    private static int[][] dpos = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; //разница координат для 4х соседних клеток
    private static Scanner input = new Scanner(System.in);
    private static PrintStream output = System.out;

    public static void main(String[] args) {
        int K = 0;
        while (K < 1) {
            output.println("Количество островов: ");
            try {
                K = Integer.parseInt(input.nextLine()); //кол-во островов
            } catch (Exception e) {
                K = 0;
            }
        }

        int[] N = new int[K]; //размер острова
        int[] M = new int[K]; //размер острова
        int[] sum = new int[K]; //объем воды на островах
        ArrayList<int[][]> islands = new ArrayList<>(); //острова

        //считываем данные по каждому острову
        for (int kk = 0; kk < K; kk++) {
            output.println("=================================");
            //ввод размеров острова
            while (N[kk] < 1 || N[kk] > 50 || M[kk] < 1 || M[kk] > 50) {
                output.println("Размеры острова " + (kk + 1) + " : ");
                String[] dims = input.nextLine().split(" ");
                if (dims.length < 2) continue;
                try {
                    N[kk] = Integer.parseInt(dims[0]);
                    M[kk] = Integer.parseInt(dims[1]);
                } catch (Exception e) {
                    N[kk] = 0;
                    M[kk] = 0;
                }
            }

            int[][] island = new int[N[kk]][M[kk]];
            islands.add(island);

            //считываем матрицу построчно
            for (int i = 0; i < N[kk]; i++) {
                output.println("Строка " + (i + 1) + " матрицы: ");
                String[] line = input.nextLine().split(" ");
                //проверка ввода
                if (line.length < M[kk]) {
                    i--;
                    continue;
                }
                //парсим строку
                for (int j = 0; j < M[kk]; j++) {
                    try {
                        island[i][j] = Integer.parseInt(line[j]);
                    } catch (Exception e) {
                        island[i][j] = 0;
                    }

                    //проверка ввода
                    if (island[i][j] < 1 || island[i][j] > 1000) {
                        i--;
                        break;
                    }
                }
            }
        }

        //для каждого острова
        for (int kk = 0; kk < K; kk++) {
            int[][] island = islands.get(kk); //остров
            boolean[][] mask = new boolean[N[kk]][M[kk]]; //маска показывающая, какие из клеток мы уже посетили

            //для каждой внутренней клетки
            for (int i = 1; i < (N[kk] - 1); i++) {
                for (int j = 1; j < (M[kk] - 1); j++) {
                    int minVal = Integer.MAX_VALUE; //минимальная соседняя клетка
                    ArrayList<int[]> minCells = new ArrayList<>(); //список всех соседних клеток, которые меньше или равны данной
                    //рекурсивный поиск минимальной соседней клетки
                    minVal = RecurCalcCell(N[kk], M[kk], island, mask, i, j, minCells, minVal);

                    //для всех клеток из списка

                    for (int[] cell : minCells) {
                        int newI = cell[0];
                        int newJ = cell[1];

                        //заполняем до уровня минимального соседа
                        if (island[newI][newJ] < minVal) {
                            sum[kk] += minVal - island[newI][newJ];
                            island[newI][newJ] = minVal;
                        }

                        mask[newI][newJ] = false;
                    }
                }
            }
        }

        output.println("=================================");
        for (int kk = 0; kk < K; kk++) {
            output.println("Объем скопившейся воды на острове " + (kk + 1) + " : " + sum[kk]);
        }
        input.nextLine();

    }


    private static int RecurCalcCell(int N, int M, int[][] island, boolean[][] mask, int i, int j, ArrayList<int[]> minCells, int minVal) {
        //помечаем, что побывали на клетке
        minCells.add(new int[]{i, j});
        mask[i][j] = true;

        //для каждой соседней клетки
        for (int k = 0; k < 4; k++) {
            int newI = i + dpos[k][0];
            int newJ = j + dpos[k][1];

            //если еще не побывали на клетке
            if (!mask[newI][newJ]) {
                //если клетка крайняя или больше текущей
                if (newI == 0 || newI == (N - 1) || newJ == 0 || newJ == (M - 1)
                        || island[newI][newJ] > island[i][j]) {
                    //если клетка меньше минимального значения
                    if (island[newI][newJ] < minVal)
                        minVal = island[newI][newJ];
                }
                //если клетка меньше или равна текущей
                else /*if (island[newI, newJ] <= island[i, j])*/ {
                    //входим в рекурсию
                    minVal = RecurCalcCell(N, M, island, mask, newI, newJ, minCells, minVal);
                }

            }
        }

        return minVal;
    }
}
