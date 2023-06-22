package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;;

import java.io.File;

import static Model.MyModel.m_playerOrientation;

public class MazeDisplayer extends Canvas {

    protected Maze m_mazeMazeDisplayer;
    private Solution m_solution;
    private int[][] m_mazeGrid;
    private int m_charachterRowPosition;
    private int m_characterColPosition;

    public MazeDisplayer() {

        widthProperty().addListener(event -> draw());
        heightProperty().addListener(event -> draw());
    }

    // the function that draws the maze
    public void draw()
    {

        if (m_mazeGrid != null)
        {

            double cellHeight = getHeight() / m_mazeGrid.length;
            double cellWidth = getWidth() / m_mazeGrid[0].length;
            try
            {
                Image cherry = new Image(new File("resources/Images/icon.png").toURI().toString());
                Image wall = new Image(new File("resources/Images/wall.png").toURI().toString());
                Image character = new Image(new File(getCharacterMoveImg()).toURI().toString());
                Image backGround = new Image(new File("resources/Images/backGround.png").toURI().toString());

                GraphicsContext graphicsContext = getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, getWidth(), getHeight());
                // draw all the parts - wall cherry and background
                graphicsContext.drawImage(backGround, 0, 0, getWidth(), getHeight());

                for (int i = 0; i < m_mazeMazeDisplayer.getRowsLength(); i++)
                {
                    for (int j = 0; j < m_mazeMazeDisplayer.getColumnsLength(); j++)
                    {
                        if(m_mazeMazeDisplayer.getCellValue(i,j) == 1)
                        {
                            graphicsContext.drawImage(wall, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }


                graphicsContext.drawImage(cherry, m_mazeMazeDisplayer.getGoalPosition().getColumnIndex() * cellWidth, m_mazeMazeDisplayer.getGoalPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);

                graphicsContext.drawImage(character, m_characterColPosition * cellWidth, m_charachterRowPosition * cellHeight, cellWidth, cellHeight);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    // setter and getter functions

    public void setM_mazeGrid(Maze m_mazeGrid)
    {
        this.m_mazeMazeDisplayer = m_mazeGrid;
        this.m_mazeGrid = new int[m_mazeMazeDisplayer.getRowsLength()][m_mazeMazeDisplayer.getColumnsLength()];

        if(m_mazeGrid !=null)
        {
            for(int i = 0; i < m_mazeMazeDisplayer.getRowsLength(); i++)
            {
                for(int j = 0; j < m_mazeMazeDisplayer.getColumnsLength(); j ++)
                {
                    this.m_mazeGrid[i][j] = this.m_mazeMazeDisplayer.getCellValue(i,j);
                }
            }
        }
        draw();
    }

    public void setCharacterPosition(int row, int col) {

        m_characterColPosition = col;
        m_charachterRowPosition = row;
        draw();
    }
    public void setM_solution(Solution solution) {

        if(solution==null)
        {
            return;
        }

        this.m_solution = solution;

        drawSolution();
    }


    public void clearView()
    {
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());

    }
    // changes the oriantation of the player by its movement
    public String getCharacterMoveImg()
    {
        String direction = m_playerOrientation;

        int playerNum = ChooseCharacter.m_playerNum;

        return switch (direction) {
            case "Up" -> "resources/Images/PlayerUp" + String.valueOf(playerNum) + ".png";
            case "Down" -> "resources/Images/PlayerDown" + String.valueOf(playerNum) + ".png";
            case "Left" -> "resources/Images/PlayerLeft" + String.valueOf(playerNum) + ".png";
            case "Right" -> "resources/Images/PlayerRight" + String.valueOf(playerNum) + ".png";
            default -> "/resources/Images/PlayerDown1.png";
        };

    }
    // this function displays the solution for the maze
    public void drawSolution() {

        Image pacManFood = new Image(new File("resources/Images/pacman food.png").toURI().toString());
        Image cherry = new Image(new File("resources/Images/icon.png").toURI().toString());
        Image character = new Image(new File(getCharacterMoveImg()).toURI().toString());


        double cellHeight = getHeight() /  m_mazeMazeDisplayer.getRowsLength();
        double cellWidth = getWidth() / m_mazeMazeDisplayer.getColumnsLength();

        GraphicsContext graphicsContext = getGraphicsContext2D();

        for (int i = 0; i < m_solution.getSolutionPath().size(); i++)
        {
            int row = ((MazeState)(m_solution.getSolutionPath().get(i))).getRow();
            int col = ((MazeState)(m_solution.getSolutionPath().get(i))).getColumn();

            if (m_solution.getSolutionPath().size() - 1 == i)
            {
                graphicsContext.drawImage(cherry, col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }

            else
            {
                if (i == 0) // draw the first step
                {
                    graphicsContext.drawImage(character, m_characterColPosition * cellWidth, m_charachterRowPosition * cellHeight, cellWidth, cellHeight);
                }
                else
                {
                    graphicsContext.drawImage(pacManFood, col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                }
            }
        }
    }


}
