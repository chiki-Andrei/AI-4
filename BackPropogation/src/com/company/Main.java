package com.company;

import java.util.Arrays;

public class Main extends Thread {
    private double[] enters;
    private double[] kohonen;
    private double[][] weights;
    private double[] kohonenCount;

    private double[][] patterns = {
            {1, 0},
            {0, 1}
    };

    public Main() {
        enters = new double[2];
        kohonen = new double[2];
        weights = new double[enters.length][kohonen.length];
        kohonenCount = new double[kohonen.length];

        for (int i = 0; i < enters.length; i++) {
            for (int j = 0; j < kohonen.length; j++) {
                weights[i][j] = Math.random() - 0.5;
            }
        }

        for (int i = 0; i < 1000; i++) {
            int maxId = studyKohonen();
            kohonenCount[maxId]++;
        }

        for (double count : kohonenCount) {
            System.out.println(count);
        }

        study();
        for (double[] pattern : patterns) {
            enters = java.util.Arrays.copyOf(pattern, pattern.length);
            countOut();
            System.out.println(kohonen[0] + " " + kohonen[1]);
        }
    }

    public void study() {
        int i = 0;
        int maxId = studyKohonen();
        studying: while (true) {
            kohonenCount[maxId]++;
            i++;

            if (i > 20 * kohonen.length) {
                double norma;
                double mean;
                double sum = 0;

                for (double koh : kohonenCount) {
                    sum += koh;
                }

                for (int j = 0; j < kohonenCount.length; j++) {
                    kohonenCount[j] /= sum;
                }
                norma = stdev(kohonenCount);
                mean = mean(kohonenCount);
                boolean done = true;

                for (double koh : kohonenCount) {
                    if (!(koh < (mean + norma) ||
                            koh > (mean - norma))) {
                        done = false;
                        break;
                    }
                }
                if (done) break studying;
            }
        }
    }

    public int studyKohonen() {
        double a = 0.5;
        int maxId = 0;
        for (double[] pattern : patterns) {
            enters = Arrays.copyOf(pattern, pattern.length);
            maxId = countOut();
            for (int i = 0; i < enters.length; i++) {
                for (int j = 0; j < kohonen.length; j++) {
                    weights[i][maxId] += a * (enters[i] - weights[i][maxId]);
                }
            }
        }
        return maxId;
    }

    public int countOut() {
        for (int i = 0; i < kohonen.length; i++) {
            kohonen[i] = 0;
            for (int j = 0; j < enters.length; j++) {
                kohonen[i] += enters[j] * weights[j][i];
            }
        }
        int change = -1;
        if (kohonenCount[0] - kohonenCount[1] > 20) {
            change = 0;
        } else if (kohonenCount[0] - kohonenCount[1] < -15) {
            change = 1;
        }

        double maxKoh = kohonen[0];
        int maxKohonenId = 0;
        for (int i = 1; i < kohonen.length; i++) {
            if (kohonen[i] > maxKoh) {
                maxKoh = kohonen[i];
                maxKohonenId = i;
            }
        }

        Arrays.fill(kohonen, 0);
        kohonen[maxKohonenId] = 1;

        if (maxKohonenId == change && (change == 0))
            return 1;
        else if (maxKohonenId == change)
            return 0;
        else return maxKohonenId;
    }


    public static double mean(double[] list) {
        double sum = 0;
        for (double i : list) {
            sum += i;
        }
        return sum / list.length;
    }

    public static double stdev(double[] list) {
        double sum = 0.0;
        double num = 0.0;
        double mean;
        double num2;

        for (double i : list) {
            sum += i;
        }

        mean = sum / list.length;
        for (double i : list) {
            num2 = Math.pow(i - mean, 2);
            num += num2;
        }
        return Math.sqrt(num / list.length);
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
