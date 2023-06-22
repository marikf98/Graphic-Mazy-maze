package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    public IModel m_model;
    private Solution m_solution;
    private int m_characterRowPosition;
    private int m_characterColPosition;

    public MyViewModel(IModel model)
    {
        this.m_model = model;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (m_model == o)
        {
            m_characterRowPosition = m_model.getCharacterRowPosition();
            m_characterColPosition = m_model.getCharacterColPosition();
            m_solution = m_model.getM_solution();
            setChanged();
            notifyObservers();
        }

    }

    public void generateMaze(int rows,int cols)
    {
        m_model.generateMaze(rows,cols);

    }

    public boolean moveCharacter(KeyCode move)
    {
        return m_model.moveCharacter(move);
    }

    // setters and getters

    public void setPosition(int row, int col)
    {
        m_characterColPosition = col;
        m_characterRowPosition = row;
        m_model.setPosition(row,col);
    }
    public int getM_characterRowPosition()
    {
        return m_characterRowPosition;
    }

    public void setM_characterRowPosition(int row)
    {
        m_characterRowPosition = row;
    }

    public int getM_characterColPosition()
    {
        return m_characterColPosition;
    }

    public void setM_characterColPosition(int col)
    {
        m_characterColPosition =col;
    }

    public Solution getSolution() {
        return m_solution;
    }
    public Maze getMaze(){return m_model.getM_maze();}

    public void setMaze(Maze maze)
    {
        m_model.setM_maze(maze);
    }
    public void solve()
    {
        m_model.solve();
    }

}
