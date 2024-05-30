import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class CombinedHistogramAndFunctionPlot extends ApplicationFrame {

    public CombinedHistogramAndFunctionPlot(String title) {
        super(title);

        // Создание набора данных для гистограммы
        HistogramDataset histogramDataset = new HistogramDataset();
        double[] values = new double[10000];
        // Генерация случайных чисел и добавление их в набор данных
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.random(); // Замените на вашу генерацию случайных чисел
        }
        histogramDataset.addSeries("Random Values", values, 20);

        // Создание графика гистограммы
        JFreeChart histogramChart = ChartFactory.createHistogram(
                "Histogram of Random Numbers", // Заголовок графика
                "Value", // Подпись оси X
                "Frequency", // Подпись оси Y
                histogramDataset, // Набор данных для графика
                PlotOrientation.VERTICAL, // Ориентация графика
                false, // Флаг включения легенды
                true, // Флаг включения инструментов
                false // Флаг включения URL
        );

        // Создание набора данных для графика функции
        XYSeries series = new XYSeries("Function");
        for (double x = -10; x <= 10; x += 0.1) {
            double y = Math.sin(x); // Замените на вашу функцию
            series.add(x, y);
        }
        XYSeriesCollection functionDataset = new XYSeriesCollection(series);

        // Создание графика функции
        JFreeChart functionChart = ChartFactory.createXYLineChart(
                "Function Plot", // Заголовок графика
                "X", // Подпись оси X
                "Y", // Подпись оси Y
                functionDataset, // Набор данных для графика
                PlotOrientation.VERTICAL, // Ориентация графика
                true, // Флаг включения легенды
                true, // Флаг включения инструментов
                false // Флаг включения URL
        );

        // Получение объектов XYPlot из графиков
        XYPlot histogramPlot = histogramChart.getXYPlot();
        XYPlot functionPlot = functionChart.getXYPlot();

        // Установка флага, чтобы графики не перекрывали друг друга
        histogramPlot.setDomainGridlinesVisible(false);
        functionPlot.setRangeGridlinesVisible(false);

        // Объединение графиков
        // Установка гистограммы как второго набора данных для первого графика
        histogramPlot.setDataset(1, histogramDataset);
        // Установка собственного рендерера для гистограммы
        XYItemRenderer renderer = histogramPlot.getRenderer();
        histogramPlot.setRenderer(1, renderer);

        // Создание панели для отображения графика
        ChartPanel chartPanel = new ChartPanel(histogramChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);
    }

    public static void main(String[] args) {
        CombinedHistogramAndFunctionPlot demo = new CombinedHistogramAndFunctionPlot("Combined Histogram and Function Plot Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}