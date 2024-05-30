import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class HistogramExample2 extends ApplicationFrame {

    public HistogramExample2(String title) {
        super(title);

        // Создание набора данных для столбчатой диаграммы
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Генерация случайных чисел и добавление их в набор данных
        LCG lcg = new LCG();
        for (int i = 0; i < 1000; i++) {
            double value = lcg.next_Smirnov();
            System.out.println(value);
            dataset.addValue(value, "Номер числа:", String.valueOf(i));
        }

        // Создание графика
        JFreeChart chart = ChartFactory.createBarChart(
                "Histogram of Random Numbers", // Заголовок графика
                "Номер", // Подпись оси X
                "Значение", // Подпись оси Y
                dataset, // Набор данных для графика
                PlotOrientation.VERTICAL, // Ориентация графика
                false, // Флаг включения легенды
                true, // Флаг включения инструментов
                false // Флаг включения URL
        );

        // Установка категорий по оси X
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Создание панели для отображения графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        setContentPane(chartPanel);
    }

    public static void main(String[] args) {
        HistogramExample2 demo = new HistogramExample2("Histogram Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}