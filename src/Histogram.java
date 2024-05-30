import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;



public class HistogramExample extends ApplicationFrame {

    public HistogramExample(String title) {
        super(title); // Вызов конструктора

        // Создание набора данных для гистограммы
        HistogramDataset dataset = new HistogramDataset();

        // Генерация случайных чисел и добавление их в набор данных
        LCG lcg = new LCG();
        double[] values = new double[1000];
        for (int i = 0; i < values.length; i++) {
            //values[i] = lcg.nextDouble() ; // Генерация числа от 0 до 1
            values[i] = lcg.next_Smirnov();
            System.out.println(values[i]);
        }
        dataset.addSeries("Random Values", values, 1000); // Добавление серии данных в набор данных

        // Создание графика
        JFreeChart chart = ChartFactory.createHistogram(
                "Histogram of Random Numbers", // Заголовок графика
                "Величина", // Подпись оси X
                "Количество", // Подпись оси Y
                dataset, // Набор данных для графика
                PlotOrientation.VERTICAL, // Ориентация графика
                false, // Флаг включения легенды
                true, // Флаг включения инструментов
                false // Флаг включения URL
        );

        // Создание панели для отображения графика
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500)); // Установка предпочтительного размера панели
        setContentPane(chartPanel); // Установка панели содержимого окна
    }

    public static void main(String[] args) {
        HistogramExample demo = new HistogramExample("Histogram Demo"); // Создание экземпляра класса HistogramExample
        demo.pack(); // Упаковка компонентов в окно
        RefineryUtilities.centerFrameOnScreen(demo); // Центрирование окна на экране
        demo.setVisible(true); // Отображение окна

    }
}