package controller;

/**
 * Enum describing the current point in time of the game.
 * 
 * (06-25-2015) The prime use of this Enum is to prevent the possibility
 * of mass input caused by subject holding down 'F' or 'J' key.
 * 
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public enum CurrentState {
    /** Introduction screen where user inputs Subject ID. */
    INTRODUCTION,
    
    /** After a user provides feedback to a question and before the
     * next question.
     */
    WAITING_BETWEEN_ROUNDS, 
    
    /** While the question is being displayed and subject has not yet
     * provided feedback.
     */
    WAITING_FOR_RESPONSE,
    
    /** After all trials have been completed. */
    FINISHED,
}
