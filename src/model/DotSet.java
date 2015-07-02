package model;

import java.util.Random;
import java.util.ArrayList;

public class DotSet {
    
    static final int EXTRA_IMAGE_BOUNDARY = 2;
    
    /** Width and height of each canvas, should match constants in SetUp.java */
    static final int OPTION_WIDTH = 300;
    static final int OPTION_HEIGHT = 450;
    static final int MAX_DIAMETER = 50;
    static final int MIN_DIAMETER = 20;
    
    private int totalNumDots;
    private ArrayList<Coordinate> positions;
    private ArrayList<Integer> diameters;
       
    
    private Random randomGenerator = new Random();
    
    public DotSet(int numDots) {
        this.setTotalNumDots(numDots);
        this.positions = new ArrayList<Coordinate>();
        this.diameters = new ArrayList<Integer>(); 
        this.fillDots();
    }
    
    private void fillDots() {
        int i = 0;
        while (i < this.totalNumDots) {
            int x = randomGenerator.nextInt(OPTION_WIDTH - MAX_DIAMETER);
            int y = randomGenerator.nextInt(OPTION_HEIGHT - MAX_DIAMETER);
            int diameter = randomGenerator.nextInt(MAX_DIAMETER - MIN_DIAMETER) + MIN_DIAMETER; 
            
            if (!overLapsOther(x, y, diameter)) {
                this.addDotAndDiameter(x, y, diameter);
                System.out.println(i);
                i++;
            }
        }
    }

    private boolean overLapsOther(int x, int y, int diameter) {
        int radius = diameter / 2;
        int centerX = x + radius;
        int centerY = y + radius;

        for (int i = 0; i < positions.size(); i++)
        {
            Coordinate otherPosition = positions.get(i);
            int otherDiameter = diameters.get(i);
            int otherRadius = otherDiameter/2;
            int otherCenterX = otherPosition.x + otherRadius;
            int otherCenterY = otherPosition.y + otherRadius;

            int dx = centerX - otherCenterX;
            int dy = centerY - otherCenterY;
            double distance = Math.hypot(dx, dy);
            if (distance < radius + otherRadius)
            {
                return true;
            }
        }
        return false;
    }
    

    public ArrayList<Coordinate> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Coordinate> positions) {
        this.positions = positions;
    }
    
    /**
     * "Adds a dot" to the array of dots by storing the position of that dot
     * 
     * @param x
     *            x coordinate of the dot
     * @param y
     *            y coordinate of the dot
     */
    public void addDotAndDiameter(int x, int y, int diameter) {
        this.positions.add(new Coordinate(x, y));
        this.diameters.add(diameter);
    }

    public int getTotalNumDots() {
        return totalNumDots;
    }

    public ArrayList<Integer> getDiameters() {
        return diameters;
    }

    public void setDiameters(ArrayList<Integer> diameters) {
        this.diameters = diameters;
    }

    public void setTotalNumDots(int totalNumDots) {
        this.totalNumDots = totalNumDots;
    }
}
