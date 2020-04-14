package utils;

import org.javatuples.Pair;

import java.util.Random;

public class Plotter {

    public static double[] getRandomCoordinates(double min, double max, int amountOfPoints) {
        Random random = new Random();

        double size = max - min;

        double[] coordinates = new double[amountOfPoints];
        for (int i = 0; i < amountOfPoints; i++) {
            coordinates[i] = random.nextDouble() * size + min;
        }

        return coordinates;
    }

    public static Pair<double[], double[]> getLineCoordinates(double[] weights, double theta) {
        double[] xCoordinates = new double[]{-100, 100};
        double[] yCoordinates = new double[2];

        for (int i = 0; i < 2; i++) {
            yCoordinates[i] = (theta - weights[0] * xCoordinates[i]) / weights[1];
        }

        return Pair.with(xCoordinates, yCoordinates);
    }
}
