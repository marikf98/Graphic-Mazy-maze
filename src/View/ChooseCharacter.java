package View;

import javafx.event.ActionEvent;

public class ChooseCharacter {
    public static int m_playerNum = 1; // hold the player num that the user will choose and we set the default value for the load option

    /**
     * Function for each button to choose the character according to what the user pressed
     **/
    public void playerOneButton(ActionEvent actionEvent) {

        m_playerNum = 1;
        Main.m_chooseCharacterStage.hide();
        Main.input();
    }

    public void playerTwoButton(ActionEvent actionEvent) {

        m_playerNum = 2;
        Main.m_chooseCharacterStage.hide();
        Main.input();
    }

    public void playerThreeButton(ActionEvent actionEvent) {

        m_playerNum = 3;
        Main.m_chooseCharacterStage.hide();
        Main.input();
    }

}
