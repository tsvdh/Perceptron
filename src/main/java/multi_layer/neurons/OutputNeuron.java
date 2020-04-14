package multi_layer.neurons;

import static multi_layer.MultiLayerPerceptron.alpha;

public class OutputNeuron extends Neuron {

    public OutputNeuron(int amountOfHidden) {
        super(amountOfHidden);
    }

    /**
     * The error gradient is calculated, using the derivative of the sigmoid.
     * If sigmoid is f(x), then the derivative is f(x) * (1 - f(x)).
     * In this case, output is f(x).
     * Then the weights and theta are updated.
     */
    public void updateWeights(double[] hiddenOutputs, double desiredOutput) {
        double error = desiredOutput - output;

        errorGradient = output * (1 - output) * error;

        for (int i = 0; i < weights.length; i++) {
            weights[i] += alpha() * hiddenOutputs[i] * errorGradient;
        }

        theta -= alpha() * errorGradient;
    }
}
