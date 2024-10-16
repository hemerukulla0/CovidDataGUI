import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert.AlertType;

/**  
 * The MainController class serves as the central controller for the application, managing its functionality 
 * and controlling various user interactions. It facilitates navigation between panels, handles date selection,
 * and coordinates the display of COVID-19 data on different views.
 * 
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class MainController
{
    @FXML
    private DatePicker toDatePicker, fromDatePicker;

    @FXML
    private Label panelNumberLabel;

    @FXML
    private Button backButton, forwardButton;
    @FXML
    private BorderPane root;
    private LocalDate fromDate, toDate;
    private final DateConverter dateConverter = new DateConverter();
    BoroughMapController boroughMapController = new BoroughMapController();
    StatisticsPanelController statisticsPanelController;
    ChartController chartController = new ChartController();
    private  ArrayList<CovidData> records = new ArrayList<>();
    private List<AnchorPane> panels = new ArrayList<>();
    private int currentPanelNumber = 0;
    private boolean fromListenerActive = true;
    private boolean toListenerActive = true;

    /**
     * Initializes the MainController after the FXML file has been loaded.
     * Sets up listeners for date pickers and initializes the first panel.
     */
    @FXML
    public void initialize() {
        updatePanelNumberText();
        setDayCellFactory(fromDatePicker);
        setDayCellFactory(toDatePicker);
        addFromDatePickerListener();
        addToDatePickerListener();
    }

    /**
     * Sets the root BorderPane of the main window.
     * 
     * @param root The BorderPane representing the root of the main window
     */
    public void setRoot(BorderPane root) {
        this.root = root;
    }

    /**
     * Sets the list of panels to be managed by the controller.
     * 
     * @param panels The list of AnchorPane objects representing panels
     */
    public void setPanels(List<AnchorPane> panels) {
        this.panels = panels;
    }

    /**
     * Switches to the next panel in the list.
     */
    @FXML
    public void goToNextPanel() {
        root.getChildren().remove(panels.get(currentPanelNumber));
        currentPanelNumber++;

        if (currentPanelNumber >= panels.size()) {
            currentPanelNumber = 0;
        }

        root.setCenter(panels.get(currentPanelNumber));
        setChart();
        setStatsLabel();
        updatePanelNumberText();
    }

    /**
     * Switches to the previous panel in the list.
     */
    @FXML
    public void goToPreviousPanel() {
        root.getChildren().remove(panels.get(currentPanelNumber));
        currentPanelNumber--;
        if (currentPanelNumber < 0) {
            currentPanelNumber = 3;
        }

        root.setCenter(panels.get(currentPanelNumber));
        setChart();
        setStatsLabel();
        updatePanelNumberText();
    }

    /**
     * Sets the chart data based on the current panel.
     */
    private void setChart(){
        if (currentPanelNumber == 3){
            AnchorPane chartPanel = (AnchorPane) root.getChildren().get(4);

            LineChart chart = (LineChart) chartPanel.getChildren().get(0);

            chartController.setDataForGraph(chart, records);
        }
    }
    
    /**
     * Sets the stattistics label text.
     */
    private void setStatsLabel(){
        if (currentPanelNumber == 2){
            statisticsPanelController.updateStatisticsLabel();
        }
    }
    
    /**
     * Updates the panel number label to reflect the current panel being displayed.
     */
    private void updatePanelNumberText() {
        panelNumberLabel.setText("Panel number: " + (currentPanelNumber + 1));
    }

    /**
     * Sets the day cell factory for a DatePicker to disable future dates.
     * 
     * @param datePicker The DatePicker for which to set the day cell factory
     */
    private void setDayCellFactory(DatePicker datePicker) {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                // Disable dates in the future
                setDisable(date.isAfter(LocalDate.now()));
            }
        });
    }

    /**
     * Adds a listener to the fromDatePicker to update the fromDate and toDate values.
     */
    private void addFromDatePickerListener() {
        fromDate = fromDatePicker.getValue();
        toDate = toDatePicker.getValue();

        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!fromListenerActive) {
                return;
            }

            if (newValue != null && toDate != null && newValue.isAfter(toDatePicker.getValue())) {
                toListenerActive = false; // Disable the "to" listener temporarily
                toDatePicker.setValue(newValue);
                toListenerActive = true;
            }
            fromDate = newValue;

            checkIfDatesSelected(fromDate, toDate);
        });
    }

    /**
     * Adds a listener to the toDatePicker to update the toDate value.
     */
    private void addToDatePickerListener(){
        fromDate = fromDatePicker.getValue();
        toDate = toDatePicker.getValue();

        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!toListenerActive) {
                return; // Skip if the "to" listener is not active
            }

            if (newValue != null && fromDate != null && newValue.isBefore(fromDate)) {
                fromListenerActive = false; // Disable the "from" listener temporarily
                fromDatePicker.setValue(newValue);
                fromListenerActive = true;
            }
            toDate = newValue;

            checkIfDatesSelected(fromDate, toDate);
        });
    }

    /**
     * Checks if valid dates are selected and updates the UI accordingly.
     * 
     * @param fromDate The selected start date
     * @param toDate The selected end date
     */
    private void checkIfDatesSelected(LocalDate fromDate, LocalDate toDate) {
        if (toDatePicker.getValue() != null && fromDatePicker.getValue() != null) {

            StatisticsCalculator calc = new StatisticsCalculator(dateConverter.toString(fromDate), dateConverter.toString(toDate));

            if (calc.checkData()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText(null);
                alert.setContentText("There is no available data for the date range you chose.");
                alert.showAndWait();

                root.getChildren().remove(panels.get(currentPanelNumber));

                currentPanelNumber = 0;
                updatePanelNumberText();
                
                root.setCenter(panels.get(currentPanelNumber));

                backButton.setDisable(true);
                forwardButton.setDisable(true);

                return;
            }

            boroughMapController.initialize(panels.get(1), calc);
            boroughMapController.updateMap(calc);

            statisticsPanelController = new StatisticsPanelController(panels.get(2));
            statisticsPanelController.setStatisticsLabel(calc);

            records = calc.getArrayListWithinRange();
            setChart();
            setStatsLabel();

            backButton.setDisable(false);
            forwardButton.setDisable(false);

        } else {
            backButton.setDisable(true);
            forwardButton.setDisable(true);
        }
    }
}
