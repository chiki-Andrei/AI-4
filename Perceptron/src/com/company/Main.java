package com.company;

import java.util.Arrays;

public class Main extends Thread {

    private static final double THRESHOLD = 0.5;
    private static final double CORRECTION = 0.1;

    private double[] enters; //входы
    private double out; // выходной нейрон
    private final double[] weights; // точки соединения нейронов (весы)

    int iterationCount;

    //обучающий шаблон
    double[][] patterns = {
            {0, 0, 0},
            {0, 1, 0},
            {1, 0, 0},
            {1, 1, 1},
    };


    public Main() {
        enters = new double[2];
        weights = new double[enters.length];
        iterationCount = 0;

        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() * 0.2 + 0.1;
        }

        learn();
        test();
    }

    public void countOut() {
        out = 0;
        for (int i = 0; i < enters.length; i++) {
            out += enters[i] * weights[i];
        }
        if (out >= THRESHOLD) {
            out = 1;
        } else {
            out = 0;
        }
    }

    public void learn() {
        double globalError;
        do {
            globalError = 0;
            iterationCount++;
            for (double[] pattern : patterns) {
                enters = Arrays.copyOf(pattern, pattern.length - 1);
                countOut();
                double error = pattern[2] - out;
                globalError += Math.abs(error);
                for (int j = 0; j < weights.length; j++) {
                    weights[j] += 0.1 * CORRECTION * enters[j];
                }
            }
        } while (globalError != 0);
    }

    public void test() {
        for (double[] pattern : patterns) {
            enters = Arrays.copyOf(pattern, pattern.length - 1);
            countOut();
            System.out.println(Arrays.toString(enters) + " " + out);
        }
        System.out.println("Number of iterations: " + iterationCount);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
