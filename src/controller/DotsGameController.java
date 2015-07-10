package controller;

import java.net.URL;

import config.Config;
import model.DotSet;
import model.DotsPair;
import model.DotsPairGenerator;
import model.GameLogic;
import model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import view.GameGUI;

/**
 * 
 * The center of the program; interface between the
 * models and the view. 
 * 
 * Classes Related to:
 *  -GameGUI.java (view)
 *      -Updates elements of the GUI as the game progresses and responds.
 *  -DotsPairGenerator.java (model)
 *      -Calls on DotsPairGenerator to generate new DotsPairs.
 *  -DotsPair.java (model)
 *      -LetterGameController keeps track of the most recent DotsPair created in variable currentDotsPair.
 *  -Player.java (model)
 *      -Updates Player information as the game progresses and responds.
 *  -GameLogic.java (model)
 *      -Calls on GameLogic to evaluate the correctness of a response from the subject.
 *  -DataWriter.java
 *      -Passes information (Player and DotsPair) to DataWriter to be exported.
 *      
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class DotsGameController implements GameController {
    
    final static Color CANVAS_COLOR = Color.BEIGE;
    final static Color DOT_COLOR = Color.GREEN;
    
    /** Punish for wrong answers */
    static final boolean PUNISH = true;
    
    /** Time in milliseconds for the player to get ready after pressing start */
    final static int GET_READY_TIME = 2000;
    
    /** Time between rounds in milliseconds. */
    static int TIME_BETWEEN_ROUNDS;
    
    /** Time in milliseconds that the DotSets flash */
    static int FLASH_TIME;
    
    /** DataWriter to export data to CSV. */
    private DataWriter dataWriter;
    
    /** DotsPairGenerator to generate an DotsPair */
    private DotsPairGenerator dpg;
    /** The graphical user interface. */
    private GameGUI theView;
    /** The current scene. */
    private Scene theScene;
    /** Left Canvas Graphics Context */
    private GraphicsContext gcLeft;
    /** Right Canvas Graphics Context */
    private GraphicsContext gcRight;
    
    /** The subject. */
    private Player thePlayer;
    /** The current DotsPair being evaluated by the subject. */
    private DotsPair currentDotsPair;
    
    /** Used to measure response time. */
    private static long responseTimeMetric;
    
    /** Current state of the game. */
    public static CurrentState state;
    
    private Object lock = new Object();
    
    /** Whether or not the user has provided feedback. This variable is used to support concurrency--Flash time
     * should not overlap with Time between rounds.
     */
    private static boolean feedback_given;
    
    /** Alternate reference to "this" to be used in inner methods */
    private DotsGameController gameController;
    
    /** 
     * Constructor for the controller. There is only meant
     * to be one instance of the controller. Attaches listener
     * for when user provides response during trials. On a response,
     * prepare the next round and record the data.
     * @param view The graphical user interface.
     */
    public DotsGameController(GameGUI view) {
        
        loadConfig();
        
        this.gameController = this;
        this.dpg = new DotsPairGenerator();
        this.currentDotsPair = null;
        this.theView = view;
        this.theScene = view.getScene();
        this.thePlayer = new Player();
        this.dataWriter = new DataWriter(this);

    }
    
    /** 
     * Load configuration settings. 
     */
    private void loadConfig() {
        new Config();
        FLASH_TIME = Config.getPropertyInt("flash.time");
        TIME_BETWEEN_ROUNDS = Config.getPropertyInt("time.between.rounds");
    }
    
    /**
     * Sets event listener for when subject clicks the start button OR presses Enter.
     * Pass in the subject's ID number entered.
     */
    public void setLoginHandlers() {
        
        this.theScene = theView.getScene();
        
        this.theView.getStart().setOnAction(e -> theView.setGameScreen(
                theView.getPrimaryStage(), theView.getEnterId().getText(), this));

        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    theView.setGameScreen(
                            theView.getPrimaryStage(), theView.getEnterId().getText(), gameController);
                }
            }
        });
    }
    
    /** 
     * Sets event listener for when subject presses 'F' or 'J' key
     * during a round. 
     */
    public void setGameHandlers() {
        this.theScene = theView.getScene();
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if ((event.getCode() == KeyCode.F || event.getCode() == KeyCode.J) 
                        && !feedback_given) {
                    
                    feedback_given = true;

                    if (state == CurrentState.WAITING_FOR_RESPONSE) {
                        state = CurrentState.WAITING_BETWEEN_ROUNDS;
                    }
                    
                    /** Update models and view appropriately according to correctness
                     * of subject's response.
                     */
                    gameController.responseAndUpdate(event, theView);
                    
                    /** Prepare the next round */
                    gameController.prepareNextRound(); 
                    
                    /** Export data to CSV file in folder 'results/<subject_id>' */
                    dataWriter.writeToCSV();
                    
                    /** Set difficulty of DotsPairGenerator */
                    gameController.dpg.setDifficulty();       
                }
                /**
                 * If subject has completed the total number of rounds specified,
                 * then change the scene to the finish screen.
                 */
                if (thePlayer.getNumRounds() >= NUM_ROUNDS) {
                    state = CurrentState.FINISHED;
                    System.out.println("Done");
                    theView.setFinishScreen(theView.getPrimaryStage(), gameController);
                }
            }
        });
    }  
    
    /**
     * Update models and view appropriately according to correctness
     * of subject's response.  
     * @param e The key event to check which key the user pressed.
     * @param dotsPair The current DotsPair being evaluated.
     * @param currentPlayer The subject.
     * @param pb the ProgressBar to update.
     * @return True if the player is correct. False otherwise.
     */
    public void responseAndUpdate (
            KeyEvent e, GameGUI view) {
        boolean correct;
        DotsPair dotsPair = this.currentDotsPair;
        Player currentPlayer = this.thePlayer;
        URL feedbackSoundFileUrl = null;
        
        correct = GameLogic.checkAnswerCorrect(e, dotsPair);
        
        this.updateProgressBar(view, correct);
        
        this.updatePlayer(currentPlayer, correct);   
        
        this.feedbackSound(feedbackSoundFileUrl, correct); 
        
        this.dataWriter.grabData(this);
    }
    
    /** Update the player appropriately.
     * 
     * @param currentPlayer The current player.
     * @param correct True if subject's reponse is correct. False otherwise.
     */
    private void updatePlayer(Player currentPlayer, boolean correct) {
        if (correct) {
            currentPlayer.addPoint();
            currentPlayer.setRight(true);
        } else {
            currentPlayer.setRight(false);
        }
        currentPlayer.incrementNumRounds();
    }
    
    /**
     * Update the progressbar. Resets to zero if progress bar is full.
     * @param pb The view's progress bar.
     */
    /**
     * Update the progressbar. Resets to zero if progress bar is full.
     * @param pb The view's progress bar.
     */
    private void updateProgressBar(GameGUI view, boolean correct) {
        if (correct) {
            if (view.getProgressBar().isIndeterminate()) {
                view.getProgressBar().setProgress(0.0);
                view.getProgressBar().setStyle("-fx-accent: #0094C5;");
            }
            view.getProgressBar().setProgress(view.getProgressBar().getProgress() + .25);
            if (view.getProgressBar().getProgress() >= 1.00) {
                view.getProgressBar().setProgress(0.25);
                
                URL powerUpSound = getClass().getResource("/res/sounds/Powerup.wav");
                new AudioClip(powerUpSound.toString()).play();
                
                int starToReveal = this.thePlayer.getNumStars();
                view.getStarNodes()[starToReveal].setVisible(true);
                this.thePlayer.incrementNumStars();
                
                if (this.thePlayer.getNumStars() > 2) {
                    view.changeBackground(1);
                }
            }
        } else {
            view.getProgressBar().setStyle("-fx-accent: #0094C5;");
            if (PUNISH) {
                view.getProgressBar().setProgress(view.getProgressBar().getProgress() - .125);
                if (view.getProgressBar().isIndeterminate()) {
                    view.getProgressBar().setStyle("-fx-accent: red;");
                    
                }
            }
        }
    }
    
    /** If user inputs correct answer play positive feedback sound,
     * if not then play negative feedback sound.
     * @param feedbackSoundFileUrl the File Url of the Sound to be played.
     * @param correct whether the subject answered correctly or not.
     */
    private void feedbackSound(URL feedbackSoundFileUrl, boolean correct) {
        if (correct) {
            feedbackSoundFileUrl = 
                    getClass().getResource("/res/sounds/Ping.aiff");
        } else {
            feedbackSoundFileUrl = 
                    getClass().getResource("/res/sounds/Basso.aiff");
        }
        new AudioClip(feedbackSoundFileUrl.toString()).play();
    }
    
    /**
     * Prepare the first round by making a load bar to 
     * let the subject prepare for the first question.
     * 
     * Also sets up the canvases on which the dots will be painted.
     */
    public void prepareFirstRound() {
//        state = null;
        feedback_given = false;
        Task<Void> sleeper = new Task<Void>() {   
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < GET_READY_TIME; i++) {
                    this.updateProgress(i, GET_READY_TIME); 
                    Thread.sleep(1);
                }
                return null;
            }
        };
        theView.getGetReadyBar().progressProperty().bind(sleeper.progressProperty());
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                
                gcLeft = theView.getLeftOption().getGraphicsContext2D();
                gcRight = theView.getRightOption().getGraphicsContext2D();
                
                gcLeft.setFill(CANVAS_COLOR);
                gcLeft.fillRect(0,0,theView.getLeftOption().getWidth(),theView.getLeftOption().getHeight());
                gcLeft.setFill(DOT_COLOR);
                
                gcRight.setFill(CANVAS_COLOR);
                gcRight.fillRect(0,0,theView.getRightOption().getWidth(),theView.getRightOption().getHeight());
                gcRight.setFill(DOT_COLOR);
                
                setOptions();

                responseTimeMetric = System.nanoTime();
                theView.getGetReadyBox().setVisible(false);
            }
        });
        Thread sleeperThread = new Thread(sleeper);
        sleeperThread.start();
    }
    
    /**
     * Prepares the next round by recording reponse time,
     * clearing the previous round, waiting, and creating the next round.
     */
    public void prepareNextRound() {
        recordResponseTime();
        //clearRound();
        waitBeforeNextRoundAndUpdate(TIME_BETWEEN_ROUNDS);
    }
  
    /**
     * Clears the options.
     */
    public void clearRound() {
        gcLeft.setFill(CANVAS_COLOR);
        gcLeft.fillRect(0, 0, theView.getLeftOption().getWidth(),theView.getLeftOption().getHeight());
        gcLeft.setFill(DOT_COLOR);
        
        gcRight.setFill(CANVAS_COLOR);
        gcRight.fillRect(0, 0, theView.getRightOption().getWidth(),theView.getRightOption().getHeight());
        gcRight.setFill(DOT_COLOR);
    }

    /**
     * Wait for a certain time and then set the next round.
     */
    public void waitBeforeNextRoundAndUpdate(int waitTime) {
        
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int i = 0;
                while (i < waitTime) {
                    //Synchronized to disallow the changing of the state.
                    synchronized (lock) {
                        if (state == CurrentState.WAITING_BETWEEN_ROUNDS) {
                            this.updateProgress(i, waitTime); 
                            Thread.sleep(1);
                            i++;
                        }
                    }
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                setOptions();
                responseTimeMetric = System.nanoTime();
                state = null;
            }
        });
        Thread sleeperThread = new Thread(sleeper);
        sleeperThread.start();
    }

    /**
     * Set the next round's choices.
     */
    public void setOptions() {
        DotSet dotSetOne, dotSetTwo;
        
        dpg.getNewDifficultyPair();
        this.currentDotsPair = dpg.getDotsPair();
        
        dotSetOne = this.currentDotsPair.getDotSetOne();
        dotSetTwo = this.currentDotsPair.getDotSetTwo();
        this.flash();
        
        this.paintDotSet(dotSetOne, gcLeft);
        this.paintDotSet(dotSetTwo, gcRight);
        feedback_given = false;
    }
    
    private void flash() { 
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected java.lang.Void call() throws Exception {
                Thread.sleep(FLASH_TIME);
                return null;
            }    
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                gameController.clearRound();    
                if (feedback_given) {
                    DotsGameController.state = CurrentState.WAITING_BETWEEN_ROUNDS;
                } else {
                    DotsGameController.state = CurrentState.WAITING_FOR_RESPONSE;
                }
            }
        });
        new Thread(sleeper).start();
    }

    /**
     * Paint the dots.
     * @param 
     */
    private void paintDotSet(DotSet dotSet, GraphicsContext graphicsContext) {
        for (int i = 0; i < dotSet.getTotalNumDots(); i++) {
            
            int x = dotSet.getPositions().get(i).x;
            int y = dotSet.getPositions().get(i).y;

            graphicsContext.fillOval(x, y, 
                    dotSet.getDiameters().get(i), 
                    dotSet.getDiameters().get(i));
        }
    }
   

    /** 
     * Record the response time of the subject. 
     */
    public void recordResponseTime() {
        long responseTime = System.nanoTime() - responseTimeMetric;
        thePlayer.setResponseTime(responseTime);
        
        //Convert from nanoseconds to seconds.
        double responseTimeSec = responseTime / 1000000000.0;
        System.out.println("Your response time was: " 
                + responseTimeSec + " seconds");
    }
    
    /**
     * Slowly drains the progress bar to encourage the user not to spend too much time thinking.
     */
    public void beginProgressBarDrainage() {
        theView.getProgressBar().setProgress(0.6);
        
        Timeline drainer = new Timeline(
                new KeyFrame(Duration.seconds(0), evt -> {
                    if (gameController.theView.getProgressBar().getProgress() > 1.0) {
                        gameController.theView.getProgressBar().setStyle("-fx-accent: #00CC00;");
                    } else {
                        gameController.theView.getProgressBar().setStyle("-fx-accent: #0094C5;");
                    }
                    if (gameController.theView.getProgressBar().getProgress() > .005) {
                        gameController.theView.getProgressBar().setProgress(theView.getProgressBar().getProgress() - .0035);
                    }
                }), new KeyFrame(Duration.seconds(0.065)));
        drainer.setCycleCount(Animation.INDEFINITE);
        drainer.play();
    }
    
    /**
     * Reorients the Controller to the current scene and reorients the DataWriter to current data (Player and DotsPair)
     * @param theView The graphical user interface.
     */
    public void grabSetting(GameGUI theView) {
        this.theScene = theView.getScene();
    }

    public Player getThePlayer() {
        return thePlayer;
    }

    public void setThePlayer(Player thePlayer) {
        this.thePlayer = thePlayer;
    }

    public DotsPair getCurrentDotsPair() {
        return currentDotsPair;
    }

    public void setCurrentDotsPair(DotsPair currentDotsPair) {
        this.currentDotsPair = currentDotsPair;
    }

    public DotsPairGenerator getApg() {
        return dpg;
    }

    public void setApg(DotsPairGenerator dpg) {
        this.dpg = dpg;
    }
    
    public GameGUI getTheView() {
        return theView;
    }
    
    public void setTheScene(Scene scene) {
        this.theScene = scene;
    }
}