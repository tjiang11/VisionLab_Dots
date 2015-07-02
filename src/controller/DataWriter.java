package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import model.DotsPair;
import model.DotsPairGenerator;
import model.Player;


/**
 * Class for grabbing and exporting data to a CSV file.
 * 
 * Classes Related to:
 *  -DotsGameController.java
 *      -Grabs DotsPair and Player from the controller to record and export their data.
 * 
 * @author Tony Jiang
 * 6-25-2015
 *
 */
public class DataWriter {

    public static final String DELIMITER = ",";
    public static final String SUBJECT_ID = "Subject ID";
    public static final String LEFT_CHOICE = "Left Choice";
    public static final String RIGHT_CHOICE = "Right Choice";
    public static final String WHICH_SIDE_CORRECT = "Side Correct";
    public static final String WHICH_SIDE_PICKED = "Side Picked";
    public static final String IS_CORRECT = "Correct";
    public static final String DIFFICULTY = "Difficulty";
    public static final String DISTANCE = "Distance";
    public static final String RESPONSE_TIME = "Response Time";
    public static final String DATE_TIME = "Date/Time";
    public static final String CONSECUTIVE_ROUND = "Consecutive Rounds";
    
    /** The subject to grab data from. */
    private Player player;
    /** DotsPair to grab data from. */
    private DotsPair dotsPair;
    /** DotsPairGenerator to grab data from */
    private DotsPairGenerator dotsPairGenerator;
    
    /**
     * Constructor for data writer that takes in a controller
     * and grabs the player and alpha pair.
     * @param dgc Controller to grab data from
     */
    public DataWriter(DotsGameController dgc) {
        this.player = dgc.getThePlayer();
        this.dotsPair = dgc.getCurrentDotsPair();
        this.dotsPairGenerator = dgc.getApg();
    }
    
    /**
     * Regrab the current subject and alphapair from the controller.
     * @param dgc Controller to grab data from
     */
    public void grabData(DotsGameController dgc) {
        this.player = dgc.getThePlayer();
        this.dotsPair = dgc.getCurrentDotsPair();
        this.dotsPairGenerator = dgc.getApg();
    }
    
    /**
     * Export data to CSV file. Appends to current CSV if data
     * for subject already exists.
     * Location of CSV file is in folder "results". "Results" will contain
     * subfolders each titled by Subject ID number containing the subject's
     * CSV data. 
     */
    public void writeToCSV() {
        
        PrintWriter writer = null;
        String subjectId = Integer.toString(this.player.getSubjectID());
        try {
            /** Grab path to project */
            String path = new File(".").getAbsolutePath();
            path = path.substring(0, path.length() - 1);
            
            /** Create results folder if doesn't exist */
            File resultsDir = new File("results");
            resultsDir.mkdir();
            
            /** Create subject folder if doesn't exist */
            File subjectDir = new File("results\\" + subjectId);
            subjectDir.mkdir();    
            
            /** Create new csv file for subject if doesn't exist */
            File file = new File(path + "\\results\\" + subjectId 
                    + "\\results_" + subjectId + ".csv");            
            System.out.println(file.getPath());
            String text = "";
            
            /** Write data to new file or append to old file */
            if (file.createNewFile()) {
                text += this.generateColumnNames();
            }
            text += this.generateTrialText();
            writer = new PrintWriter(
                        new BufferedWriter(
                            new FileWriter(file, true)));
            writer.write(text);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            
        } finally {
            writer.flush();
            writer.close();
        } 
    }
    
    /**
     * Generate the column names.
     * @return String column names.
     */
    private String generateColumnNames() {
        String text = SUBJECT_ID + DELIMITER
                + LEFT_CHOICE + DELIMITER
                + RIGHT_CHOICE + DELIMITER
                + WHICH_SIDE_CORRECT + DELIMITER
                + WHICH_SIDE_PICKED + DELIMITER
                + IS_CORRECT + DELIMITER
                + DIFFICULTY + DELIMITER
                + DISTANCE + DELIMITER
                + RESPONSE_TIME + DELIMITER
                + DATE_TIME + DELIMITER
                + CONSECUTIVE_ROUND + "\n";
        return text;
    }

    /**
     * Generate the CSV text data for the round (one pair).
     * @return String CSV text data
     */
    public String generateTrialText() {
        String subjectID = this.generateSubjectIdText();
        String leftChoice = this.generateLeftChoiceText();       
        String rightChoice = this.generateRightChoiceText();
        String whichSideCorrect = this.generateWhichSideCorrectText();
        String whichSidePicked = this.generateWhichSidePickedText(whichSideCorrect);
        String correct = this.generateCorrectText();
        String difficulty = this.generateDifficultyText();
        String distance = this.generateDistanceText();
        String responseTime = this.generateResponseTimeText();
        String dateTime = this.generateDateTimeText();
        String consecutiveRounds = this.generateConsecutiveRoundsText();
        
        String trialText = subjectID + DELIMITER
                + leftChoice + DELIMITER
                + rightChoice + DELIMITER
                + whichSideCorrect + DELIMITER
                + whichSidePicked + DELIMITER
                + correct + DELIMITER
                + difficulty + DELIMITER
                + distance + DELIMITER
                + responseTime + DELIMITER
                + dateTime + DELIMITER
                + consecutiveRounds + "\n";
        
        return trialText;
    }
    
    private String generateSubjectIdText() {
        return Integer.toString(
                this.player.getSubjectID());
    }
    
    private String generateLeftChoiceText() {
        return String.valueOf(
                this.dotsPair.getDotSetOne());
    }
    
    private String generateRightChoiceText() {
        return String.valueOf(
                this.dotsPair.getDotSetTwo());
    }
    
    private String generateWhichSideCorrectText() {
        if (this.dotsPair.isLeftCorrect()) {
            return "left";
        } else {
            return "right";
        }
    }
    
    private String generateWhichSidePickedText(String whichSideCorrect) {
        if (this.player.isRight()) {
            return whichSideCorrect;
        } else {
            if (whichSideCorrect.equals("left")) {
                return "right";
            } else {
                return "left";
            }
        }
    }
    
    private String generateCorrectText() {
        if (this.player.isRight()) {
            return "yes";
        } else {
            return "no";
        }
    }
    
    private String generateDifficultyText() {
        int difficulty = this.dotsPairGenerator.getDifficultyMode();
        if (difficulty == 0) {
            return "EASY";
        } else if (difficulty == 1) {
            return "MEDIUM";
        } else if (difficulty == 2) {
            return "HARD";
        } 
        return "";
    }
    
    private String generateDistanceText() {
        return Integer.toString(Math.abs(
                    this.dotsPair.getDifference()));
    }
    
    private String generateResponseTimeText() {
        return String.valueOf(this.player.getRT() / 1000000000.0);
    }
    
    private String generateDateTimeText() {
        return LocalDateTime.now().toString();
    }
    
    private String generateConsecutiveRoundsText() {
        return Integer.toString(
                this.player.getNumRounds());
    }
}
