package view;

import controller.DotsGameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * Functions to set up various scenes of the GameGUI.
 * 
 * Classes Related To:
 *  -GameGUI.java
 *      -This class is a support class for GameGUI.java
 *  -LetterGameController.java
 *      -Used to read in and display information contained in the models, 
 *      which can be modified/accessed through LetterGameController.java.
 *      
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public final class SetUp {
    
    /** Background */
    static final String BACKGROUNDS[] = {"sky", "journey"};

    
    /** 
     * Login Screen Element Positions. 
     *
     * Position of the label prompting the subject
     * to enter her subject ID. */
    static final int LABEL_POSITION_X = 345;
    static final int LABEL_POSITION_Y = 230;
    
    /** Position of text field where subject will enter
     * her subject ID. */
    static final int ENTER_ID_POSITION_X = 325;
    static final int ENTER_ID_POSITION_Y = 250;
    
    /** Position of feedback telling subject to re-enter
     * their subject ID. */
    static final int FEEDBACK_POSITION_X = 335;
    static final int FEEDBACK_POSITION_Y = 330;
    
    /** Position of start button to start the trials. */
    static final int START_POSITION_X = 380;
    static final int START_POSITION_Y = 280;
    
    static int testX = 145;
    static int test = 80;
    /**
     * Game Screen. */
    static final int NUM_STARS = 100;
    /** Positions of the choices the subject can pick. */
    static final int LEFT_OPTION_X = 64;
    static final int LEFT_OPTION_Y = 80;
    static final int RIGHT_OPTION_X = 434;
    static final int RIGHT_OPTION_Y = 80;
    static final int OPTION_WIDTH = 300;
    static final int OPTION_HEIGHT = 450;
    static final int PROGRESS_BAR_X = 20;
    static final int PROGRESS_BAR_Y = 20;
    static final int GET_READY_X = 277;
    static final int GET_READY_Y = 230;
    static final int GET_READY_BAR_X = GET_READY_X - 27;
    static final int GET_READY_BAR_Y = GET_READY_Y + 70;
    static final int FIRST_STAR_X = 415;
    static final int STAR_Y = -35;
    static final int STAR_SHIFT = 30;
    static final double STAR_SCALE = .23;
    /** Font size of the letter options. */
    static final int LETTER_SIZE = 100;
    
    /**
     * Finish Screen Element Positions.
     */
    /** Position of message congratulating subject on completing
     * the experiment. */
    static final int CONGRATS_X = 203;
    static final int CONGRATS_Y = 170;
    /** Position of message with subject's score. */
    static final int SCORE_X = 183;
    static final int SCORE_Y = 200;
    
    /** Disable constructing of an object. */
    private SetUp() {
        
    }
    
    /**
     * Set up the login screen.
     * @param view The graphical user interface.
     * @param primaryStage The stage.
     * @return The login scene.
     */
    public static Scene setUpLoginScreen(GameGUI view, Stage primaryStage) {
        
        Label label = new Label("Enter your Subject ID");
        label.setLayoutY(LABEL_POSITION_Y);
        label.setLayoutX(LABEL_POSITION_X);
        view.setStart(new Button("Start"));
        view.getEnterId().setLayoutY(ENTER_ID_POSITION_Y);
        view.getEnterId().setLayoutX(ENTER_ID_POSITION_X);
        view.getEnterId().setAlignment(Pos.CENTER);
        view.setFeedback(new Label());
        view.getFeedback().setLayoutY(FEEDBACK_POSITION_Y);
        view.getFeedback().setLayoutX(FEEDBACK_POSITION_X);
        view.setLayout(new AnchorPane());
        
        view.getLayout().getChildren().addAll(
                label, view.getStart(), view.getEnterId(), view.getFeedback());
        view.getStart().setLayoutY(START_POSITION_Y);
        view.getStart().setLayoutX(START_POSITION_X);
        
        Scene scene = new Scene(view.getLayout(), 
                GameGUI.SCREEN_WIDTH, GameGUI.SCREEN_HEIGHT);

        setBackground(view.getLayout(), 0);
        
        
        
        return scene;
    }
    
    /**
     * Set up the game screen where subject will undergo trials.
     * @param view The graphical user interface.
     * @param primaryStage The stage.
     * @param subjectID The subject's ID.
     * @return The game scene.
     */
    public static Scene setUpGameScreen(GameGUI view, 
            Stage primaryStage, String subjectID, DotsGameController dgc) {
        
        view.setLayout(new AnchorPane());
        dgc.getThePlayer().setSubjectID(Integer.parseInt(subjectID));
        System.out.println(dgc.getThePlayer().getSubjectID());
        setUpOptions(view);
        initialButtonSetUp(view);
        
        view.setProgressBar(new ProgressBar(0.0));
        view.getProgressBar().setLayoutX(PROGRESS_BAR_X);
        view.getProgressBar().setLayoutY(PROGRESS_BAR_Y);
        view.getProgressBar().getTransforms().setAll(
                new Rotate(-90, 0, 0),
                new Translate(-100, 0));
        
        view.setGetReadyBar(new ProgressBar(0.0));
        view.getGetReadyBar().setLayoutX(GET_READY_BAR_X);
        view.getGetReadyBar().setLayoutY(GET_READY_BAR_Y);
        view.getGetReadyBar().setPrefWidth(300.0);
        view.getGetReadyBar().setStyle("-fx-accent: green;");
        
        view.setGetReady(new Label("Get Ready!"));
        view.getGetReady().setLayoutX(GET_READY_X);
        view.getGetReady().setLayoutY(GET_READY_Y);
        view.getGetReady().setFont(new Font("Tahoma", 50));
        
        setStars(view, view.getLayout());
        
        view.getLayout().getChildren().addAll(view.getGetReadyBar(), view.getGetReady(), view.getProgressBar(), view.getLeftOption(), view.getRightOption());
        setBackground(view.getLayout(), 0);
        //primaryStage.setFullScreen(true);
        return new Scene(view.getLayout(), GameGUI.SCREEN_WIDTH, GameGUI.SCREEN_HEIGHT);
    }
    
    private static void setStars(GameGUI view, AnchorPane layout) {
        
        Image stars[] = new Image[NUM_STARS];
        view.setStarNodes(new ImageView[NUM_STARS]);
        
        for (int i = 0; i < NUM_STARS; i++) {
            stars[i] = new Image("/res/images/star2.png");
            view.getStarNodes()[i] = new ImageView(stars[i]);
            view.getStarNodes()[i].setScaleX(STAR_SCALE);
            view.getStarNodes()[i].setScaleY(STAR_SCALE);
            view.getStarNodes()[i].setLayoutY(STAR_Y);
            view.getStarNodes()[i].setLayoutX(FIRST_STAR_X - (i * STAR_SHIFT));
            view.getStarNodes()[i].setVisible(false);
            layout.getChildren().add(view.getStarNodes()[i]);
        }            
    }

    /**
     * Set up the positioning of the two options.
     * @param view The graphical user interface.
     */
    static void setUpOptions(GameGUI view) {
        //Create buttons and set text
        view.setLeftOption(new Canvas(OPTION_WIDTH, OPTION_HEIGHT));
        view.setRightOption(new Canvas(OPTION_WIDTH, OPTION_HEIGHT));

      //Set absolute positions of each leftOption
        view.getLeftOption().setLayoutX(LEFT_OPTION_X);
        view.getLeftOption().setLayoutY(LEFT_OPTION_Y);
        view.getRightOption().setLayoutX(RIGHT_OPTION_X);
        view.getRightOption().setLayoutY(RIGHT_OPTION_Y);
    }
    
    /**
     * Setup the style of the two options.
     * @param view The graphical user interface.
     */
    public static void initialButtonSetUp(GameGUI view) {
        view.getLeftOption().setStyle("-fx-background-color: black;");
        view.getRightOption().setStyle("-fx-background-color: black;");
    }

    /**
     * Set up the finish screen.
     * @param view The graphical user interface.
     * @param primaryStage The stage.
     * @return The finishing scene.
     */
    public static Scene setUpFinishScreen(GameGUI view, Stage primaryStage, DotsGameController dgc) {
        
        AnchorPane layout = new AnchorPane();
        
        Label score = new Label();
        score.setText("You scored " 
                + dgc.getThePlayer().getNumCorrect() + " points!");
        view.setCongratulations(new Label("You did it!"));
        view.getCongratulations().setFont(Font.font("Verdana", 20));
        score.setFont(Font.font("Tahoma", 16));
        
        view.getCongratulations().setLayoutX(CONGRATS_X);
        view.getCongratulations().setLayoutY(CONGRATS_Y);
        
        score.setLayoutX(SCORE_X);
        score.setLayoutY(SCORE_Y);
        
        layout.getChildren().addAll(view.getCongratulations(), score);
        
        setBackground(layout, 0);
        
        return new Scene(layout, GameGUI.SCREEN_WIDTH, GameGUI.SCREEN_HEIGHT);
    }
    
    /**
     * Set the background.
     * @param view The graphical user interface.
     * @param layout The layout.
     */
    public static void setBackground(AnchorPane layout, int level) {
        
        String backgroundName = BACKGROUNDS[level];
        
        BackgroundImage bg = new BackgroundImage(
                new Image(
                        "/res/images/" + backgroundName + ".png", 
                        GameGUI.SCREEN_WIDTH,
                        GameGUI.SCREEN_HEIGHT, 
                        false, true),
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        layout.setBackground(new Background(bg));
    }
    
}
