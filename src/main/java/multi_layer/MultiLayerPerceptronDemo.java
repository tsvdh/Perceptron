package multi_layer;

import org.javatuples.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.util.ArrayList;

import static utils.Plotter.getLineCoordinates;
import static utils.Plotter.getRandomCoordinates;

public class MultiLayerPerceptronDemo {

    public static void main(String[] args) {
        double alpha = 0.01;
        int sleepTime = 1;

        int amountOfPoints = 30;

        double[] classAX = getRandomCoordinates(1, 4, amountOfPoints);
        double[] classAY = getRandomCoordinates(3, 4, amountOfPoints);

        double[] classBX = getRandomCoordinates(1, 4, amountOfPoints);
        double[] classBY = getRandomCoordinates(1, 2, amountOfPoints);

        XYChart chart = new XYChartBuilder().build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax(5.0);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(5.0);

        XYSeries seriesA = chart.addSeries("A", classAX, classAY);
        seriesA.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        XYSeries seriesB = chart.addSeries("B", classBX, classBY);
        seriesB.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        MultiLayerPerceptron perceptron = new MultiLayerPerceptron(2, 1, 2, alpha);

        ArrayList<Pair<double[], Double>> results = perceptron.getResults();

        for (int i = 0; i < perceptron.getHiddenDimension(); i++) {
            Pair<double[], Double> result = results.get(i);
            Pair<double[], double[]> coordinates = getLineCoordinates(result.getValue0(), result.getValue1());
            chart.addSeries("Boundary " + i + 1, coordinates.getValue0(), coordinates.getValue1());
        }

        SwingWrapper<XYChart> swingWrapper = new SwingWrapper<>(chart);
        swingWrapper.displayChart();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        boolean errorless = false;
        while (!errorless) {
            errorless = true;
            for (int i = 0; i < amountOfPoints; i++) {
                if (perceptron.process(new double[]{classAX[i], classAY[i]}, 1) != 1) {
                    errorless = false;
                }
                if (perceptron.process(new double[]{classBX[i], classBY[i]}, 2) != 2) {
                    errorless = false;
                }
            }

            for (int i = 0; i < perceptron.getHiddenDimension(); i++) {
                Pair<double[], Double> result = results.get(i);
                Pair<double[], double[]> coordinates = getLineCoordinates(result.getValue0(), result.getValue1());
                chart.updateXYSeries("Boundary " + i + 1, coordinates.getValue0(), coordinates.getValue1(), null);
            }

            swingWrapper.repaintChart();

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
