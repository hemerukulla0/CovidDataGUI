import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.paint.LinearGradient;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Background;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class loads the necessary FXML files to construct different panels of the application
 * and initialises the main controller to manage the application's functionality.
 * 
 * This class sets up the main window, loads the panels, and configures the scene 
 * with the required components. Additionally, it handles the application's icon and 
 * ensures the window's responsiveness.
 *
 * @author Gurvir Singh (k23010952), Hemchandra Erukulla (k23025212), Ching Lok Tsang (k23078461), Ihab Azhar (k23049043)
 * 
 * @version 25/03/2024
 */
public class ApplicationWindow extends Application
{
    MainController controller;
    
    private final String[] fxmlText = {"WelcomePanel.fxml", "BoroughMap.fxml", "StatisticsPanel.fxml", "chart.fxml"};

    ArrayList<AnchorPane> panels = new ArrayList<>();
    /**
     * Starts the JavaFX application.
     * 
     * @param stage The primary stage for the application
     * @throws IOException if an error occurs during FXML loading
     */
    @Override
    public void start(Stage stage) throws IOException{
        Image covidIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("virus_icon.jpg")));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ApplicationWindow.fxml"));
        BorderPane root = loader.load();

        controller = loader.getController();
        controller.setRoot((BorderPane) root);
        panelLoader();
        controller.setPanels(panels);
        
        Stop[] stops = new Stop[] { 
            new Stop(0, Color.CADETBLUE), 
            new Stop(0.5, Color.LIGHTCYAN),
            new Stop(1, Color.CADETBLUE)

        };

        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0, true, null, stops);

        BackgroundFill backgroundFill = new BackgroundFill(linearGradient, null, null);

        Background background = new Background(backgroundFill);
        
        root.setBackground(background);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Covid-19 Stats For London");
        stage.setResizable(false);
        stage.getIcons().add(covidIcon);
    }
    
    /**
     * Loads panels from FXML files and adds them to the panels ArrayList.
     * 
     * @throws IOException if an error occurs during FXML loading
     */
    public void panelLoader() throws IOException
    {
        for(String fxml: fxmlText){
            FXMLLoader chartLoader = new FXMLLoader(getClass().getResource(fxml));
            AnchorPane chart = chartLoader.load();
            panels.add(chart);
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
