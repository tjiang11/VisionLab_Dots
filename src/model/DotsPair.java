package model;

import java.util.Random;

/** 
 * Object to represent two sets of dots.
 * 
 * Classes Related To;
 *  -DotSet.java
 *      -DotsPair contains a pair of DotSets.
 * 
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class DotsPair {
            
    /** TOTAL_AREA_CONTROL true if each dot set in a pair should have the same total area.
     * If dotSetOne has X dots with average radius Rx nd dotSetTwo has Y dots with average radius Ry,
     * then Ry = sqrt((X * Rx^2) / Y).
     */
    private static final boolean TOTAL_AREA_CONTROL = true;
    
    /** The first letter. */
    private DotSet dotSetOne;
    
    /** The second letter. */
    private DotSet dotSetTwo;
    
    /** The difference in number of dots between the sets. */
    private int difference;
    
    /** Whether the left answer is correct or not. */
    private boolean leftCorrect;
    
    
    /** 
     * Constructor for DotsPair.
     * @param numDotsOne The number of dots in the first set.
     * @param numDotsTwo The number of dots in the second set.
     */
    public DotsPair(int numDotsOne, int numDotsTwo) {
        
        if (TOTAL_AREA_CONTROL) {
            System.out.println("Num dots One: " + numDotsOne);
            System.out.println("Num dots Two: " + numDotsTwo);

            this.dotSetOne = new DotSet(numDotsOne);
            this.dotSetTwo = new DotSet(numDotsTwo);
            
            double totalAreaOne = this.dotSetOne.getTotalArea();
            double totalAreaTwo = this.dotSetTwo.getTotalArea();
            
            if (totalAreaOne > totalAreaTwo) {
                dotSetOne.matchArea(totalAreaTwo);        
            } else {
                dotSetTwo.matchArea(totalAreaOne);
            }
        }
        else {
            this.dotSetOne = new DotSet(numDotsOne);
            this.dotSetTwo = new DotSet(numDotsTwo);
        }
        
        this.difference = numDotsOne - numDotsTwo;
        if (this.difference > 0) {
            this.setLeftCorrect(true);
        } else if (this.difference < 0) {
            this.setLeftCorrect(false);
        }
    }
    
    public DotSet getDotSetOne() {
        return this.dotSetOne;
    }

    public void setDotSetOne(DotSet dotSetOne) {
        this.dotSetOne = dotSetOne;
    }

    public DotSet getDotSetTwo() {
        return this.dotSetTwo;
    }

    public void setDotSetTwo(DotSet dotSetTwo) {
        this.dotSetTwo = dotSetTwo;
    }


    public int getDifference() {
        return this.difference;
    }


    public void setDifference(int difference) {
        this.difference = difference;
    }

    public boolean isLeftCorrect() {
        return this.leftCorrect;
    }

    public void setLeftCorrect(boolean leftCorrect) {
        this.leftCorrect = leftCorrect;
    }
    
}
