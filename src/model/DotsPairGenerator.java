package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates DotsPairs with random numbers of dots.
 * 
 * Classes Related To:
 *  -DotsPair.java
 * 
 * @author Tony Jiang
 * 6-25-2015
 *
 */
public class DotsPairGenerator implements PairGenerator {
    
    /** Number of characters to choose from. */
    static final int MAX_DOTS = 26;
    
    /** Map from each difficulty mode to an integer representation. */
    static final int EASY_MODE = 0;
    static final int MEDIUM_MODE = 1;
    static final int HARD_MODE = 2;
    
    /** Number of difficulty modes. */
    static final int NUM_MODES = 3;
    
    /** Define the lowest distance (in number of letters) each difficulty can have. */
    public static final int EASY_MODE_MIN = 14;
    public static final int MEDIUM_MODE_MIN = 8;
    public static final int HARD_MODE_MIN = 2;
    
    /** The highest distance each difficulty can have is their minimum plus NUM_CHOICES_IN_MODE. */
    public static final int NUM_CHOICES_IN_MODE = 4;
    
    /**
     * Number of triplets of modes per set. See fillDifficultySet().
     */
    static final int NUM_MODE_TRIPLETS = 2;
    
    /** Random number generator. */
    Random randomGenerator = new Random();

    /** The most recent DotsPair produced by DotsPairGenerator. */
    private DotsPair dotsPair; 
    
    /** The difficulty setting: EASY, MEDIUM, HARD */
    private int difficultyMode;
    
    /** The list containing the difficulties. */
    private ArrayList<Integer> difficultySet;
    
    /** A measure of how many times the same side has been correct. */
    private int sameChoice;
    
    /** True if the last correct choice was left. False otherwise. */
    private boolean lastWasLeft;
    
    /**
     * Constructor. 
     */
    public DotsPairGenerator() {
        this.setSameChoice(0);
        this.setLastWasLeft(false);
        this.difficultySet = new ArrayList<Integer>();
        this.fillDifficultySet();
    }
    
    /**
     * Gets a new DotsPair with random letters while
     * checking to make sure that the same choice will
     * not be picked more than three times in a row
     * as being correct.
     */
    public void getNewPair() {
        this.setDifficulty();
        System.out.println(this.getSameChoice());
        int dotSetOne, dotSetTwo;
        dotSetOne = this.randomGenerator.nextInt(MAX_DOTS) + 1;
        do {
            dotSetTwo = this.randomGenerator.nextInt(MAX_DOTS) + 1; 
        } while (dotSetOne == dotSetTwo);        
           
        this.checkAndSet(dotSetOne, dotSetTwo);
    }
    
    /**
     * This is how the difficulty is pseudo-randomly decided:
     * 
     * There will be a list (difficultySet) containing triplets of modes, 
     * where each triplet would contain one of each difficulty mode.
     * NUM_MODE_SETS is the number of triplets that the difficultySet contains.
     * 
     * When resetting the difficulty <setDifficulty()>, one mode will be randomly selected
     * from the difficultySet and removed. This repeats until difficultySet
     * is empty where it will then refill.
     * 
     */
    private void fillDifficultySet() {
        for (int i = 0; i < NUM_MODE_TRIPLETS; i++) {
            this.difficultySet.add(EASY_MODE);
            this.difficultySet.add(MEDIUM_MODE);
            this.difficultySet.add(HARD_MODE);
        }
    }
    
    public void setDifficulty() {
        this.difficultyMode = 
                this.difficultySet.remove(
                        randomGenerator.nextInt(this.difficultySet.size()));
        if (this.difficultySet.isEmpty()) {
            this.fillDifficultySet();
        }
    }
    
    /**
     * Get a new pair based on the current difficulty.
     */
    public void getNewDifficultyPair() {
        this.setDifficulty();
        int difference = 0;
        if (this.getDifficultyMode() == EASY_MODE) {
            difference = this.randomGenerator.nextInt(NUM_CHOICES_IN_MODE) + EASY_MODE_MIN;
        } else if (this.getDifficultyMode() == MEDIUM_MODE) {
            difference = this.randomGenerator.nextInt(NUM_CHOICES_IN_MODE) + MEDIUM_MODE_MIN;
        } else if (this.getDifficultyMode() == HARD_MODE) {
            difference = this.randomGenerator.nextInt(NUM_CHOICES_IN_MODE) + HARD_MODE_MIN;
        }
        this.getNewPair(difference);
    }
    
    /**
     * Gets a new DotsPair with letters a certain distance apart.
     * @param difference distance between the letters.
     */
    public void getNewPair(int difference) {
        int dotSetOne, dotSetTwo;
        dotSetOne = this.randomGenerator.nextInt(MAX_DOTS - difference) + 1;
        dotSetTwo = dotSetOne + difference;
        
        if (randomGenerator.nextBoolean()) {
            int temp = dotSetTwo;
            dotSetTwo = dotSetOne;
            dotSetOne = temp;
        }
        this.checkAndSet(dotSetOne, dotSetTwo);
    }
    
    /**
     * Check if choices are the same and set the reverse if the same side has been
     * correct for MAX_TIMES_SAME_ANSWER times in a row.
     * @param dotSetOne
     * @param dotSetTwo
     */
    private void checkAndSet(int dotSetOne, int dotSetTwo) {
        this.checkSameChoice(dotSetOne, dotSetTwo);
        
        if (this.getSameChoice() >= MAX_TIMES_SAME_ANSWER) {
            this.setReversePair(dotSetOne, dotSetTwo);
        } else {
            this.setDotsPair(new DotsPair(dotSetOne, dotSetTwo));
        }
    }
    
    /**
     * Occurs under the condition that the same side has been correct
     * for MAX_TIMES_SAME_ANSWER times in a row.
     * 
     * Set the DotsPair with the positions of the right and left letters
     * flipped as to what it would have otherwise been.
     * 
     * Toggles the lastWasLeft property because we are toggling the side
     * of which each component of the pair is being shown, so the opposite
     * side will be correct after setting the alpha pair in reverse order.
     * 
     * @param dotSetOne 
     * @param dotSetTwo
     */
    public void setReversePair(int dotSetOne, int dotSetTwo) {
        this.setDotsPair(new DotsPair(dotSetTwo, dotSetOne));
        this.toggleLastWasLeft();
        this.setSameChoice(0);
    }

    /**
     * Check if the same side is correct as the last round.
     * @param dotSetOne Position of first letter of current round.
     * @param dotSetTwo Position of second letter of current round.
     */
    public void checkSameChoice(int dotSetOne, int dotSetTwo) {
        if (dotSetOne > dotSetTwo) {
            if (this.lastWasLeft) {
                this.incrementSameChoice();
            } else {
                this.setSameChoice(0);
            }
            this.lastWasLeft = true;
        } else {
            if (!this.lastWasLeft) {
                this.incrementSameChoice();
            } else {
                this.setSameChoice(0);
            }
            this.lastWasLeft = false;
        }   
    }

    /**
     * Toggles which of the last choices was correct.
     */
    private void toggleLastWasLeft() {
        if (this.lastWasLeft) {
            this.lastWasLeft = false;
        } else {
            this.lastWasLeft = true;
        }
        
    }
    
    public void setRandomDifficulty() {
        this.difficultyMode = this.randomGenerator.nextInt(NUM_MODES);
    }
    
    public void increaseDifficulty() {
        this.difficultyMode++;
    }

    public DotsPair getDotsPair() {
        return this.dotsPair;
    }

    public void setDotsPair(DotsPair dotsPair) {
        this.dotsPair = dotsPair;
    }

    public int getSameChoice() {
        return this.sameChoice;
    }

    public void setSameChoice(int sameChoice) {
        this.sameChoice = sameChoice;
    }

    public void incrementSameChoice() {
        this.sameChoice++;
    }
    
    public boolean isLastWasLeft() {
        return this.lastWasLeft;
    }

    public void setLastWasLeft(boolean lastWasLeft) {
        this.lastWasLeft = lastWasLeft;
    }

    public int getDifficultyMode() {
        return difficultyMode;
    }

    public void setDifficultyMode(int difficultyMode) {
        this.difficultyMode = difficultyMode;
    }
}
