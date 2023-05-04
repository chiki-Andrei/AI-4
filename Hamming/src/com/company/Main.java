package com.company;

public class Main extends Thread {
    private int[][] weights = {{1, -1, 1, -1}, {-1, 1, -1, 1}, {1, 1, -1, -1}};
    private int[] thresholds = {1, 1, 0};
    private int[] input = {1, 1, -1, -1};
    private int numNeurons = weights.length;

    public Main() {
        int recognizedIndex = recognize(input);
        System.out.println("The input pattern is recognized as pattern " + recognizedIndex);
    }

    public int recognize(int[] input) {
        int[] activations = new int[numNeurons];

        for (int i = 0; i < numNeurons; i++) {
            int sum = 0;
            for (int j = 0; j < input.length; j++) {
                sum += weights[i][j] * input[j];
            }
            activations[i] = sum >= thresholds[i] ? 1 : 0; // активация или ингибирование
        }

        int index = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < numNeurons; i++) {
            int distance = hammingDistance(activations, weights[i]); // расстояние Хэмминга
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }
        return index;
    }

    private int hammingDistance(int[] v1, int[] v2) {
        int distance = 0;
        for (int i = 0; i < v1.length; i++) {
            if (v1[i] != v2[i]) {
                distance++;
            }
        }
        return distance;
    }

    public static void main(String[] args) {
        new Main().start();
    }
}

