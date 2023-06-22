package View;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Options {


    /**
     * This classs handles the change of the options in the maze
     * lets choose different types of maze and solvers**/
    public Button m_emptyMazeButton;
    public Button m_simpleMazeButton;
    public Button m_primMazeButton;
    public Button m_bfsButton;
    public Button m_dfsButton;
    public Button m_bestButton;
    public String m_solutionKind ="BreadthFirstSearch"; // default values
    public String m_mazeKind = "MyMazeGenerator";// default values


    public void emptyMazeButton(ActionEvent actionEvent)
    {
        m_emptyMazeButton.setStyle("-fx-background-color:yellow ");
        m_simpleMazeButton.setStyle("-fx-background-color:white ");
        m_primMazeButton.setStyle("-fx-background-color:white ");
        m_mazeKind = "EmptyMazeGenerator";
    }

    public void simpleMazeButton(ActionEvent actionEvent)
    {
        m_emptyMazeButton.setStyle("-fx-background-color: white");
        m_simpleMazeButton.setStyle("-fx-background-color:yellow ");
        m_primMazeButton.setStyle("-fx-background-color:white ");
        m_mazeKind = "SimpleMazeGenerator";
    }

    public void primMazeButton(ActionEvent actionEvent)
    {
        m_emptyMazeButton.setStyle("-fx-background-color:white ");
        m_simpleMazeButton.setStyle("-fx-background-color:white ");
        m_primMazeButton.setStyle("-fx-background-color:yellow ");
        m_mazeKind = "MyMazeGenerator";
    }

    public void bfsButton(ActionEvent actionEvent)
    {
        m_bfsButton.setStyle("-fx-background-color:yellow");
        m_dfsButton.setStyle("-fx-background-color:white");
        m_bestButton.setStyle("-fx-background-color:white");
        m_solutionKind ="BreadthFirstSearch";
    }

    public void dfsButton(ActionEvent actionEvent)
    {
        m_bfsButton.setStyle("-fx-background-color:white");
        m_dfsButton.setStyle("-fx-background-color:yellow");
        m_bestButton.setStyle("-fx-background-color:white");
        m_solutionKind ="DepthFirstSearch";
    }

    public void bestButton(ActionEvent actionEvent)
    {
        m_bfsButton.setStyle("-fx-background-color:white");
        m_dfsButton.setStyle("-fx-background-color:white");
        m_bestButton.setStyle("-fx-background-color:yellow");
        m_solutionKind ="BestFirstSearch";
    }

    /**
     * After the user changed the settings we update the configuration file
     **/
    public void backButtonAction(ActionEvent actionEvent) {

        try (OutputStream output = new FileOutputStream("resources/config.properties"))
        {
            Properties properties = new Properties();
            properties.setProperty("mazeGeneratingAlgorithm", m_mazeKind);
            properties.setProperty("mazeSearchingAlgorithm", m_solutionKind);
            properties.store(output, null);
            Main.m_optionsStage.hide();
            Main.closeScreen();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
