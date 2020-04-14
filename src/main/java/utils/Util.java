package utils;

import org.javatuples.Pair;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Util {

    public static Pair<double[], double[]> getRectangleCoordinates(double xMin, double xMax, double yMin, double yMax, int amountOfPoints) {
        Random random = new Random();

        double xSize = xMax - xMin;

        double[] xCoordinates = new double[amountOfPoints];
        for (int i = 0; i < amountOfPoints; i++) {
            xCoordinates[i] = random.nextDouble() * xSize + xMin;
        }

        double ySize = yMax - yMin;

        double[] yCoordinates = new double[amountOfPoints];
        for (int i = 0; i < amountOfPoints; i++) {
            yCoordinates[i] = random.nextDouble() * ySize + yMin;
        }

        return Pair.with(xCoordinates, yCoordinates);
    }

    public static Pair<double[], double[]> getCircleCoordinates(double[] center, double min, double max, int amountOfPoints) {
        double[] xCoordinates = new double[amountOfPoints];
        double[] yCoordinates = new double[amountOfPoints];

        int counter = 0;
        while (counter < amountOfPoints) {
            Pair<double[], double[]> coordinates = getRectangleCoordinates(center[0] - max, center[0] + max,
                                                                           center[1] - max, center[1] + max, 1);
            double x = coordinates.getValue0()[0];
            double y = coordinates.getValue1()[0];

            double distance = sqrt(pow(center[0] - x, 2) + pow(center[1] - y, 2));
            if (distance >= min && distance <= max) {
                xCoordinates[counter] = x;
                yCoordinates[counter] = y;

                counter++;
            }
        }

        return Pair.with(xCoordinates, yCoordinates);
    }

    public static Pair<double[], double[]> getLineCoordinates(double[] weights, double theta) {
        double[] xCoordinates = new double[]{-100, 100};
        double[] yCoordinates = new double[2];

        for (int i = 0; i < 2; i++) {
            yCoordinates[i] = (theta - weights[0] * xCoordinates[i]) / weights[1];
        }

        return Pair.with(xCoordinates, yCoordinates);
    }
    
    public static double[] concatenate(double[]... arrays) {
        if (arrays.length == 2) {
            return concatenate(arrays[0], arrays[1]);
        } else {
            return concatenate(arrays[0], concatenate(Arrays.copyOfRange(arrays, 1, arrays.length)));
        }
    }

    private static double[] concatenate(double[] first, double[] second) {
        double[] result = new double[first.length + second.length];

        for (int i = 0; i < result.length; i++) {
            if (i < first.length) {
                result[i] = first[i];
            } else {
                result[i] = second[i - first.length];
            }
        }

        return result;
    }
}
