package multi_layer;

import multi_layer.neurons.HiddenNeuron;
import multi_layer.neurons.OutputNeuron;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class MultiLayerPerceptron {

    private static double alpha;

    public static double alpha() {
        return alpha;
    }

    private int inputDimension;
    private int hiddenDimension;
    private int outputDimension;

    private List<HiddenNeuron> hiddenLayer;
    private List<OutputNeuron> outputLayer;

    public MultiLayerPerceptron(int inputDimension, int hiddenDimension, int outputDimension, double alpha) {
        MultiLayerPerceptron.alpha = alpha;

        this.inputDimension = inputDimension;
        //hiddenDimension = (inputDimension - outputDimension) / 2 + outputDimension;
        this.hiddenDimension = hiddenDimension;
        this.outputDimension = outputDimension;


        hiddenLayer = new ArrayList<>(hiddenDimension);
        outputLayer = new ArrayList<>(outputDimension);

        for (int i = 0; i < hiddenDimension; i++) {
            hiddenLayer.add(new HiddenNeuron(inputDimension));
        }
        for (int i = 0; i < outputDimension; i++) {
            outputLayer.add(new OutputNeuron(hiddenDimension));
        }
    }

    public int getInputDimension() {
        return inputDimension;
    }

    public int getHiddenDimension() {
        return hiddenDimension;
    }

    public int getOutputDimension() {
        return outputDimension;
    }

    public ArrayList<Pair<double[], Double>> getResults() {
        ArrayList<Pair<double[], Double>> results = new ArrayList<>();

        for (int i = 0; i < hiddenDimension; i++) {
            results.add(Pair.with(hiddenLayer.get(i).getWeights(), hiddenLayer.get(i).getTheta()));
        }

        return results;
    }

    int process(double[] inputs, Integer target) {
        if (inputs.length != inputDimension) {
            throw new IllegalArgumentException("Inputs has wrong dimension");
        }

        // forward propagation
        for (HiddenNeuron neuron : hiddenLayer) {
            neuron.calculateOutput(inputs);
        }

        double[] hiddenOutputs = getHiddenOutputs();

        for (OutputNeuron neuron : outputLayer) {
            neuron.calculateOutput(hiddenOutputs);
        }

        // backward propagation, only if a target is given
        // if no target is given, only an output is predicted
        if (target != null) {
            if (target > outputDimension) {
                throw new IllegalArgumentException("Target class out of bounds");
            }

            // determine desired output
            double[] desiredOutputs = new double[outputDimension];
            for (int i = 0; i < outputDimension; i++) {
                if (i == target - 1) {
                    desiredOutputs[i] = 1;
                } else {
                    desiredOutputs[i] = 0;
                }
            }

            for (int i = 0; i < outputDimension; i++) {
                outputLayer.get(i).updateWeights(hiddenOutputs, desiredOutputs[i]);
            }

            for (int i = 0; i < hiddenDimension; i++) {
                hiddenLayer.get(i).updateWeights(i, outputLayer, inputs);
            }
        }

        // determine actual output
        double maxOutput = 0;
        int maxI = -1;
        for (int i = 0; i < outputDimension; i++) {
            double output = outputLayer.get(i).getOutput();

            if (output > maxOutput) {
                maxOutput = output;
                maxI = i;
            }
        }

        return maxI + 1;
    }

    private double[] getHiddenOutputs() {
        double[] outputs = new double[hiddenDimension];

        for (int i = 0; i < hiddenDimension; i++) {
            outputs[i] = hiddenLayer.get(i).getOutput();
        }

        return outputs;
    }
}
