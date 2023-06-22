package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MyModel  extends Observable implements IModel {

    private Server m_mazeGeneratorServer; // generate maze server
    private Server m_solutionServer; // solve maze server
    private Solution m_solution; // maze solution
    public Maze m_maze; // the maze itself
    private int m_characterRowPosition;
    private int m_charColPosition;
    public static String m_playerOrientation = "Up";
    public static final Logger m_logger = LogManager.getLogger("MyModel");

    public MyModel()
    {
        // initialize both servers
        this.m_mazeGeneratorServer = new Server(5400,1000,new ServerStrategyGenerateMaze());
        this.m_solutionServer = new Server(5401,1000,new ServerStrategySolveSearchProblem());
    }
    @Override
    public void startServer()
    {
        this.m_mazeGeneratorServer.start();
        this.m_solutionServer.start();
    }
    @Override
    public void stopServer()
    {
        this.m_mazeGeneratorServer.stop();
        this.m_solutionServer.stop();
    }

    private void generate(int row,int col) // maze generating function with server
    {
        try
        {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy()
            {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                {
                    try
                    {
                        m_logger.info("Generating maze");
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(row * col) + 10];
                        is.read(decompressedMaze);
                        m_maze = new Maze(decompressedMaze);
                        setM_maze(m_maze);
                        setPosition(m_maze.getStartPosition().getRowIndex(), m_maze.getStartPosition().getColumnIndex());
                    }
                    catch (Exception var10)
                    {
                        var10.printStackTrace();
                    }

                }
            });

            client.communicateWithServer();

        }
        catch (UnknownHostException var1)
        {
            var1.printStackTrace();
        }
    }

    // the server that solves the maze function
    @Override
    public void solve()
    {
        try
        {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy()
            {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer)
                {
                    try
                    {
                        m_logger.info("Solving maze");
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(m_maze);
                        toServer.flush();
                        Solution mazeSolution = (Solution)fromServer.readObject();
                        setSolution(mazeSolution);
                    }
                    catch (Exception var10)
                    {
                        var10.printStackTrace();
                    }
                }
            });

            client.communicateWithServer();

        }
        catch (UnknownHostException var1)
        {
            var1.printStackTrace();
        }
    }

    // function that loads us the maze from the saves
    @Override
    public void createWithMaze(Maze maze)
    {
        setM_maze(maze);
        setPosition(maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex());
        setChanged();
        notifyObservers();
    }

    @Override
    public void setM_maze(Maze m_maze) {
        this.m_maze = m_maze;
        setChanged();
        notifyObservers();
    }

    public void setSolution(Solution solution)
    {
        this.m_solution = solution;
        setChanged();
        notifyObservers();
    }

    // generate maze with a given number of rows and cols
    @Override
    public void generateMaze(int row, int col)
    {
        generate(row, col);
        setChanged();
        notifyObservers();
    }
    @Override
    public Maze getM_maze() {
        return m_maze;
    }

    @Override
    public Solution getM_solution() {
        return m_solution;
    }


    // the function that moves the character by inputs from the keyboard , the user can use both the numpad and the arrows
    @Override
    public boolean moveCharacter(KeyCode move)
    {
        switch (move) {
            case NUMPAD2, DOWN -> {
                if (canMove(m_characterRowPosition + 1, m_charColPosition)) {
                    m_characterRowPosition++;
                }
                m_playerOrientation = "Down";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD8, UP -> {
                if (canMove(m_characterRowPosition - 1, m_charColPosition)) {
                    m_characterRowPosition--;
                }
                m_playerOrientation = "Up";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD4, LEFT -> {
                if (canMove(m_characterRowPosition, m_charColPosition - 1)) {
                    m_charColPosition--;
                }
                m_playerOrientation = "Left";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD6, RIGHT -> {
                if (canMove(m_characterRowPosition, m_charColPosition + 1)) {
                    m_charColPosition++;
                }
                m_playerOrientation = "Right";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD3 -> {
                if (canMove(m_characterRowPosition + 1, m_charColPosition + 1) && (canMove(m_characterRowPosition + 1, m_charColPosition) || canMove(m_characterRowPosition, m_charColPosition + 1))) {
                    m_characterRowPosition++;
                    m_charColPosition++;
                }
                m_playerOrientation = "Down";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD1 -> {
                if (canMove(m_characterRowPosition + 1, m_charColPosition - 1) && (canMove(m_characterRowPosition, m_charColPosition - 1) || canMove(m_characterRowPosition + 1, m_charColPosition))) {
                    m_characterRowPosition++;
                    m_charColPosition--;
                }
                m_playerOrientation = "Down";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD9 -> {
                if (canMove(m_characterRowPosition - 1, m_charColPosition + 1) && (canMove(m_characterRowPosition, m_charColPosition + 1) || canMove(m_characterRowPosition - 1, m_charColPosition))) {
                    m_characterRowPosition--;
                    m_charColPosition++;
                }
                m_playerOrientation = "Up";
                setChanged();
                notifyObservers();
                return true;
            }
            case NUMPAD7 -> {
                if (canMove(m_characterRowPosition - 1, m_charColPosition - 1) && (canMove(m_characterRowPosition - 1, m_charColPosition) || canMove(m_characterRowPosition, m_charColPosition - 1))) {
                    m_characterRowPosition--;
                    m_charColPosition--;
                }
                m_playerOrientation = "Up";
                setChanged();
                notifyObservers();
                return true;
            }
        }
        setChanged();
        notifyObservers();
        return false;
    }


    // function that checks if the character can move to a given direction
    public boolean canMove(int row , int col)
    {
        return m_maze.getCellValue(row,col) == 0;
    }

    @Override
    public void setPosition(int row, int col)
    {
        m_characterRowPosition = row;
        m_charColPosition = col;

        setChanged();
        notifyObservers();
    }

    @Override
    public int getCharacterRowPosition() {
        return m_characterRowPosition;
    }

    @Override
    public int getCharacterColPosition() {
        return m_charColPosition;
    }




}
