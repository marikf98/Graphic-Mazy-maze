package View;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

import java.util.Observable;
import java.util.Random;

import static View.Main.*;

public class Input extends Observable {
    public TextField m_rowsInput;
    public TextField m_colsInput;
    public static int m_rowGenerate;
    public static int m_colGenerate;

    public void createRandomMaze(ActionEvent actionEvent) {

        m_inputStage.hide();

        Random rand = new Random();

        m_rowGenerate = rand.nextInt(20); // make sure that the random is between 20 and 40
        m_rowGenerate += 20;
        m_colGenerate = m_rowGenerate; // and we create the maze as a square for the best result

        m_inputStage.hide();
        m_playView.generateMaze(m_rowGenerate, m_colGenerate,null); // and we sent the sizes to the maze creator
    }


    //this function creates a maze from the user input
    public void createInputMaze(ActionEvent actionEvent) {

        try {
            m_rowGenerate = Integer.parseInt(m_rowsInput.getText());
            m_colGenerate = Integer.parseInt(m_colsInput.getText());
            if (m_rowGenerate < 3 || m_colGenerate < 3) // we dont a maze that is too small to make the game interesting
            {
                error();
                return;
            }

        }
        catch (Exception e)
        {
            error();
            return;
        }


        m_inputStage.hide();
        m_inputStage.hide();
        m_playView.generateMaze(m_rowGenerate, m_colGenerate,null);// and we sent the sizes to the maze creator
    }
}

