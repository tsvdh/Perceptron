package multi_layer.neurons;

import java.util.List;

import static multi_layer.MultiLayerPerceptron.alpha;

public class HiddenNeuron extends Neuron {

    public HiddenNeuron(int amountOfInputs) {
        super(amountOfInputs);
    }

    /**
     * Mostly the same as the output neuron, as both layers use the sigmoid activation function.
     */
    public void updateWeights(int index, List<OutputNeuron> outputLayer, double[] inputs) {
        double total = 0;
        for (OutputNeuron outputNeuron : outputLayer) {
            total += outputNeuron.errorGradient * outputNeuron.weights[index];
        }

        errorGradient = output * (1 - output)  * total;

        for (int i = 0; i < inputs.length; i++) {
            weights[i] += alpha() * inputs[i] * errorGradient;
        }

        theta -= alpha() * errorGradient;
    }
}
