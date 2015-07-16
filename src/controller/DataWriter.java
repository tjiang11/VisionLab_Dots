package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import model.ControlType;
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
    public static final String CONTROL_TYPE = "Control Type";
    public static final String RESPONSE_TIME = "Response Time";
    public static final String DATE_TIME = "Date/Time";
    public static final String CONSECUTIVE_ROUND = "Consecutive Rounds";
    
    /** The subject to grab data from. */
    private Player player;
    /** DotsPair to grab data from. */
    private DotsPair dotsPair;
    
    /**
     * Constructor for data writer that takes in a controller
     * and grabs the player and dots pair.
     * @param dgc Controller to grab data from
     */
    public DataWriter(DotsGameController dgc) {
        this.player = dgc.getThePlayer();
        this.dotsPair = dgc.getCurrentDotsPair();
    }
    
    /**
     * Regrab the current subject and dots pair from the controller.
     * @param dgc Controller to grab data from
     */
    public void grabData(DotsGameController dgc) {
        this.player = dgc.getThePlayer();
        this.dotsPair = dgc.getCurrentDotsPair();
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
            String text = "";
            System.out.println(file.getPath());
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
                + CONTROL_TYPE + DELIMITER
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
        String controlType = this.generateControlTypeText();
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
                + controlType + DELIMITER
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
        return Integer.toString(
                this.dotsPair.getDotSetOne().getPositions().size());
    }
    
    private String generateRightChoiceText() {
        return Integer.toString(
                this.dotsPair.getDotSetTwo().getPositions().size());
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
        int difference = Math.abs(this.dotsPair.getDifference());
        if (difference >= DotsPairGenerator.EASY_MODE_MIN &&
                difference < DotsPairGenerator.EASY_MODE_MIN 
                + DotsPairGenerator.NUM_CHOICES_IN_MODE) {
            return "EASY";
        } else if (difference >= DotsPairGenerator.MEDIUM_MODE_MIN &&
                difference < DotsPairGenerator.MEDIUM_MODE_MIN
                + DotsPairGenerator.NUM_CHOICES_IN_MODE) {
            return "MEDIUM";
        } else if (difference >= DotsPairGenerator.HARD_MODE_MIN &&
                difference < DotsPairGenerator.HARD_MODE_MIN 
                + DotsPairGenerator.NUM_CHOICES_IN_MODE) {
            return "HARD";
        } 
        return "";
    }
    
    private String generateDistanceText() {
        return Integer.toString(Math.abs(
                    this.dotsPair.getDifference()));
    }
    
    private String generateControlTypeText() {
        if (this.dotsPair.getControlType() == ControlType.EQUAL_AREAS) {
            return "Equal Areas";
        } else if (this.dotsPair.getControlType() == ControlType.INVERSE_AREAS) {
            return "Inverse Areas";
        } else if (this.dotsPair.getControlType() == ControlType.RADIUS_AVERAGE_EQUAL) {
            return "Equal Average Radii";
        } else if (this.dotsPair.getControlType() == ControlType.NONE) {
            return "None";
        } 
        return "-";
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
