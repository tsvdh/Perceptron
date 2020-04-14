package single_layer;

import org.javatuples.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.util.LinkedList;
import java.util.List;

import static utils.Util.getLineCoordinates;
import static utils.Util.getRectangleCoordinates;

class SingleLayerPerceptronDemo {

    public static void main(String[] args) throws InterruptedException {
        double alpha = 0.00001;
        int sleepTime = 1;

        SingleLayerPerceptron perceptron = new SingleLayerPerceptron(2, alpha);

        int amountOfPoints = 30;

        Pair<double[], double[]> classA = getRectangleCoordinates(1, 4, 3, 4, amountOfPoints);

        Pair<double[], double[]> classB = getRectangleCoordinates(1, 3, 1, 2.5, amountOfPoints);

        List<Pair<double[], Integer>> points = new LinkedList<>();
        for (int i = 0; i < classA.getValue0().length; i++) {
            points.add(Pair.with(new double[]{classA.getValue0()[i], classA.getValue1()[i]}, 0));
        }
        for (int i = 0; i < classB.getValue0().length; i++) {
            points.add(Pair.with(new double[]{classB.getValue0()[i], classB.getValue1()[i]}, 1));
        }

        perceptron.setPoints(points);

        XYChart chart = new XYChartBuilder().title("Single layer perceptron learning")
                .width(700).height(700).xAxisTitle("X").yAxisTitle("Y")
                .theme(Styler.ChartTheme.Matlab)
                .build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setXAxisMin(0.0);
        chart.getStyler().setXAxisMax(4.0);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setYAxisMax(6.0);
        //chart.getStyler().setPlotGridLinesVisible(false);
        //chart.getStyler().setAxisTicksVisible(false);

        XYSeries seriesA = chart.addSeries("A", classA.getValue0(), classA.getValue1());
        seriesA.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        XYSeries seriesB = chart.addSeries("B", classB.getValue0(), classB.getValue1());
        seriesB.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);

        Pair<double[], double[]> lineCoordinates = getLineCoordinates(perceptron.getWeights(), perceptron.getTheta());

        XYSeries line = chart.addSeries("boundary", lineCoordinates.getValue0(), lineCoordinates.getValue1());
        line.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        SwingWrapper<XYChart> swingWrapper = new SwingWrapper<>(chart);
        swingWrapper.displayChart();

        Thread.sleep(2000);

        boolean errorless = false;

        while (!errorless) {
            errorless = perceptron.train();

            Pair<double[], double[]> newlineCoordinates = getLineCoordinates(perceptron.getWeights(), perceptron.getTheta());

            chart.updateXYSeries("boundary", newlineCoordinates.getValue0(), newlineCoordinates.getValue1(), null);
            swingWrapper.repaintChart();

            Thread.sleep(sleepTime);
        }
    }
}
