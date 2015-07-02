package model;

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
    static final int NUM_LETTERS = 26;
    
    /** Map from each difficulty mode to an integer representation. */
    static final int EASY_MODE = 0;
    static final int MEDIUM_MODE = 1;
    static final int HARD_MODE = 2;
    
    /** Number of difficulty modes. */
    static final int NUM_MODES = 3;
    
    /** Define the lowest distance (in number of letters) each difficulty can have. */
    static final int EASY_MODE_MIN = 14;
    static final int MEDIUM_MODE_MIN = 8;
    static final int HARD_MODE_MIN = 2;
    
    /** The highest distance each difficulty can have is their minimum plus NUM_CHOICES_IN_MODE. */
    static final int NUM_CHOICES_IN_MODE = 4;
    
    /** Random number generator. */
    Random randomGenerator = new Random();

    /** The most recent DotsPair produced by DotsPairGenerator. */
    private DotsPair dotsPair; 
    
    /** The difficulty setting: EASY, MEDIUM, HARD */
    private int difficultyMode;
    
    /** A measure of how many times the same side has been correct. */
    private int sameChoice;
    
    /** True if the last correct choice was left. False otherwise. */
    private boolean lastWasLeft;
    
    /**
     * Constructor. 
     */
    public DotsPairGenerator() {
        this.getNewPair();
        this.setSameChoice(0);
        this.setLastWasLeft(false);
        this.setDifficultyMode(EASY_MODE);
    }
    
    /**
     * Gets a new DotsPair with random letters while
     * checking to make sure that the same choice will
     * not be picked more than three times in a row
     * as being correct.
     */
    public void getNewPair() {
        System.out.println(this.getSameChoice());
        int letterOne, letterTwo;
        letterOne = this.randomGenerator.nextInt(NUM_LETTERS);
        do {
            letterTwo = this.randomGenerator.nextInt(NUM_LETTERS); 
        } while (letterOne == letterTwo);        
           
        this.checkAndSet(letterOne, letterTwo);
    }
    
    /**
     * Get a new pair based on the current difficulty.
     */
    public void getNewDifficultyPair() {
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
        int letterOne, letterTwo;
        letterOne = this.randomGenerator.nextInt(NUM_LETTERS - difference);
        letterTwo = letterOne + difference;
        
        //Swap the order
        int x = this.randomGenerator.nextInt(2);
        if (x == 1) {
            int temp = letterTwo;
            letterTwo = letterOne;
            letterOne = temp;
        }
        this.checkAndSet(letterOne, letterTwo);
    }
    
    /**
     * Check if choices are the same and set the reverse if the same side has been
     * correct for MAX_TIMES_SAME_ANSWER times in a row.
     * @param letterOne
     * @param letterTwo
     */
    private void checkAndSet(int letterOne, int letterTwo) {
        this.checkSameChoice(letterOne, letterTwo);
        
        if (this.getSameChoice() >= MAX_TIMES_SAME_ANSWER) {
            this.setReversePair(letterOne, letterTwo);
        } else {
            this.setDotsPair(new DotsPair(letterOne, letterTwo));
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
     * @param letterOne 
     * @param letterTwo
     */
    public void setReversePair(int letterOne, int letterTwo) {
        this.setDotsPair(new DotsPair(letterTwo, letterOne));
        this.toggleLastWasLeft();
        this.setSameChoice(0);
    }

    /**
     * Check if the same side is correct as the last round.
     * @param letterOne Position of first letter of current round.
     * @param letterTwo Position of second letter of current round.
     */
    public void checkSameChoice(int letterOne, int letterTwo) {
        if (letterOne > letterTwo) {
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
        if (this.difficultyMode == 0) {
            System.out.println("EASY");
        } else if (this.difficultyMode == 1) {
            System.out.println("MEDIUM"); 
        } else if (this.difficultyMode == 2) {
            System.out.println("HARD");
        } else {
            System.out.println("Uhh..");
        }
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
