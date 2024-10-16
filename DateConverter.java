import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

/**
 * The DateConverter class provides functionality to convert dates between different formats.
 * It encapsulates methods for converting LocalDate objects to strings and vice versa using 
 * a specified date format.
 *
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class DateConverter
{
    DateTimeFormatter dateFormatter;
    String format = "yyyy-MM-dd";
    
    
    /**
     * Constructor for objects of class DateConverter.
     * Initializes the DateTimeFormatter with the specified date format.
     */
    public DateConverter()
    {
        dateFormatter = DateTimeFormatter.ofPattern(format);
    }

    /**
     * Converts a LocalDate object to a string using the specified date format.
     * 
     * @param date The LocalDate object to convert
     * @return The string representation of the date
     */
    public String toString(LocalDate date) {
        if (date != null) {
            return dateFormatter.format(date);
        } else {
            return "";
        }
    }

    /**
     * Converts a string representation of a date to a LocalDate object.
     * 
     * @param string The string representation of the date
     * @return The LocalDate object
     */
    public LocalDate fromString(String string){
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
    }

}
