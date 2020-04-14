package single_layer;

import org.javatuples.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class SingleLayerPerceptron {

    private Random randomGenerator;

    private int inputDimension;

    private double[] weights;

    private double theta;
    private double alpha;

    private List<Pair<double[], Integer>> points;

    SingleLayerPerceptron(int inputDimension, double alpha) {
        randomGenerator = new Random();

        this.inputDimension = inputDimension;

        theta = getRandomInitialValue();
        this.alpha = alpha;

        weights = new double[inputDimension];
        for (int i = 0; i < inputDimension; i++) {
            weights[i] = getRandomInitialValue();
        }

        points = new LinkedList<>();
    }

    private double getRandomInitialValue() {
        return randomGenerator.nextDouble() - 0.5;
    }

    void setPoints(List<Pair<double[], Integer>> points) {
        for (Pair<double[], Integer> point : points) {
            if (point.getValue0().length != inputDimension) {
                throw new IllegalArgumentException("One of the points does not have the correct dimension");
            }
        }

        this.points = points;
    }

    public double getTheta() {
        return theta;
    }

    public double[] getWeights() {
        return weights;
    }

    /**
     * For every training point, it does the following:
     *  - the class the point belongs to according to the current weights is calculated
     *  - the difference between the calculated class and the actual class of the point is taken
     *  - the weights are updated according to the error
     */
    boolean train() {
        boolean errorless = true;

        for (Pair<double[], Integer> point : points) {
            double[] inputs = point.getValue0();

            int actualOutput = calculateOutput(inputs);
            int desiredOutput = point.getValue1();

            int error = desiredOutput - actualOutput;
            
            if (error != 0) {
                errorless = false;
            }

            updateWeights(error, inputs);
        }

        return errorless;
    }

    /**
     * A point is classified.
     * First, every part of the input is multiplied with its respective weight and is summed.
     * Then, theta is subtracted. If the total is bigger than zero, it is class 1. Else, it is class 0.
     */
    private int calculateOutput(double[] inputs) {
        double total = 0;

        for (int i = 0; i < inputDimension; i++) {
            total += inputs[i] * weights[i];
        }

        total -= theta;

        if (total >= 0) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /**
     * The weight of each dimension is updated.
     * If the error is positive, the output should be bigger. So the weight change is positive.
     * If the error is negative, it is the other way around.
     * The amount the weight changes is dependent on the size of the input.
     * If the input is large and wrongly classified, the boundary has to shift far in order to get to the input.
     * If the input is small, it is the other way around.
     */
    private void updateWeights(int error, double[] inputs) {
        for (int i = 0; i < inputDimension; i++) {
            double weightChange = alpha * inputs[i] * error;

            weights[i] += weightChange;
        }

        theta -= alpha * error;
    }
}
