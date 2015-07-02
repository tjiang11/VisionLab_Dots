package model;


/**
 * Interface for generating Pairs with random choices.
 * 
 * A certain type of Pair class should be created first
 * based on the task at hand.
 * 
 * @author Tony Jiang
 * 6-25-2015
 *
 */
interface PairGenerator {
    
    /**
     * Max number of times the same side may be the correct choice.
     */
    int MAX_TIMES_SAME_ANSWER = 3;
    
    /**
     * Gets a new Pair with random components.
     */
    void getNewPair();
    
    /**
     * Occurs under the condition that the same side has been correct
     * for MAX_TIMES_SAME_ANSWER times in a row.
     * 
     * Set the Pair with the positions of the right and left letters
     * flipped as to what it would have otherwise been.
     */
    void setReversePair(int ChoiceOne, int ChoiceTwo);
    
    /**
     * Check if the same side is correct as the last round.
     * @param ChoiceOne Position of first letter of current round.
     * @param ChoiceTwo Position of right letter of current round.
     */
    void checkSameChoice(int ChoiceOne, int ChoiceTwo);
    
    
}
