package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        //Обучающие данные
        double[][] inputs = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        double[][] targets = new double[][]{{0}, {1}, {1}, {0}};

        MultiLayerPerceptron mlp = new MultiLayerPerceptron(new int[]{2, 2, 1});
        mlp.train(inputs, targets, 100000, 0.1);

        //Предсказания для тестовых данных
        double[][] testInputs = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (double[] testInput : testInputs) {
            double prediction = mlp.predict(testInput);
            System.out.println("Input " + Arrays.toString(testInput) + " -> Prediction: " + prediction);
        }
    }
}

class MultiLayerPerceptron {
    private double[][][] weights; // веса перцептрона
    private double[][] biases; // смещения перцептрона
    private int[] layers; // количество нейронов в каждом слое

    public MultiLayerPerceptron(int[] layers) {
        this.layers = layers;
        this.weights = new double[layers.length - 1][][];
        this.biases = new double[layers.length - 1][];
        for (int i = 0; i < layers.length - 1; i++) {
            this.weights[i] = new double[layers[i + 1]][layers[i]];
            this.biases[i] = new double[layers[i + 1]];
            for (int j = 0; j < layers[i + 1]; j++) {
                for (int k = 0; k < layers[i]; k++) {
                    this.weights[i][j][k] = Math.random() * 2 - 1;
                }
                this.biases[i][j] = Math.random() * 2 - 1;
            }
        }
    }

    //Обучение перцептрона с помощью обратного распространения ошибки
    public void train(double[][] inputs, double[][] targets, int epochs, double learningRate) {
        for (int epoch = 0; epoch < epochs; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                double[][] outputs = new double[layers.length][];
                outputs[0] = inputs[i];
                for (int j = 0; j < layers.length - 1; j++) {
                    outputs[j + 1] = new double[layers[j + 1]];
                    for (int k = 0; k < layers[j + 1]; k++) {
                        double z = biases[j][k];
                        for (int l = 0; l < layers[j]; l++) {
                            z += weights[j][k][l] * outputs[j][l];
                        }
                        outputs[j + 1][k] = sigmoid(z);
                    }
                }


                double[][] errors = new double[layers.length][];
                errors[layers.length - 1] = new double[layers[layers.length - 1]];
                for (int j = 0; j < layers[layers.length - 1]; j++) {
                    errors[layers.length - 1][j] = outputs[layers.length - 1][j] - targets[i][j];
                }
                for (int j = layers.length - 2; j >= 1; j--) {
                    errors[j] = new double[layers[j]];
                    for (int k = 0; k < layers[j]; k++) {
                        double sum = 0;
                        for (int l = 0; l < layers[j + 1]; l++) {
                            sum += errors[j + 1][l] * weights[j][l][k];
                        }
                        errors[j][k] = sum * sigmoidDerivative(outputs[j][k]);
                    }
                }

                // обновление весов и смещений
                for (int j = 0; j < layers.length - 1; j++) {
                    for (int k = 0; k < layers[j + 1]; k++) {
                        biases[j][k] -= learningRate * errors[j + 1][k];
                        for (int l = 0; l < layers[j]; l++) {
                            weights[j][k][l] -= learningRate * errors[j + 1][k] * outputs[j][l];
                        }
                    }
                }
            }
        }
    }

    // предсказание для одного входного значения
    public double predict(double[] input) {
        double[] output = input;
        for (int j = 0; j < layers.length - 1; j++) {
            double[] nextOutput = new double[layers[j + 1]];
            for (int k = 0; k < layers[j + 1]; k++) {
                double z = biases[j][k];
                for (int l = 0; l < layers[j]; l++) {
                    z += weights[j][k][l] * output[l];
                }
                nextOutput[k] = sigmoid(z);
            }
            output = nextOutput;
        }
        return output[0];
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return sigmoid(x) * (1 - sigmoid(x));
    }
}