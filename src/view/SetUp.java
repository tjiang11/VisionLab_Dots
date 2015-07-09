package view;

import controller.DotsGameController;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
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

    /** Width and height of the computer's screen */
    static final Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
    static final double SCREEN_WIDTH = primaryScreenBounds.getWidth();
    static final double SCREEN_HEIGHT = primaryScreenBounds.getHeight();
    
    /**
     * Game Screen. */
    static final int NUM_STARS = 100;
    /** Positions of the choices the subject can pick. */
    static final int LEFT_OPTION_X = (int) (SCREEN_WIDTH * .15);
    static final int LEFT_OPTION_Y = (int) (SCREEN_HEIGHT * .15);
    public static final int OPTION_WIDTH = (int) (SCREEN_WIDTH * .3);
    public static final int OPTION_HEIGHT = (int) (SCREEN_HEIGHT * .7);
    static final int RIGHT_OPTION_X = (int) (SCREEN_WIDTH - LEFT_OPTION_X - OPTION_WIDTH);
    static final int RIGHT_OPTION_Y = LEFT_OPTION_Y;
    static final int PROGRESS_BAR_X = (int) (SCREEN_WIDTH * .02);
    static final int PROGRESS_BAR_Y = (int) (SCREEN_HEIGHT * .05);
    static final int FIRST_STAR_X = (int) (SCREEN_WIDTH * .93);
    static final int STAR_Y = -25;
    static final int STAR_SHIFT = 35;
    static final double STAR_SCALE = .28;
    /** Font size of the letter options. */
    static final int LETTER_SIZE = 100;
    
    /**
     * Finish Screen Element Positions.
     */
    /** Position of message congratulating subject on completing
     * the experiment. */
    static final int CONGRATS_X = 348;
    static final int CONGRATS_Y = 270;
    /** Position of message with subject's score. */
    static final int SCORE_X = 328;
    static final int SCORE_Y = 300;
    
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

        view.setStart(new Button("Start"));
        view.setEnterId(new TextField());
        view.setFeedback(new Label());

        view.setLayout(new AnchorPane());

        view.getEnterId().setAlignment(Pos.CENTER);
        view.setLoginBox(new VBox(5));
        view.getLoginBox().setAlignment(Pos.CENTER);
        view.getLoginBox().getChildren().addAll(label, view.getEnterId(), view.getStart(), view.getFeedback());

        view.getLayout().getChildren().add(view.getLoginBox());
        
        Scene scene = new Scene(view.getLayout(), 
                SCREEN_WIDTH, SCREEN_HEIGHT);

        view.getEnterId().requestFocus();
        
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
        view.getProgressBar().setScaleY(2.0);
        view.getProgressBar().setLayoutX(PROGRESS_BAR_X);
        view.getProgressBar().setLayoutY(PROGRESS_BAR_Y);
        view.getProgressBar().getTransforms().setAll(
                new Rotate(-90, 0, 0),
                new Translate(-100, 0));
        
        view.setGetReadyBar(new ProgressBar(0.0));
        view.getGetReadyBar().setPrefWidth(300.0);
        view.getGetReadyBar().setStyle("-fx-accent: green;");
        
        view.setGetReady(new Label("Get Ready!"));
        view.getGetReady().setFont(new Font("Tahoma", 50));
        
        view.setGetReadyBox(new VBox(10));
        view.getGetReadyBox().setAlignment(Pos.CENTER);
        view.getGetReadyBox().getChildren().addAll(view.getGetReady(), view.getGetReadyBar());
        
        setStars(view, view.getLayout());
        
        view.getLayout().getChildren().addAll(view.getGetReadyBox(), view.getProgressBar(), view.getLeftOption(), view.getRightOption());
        setBackground(view.getLayout(), 0);
        return new Scene(view.getLayout(), SCREEN_WIDTH, SCREEN_HEIGHT);
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
        
        view.setFinishMessage(new VBox(6));
        view.getFinishMessage().getChildren().addAll(view.getCongratulations(), score);
        view.getFinishMessage().setAlignment(Pos.CENTER);
        
        layout.getChildren().addAll(view.getFinishMessage());
        
        setBackground(layout, 0);
        
        return new Scene(layout, SCREEN_WIDTH, SCREEN_HEIGHT);
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
                        SCREEN_WIDTH,
                        SCREEN_HEIGHT, 
                        false, true),
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        layout.setBackground(new Background(bg));
    }
    
}
