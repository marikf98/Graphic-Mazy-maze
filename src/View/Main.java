package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;


public class Main extends Application {

    public static Maze m_maze;
    public static Solution m_solution;
    public static MyModel m_myModel;
    public static MyViewModel m_myViewModel;
    public static Parent m_aboutParent;
    public static Scene m_aboutScene;
    public static Stage m_errorStage;
    public static Parent m_errorParent;
    public static Scene m_errorScene;
    public static Stage m_inputStage;
    public static Parent m_inputParent;
    public static Scene m_inputScene;
    public static Parent m_helpParent;
    public static Scene m_helpScene;
    public static Parent m_myViewParent;
    public static Scene m_myViewScene;
    public static Stage m_optionsStage;
    public static Parent m_optionsParent;
    public static Scene m_optionsScene;
    public static Stage m_chooseCharacterStage;
    public static Parent m_chooseCharacterParent;
    public static Scene m_chooseCharacterScene;
    public static Parent m_playViewParent;
    public static Scene m_playViewScene;
    public static Parent m_winningParent;
    public static Scene m_winningScene;
    public static Stage m_winningStage;
    public static Stage m_loadStage;
    public static Scene m_loadScene;
    public static Stage m_mainStage;
    public static MediaPlayer m_backgroudMediaPlayer;
    public static MediaPlayer m_winningMediaPlayer;
    public static MediaPlayer m_chooseCharacterMediaPlayer;
    public static FXMLLoader m_fxmlLoader;
    public static StackPane m_stackPane;
    public static PlayView m_playView;

    public static void main(String args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // first we load all the screens
        m_errorParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Error.fxml")));
        m_aboutParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("About.fxml")));
        m_inputParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Input.fxml")));
        m_helpParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Help.fxml")));
        m_myViewParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MyView.fxml")));
        m_optionsParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Options.fxml")));
        m_chooseCharacterParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ChooseCharacter.fxml")));

        m_fxmlLoader = new FXMLLoader(getClass().getResource("PlayView.fxml")); // then we load the main screen
        m_playViewParent = m_fxmlLoader.load();
        m_playView = m_fxmlLoader.getController();

        // set all the scenes with their sizes
        m_aboutScene =  new Scene(m_aboutParent,800,550);
        m_errorScene =  new Scene(m_errorParent,500,200);
        m_chooseCharacterScene =  new Scene(m_chooseCharacterParent,500,200);
        m_inputScene =  new Scene(m_inputParent,600,400);
        m_helpScene =  new Scene(m_helpParent,800,550);
        m_optionsScene =  new Scene(m_optionsParent,800,550);
        m_playViewScene =  new Scene(m_playViewParent,1000,700);
        m_myViewScene =  new Scene(m_myViewParent,800,550);
        // we create the winning screen seperatly so nothing wont be overloaded
        m_winningParent = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("Winning.fxml")));
        m_winningScene =  new Scene(m_winningParent,1000,800);
        m_winningStage = new Stage();
        m_winningStage.initStyle(StageStyle.UNDECORATED);
        m_winningStage.setTitle("Eat the cherry");
        m_winningStage.getIcons().add(new Image("/Images/icon.png"));
        m_winningStage.setScene(m_winningScene);
        // start main screen and initialize the model and display it
        m_mainStage = stage;
        m_mainStage.getIcons().add(new Image("/Images/icon.png"));
        m_mainStage.initStyle(StageStyle.UNDECORATED);
        m_mainStage.setTitle("Eat the cherry");


        m_myModel = new MyModel();
        m_myModel.startServer();
        m_myViewModel = new MyViewModel(m_myModel);
        m_myModel.addObserver(m_myViewModel);

        playMainMusic();

        m_mainStage.initStyle(StageStyle.UNDECORATED);
        m_mainStage.setScene(m_myViewScene);

        m_mainStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });
        m_mainStage.show();
    }

    @Override
    public void init() throws Exception {

        super.init();
    }

    @Override
    public void stop() throws Exception {

        super.stop();
    }
    // close the screen and get back to the main manu
    public static void closeScreen()
    {
        m_mainStage.setScene(m_playViewScene);
        m_mainStage.show();
    }


    // lets us start a new game where we set everything from scratch and get back to the main menu just like the main function
    public static void newGame()
    {
        m_mainStage.hide();
        m_mainStage = new Stage();

        try
        {
            m_fxmlLoader = new FXMLLoader(Main.class.getResource("PlayView.fxml"));
            m_playViewParent = m_fxmlLoader.load();
            m_playView = m_fxmlLoader.getController();
            m_maze = null;
            m_playViewScene = new Scene(m_playViewParent,1000,700);
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        m_mainStage.setScene(m_playViewScene);
        m_mainStage.setTitle("Eat the cherry");
        m_mainStage.getIcons().add(new Image("/Images/icon.png"));
        m_mainStage.initStyle(StageStyle.DECORATED);
        m_mainStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        m_backgroudMediaPlayer.stop();
        playMainMusic();
        m_mainStage.show();
    }

    // this funciton terminates the game stops the servers and exits the game
    public static void exitGame()
    {
        m_myViewModel.m_model.stopServer();
        Platform.exit();
        System.exit(0);
    }


    public static void chooseCharacter() // function that plays the chooseCharacter screen music
    {
        m_chooseCharacterStage = new Stage();

        m_chooseCharacterStage.setScene(m_chooseCharacterScene);
        m_chooseCharacterStage.initStyle(StageStyle.UNDECORATED);
        m_chooseCharacterStage.initModality(Modality.APPLICATION_MODAL);

        m_chooseCharacterStage.getIcons().add(new Image("/Images/icon.png"));
        playChooseCharacterMusic();

        m_chooseCharacterStage.show();
    }

    public static void input()// activates the input for maze size screen
    {
        m_inputStage = new Stage();

        m_inputStage.setScene(m_inputScene);
        m_inputStage.initStyle(StageStyle.UNDECORATED);
        m_inputStage.initModality(Modality.APPLICATION_MODAL);

        m_inputStage.getIcons().add(new Image("/Images/icon.png"));

        m_inputStage.show();
        m_chooseCharacterMediaPlayer.stop();
    }

    public static void error()// activates the input error screen
    {
        m_errorStage = new Stage();

        m_errorStage.setScene(m_errorScene);
        m_errorStage.initStyle(StageStyle.UNDECORATED);
        m_errorStage.initModality(Modality.APPLICATION_MODAL);

        m_errorStage.getIcons().add(new Image("/Images/icon.png"));

        m_errorStage.show();
    }

    public static void playChooseCharacterMusic() // function that plays the chooseCharacter screen music
    {
        File file = new File("resources/Music/choose-Character.mp3");
        Media Song = new Media((file.toURI().toString()));

        m_chooseCharacterMediaPlayer = new MediaPlayer(Song);
        m_chooseCharacterMediaPlayer.setOnEndOfMedia(new Runnable()
        {
            @Override
            public void run()
            {
                m_chooseCharacterMediaPlayer.seek(Duration.ZERO);
                m_chooseCharacterMediaPlayer.play();
            }
        });

        m_chooseCharacterMediaPlayer.play();
    }

    public static void playMainMusic()// function that plays the main menu screen music
    {
        File file = new File("resources/Music/Main.mp3");
        Media Song = new Media((file.toURI().toString()));

        m_backgroudMediaPlayer = new MediaPlayer(Song);

        m_backgroudMediaPlayer.setOnEndOfMedia(new Runnable()
        {
            @Override
            public void run()
            {
                m_backgroudMediaPlayer.seek(Duration.ZERO);
                m_backgroudMediaPlayer.play();
            }
        });

        m_backgroudMediaPlayer.play();
    }

    public static void winning() // activates the winning screen
    {
        m_backgroudMediaPlayer.stop();

        File Wins = new File("resources/Music/Won.mp3");
        Media Song = new Media(Wins.toURI().toString());
        m_winningMediaPlayer = new MediaPlayer(Song);

        m_winningMediaPlayer.play();

        m_mainStage.hide();

        m_winningStage.show();
    }
}
