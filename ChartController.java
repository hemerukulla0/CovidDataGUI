import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;


/**
 * The ChartController class manages data and operations related to a LineChart visualization.
 * It facilitates setting data for the chart, handling data updates, and configuring chart properties.
 * This class collaborates with the DateConverter utility for handling date conversions.
 *
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class ChartController
{
    DateConverter converter = new DateConverter();

    /**
     * Sets the data to be displayed on the LineChart.
     * 
     * @param chart The LineChart object to which the data will be added
     * @param dataToDisplay The ArrayList of CovidData objects representing the data to be displayed
     */
    public void setDataForGraph(LineChart<?,?> chart, ArrayList<CovidData> dataToDisplay){
        chart.getData().clear();
        chart.getXAxis().setAnimated(false);
        chart.setLegendVisible(false);

        XYChart.Series series = new XYChart.Series();

        int totalCasesForDate = 0;
        String lastDate = dataToDisplay.get(dataToDisplay.size() - 1).getDate();
        
        
        for (CovidData record: dataToDisplay){
            String currentDate = record.getDate();
            if (!currentDate.equals(lastDate)) {
                series.getData().add(new XYChart.Data(converter.toString(converter.fromString(currentDate)), totalCasesForDate));
                totalCasesForDate = 0;
                lastDate = currentDate;
            }
            totalCasesForDate += record.getNewCases();
        }
        chart.getData().addAll(series);
    }
}
