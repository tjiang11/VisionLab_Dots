package view;

import controller.DotsGameController;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    
    /** Controller for setting event handlers */
    private DotsGameController DGC;

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
    /** Login Box to contain start button, feedback label, and enterId TextField. */
    private VBox loginBox;
    
    /** Instructions Screen - Next button */
    private Button next;
    
    /** Practice Complete screen */
    /** Label informing user has complete the practice trials. */
    private Text practiceComplete;
    /** Button to begin actual assessment. */
    private Button startAssessment;
    
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
    private VBox getReadyBox;
    /** Game Screen - Stars */
    private ImageView starNodes[];
    /** Game Screen - Practice label */
    private Label practice;
    
    /** End Screen - message informing the user has finished. */
    private Label congratulations;
    
    private VBox finishMessage;

    /** 
     * Constructor for the user interface. Sets the stage
     * and login screen.
     * @param stage The user interface stage.
     * @throws IOException 
     */
    public GameGUI(Stage stage) {
        
        this.setPrimaryStage(stage);
        
        this.setLoginScreen();
    }
    
    /**
     * Sets the login screen where user will input their informmation.
     * @param stage The user interface stage.
     * @throws IOException
     */
    private void setLoginScreen() {
        this.primaryStage.setTitle("Game");
        this.enterId = new TextField();
        
        Scene loginScene = SetUp.setUpLoginScreen(this, this.primaryStage);
        this.scene = loginScene;
        
        DGC = new DotsGameController(this);
        DGC.setLoginHandlers();
        
        this.primaryStage.setResizable(false);
        
        this.primaryStage.sizeToScene();
        
        this.primaryStage.setScene(this.scene);

        this.primaryStage.show(); 
        
        System.out.println("Width: " + this.getLoginBox().getHeight());
        
        this.getLoginBox().setLayoutY(SetUp.SCREEN_HEIGHT / 2 - this.getLoginBox().getHeight());
        this.getLoginBox().setLayoutX((SetUp.SCREEN_WIDTH / 2) - (this.getLoginBox().getWidth() / 2));
        
        this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.primaryStage.setFullScreen(true);
    }
    
    /**
     * Sets the screen where instructions are shown.
     */
    public void setInstructionsScreen() {
        Scene instructionsScene = SetUp.setUpInstructionsScreen(this, this.primaryStage);
        this.primaryStage.setScene(instructionsScene);
        this.primaryStage.setFullScreen(true);
        this.getNext().setLayoutX(this.getNext().getLayoutX() - this.getNext().getWidth() / 2);
        this.DGC.setInstructionsHandlers();
    }
    
    /**
     * Sets the screen whre user has finished practice trials and is about to begin assessment.
     */
    public void setPracticeCompleteScreen() {
        Scene practiceCompleteScene = SetUp.setUpPracticeCompleteScreen(this);
        this.primaryStage.setScene(practiceCompleteScene);
        this.primaryStage.setFullScreen(true);
        this.DGC.setPracticeCompleteHandlers();
        this.getPracticeComplete().setLayoutY(SetUp.SCREEN_HEIGHT * .4);
        this.getPracticeComplete().setLayoutX(SetUp.SCREEN_WIDTH / 2 - this.getPracticeComplete().getWrappingWidth() / 2);
        this.getStartAssessment().setLayoutY(SetUp.SCREEN_HEIGHT * .6);
        this.getStartAssessment().setLayoutX(SetUp.SCREEN_WIDTH / 2 - this.getStartAssessment().getWidth() / 2);
    }
    
    /**
     * Sets the game screen where subject will be presented with two letters.
     * @param stage The user interface stage.
     * @param subjectID The subject's ID number.
     */
    public void setGameScreen() {
        try {
            Scene gameScene = SetUp.setUpGameScreen(
                    this, this.primaryStage);  
            
            this.scene = gameScene;
            this.primaryStage.setScene(this.scene);
            
            System.out.println(this.getReadyBar.getWidth());
            this.getGetReadyBox().setLayoutY((SetUp.SCREEN_HEIGHT / 2) - this.getGetReadyBox().getHeight());
            this.getGetReadyBox().setLayoutX((SetUp.SCREEN_WIDTH / 2) - (this.getGetReadyBox().getWidth() / 2));
            
            this.getReadyBar.setLayoutX((SetUp.SCREEN_WIDTH / 2) - (this.getReady.getWidth() / 2));
            
            this.getPractice().setLayoutX((SetUp.SCREEN_WIDTH / 2) - (this.practice.getWidth() / 2));
            this.getPractice().setLayoutY(SetUp.SCREEN_HEIGHT * .06);
            
            this.primaryStage.setFullScreen(true);
            
            this.DGC.prepareFirstRound();
            
            if (PROGRESS_DRAIN) { this.DGC.beginProgressBarDrainage(); }
            this.DGC.setGameHandlers();
            
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
    public void setFinishScreen(DotsGameController lgc) {
        Scene finishScene = SetUp.setUpFinishScreen(this, this.primaryStage, lgc);
        this.scene = finishScene;
        this.primaryStage.setScene(this.scene);

        this.getFinishMessage().setLayoutX((SetUp.SCREEN_WIDTH / 2) - (this.getFinishMessage().getWidth() / 2));
        this.getFinishMessage().setLayoutY((SetUp.SCREEN_HEIGHT / 2) - this.getFinishMessage().getHeight());
        
        this.primaryStage.setFullScreen(true);
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

    public VBox getLoginBox() {
        return loginBox;
    }

    public void setLoginBox(VBox loginBox) {
        this.loginBox = loginBox;
    }

    public VBox getGetReadyBox() {
        return getReadyBox;
    }

    public void setGetReadyBox(VBox getReadyBox) {
        this.getReadyBox = getReadyBox;
    }
    
    
    public VBox getFinishMessage() {
        return finishMessage;
    }

    public void setFinishMessage(VBox finishMessage) {
        this.finishMessage = finishMessage;
    }

    public Button getNext() {
        return next;
    }

    public void setNext(Button next) {
        this.next = next;
    }

    public Text getPracticeComplete() {
        return practiceComplete;
    }

    public void setPracticeComplete(Text practiceComplete) {
        this.practiceComplete = practiceComplete;
    }

    public Button getStartAssessment() {
        return startAssessment;
    }

    public void setStartAssessment(Button startAssessment) {
        this.startAssessment = startAssessment;
    }

    public Label getPractice() {
        return practice;
    }

    public void setPractice(Label practice) {
        this.practice = practice;
    }
}
