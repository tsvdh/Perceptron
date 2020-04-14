package multi_layer.neurons;

import java.util.Random;

import static java.lang.Math.exp;

abstract class Neuron {

    double[] weights;
    double theta;

    double output;

    double errorGradient;

    Neuron(int inputDimension) {
        weights = new double[inputDimension];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = getInitialValue();
        }

        theta = getInitialValue();
    }

    public double getOutput() {
        return output;
    }

    public double[] getWeights() {
        return weights;
    }

    public double getTheta() {
        return theta;
    }

    private double getInitialValue() {
        Random random = new Random();
        double value = random.nextDouble() * (2.4 / weights.length);

        if (random.nextDouble() > 0.5) {
            value *= -1;
        }

        return value;
    }


    /**
     * The dot product of the inputs and weight vector is taken, minus theta.
     * Sigmoid is then applied on the output, so the output is always in a certain range.
     * In this case, between 0 and 1.
     */
    public void calculateOutput(double[] inputs) {
        double total = 0;
        for (int i = 0; i < weights.length; i++) {
            total += inputs[i] * weights[i];
        }

        total -= theta;

        output = sigmoid(total);
    }

    private double sigmoid(double x) {
        return 1 / (1 + exp(-x));
    }
}
