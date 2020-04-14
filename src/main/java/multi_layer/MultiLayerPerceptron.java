package multi_layer;

import multi_layer.neurons.HiddenNeuron;
import multi_layer.neurons.OutputNeuron;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiLayerPerceptron {

    private static double alpha;

    public static double alpha() {
        return alpha;
    }

    private int inputDimension;
    private int hiddenDimension;

    private List<HiddenNeuron> hiddenLayer;
    private OutputNeuron outputNeuron;

    public MultiLayerPerceptron(int inputDimension, int hiddenDimension, double alpha) {
        MultiLayerPerceptron.alpha = alpha;

        this.inputDimension = inputDimension;
        //hiddenDimension = (inputDimension - outputDimension) / 2 + outputDimension;
        this.hiddenDimension = hiddenDimension;


        hiddenLayer = new ArrayList<>(hiddenDimension);
        for (int i = 0; i < hiddenDimension; i++) {
            hiddenLayer.add(new HiddenNeuron(inputDimension));
        }

        outputNeuron = new OutputNeuron(hiddenDimension);
    }

    public int getInputDimension() {
        return inputDimension;
    }

    public int getHiddenDimension() {
        return hiddenDimension;
    }

    public ArrayList<Pair<double[], Double>> getResults() {
        ArrayList<Pair<double[], Double>> results = new ArrayList<>();

        for (int i = 0; i < hiddenDimension; i++) {
            results.add(Pair.with(hiddenLayer.get(i).getWeights(), hiddenLayer.get(i).getTheta()));
        }

        return results;
    }

    double process(double[] inputs, Integer target) {
        if (inputs.length != inputDimension) {
            throw new IllegalArgumentException("Inputs has wrong dimension");
        }

        // forward propagation
        for (HiddenNeuron neuron : hiddenLayer) {
            neuron.calculateOutput(inputs);
        }

        double[] hiddenOutputs = getHiddenOutputs();

        outputNeuron.calculateOutput(hiddenOutputs);

        // backward propagation, only if a target is given
        // if no target is given, only an output is predicted
        if (target != null) {
            if (target != 0 && target != 1) {
                throw new IllegalArgumentException("Target class out of bounds");
            }

            outputNeuron.updateWeights(hiddenOutputs, target);

            for (int i = 0; i < hiddenDimension; i++) {
                hiddenLayer.get(i).updateWeights(i, outputNeuron, inputs);
            }
        }

        return outputNeuron.getOutput();
    }

    private double[] getHiddenOutputs() {
        double[] outputs = new double[hiddenDimension];

        for (int i = 0; i < hiddenDimension; i++) {
            outputs[i] = hiddenLayer.get(i).getOutput();
        }

        return outputs;
    }
}
