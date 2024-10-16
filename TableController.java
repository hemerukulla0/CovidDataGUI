import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;

/**
 * The TableController class manages data presentation and sorting functionality for the TableView
 * displaying COVID-19 statistics for a selected borough. It allows users to view data by various statistics
 * and sorts the data accordingly. This class interacts with a StatisticsCalculator to fetch and process data.
 *
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class TableController
{
    @FXML
    private TableView<CovidData> database;

    @FXML
    public TableColumn<CovidData, String> dateColumn;

    @FXML
    public TableColumn<CovidData, Integer> retailColumn;    
    @FXML
    public TableColumn<CovidData, Integer> groceryColumn;

    @FXML
    public TableColumn<CovidData, Integer> parksColumn;

    @FXML
    public TableColumn<CovidData, Integer> transitColumn;

    @FXML
    public TableColumn<CovidData, Integer> workplacesColumn;

    @FXML
    public TableColumn<CovidData, Integer> residentialColumn;

    @FXML
    TableColumn<CovidData, Integer> newCasesColumn;

    @FXML
    TableColumn<CovidData, Integer> totalCasesColumn;

    @FXML
    TableColumn<CovidData, Integer> newDeathsColumn;
    
    @FXML
    Label titleLabel;

    @FXML
    ChoiceBox<String> choiceBox;

    private final String[] choicesForChoiceBox = {"Date", "Retail and Recreation", "Grocery and Pharmacy", "Parks", "Transit Stations", "Workplaces", "Residential", "New cases", "Total Cases", "New Deaths"};

    private String boroughName;

    /**
     * Sets the title label and loads data for the selected borough into the TableView.
     * 
     * @param calc The StatisticsCalculator object used to fetch data
     * @param boroughName The name of the selected borough
     */
    public void setBoundsForData(StatisticsCalculator calc, String boroughName){
        this.boroughName = boroughName;

        titleLabel.setText("Data for: " + boroughName);
        database.setItems((getData(calc)));
    }
    
    /**
     * Sets up the initial state of the TableView and ChoiceBox when the FXML is loaded.
     */
    @FXML
    public void initialize(){
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        retailColumn.setCellValueFactory(new PropertyValueFactory<>("retailRecreationGMR"));
        groceryColumn.setCellValueFactory(new PropertyValueFactory<>("groceryPharmacyGMR"));
        parksColumn.setCellValueFactory(new PropertyValueFactory<>("parksGMR"));
        transitColumn.setCellValueFactory(new PropertyValueFactory<>("transitGMR"));
        workplacesColumn.setCellValueFactory(new PropertyValueFactory<>("workplacesGMR"));
        residentialColumn.setCellValueFactory(new PropertyValueFactory<>("residentialGMR"));
        newCasesColumn.setCellValueFactory(new PropertyValueFactory<>("newCases"));
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCases"));
        newDeathsColumn.setCellValueFactory(new PropertyValueFactory<>("newDeaths"));
        
        database.setPlaceholder(new Label("No data for these days (update your date range)"));
        choiceBox.getItems().addAll(choicesForChoiceBox);
        choiceBox.setOnAction(event -> sortTable());
    }

    /**
     * Retrieves data for the selected borough from the StatisticsCalculator.
     * 
     * @param calc The StatisticsCalculator object used to fetch data
     * @return An ObservableList of CovidData objects representing the data for the selected borough
     */
    public ObservableList<CovidData> getData(StatisticsCalculator calc){
        calc.selectRecordsForBoroughs(boroughName);

        ObservableList<CovidData> data = FXCollections.observableArrayList();

        data.addAll(calc.getArrayListForBorough());
        
        return data;
    }

    /**
     * Sorts the TableView based on the selected option in the ChoiceBox.
     */
    public void sortTable(){
        String selectedOption = choiceBox.getValue();

        database.getSortOrder().clear();

        switch(selectedOption) {
            case "Date":
                database.getSortOrder().add(dateColumn);
                break;
            case "Retail and Recreation":
                database.getSortOrder().add(retailColumn);
                break;
            case "Grocery and Pharmacy":
                database.getSortOrder().add(groceryColumn);
                break;
            case "Parks":
                database.getSortOrder().add(parksColumn);
                break;
            case "Transit Stations":
                database.getSortOrder().add(transitColumn);
                break;
            case "Workplaces":
                database.getSortOrder().add(workplacesColumn);
                break;
            case  "Residential":
                database.getSortOrder().add(residentialColumn);
                break;
            case "New cases":
                database.getSortOrder().add(newCasesColumn);
                break;
            case "Total Cases":
                database.getSortOrder().add(totalCasesColumn);
                break;
            case "New Deaths":
                database.getSortOrder().add(newDeathsColumn);
                break;
        }
    }
}