import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * The StatisticsCalculator class computes various figures and data concerning COVID-19 
 * and retrieves them in the required format. It handles operations such as selecting records 
 * within a specified date range, counting deaths, calculating averages, and sorting records 
 * based on specific criteria.
 *
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class StatisticsCalculator
{

    CovidDataLoader data = new CovidDataLoader();
    ArrayList<CovidData> allRecords = new ArrayList<>();
    ArrayList<CovidData> recordsWithinRange = new ArrayList<>();
    ArrayList<CovidData> boroughRecords = new ArrayList<>();
    

    private LocalDate startDate, endDate;
    
    DateConverter dateConverter = new DateConverter();
    
    /**
     * Constructs a StatisticsCalculator object with the given start and end dates.
     * 
     * @param startDateS The start date as a string in the format "yyyy-MM-dd"
     * @param endDateS The end date as a string in the format "yyyy-MM-dd"
     */
    public StatisticsCalculator(String startDateS, String endDateS)
    {
        allRecords = data.load();

        startDate = dateConverter.fromString(startDateS);
        endDate = dateConverter.fromString(endDateS);

        selectRecordsWithinRange();
    }

    /**
     * Default constructor for StatisticsCalculator.
     */
    public StatisticsCalculator(){
    }

    /**
     * Sets start date.
     *
     * @param startDate
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    /**
     * Sets end date.
     * 
     * @param endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets start date.
     */
        public LocalDate getStartDate() {
        return startDate;
    }
    
    /**
     * Gets end date,
     */
    public LocalDate getEndDate() {
        return endDate;
    }
    
    /**
     * Selects records within the specified date range and stores them in recordsWithinRange.
     */
    public void selectRecordsWithinRange(){
        recordsWithinRange.clear();

        for (CovidData record : allRecords){
            if (dateConverter.fromString(record.getDate()).isAfter(startDate.minusDays(1)) && (dateConverter.fromString(record.getDate())).isBefore(endDate.plusDays(1))){
              recordsWithinRange.add(record);
            }
        }
    }
    
    /**
     * Checks if there is any data available within the specified date range.
     * 
     * @return true if there is no data within the date range, otherwise false
     */
    public boolean checkData(){
        return recordsWithinRange.isEmpty();
    }
    
    /**
     * Selects records for the specified borough within the date range.
     * 
     * @param borough The name of the borough
     */
    public void selectRecordsForBoroughs(String borough){
        boroughRecords.clear();
        
        for (CovidData record : recordsWithinRange){
            if (record.getBorough().equals(borough)){
                boroughRecords.add(record);
            }
        }
    }

    /**
     * Counts the total number of deaths within the date range.
     * 
     * @return The total number of deaths
     */
    public int countDeaths(){
        int total = 0;
        for (CovidData record : recordsWithinRange){
            total += record.getNewDeaths();
        }

        return total;
    }

    /**
     * Counts the total number of deaths for a specific borough within the date range.
     * 
     * @param borough The name of the borough
     * @return The total number of deaths for the specified borough
     */
    public int countDeathsByBorough(String borough){
        int totalNewDeaths = 0;
        for (CovidData record : recordsWithinRange){
            if(record.getBorough().equals(borough))
                totalNewDeaths += record.getNewDeaths();
        }
        return totalNewDeaths;
    }
    
    /**
     * Retrieves the ArrayList of CovidData records for a specific borough within the date range.
     * 
     * @return The ArrayList of CovidData records for the specified borough
     */
    public ArrayList<CovidData> getArrayListForBorough(){
        return boroughRecords;
    }
    
    /**
     * Retrieves the ArrayList of CovidData records within the date range, sorted by date.
     * 
     * @return The ArrayList of CovidData records within the date range
     */
    public ArrayList<CovidData> getArrayListWithinRange(){
        recordsWithinRange.sort(Comparator.comparing(CovidData::getDate));
        return recordsWithinRange;
    }
    
    /**
     * Calculates the average value of transit GMR (Google Mobility Report) within the date range.
     * 
     * @return The average transit GMR value
     */
    public double transitAVGCalc(){
        int sum = 0;
        int numOfRecords = recordsWithinRange.size();
        if (numOfRecords != 0) {
        for (CovidData record : recordsWithinRange){
            sum += record.getTransitGMR();
        }
        return sum/numOfRecords;
        } else {
        return 0;
        }
    }

    /**
     * Calculates the average value of residential GMR (Google Mobility Report) within the date range.
     * 
     * @return The average residential GMR value
     */
    public double residentialAVGCalc(){
        int sum = 0;
        int numOfRecords = recordsWithinRange.size();
        if (numOfRecords != 0) { // Check if numOfRecords is not zero
        for (CovidData record : recordsWithinRange){
            sum += record.getResidentialGMR();
        }
        return sum/numOfRecords;
        } else {
            return 0;
        }
    }
    
    /**
     * Calculates the average value of workplaces GMR (Google Mobility Report) within the date range.
     * 
     * @return The average workplaces GMR value
     */
    public double workplacesGMRCalc() {
        int sum = 0;
        int numOfRecords = recordsWithinRange.size();
        if (numOfRecords != 0) { // Check if numOfRecords is not zero
        for (CovidData record : recordsWithinRange) {
            sum += record.getWorkplacesGMR();
        }
        return sum/numOfRecords;
        } else {
            return 0; // Return 0 if numOfRecords is zero to avoid division by zero
        }
    }
    
    /**
     * Calculates the average value of new cases within the date range.
     * 
     * @return The average number of new cases
     */
    public double averageCasesCalc() {
        int sum = 0;
        int numOfRecords = recordsWithinRange.size();
        if (numOfRecords != 0) { // Check if numOfRecords is not zero
        for (CovidData record : recordsWithinRange) {
            sum += record.getNewCases();
        }
        return sum/numOfRecords;
        } else {
            return 0; // Return 0 if numOfRecords is zero to avoid division by zero
        }
    }
    
    /**
     * Calculates the average value of parks GMR (Google Mobility Report) within the date range.
     * 
     * @return The average parks GMR value
     */
    public double parksGMRCalc() {
        int sum = 0;
        int numOfRecords = recordsWithinRange.size();
        if (numOfRecords != 0) { // Check if numOfRecords is not zero
        for (CovidData record : recordsWithinRange) {
            sum += record.getParksGMR();
        }
        return sum/numOfRecords;
        } else {
            return 0; // Return 0 if numOfRecords is zero to avoid division by zero
        }
    }
    
    /**
     * Calculates the average value of retail recreation GMR (Google Mobility Report) within the date range.
     * 
     * @return The average retail recreation GMR value
     */
    public double retailRecreationGMRCalc() {
        int sum = 0;
        int numOfRecords = recordsWithinRange.size();
        if (numOfRecords != 0) { // Check if numOfRecords is not zero
        for (CovidData record : recordsWithinRange) {
            sum += record.getRetailRecreationGMR();
        }
        return sum/numOfRecords;
        } else {
            return 0; // Return 0 if numOfRecords is zero to avoid division by zero
        }
    }
    
}
