package view;

import controller.DotsGameController;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * The graphical user interface of the game.
 * 
 * Classes Related to:
 *  -SetUp.java (utility/support)
 *      -SetUp is a utility class that contains functions that support the setup of GameGUI.
 *  -DotsGameController.java
 *      -The controller is created in this class. As the game progresses, the controller 
 *      adopts to the scenes of the GameGUI and sets their event handlers appropriately.
 *  
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class GameGUI {
    
    /** Whether or not to slowly drain the progress bar as time pressure. */
    static final boolean PROGRESS_DRAIN = false;
    
    
    /** Width of the game window. */
    static final int SCREEN_WIDTH = 800;
    /** Height of the game window. */
    static final int SCREEN_HEIGHT = 600;
    
    /** Controller for setting event handlers */
    private DotsGameController LGC;

    /** The JavaFX stage for the game. */
    private Stage primaryStage;
    /** The current scene of the game. */
    private Scene scene;
    /** The pane of the game */
    private AnchorPane layout;
    
    /** Login Screen - start button. */
    private Button start;
    /** Login Screen - feedback to tell if user needs to
     * reinput their ID. */
    private Label feedback;
    /** Login Screen - Text field for user to enter their 
     * Subject ID. */
    private TextField enterId;
    
    /** Game Screen - The left choice. */
    private Canvas leftOption;
    /** Game Screen - The right choice. */
    private Canvas rightOption;
    /** Game Screen - Progress Bar. */
    private ProgressBar progressBar;
    /** Game Screen - Get Ready */
    private Label getReady;
    /** Game Screen - Get Ready Bar */
    private ProgressBar getReadyBar;
    /** Game Screen - Stars */
    private ImageView starNodes[];
    
    /** End Screen - message informing the user has finished. */
    private Label congratulations;
    
    /** 
     * Constructor for the user interface. Sets the stage
     * and login screen.
     * @param stage The user interface stage.
     * @throws IOException 
     */
    public GameGUI(Stage stage) {
        
        this.setPrimaryStage(stage);
        
        this.setLoginScreen(stage);
    }
    
    /**
     * Sets the login screen where user will input their informmation.
     * @param stage The user interface stage.
     * @throws IOException
     */
    private void setLoginScreen(Stage stage) {
        this.primaryStage.setTitle("Game");
        this.enterId = new TextField();
        
        Scene loginScene = SetUp.setUpLoginScreen(this, this.primaryStage);
        this.scene = loginScene;
        
        LGC = new DotsGameController(this);
        LGC.setLoginHandlers();

        this.enterId.requestFocus();
        
        this.primaryStage.setResizable(false);
        

        
        this.primaryStage.sizeToScene();
        this.primaryStage.setScene(this.scene);
        
        
        this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//        
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        this.primaryStage.setX(primaryScreenBounds.getMinX());
//        this.primaryStage.setY(primaryScreenBounds.getMinY());
//        this.primaryStage.setWidth(primaryScreenBounds.getWidth());
//        this.primaryStage.setHeight(primaryScreenBounds.getHeight());
        
        this.primaryStage.show();  
//        this.primaryStage.setFullScreen(true);
    }
    
    /**
     * Sets the game screen where subject will be presented with two letters.
     * @param stage The user interface stage.
     * @param subjectID The subject's ID number.
     */
    public void setGameScreen(Stage stage, String subjectID, DotsGameController lgc) {
        try {
            Scene gameScene = SetUp.setUpGameScreen(
                    this, stage, subjectID, lgc);  
            
            this.scene = gameScene;
            
            this.primaryStage.setScene(this.scene);
//            this.primaryStage.setFullScreen(true);
            
            this.LGC.prepareFirstRound();
            
            /** Set event handlers for gameplay */
            
            if (PROGRESS_DRAIN) { this.LGC.beginProgressBarDrainage(); }
            this.LGC.setGameHandlers();
            
        } catch (NumberFormatException e) {
            System.out.println("Oops!");
            this.enterId.setText("");
            this.enterId.requestFocus();
            this.feedback.setText("That's not your ID, silly!");
        }
    }
    
    /** 
     * Sets the ending screen informing the subject of their completion.
     * @param stage The user interface stage.
     */
    public void setFinishScreen(Stage stage, DotsGameController lgc) {
        Scene finishScene = SetUp.setUpFinishScreen(this, stage, lgc);
        this.scene = finishScene;
        this.primaryStage.setScene(this.scene);
    }
    
    /**
     * Change the background in real time.
     */
    public void changeBackground(int level) { 
        SetUp.setBackground(this.layout, level);
        this.scene.setRoot(this.layout);
    }
    
    public Scene getScene() {
        return this.scene;
    }
    
    public void setScene(Scene s) {
        this.scene = s;
    }
    
    public Canvas getLeftOption() {
        return this.leftOption;
    }
    
    public void setLeftOption(Canvas c) {
        this.leftOption = c;
    }
    
    public void setRightOption(Canvas c) {
        this.rightOption = c;
    }
    
    public Canvas getRightOption() {
        return this.rightOption;
    }

    public Button getStart() {
        return start;
    }

    public void setStart(Button start) {
        this.start = start;
    }
    
    public void setEnterId(TextField t) {
        this.enterId = t;
    }
    
    public TextField getEnterId() {
        return this.enterId;
    }

    public Label getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Label feedback) {
        this.feedback = feedback;
    }

    public Label getCongratulations() {
        return this.congratulations;
    }

    public void setCongratulations(Label congratulations) {
        this.congratulations = congratulations;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Label getGetReady() {
        return getReady;
    }

    public void setGetReady(Label getReady) {
        this.getReady = getReady;
    }

    public ProgressBar getGetReadyBar() {
        return getReadyBar;
    }

    public void setGetReadyBar(ProgressBar getReadyBar) {
        this.getReadyBar = getReadyBar;
    }

    public AnchorPane getLayout() {
        return layout;
    }

    public void setLayout(AnchorPane layout) {
        this.layout = layout;
    }

    public ImageView[] getStarNodes() {
        return starNodes;
    }

    public void setStarNodes(ImageView starNodes[]) {
        this.starNodes = starNodes;
    }

}
