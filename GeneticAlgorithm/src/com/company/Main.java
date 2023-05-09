package com.company;

import java.util.Random;

public class Main extends Thread {
    private static int GENERATIONS = 16;
    private static int POPULATION = 128;
    private static double MUTATION_RATE = 0.01;
    private static int TOURNAMENT_SIZE = 2;
    private static int ELITE_SIZE = 4;

    private static final Random random = new Random();
    private static double[][] population = new double[POPULATION][2];

    public Main() {
        for (int i = 0; i < POPULATION; i++) {
            population[i][0] = random.nextDouble() * 20 - 10;
            population[i][1] = random.nextDouble() * 20 - 10;
        }

        for (int generation = 1; generation <= GENERATIONS; generation++) {
            double[] fitness = new double[POPULATION];
            double bestFitness = Double.MIN_VALUE;
            int maxIndex = 0;
            for (int i = 0; i < POPULATION; i++) {
                double x = population[i][0];
                double y = population[i][1];
                fitness[i] = 1 / (1 + x * x + y * y);
                if (fitness[i] > bestFitness) {
                    bestFitness = fitness[i];
                    maxIndex = i;
                }
            }

            System.out.println("Generation: " + generation + " best fitness: " + bestFitness + " x = " + population[maxIndex][0] + " y = " + population[maxIndex][1]);

            double[][] elitePopulation = new double[ELITE_SIZE][2];
            for (int i = 0; i < ELITE_SIZE; i++) {
                elitePopulation[i] = population[getMaxFitnessIndex(fitness)];
            }

            double[][] newPopulation = new double[POPULATION][2];
            if (ELITE_SIZE >= 0) System.arraycopy(elitePopulation, 0, newPopulation, 0, ELITE_SIZE);
            for (int i = ELITE_SIZE; i < POPULATION; i++) {
                double[] parent1 = tournamentSelection(fitness);
                double[] parent2 = tournamentSelection(fitness);
                double[] child = crossover(parent1, parent2);
                mutate(child);
                newPopulation[i] = child;
            }
            population = newPopulation;
        }
    }

    private static int getMaxFitnessIndex(double[] fitness) {
        int maxIndex = 0;
        double maxFitness = Double.MIN_VALUE;
        for (int i = 0; i < POPULATION; i++) {
            if (fitness[i] > maxFitness) {
                maxFitness = fitness[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private static double[] tournamentSelection(double[] fitness) {
        double[] parent = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            int index = random.nextInt(POPULATION);
            if (parent == null || fitness[index] > 1 / (1 + parent[0] * parent[0] + parent[1] * parent[1])) {
                parent = population[index];
            }
        }
        return parent;
    }

    private static double[] crossover(double[] parent1, double[] parent2) {
        double[] child = new double[2];
        child[0] = (parent1[0] + parent2[0]) / 2;
        child[1] = (parent1[1] + parent2[1]) / 2;
        return child;
    }

    private static void mutate(double[] child) {
        if (random.nextDouble() < MUTATION_RATE) {
            child[0] += random.nextGaussian() * 0.1;
        }
        if (random.nextDouble() < MUTATION_RATE) {
            child[1] += random.nextGaussian() * 0.1;
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
