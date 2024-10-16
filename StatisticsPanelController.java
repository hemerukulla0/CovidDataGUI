import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;


/**
 * The StatisticsPanelController class manages the Statistics Panel in the application. 
 * It controls the display of various statistical data on the panel and allows users 
 * to navigate through different statistics.
 * 
 * This class initializes the panel, updates the statistics label, and handles user 
 * interactions to navigate between different statistics.
 * 
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class StatisticsPanelController{
    private static ArrayList<String> stats = new ArrayList<>();

    private int currentStatNumber;

    private static AnchorPane root;

    public StatisticsPanelController(){
    }
    
    /**
     * Constructor with parameters for the StatisticsPanelController class.
     * Sets the panel where the statistics are displayed.
     * 
     * @param root The root AnchorPane of the Statistics Panel
     */
    public StatisticsPanelController(AnchorPane root){
        this.root = root;
    }

    /**
     * This method initializes the statistics data and adds it to the stats ArrayList.
     * 
     * @param calc The StatisticsCalculator object used to calculate statistics
     */
    public void setStatisticsLabel(StatisticsCalculator calc) {
        currentStatNumber = 0;
        
        stats.clear();
        stats.add("Number of deaths in selected date range: " + calc.countDeaths());
        stats.add("Average percentage change in transit: " + calc.transitAVGCalc());
        stats.add("Average percentage change in residential: " + calc.residentialAVGCalc());
        stats.add("Average number of new cases per day: " + calc.averageCasesCalc());
    }
    
    /**
     * Switches to the next statistic in the list and updates the statistics label accordingly.
     */
    @FXML
    public void goToNextStat(){
        currentStatNumber++;
        if (currentStatNumber > 3){
            currentStatNumber = 0;
        }
        updateStatisticsLabel();
    }

    /**
     * Switches to the previous statistic in the list and updates the statistics label accordingly.
     */
    @FXML
    public void goToPreviousStat(){
        currentStatNumber--;
        if (currentStatNumber < 0){
            currentStatNumber = 3;
        }
        updateStatisticsLabel();
    }

    /**
     * Updates the statistics label with the current statistic.
     */
    public void updateStatisticsLabel() {
        Label statisticsLabel = (Label) root.getChildren().get(2);
        
        statisticsLabel.setText(stats.get(currentStatNumber));
    }
}
