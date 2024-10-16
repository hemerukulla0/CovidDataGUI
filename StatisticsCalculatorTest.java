import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.time.LocalDate;

/**
 * The StatisticsCalculatorTest class contains test cases for the StatisticsCalculator class. 
 * It ensures the accuracy of methods in StatisticsCalculator by using dummy data 
 * and verifying the correctness of method calculations and data gathering.
 * 
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043) 
 *  
 * @version 25/03/2024
 */
public class StatisticsCalculatorTest
{
    private StatisticsCalculator calculator;
    private ArrayList<CovidData> testData = new ArrayList<>();
    
    /**
     * Default constructor for test class StatisticsCalculatorTest
     */
    public StatisticsCalculatorTest()
    {
    }

    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        calculator = new StatisticsCalculator();
    }
    
    /**
     * Test for the countDeaths() method.
     * Verifies that the method correctly counts the total number of deaths.
     */
    @Test
    public void testCountDeaths() {
        testData.add(new CovidData("2023-01-01", "Borough", 0, 0, 0, 0, 0, 0, 0, 0, 5, 5));
        testData.add(new CovidData("2023-01-02", "Borough", 0, 0, 0, 0, 0, 0, 0, 0, 10, 10));
        
        calculator.recordsWithinRange = testData;

        assertEquals(15, calculator.countDeaths());
    }
    
    /**
     * Test for the selectRecordsForBoroughs() method.
     * Verifies that the method correctly selects records for a specified borough.
     */
    @Test
    public void testSelectRecordsForBoroughs() {
        // Create test data
        
        testData.add(new CovidData("2023-03-01", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 5, 0)); // Borough1 record
        testData.add(new CovidData("2023-03-02", "Borough2", 0, 0, 0, 0, 0, 0, 0, 0, 0, 5)); // Borough2 record
        testData.add(new CovidData("2023-03-03", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 0, 5)); // Borough1 record
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Call the method to select records for a specific borough
        calculator.selectRecordsForBoroughs("Borough1");

        // Get the records selected for the specified borough
        ArrayList<CovidData> selectedRecords = calculator.getArrayListForBorough();

        // Assert that the selected records belong to the specified borough
        for (CovidData record : selectedRecords) {
            assertEquals("Borough1", record.getBorough());
        }
    }
    
    /**
     * Test for the countDeathsByBorough() method.
     * Verifies that the method correctly counts deaths for a specified borough.
     */
    @Test
    public void testCountDeathsByBorough() {
        // Create test data
        
        testData.add(new CovidData("2023-04-01", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 5, 5)); // 5 new deaths for Borough1
        testData.add(new CovidData("2023-04-02", "Borough2", 0, 0, 0, 0, 0, 0, 0, 0, 0, 5)); // 0 new deaths for Borough2
        testData.add(new CovidData("2023-04-03", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 5, 5)); // 5 new deaths for Borough1
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Call the method to count deaths for a specific borough
        int deathsForBorough1 = calculator.countDeathsByBorough("Borough1");

        // Assert that the count of deaths for the specified borough is correct
        assertEquals(10, deathsForBorough1); // 5 + 5 = 10 new deaths for Borough1
    }
    
    /**
     * Test for selectRecordsWithinRange() method.
     * Verifies that the method correctly selects only those records within range.
     */
    @Test
    public void testSelectRecordsWithinRange() {
        // Create test data
        
        testData.add(new CovidData("2023-03-01", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 5, 0)); // Inside range
        testData.add(new CovidData("2023-03-02", "Borough2", 0, 0, 0, 0, 0, 0, 0, 0, 0, 5)); // Inside range
        testData.add(new CovidData("2023-02-28", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 5, 0)); // Inside range
        testData.add(new CovidData("2023-03-03", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 0, 5)); // Inside range
        testData.add(new CovidData("2023-02-27", "Borough1", 0, 0, 0, 0, 0, 0, 0, 0, 5, 0)); // Outside range
        testData.add(new CovidData("2023-03-04", "Borough2", 0, 0, 0, 0, 0, 0, 0, 0, 0, 5)); // Outside range
    
        // Set test data for the calculator
        
        calculator.allRecords = testData;
        calculator.setStartDate(LocalDate.parse("2023-03-01"));
        calculator.setEndDate(LocalDate.parse("2023-03-03"));

        
        
        // Call the method to select records within the range
        calculator.selectRecordsWithinRange();
    
        // Get the records selected within the range
        ArrayList<CovidData> selectedRecords = calculator.getArrayListWithinRange();
    
        // Assert that the selected records fall within the specified range
        for (CovidData record : selectedRecords) {
            assertTrue(record.getDate().compareTo("2023-03-01") >= 0 && record.getDate().compareTo("2023-03-03") <= 0);
        }
        
        // Assert that records outside the range are not included
        for (CovidData record : calculator.allRecords) {
            if (record.getDate().compareTo("2023-03-01") < 0 || record.getDate().compareTo("2023-03-03") > 0) {
                assertFalse(selectedRecords.contains(record));
            }
        }
    }
    
    /**
     * Test for the checkData() method.
     * Verifies that the method correctly checks for empty records.
     */
    @Test
    public void testCheckData() {
        // Test checkData method with empty records
        assertTrue(calculator.checkData());

        // Add data and test again
        calculator.recordsWithinRange.add(new CovidData("2023-01-01", "Borough", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        assertFalse(calculator.checkData());
    }
    
    /**
     * Test for the retailRecreationGMRCalc() method.
     * Verifies that the method correctly calculates the average retail recreation GMR.
     */
    @Test
    public void testRetailRecreationGMRCalc() {
        // Create test data
        
        testData.add(new CovidData("2023-02-01", "Borough", 10, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-02", "Borough", 20, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-03", "Borough", 30, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Test the retailRecreationGMRCalc method
        double expectedAverage = (10 + 20 + 30) / 3.0; // (Sum of retailRecreationGMR) / (Number of records)
        assertEquals(expectedAverage, calculator.retailRecreationGMRCalc(), 0.001); // 0.001 is delta for double comparison
    }
    
    /**
     * Test for the parksGMRCalc() method.
     * Verifies that the method correctly calculates the average parks GMR.
     */
    @Test
    public void testParksGMRCalc() {
        // Create test data
        
        testData.add(new CovidData("2023-02-01", "Borough", 0, 0, 10, 0, 0, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-02", "Borough", 0, 0, 20, 0, 0, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-03", "Borough", 0, 0, 30, 0, 0, 0, 0, 0, 0, 0));
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Test the parksGMRCalc method
        double expectedAverage = (10 + 20 + 30) / 3.0; // (Sum of parksGMR) / (Number of records)
        assertEquals(expectedAverage, calculator.parksGMRCalc(), 0.001); // 0.001 is delta for double comparison
    }
    
    /**
     * Test for the transitAVGCalc() method.
     * Verifies that the method correctly calculates the average transit GMR.
     */
    @Test
    public void testTransitAVGCalc() {
        // Create test data
        
        testData.add(new CovidData("2023-02-01", "Borough", 0, 0, 0, 10, 0, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-02", "Borough", 0, 0, 0, 20, 0, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-03", "Borough", 0, 0, 0, 30, 0, 0, 0, 0, 0, 0));
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Test the transitAVGCalc method
        double expectedAverage = (10 + 20 + 30) / 3.0; // (Sum of transitGMR) / (Number of records)
        assertEquals(expectedAverage, calculator.transitAVGCalc(), 0.001); // 0.001 is delta for double comparison
    }
    
    /**
     * Test for the workplacesGMRCalc() method.
     * Verifies that the method correctly calculates the average workplaces GMR.
     */
    @Test
    public void testWorkplacesGMRCalc() {
        // Create test data
        
        testData.add(new CovidData("2023-02-01", "Borough", 0, 0, 0, 0, 10, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-02", "Borough", 0, 0, 0, 0, 20, 0, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-03", "Borough", 0, 0, 0, 0, 30, 0, 0, 0, 0, 0));
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Test the workplacesGMRCalc method
        double expectedAverage = (10 + 20 + 30) / 3.0; // (Sum of workplacesGMR) / (Number of records)
        assertEquals(expectedAverage, calculator.workplacesGMRCalc(), 0.001); // 0.001 is delta for double comparison
    }
    
    /**
     * Test for the residentialAVGCalc() method.
     * Verifies that the method correctly calculates the average residential GMR.
     */
    @Test
    public void testResidentialAVGCalc() {
        // Create test data
        
        testData.add(new CovidData("2023-02-01", "Borough", 0, 0, 0, 0, 0, 10, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-02", "Borough", 0, 0, 0, 0, 0, 20, 0, 0, 0, 0));
        testData.add(new CovidData("2023-02-03", "Borough", 0, 0, 0, 0, 0, 30, 0, 0, 0, 0));
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Test the residentialAVGCalc method
        double expectedAverage = (10 + 20 + 30) / 3.0; // (Sum of residentialGMR) / (Number of records)
        assertEquals(expectedAverage, calculator.residentialAVGCalc(), 0.001); // 0.001 is delta for double comparison
    }
    
    /**
     * Test for the averageCasesCalc() method.
     * Verifies that the method correctly calculates the average new cases.
     */
    @Test
    public void testAverageCasesCalc() {
        // Create test data
        
        testData.add(new CovidData("2023-02-01", "Borough", 0, 0, 0, 0, 0, 0, 10, 0, 0, 0));
        testData.add(new CovidData("2023-02-02", "Borough", 0, 0, 0, 0, 0, 0, 20, 0, 0, 0));
        testData.add(new CovidData("2023-02-03", "Borough", 0, 0, 0, 0, 0, 0, 30, 0, 0, 0));
        
        // Set test data for the calculator
        calculator.recordsWithinRange = testData;

        // Test the averageCasesCalc method
        double expectedAverage = (10 + 20 + 30) / 3.0; // (Sum of average new cases) / (Number of records)
        assertEquals(expectedAverage, calculator.averageCasesCalc(), 0.001); // 0.001 is delta for double comparison
    }
    
    
}
