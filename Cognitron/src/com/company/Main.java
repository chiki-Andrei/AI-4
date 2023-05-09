package com.company;

public class Main extends Thread {
    private final static double Q = 0.1;
    private int currentPattern;

    private double[] enters;
    private double[] outers;
    private double[] inhib;
    private double[] latInhib;

    private double[][] wEOut;
    private double[][] wEInput;
    private double[][] wIOut;
    private double[][] wLOut;

    private double[][] patterns = {
            {1, 1, 1, 1, 1, 1, 1},
            {1, 1, 0, 0, 1, 0, 1}
    };

    public Main() {
        enters = new double[patterns[0].length];
        outers = new double[patterns.length];
        inhib = new double[outers.length];
        latInhib = new double[outers.length];

        wEInput = new double[enters.length][inhib.length];
        wEOut = new double[enters.length][outers.length];
        wIOut = new double[inhib.length][outers.length];
        wLOut = new double[latInhib.length][outers.length];

        for (int i = 0; i < enters.length; i++) {
            for (int j = 0; j < inhib.length; j++) {
                wEInput[i][j] = 1. / enters.length;
            }
        }

        for (int i = 0; i < outers.length; i++) {
            for (int j = 0; j < enters.length; j++) {
                wEOut[j][i] = 1;
            }
        }

        for (int i = 0; i < inhib.length; i++) {
            for (int j = 0; j < outers.length; j++) {
                wIOut[i][j] = 1;
            }
        }

        for (int i = 0; i < outers.length; i++) {
            for (int j = 0; j < latInhib.length; j++) {
                wLOut[j][i] = 1. / outers.length;
            }
        }

        currentPattern = 0;
        test();
    }

    public void countOut() {
        double E = 0;
        double I = 0;
        for (int i = 0; i < inhib.length; i++) {
            for (int j = 0; j < enters.length; j++) {
                inhib[i] += enters[j] * wEInput[j][i];
            }
        }
        for (int i = 0; i < outers.length; i++) {
            for (int j = 0; j < enters.length; j++) {
                E += enters[j] * wEOut[j][i];
            }
            for (int j = 0; j < inhib.length; j++) {
                I += inhib[j] * wIOut[j][i];
            }
            double NET = (1 + E) / (1 + I);
            outers[i] = NET > 0 ? NET : 0;
        }
        for (int i = 0; i < latInhib.length; i++) {
            latInhib[i] = 0;
            for (int j = 0; j < outers.length; j++) {
                latInhib[i] += outers[j] * wLOut[i][j];
            }
        }
        for (int j = 0; j < outers.length; j++) {
            outers[j] /= latInhib[j];
        }
    }

    public void study() {
        countOut();
        currentPattern = currentPattern == 0 ? 1 : 0;
        int maxId = currentPattern;
        for (int i = 0; i < enters.length; i++) {
            wEOut[i][maxId] += Q / enters.length * enters[i];
        }
        double E = 0;
        for (int i = 0; i < enters.length; i++) {
            E += enters[i] * wEOut[i][maxId];
        }
        for (int i = 0; i < inhib.length; i++) {
            wIOut[i][maxId] += (E * Q) / (2. * inhib[i]);
        }
    }

    public void test() {
        for (int i = 0; i < 50000; i++) {
            for (double[] pattern : patterns) {
                enters = java.util.Arrays.copyOf(pattern, pattern.length);
                study();
            }
        }
        for (double[] pattern : patterns) {
            enters = java.util.Arrays.copyOf(pattern, pattern.length);
            countOut();
            System.out.println(outers[0] + " " + outers[1]);
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}

