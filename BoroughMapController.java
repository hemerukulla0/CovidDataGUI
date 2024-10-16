import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * The BoroughMapController class manages the functionality of the Borough Map in the application. 
 * It allows users to select a borough and obtain data within it. Additionally, it visualizes 
 * the number of deaths in each borough through a heat map representation.
 * 
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class BoroughMapController {
    private ArrayList<SVGPath> boroughs;

    private static StatisticsCalculator calc;

    /**
     * Initializes the Borough Map by finding SVG paths representing boroughs.
     * 
     * @param root The AnchorPane containing the Borough Map
     * @param calc The StatisticsCalculator used to calculate data for boroughs
     */
    public void initialize(AnchorPane root , StatisticsCalculator calc){
        boroughs = new ArrayList<>();
        this.calc = calc;
        findSVGPaths(root, boroughs);
    }

    /**
     * Handles the event when a borough on the map is clicked to show its data.
     * 
     * @param event The MouseEvent indicating a click on a borough
     * @throws IOException If there is an error loading the table.fxml file
     */
    @FXML
    public void showBoroughData(MouseEvent event) throws IOException {
        SVGPath borough = (SVGPath) event.getSource();
        String boroughID = borough.getId().replace('_', ' ');

        AnchorPane table;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("table.fxml"));
        table = loader.load();

        TableController tableController = loader.getController();
        tableController.setBoundsForData(calc, boroughID);
        Stage popupStage = new Stage();
        Scene popupScene = new Scene(table, table.getPrefWidth(), table.getPrefHeight());
        popupStage.setTitle(boroughID);
        popupStage.setScene(popupScene);
        popupStage.setResizable(false);
        popupStage.show();
    }

    /**
     * Finds SVG paths representing boroughs within the provided node.
     * 
     * @param node     The node to search for SVG paths
     * @param svgPaths The list to store found SVG paths
     */
    private void findSVGPaths(Node node, ArrayList<SVGPath> svgPaths) {
        if (node instanceof SVGPath) {
            svgPaths.add((SVGPath) node);
        } else if (node instanceof Parent) {
            for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                findSVGPaths(child, svgPaths);
            }
        }
    }

    /**
     * Updates the heat map representation of the boroughs based on the number of deaths.
     * 
     * @param calc The StatisticsCalculator used to calculate the number of deaths for each borough
     */
    public void updateMap(StatisticsCalculator calc){
        HashMap<SVGPath, Integer> deathsPerBorough = new HashMap<>();
        for (SVGPath borough : boroughs){
            String id = borough.getId().replace('_', ' ');
            deathsPerBorough.put(borough, calc.countDeathsByBorough(id));
        }

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int value : deathsPerBorough.values()) {
            max = Math.max(max, value);
            min = Math.min(min, value);
        }

        double range = max - min;

        for (SVGPath svgPath : deathsPerBorough.keySet()) {
            int value = deathsPerBorough.get(svgPath);
            double fraction = (value - min) / range;
            
            Color color = interpolateColor(fraction);

            svgPath.setFill(color);
        }
    }

    /**
     * Interpolates a color between start and end colors based on the given fraction.
     * 
     * @param fraction The fraction determining the position between start and end
     * @return The interpolated color
     */
    private Color interpolateColor(double fraction) {
        Color start = Color.YELLOW;
        Color end = Color.RED;
        
        double r = start.getRed() + (end.getRed() - start.getRed()) * fraction;
        double g = start.getGreen() + (end.getGreen() - start.getGreen()) * fraction;
        double b = start.getBlue() + (end.getBlue() - start.getBlue()) * fraction;
        return new Color(r, g, b, 1);
    }
}
