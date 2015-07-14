package model;

import java.util.Random;

import config.Config;

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
    
    /** The first letter. */
    private DotSet dotSetOne;
    
    /** The second letter. */
    private DotSet dotSetTwo;
    
    /** The difference in number of dots between the sets. */
    private int difference;
    
    /** Whether the left answer is correct or not. */
    private boolean leftCorrect;
    
    private boolean areaControlOn;
    
    private Random randomGenerator = new Random();
    
    /** 
     * Constructor for DotsPair.
     * @param numDotsOne The number of dots in the first set.
     * @param numDotsTwo The number of dots in the second set.
     */
    public DotsPair(int numDotsOne, int numDotsTwo) {
        new Config();
        System.out.println("Num dots One: " + numDotsOne);
        System.out.println("Num dots Two: " + numDotsTwo);

        this.dotSetOne = new DotSet(numDotsOne);
        this.dotSetTwo = new DotSet(numDotsTwo);
        
        if (Config.getPropertyBoolean("total.area.control.on")) {
            this.scaleAreas();
        }
        
        this.difference = numDotsOne - numDotsTwo;
        if (this.difference > 0) {
            this.setLeftCorrect(true);
        } else if (this.difference < 0) {
            this.setLeftCorrect(false);
        }
    }
    
    /**
     * Scale the total areas of the dots.
     */
    private void scaleAreas() {      
        if (randomGenerator.nextBoolean()) {
            this.matchAreas(dotSetOne, dotSetTwo);
            setAreaControlOn(true);
        } else {
            this.inverseMatchAreas(dotSetOne, dotSetTwo);
            setAreaControlOn(false);
        }
    }
    
    /**
     * Make two dot sets have equal areas by scaling the dot set with greater area down.
     * @param dotSetOne
     * @param dotSetTwo
     */
    private void matchAreas(DotSet dotSetOne, DotSet dotSetTwo) {
        double totalAreaOne = this.dotSetOne.getTotalArea();
        double totalAreaTwo = this.dotSetTwo.getTotalArea();
        
        if (totalAreaOne > totalAreaTwo) {
            dotSetOne.matchArea(totalAreaTwo);        
        } else {
            dotSetTwo.matchArea(totalAreaOne);
        }
    }

    /**
     * Further scale down the dot set with lesser area.
     * @param dotSetOne
     * @param dotSetTwo
     */
    private void inverseMatchAreas(DotSet dotSetOne, DotSet dotSetTwo) {
        double totalAreaOne = dotSetOne.getTotalArea();
        double totalAreaTwo = dotSetTwo.getTotalArea();
        
        if (totalAreaOne > totalAreaTwo) {
            dotSetTwo.inverseMatchArea(totalAreaOne);
        } else {
            dotSetOne.inverseMatchArea(totalAreaTwo);
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

    public boolean isAreaControlOn() {
        return areaControlOn;
    }

    public void setAreaControlOn(boolean areaControlOn) {
        this.areaControlOn = areaControlOn;
    }
    
}
