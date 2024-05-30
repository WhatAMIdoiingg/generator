import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;

import javax.swing.*;
import java.awt.*;

public class Graph2 {

    public static void main(String[] args) {
        // Создаем данные для гистограммы
        IntervalXYDataset histogramDataset = createHistogramDataset();

        // Создаем график
        JFreeChart chart = ChartFactory.createXYBarChart(
                "Объединенный график",
                "X",
                false,
                "Y",
                histogramDataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        // Получаем объект plot
        XYPlot plot = chart.getXYPlot();

// Устанавливаем цвет для гистограммы
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 0, 128)); // Полупрозрачный черный цвет

// Устанавливаем прозрачный рендерер для гистограммы
        renderer.setBarPainter(new StandardXYBarPainter());
        renderer.setDrawBarOutline(false); // Отключаем контуры столбцов


        // Создаем данные для графика функции плотности нормального распределения
        XYSeries densitySeries = new XYSeries("Функция плотности нормального распределения");
        for (double x = -5; x <= 5; x += 0.1) {
            double y = exponentialPDF(x, 0.5); // Среднее значение 0, стандартное отклонение 1
            densitySeries.add(x, y);
        }

        // Создаем набор данных для графика функции плотности нормального распределения
        XYSeriesCollection densityDataset = new XYSeriesCollection();
        densityDataset.addSeries(densitySeries);

        // Добавляем набор данных для графика функции плотности нормального распределения вместо гистограммы

        plot.setDataset(1, densityDataset);

        // Устанавливаем рендерер для графика функции плотности нормального распределения
        plot.setRenderer(1, new org.jfree.chart.renderer.xy.XYLineAndShapeRenderer(true, false));

        // Создаем панель для отображения графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));

        // Создаем JFrame для отображения панели
        JFrame frame = new JFrame("Объединенный график");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    private static IntervalXYDataset createHistogramDataset() {
        // Создаем данные для гистограммы
        XYSeries series = new XYSeries("Гистограмма");
        Transformation transformation = new Transformation(); //
        // Определяем диапазон значений, который должен охватывать каждый столбец
        double rangeMin = -5.0; // Минимальное значение диапазона
        double rangeMax = 5.0; // Максимальное значение диапазона
        double rangeSize = rangeMax - rangeMin;
        double binWidth = rangeSize / 15; // 20 столбцов

        // Инициализируем массив для хранения количества чисел в каждом столбце
        double[] binCounts = new double[15];

        // Генерируем данные и распределяем их по столбцам
        for (int i = 0; i < 1000; i++) {
            double[] Numbers = transformation.generateGaussianNumbers();

            for (double number : Numbers) {
                // Определяем, в какой столбец попадает число
                int binIndex = (int) ((number - rangeMin) / binWidth);
                if (binIndex < 0) {
                    binIndex = 0; // Число меньше минимального диапазона
                } else if (binIndex >= 15) {
                    binIndex = 14; // Число больше максимального диапазона
                }

                // Увеличиваем количество чисел в соответствующем столбце
                binCounts[binIndex]++;
            }
        }

        // Нормализуем binCounts, деля каждое значение на общее количество чисел
        for (int i = 0; i < binCounts.length; i++) {
            binCounts[i] /= 1000; // Делим на общее количество чисел
        }

        // Добавляем нормализованные данные в ряд
        for (int i = 0; i < 15; i++) {
            double binCenter = rangeMin + (i * binWidth) + (binWidth / 2.0);
            series.add(binCenter, binCounts[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }



    public static double exponentialPDF(double x, double lambda) {
        if (x < 0) {
            return 0;
        } else {
            return lambda * Math.exp(-lambda * x);
        }
    }
}