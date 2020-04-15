package multi_layer;

import org.javatuples.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static utils.Util.getCircleCoordinates;
import static utils.Util.getLineCoordinates;
import static utils.Util.getRectangleCoordinates;

public class MultiLayerPerceptronDemo {

    public static void main(String[] args) {
        double alpha = 0.1;

        int amountOfPoints = 200;
        int amountOfGroups = 2;

        Pair<double[], double[]> classA = getCircleCoordinates(new double[]{3, 3}, 2, 3, amountOfPoints);
//        Pair<double[], double[]> classA = getRectangleCoordinates(0, 6, 3.5, 6, amountOfPoints);
        double[] classAX = classA.getValue0();
        double[] classAY = classA.getValue1();

        Pair<double[], double[]> classB = getCircleCoordinates(new double[]{3, 3}, 0, 1.5, amountOfPoints);
//        Pair<double[], double[]> classB = getRectangleCoordinates(0, 6, 0, 2.5, amountOfPoints);
        double[] classBX = classB.getValue0();
        double[] classBY = classB.getValue1();

        XYChart chart = new XYChartBuilder().title("Multi layer perceptron").xAxisTitle("x").yAxisTitle("y")
                                                                    .theme(Styler.ChartTheme.Matlab).build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setXAxisMin(-1.0);
        chart.getStyler().setXAxisMax(7.0);
        chart.getStyler().setYAxisMin(-1.0);
        chart.getStyler().setYAxisMax(7.0);

        XYSeries seriesA = chart.addSeries("A", classAX, classAY);
        seriesA.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        XYSeries seriesB = chart.addSeries("B", classBX, classBY);
        seriesB.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        MultiLayerPerceptron perceptron = new MultiLayerPerceptron(2, 4, alpha);

        ArrayList<Pair<double[], Double>> results = perceptron.getResults();

        for (int i = 0; i < perceptron.getHiddenDimension(); i++) {
            Pair<double[], Double> result = results.get(i);
            Pair<double[], double[]> coordinates = getLineCoordinates(result.getValue0(), result.getValue1());
            chart.addSeries("Boundary " + (i + 1), coordinates.getValue0(), coordinates.getValue1());
        }

        SwingWrapper<XYChart> swingWrapper = new SwingWrapper<>(chart);

        swingWrapper.displayChart();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        boolean normal = false;
        boolean fast = false;

        int totalPoints = amountOfPoints * amountOfGroups;
        int sleepTime = 50;

        double sumOfSquaredErrors = Double.MAX_VALUE;
        while (sumOfSquaredErrors > 0.0001 * totalPoints) {

            if (!fast && sumOfSquaredErrors < 0.008 * totalPoints) {
                sleepTime = 1;
                fast = true;
//                System.out.println("fast");
            }

            else if (!normal && sumOfSquaredErrors < 0.5 * totalPoints) {
                sleepTime = 10;
                normal = true;
//                System.out.println("normal");
            }

            sumOfSquaredErrors = 0;
            for (int i = 0; i < classAX.length; i++) {
                double actualOutput = perceptron.process(new double[]{classAX[i], classAY[i]}, 0);
                sumOfSquaredErrors += pow(actualOutput, 2);
            }
            for (int i = 0; i < classBX.length; i++) {
                double actualOutput = perceptron.process(new double[]{classBX[i], classBY[i]}, 1);
                sumOfSquaredErrors += pow(1 - actualOutput, 2);
            }

            results = perceptron.getResults();

            for (int i = 0; i < perceptron.getHiddenDimension(); i++) {
                Pair<double[], Double> result = results.get(i);
                Pair<double[], double[]> coordinates = getLineCoordinates(result.getValue0(), result.getValue1());
                chart.updateXYSeries("Boundary " + (i + 1), coordinates.getValue0(), coordinates.getValue1(), null);
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
