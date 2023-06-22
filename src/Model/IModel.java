package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

public interface IModel {

    void startServer();
    void stopServer();
    void generateMaze(int row,int col);
    Maze getM_maze();
    Solution getM_solution();
    void setM_maze(Maze m_maze);
    void solve();
    int getCharacterRowPosition();
    int getCharacterColPosition();
    boolean moveCharacter(KeyCode move);
    void setPosition(int rowIndex, int columnIndex);
    void createWithMaze(Maze mazeThatSend);
}
