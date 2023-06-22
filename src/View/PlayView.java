package View;

import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Button;

import java.io.*;
import java.util.Observable;
import java.util.Observer;

import static View.Main.*;

// this is the controller of the main menu with all the functions
public class PlayView implements IView, Observer {

    @FXML
    private Pane m_pane;
    @FXML
    private MazeDisplayer m_mazeDisplayer;
    @FXML
    private AnchorPane m_anchorPane;
    private static boolean m_showSolution;
    private static boolean m_newGame;

    public void newGame(ActionEvent actionEvent) {
        Main.newGame();
    } // create a new game
    public void save(ActionEvent actionEvent) // save an existing game
    {

        if(m_myViewModel.m_model.getM_maze() == null)
        {
            return;
        }

        try
        {
            File directory = new File("resources/Saved");
            int saveCount = directory.list().length;
            File saveFile = new File("resources/Saved/Gamesave" + saveCount); // create a new save file
            FileOutputStream writer = new FileOutputStream("resources/Saved/Gamesave" + saveCount); // write to that file
            ObjectOutputStream save = new ObjectOutputStream(writer);
            Maze saveMaze = m_myViewModel.m_model.getM_maze();
            saveMaze.setNewStartPosition(m_myViewModel.getM_characterRowPosition(), m_myViewModel.getM_characterColPosition());
            save.writeObject(saveMaze);
            save.close();
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void load(ActionEvent actionEvent) // lets us choose a game that was saved with buttons that represent a different save game
    {
        try
        {
            File directory = new File("resources/Saved");
            int loadCount = directory.list().length;

            int locationInPane = 0;

            boolean flag = false;

            Button[] savedMazes = new Button[loadCount];

            for(int i = 0 ;i<loadCount; i++)
            {
                savedMazes[i] = new Button("Game Saved Number: " + String.valueOf(i));
                int curr = i;
                savedMazes[i].setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent actionEvent) // set the game after a save was loaded
                    {
                        try
                        {
                            FileInputStream reader = new FileInputStream("resources/Saved/Gamesave"  + String.valueOf(curr));
                            ObjectInputStream objectReader = new ObjectInputStream(reader);
                            Maze savedMaze = (Maze) objectReader.readObject();
                            objectReader.close();

                            m_mazeDisplayer.clearView();

                            m_showSolution = false;
                            m_newGame = false;

                            m_myViewModel.setPosition(savedMaze.getStartPosition().getRowIndex(),savedMaze.getStartPosition().getColumnIndex());
                            m_myViewModel.setMaze(savedMaze);
                            m_solution = m_myViewModel.getSolution();
                            m_maze = m_myViewModel.getMaze();

                            displayMaze(savedMaze);

                            m_loadStage.hide();
                            m_backgroudMediaPlayer.stop();

                            playMainMusic();

                            m_mainStage.show();

                            generateMaze(0,0,savedMaze);
                        }
                        catch (Exception e)
                        {
                            System.out.println(e.getMessage());
                        }
                    }
                });

                savedMazes[i].setStyle("-fx-background-color: yellow;"); // set the style of all the buttons of the save

                savedMazes[i].setTranslateY(locationInPane);

                if(flag)
                {
                    locationInPane =- (locationInPane + 30);
                    flag = false;
                }
                else
                {
                    locationInPane = locationInPane + 30;
                    flag = true;
                }
            }
                m_stackPane = FXMLLoader.load(getClass().getResource("LoadGame.fxml"));
                m_loadScene =  new Scene(m_stackPane,800,550);
                m_stackPane.getChildren().addAll(savedMazes);

                if(loadCount==0)
                {
                    m_stackPane.getChildren().add(new Label("There Isn't Any Saved Mazes"));
                }

                Button back = new Button("Back");
                back.setStyle("-fx-background-color: yellow;");

                back.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent actionEvent)
                    {
                        Main.m_loadStage.hide();
                        Main.m_mainStage.show();
                    }
                });

                back.setTranslateY(300);

                m_stackPane.getChildren().add(back);
                // and display all the scenes
                m_loadStage = new Stage();

                m_loadStage.setScene(m_loadScene);
                m_loadStage.initStyle(StageStyle.UNDECORATED);
                m_mainStage.hide();
                m_loadStage.show();
            }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //handles moving the character with the keyboard

    public void move(javafx.scene.input.KeyEvent keyEvent)
    {
        if(m_maze == null)
        {
            return;
        }

        else
        {
            m_myViewModel.setM_characterColPosition(0);
            m_myViewModel.setM_characterRowPosition(0);
        }
        if(m_newGame)
        {
            return;
        }
        if(m_mazeDisplayer ==null)
        {
            return;
        }

        Main.m_myViewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
        Main.m_myViewModel.getMaze().setNewStartPosition(Main.m_myViewModel.getM_characterRowPosition(),Main.m_myViewModel.getM_characterColPosition());
        m_myViewModel.setPosition(Main.m_myViewModel.getM_characterRowPosition(),Main.m_myViewModel.getM_characterColPosition());
        displayMaze(m_myViewModel.getMaze());
        if(m_showSolution && (m_myViewModel.getM_characterRowPosition()!= m_myViewModel.getMaze().getGoalPosition().getRowIndex() || m_myViewModel.getM_characterColPosition() != m_myViewModel.getMaze().getGoalPosition().getColumnIndex()))
        {
            m_myViewModel.solve();
            m_solution = m_myViewModel.getSolution();
            m_mazeDisplayer.setM_solution(m_solution);
        }
    }

    //handles displaying the options screen
    public void options(ActionEvent actionEvent)
    {
        m_optionsStage = new Stage();
        m_optionsStage.initStyle(StageStyle.UNDECORATED);
        m_optionsStage.setScene(Main.m_optionsScene);
        Main.m_mainStage.hide();
        m_optionsStage.show();
    }
    //handles displaying the help screen

    public void help(ActionEvent actionEvent)
    {
        Main.m_mainStage.setScene(Main.m_helpScene);
        Main.m_mainStage.show();
    }

    //handles displaying the about screen

    public void about(ActionEvent actionEvent)
    {
        Main.m_mainStage.setScene(Main.m_aboutScene);
        Main.m_mainStage.show();
    }
    //handles displaying the options screen

    public void exit(ActionEvent actionEvent) {
        Main.exitGame();
    }
    // draw the maze
    @Override
    public  void displayMaze(Maze maze)
    {
      m_mazeDisplayer.setCharacterPosition(Main.m_myViewModel.getM_characterRowPosition(),Main.m_myViewModel.getM_characterColPosition());
      m_mazeDisplayer.setM_mazeGrid(maze);
    }

    // check if we reached the cherry
    public void readCherry()
    {
        if(m_mazeDisplayer.m_mazeMazeDisplayer.getGoalPosition().getColumnIndex() == m_myViewModel.getM_characterColPosition() && m_mazeDisplayer.m_mazeMazeDisplayer.getGoalPosition().getRowIndex() == m_myViewModel.getM_characterRowPosition())
        {
            m_newGame = true;

            winning();
        }
    }

    // update that model of any change
    @Override
    public void update(Observable o, Object arg)
    {

        if(Main.m_myViewModel == o)
        {
            if(m_newGame || m_maze == null)
            {
                return;
            }
            displayMaze(m_myViewModel.getMaze());

            Main.m_solution = m_myViewModel.getSolution();

            readCherry();
        }
    }

    //handles displaying the solve button
    public void solve(ActionEvent actionEvent)
    {
        if(m_maze ==null)
        {
            return;
        }
        m_showSolution = true;
        m_maze.setNewStartPosition(m_myViewModel.getM_characterRowPosition(), m_myViewModel.getM_characterColPosition());
        m_myViewModel.setMaze(m_maze);
        m_myViewModel.solve();
        m_solution = m_myViewModel.getSolution();
        m_mazeDisplayer.setM_solution(m_solution);
        m_mazeDisplayer.requestFocus();
    }

    // generates the maze for load or new game with values
    public void generateMaze(int row, int col, Maze maze)
    {
        try
        {
            m_newGame = false; // check in what state we are in
            m_showSolution = false;
            PlayView view = m_fxmlLoader.getController();
            m_myViewModel.addObserver(view);

            if(maze != null) // check in what mode we are
            {
                m_myViewModel.m_model.createWithMaze(maze);
            }

            else
            {
                m_myViewModel.generateMaze(row,col);
            }

            m_pane.prefHeightProperty().bind(m_anchorPane.heightProperty().subtract(50));
            m_pane.prefWidthProperty().bind(m_anchorPane.widthProperty());
            m_mazeDisplayer.heightProperty().bind(m_pane.heightProperty());
            m_mazeDisplayer.widthProperty().bind(m_pane.widthProperty());
            m_maze = m_myViewModel.getMaze();

            m_anchorPane.setOnKeyPressed(new EventHandler<KeyEvent>()
            {
                @Override
                public void handle(KeyEvent keyEvent)
                {
                    if(m_newGame)
                    {
                        return;
                    }
                    m_myViewModel.moveCharacter(keyEvent.getCode());
                }
            });
            displayMaze(m_maze);
            m_mazeDisplayer.requestFocus();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //handles displaying the choose character screen

    public void chooseCharacter(ActionEvent actionEvent)
    {
        Main.chooseCharacter();
    }

    public void mouseClicked(MouseEvent mouseEvent)
    {}


}

