package View;

import javafx.event.ActionEvent;

public class MyViewController
{
    public void startButton(ActionEvent actionEvent) {
        Main.newGame();
    }

    public void exitButton(ActionEvent actionEvent) {
        Main.exitGame();
    }
}
