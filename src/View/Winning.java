package View;

import javafx.event.ActionEvent;

public class Winning {
    public void newGameButton(ActionEvent actionEvent) // if we want a new game
    {
        Main.m_winningStage.hide();
        Main.newGame();
    }

    public void exitButton(ActionEvent actionEvent)
    {
        Main.exitGame();
    } // if we want to quit the game
}
