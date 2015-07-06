package model;

import java.util.Random;
import java.util.ArrayList;

public class DotSet {
    
    /** AVERAGE_RADIUS_CONTROL true if each dotset should have the same average radius. */
    static final boolean AVERAGE_RADIUS_CONTROL = false;
    static final int AVERAGE_DIAMETER_ARC = 40;
    static final int MAX_DIAMETER_VARIANCE_ARC = 20;
    
    /** Width and height of each canvas, should match constants in SetUp.java */
    static final int OPTION_WIDTH = 300;
    static final int OPTION_HEIGHT = 450;
    
    /** Properties without radius control on */
    static final int AVG_DIAMETER = 50;
    static final int MAX_DIAMETER_VARIANCE = 20;
    static final int MIN_DIAMETER = AVG_DIAMETER - MAX_DIAMETER_VARIANCE;
    static final int MAX_DIAMETER = AVG_DIAMETER + MAX_DIAMETER_VARIANCE;
    
    private int totalNumDots;
    private ArrayList<Coordinate> positions;
    private ArrayList<Double> diameters;
    
    private double totalArea;
    
    private Random randomGenerator = new Random();
    
    public DotSet(int numDots) {
        this.setTotalNumDots(numDots);
        this.positions = new ArrayList<Coordinate>();
        this.diameters = new ArrayList<Double>(); 
        
        this.totalArea = 0;
        
        if (AVERAGE_RADIUS_CONTROL) { this.fillDots(AVERAGE_DIAMETER_ARC, MAX_DIAMETER_VARIANCE_ARC); }
        else { this.fillDots(); }
    }
    
    public DotSet(int numDots, double averageDiameter, int maxDiameterVariance) {
        this.setTotalNumDots(numDots);
        this.positions = new ArrayList<Coordinate>();
        this.diameters = new ArrayList<Double>(); 
        
        this.fillDots(averageDiameter, maxDiameterVariance); 
    }
    
    private void fillDots() {
        int i = 0;
        while (i < this.totalNumDots) {
            int x = randomGenerator.nextInt(OPTION_WIDTH - MAX_DIAMETER);
            int y = randomGenerator.nextInt(OPTION_HEIGHT - MAX_DIAMETER);
            int diameter = randomGenerator.nextInt(MAX_DIAMETER - MIN_DIAMETER) + MIN_DIAMETER; 
            
            if (!overLapsOther(x, y, diameter)) {
                this.addDotAndDiameterAndArea(x, y, diameter);
                i++;
            }
        }
        System.out.println(this.totalNumDots);
    }
    
    /**
     * Fill the dots with an average diameter and max variance.
     * @param avgDiameter average diameter
     * @param maxDiameterVariance max variance
     */
    private void fillDots(double avgDiameter, int maxDiameterVariance) {
        int dotsFilled = 0;
        
        while (dotsFilled < this.totalNumDots) {
            
            int x = randomGenerator.nextInt(OPTION_WIDTH - MAX_DIAMETER);
            int y = randomGenerator.nextInt(OPTION_HEIGHT - MAX_DIAMETER);
            
            if (this.totalNumDots - dotsFilled >= 2) {
                int diameterVariance = randomGenerator.nextInt(maxDiameterVariance) + 1;
                
                double diameterGreater = avgDiameter + diameterVariance;
                double diameterLower = avgDiameter - diameterVariance;
                
                this.addDotNoOverlap(x, y, diameterGreater);
                dotsFilled++;
                
                x = randomGenerator.nextInt(OPTION_WIDTH - MAX_DIAMETER); 
                y = randomGenerator.nextInt(OPTION_HEIGHT - MAX_DIAMETER);
                
                this.addDotNoOverlap(x, y, diameterLower);
                dotsFilled++;
                
            } else {
                double diameter = avgDiameter;
                
                this.addDotNoOverlap(x, y, diameter);
                dotsFilled++;
            }

        }
    }
//    
//    private void fillDots(double totalArea) {
//        
//        double averageAreaPerDot = totalArea / this.getTotalNumDots();
//        
//        int dotsFilled = 0;
//        
//        while (dotsFilled < this.totalNumDots) {
//            
//            int x = randomGenerator.nextInt(OPTION_WIDTH - MAX_DIAMETER);
//            int y = randomGenerator.nextInt(OPTION_HEIGHT - MAX_DIAMETER);
//            
//            if (this.totalNumDots - dotsFilled > 2) {
//                double diameter = AVG_DIAMETER;
//                
//                double diameterGreater = diameter + diameterVariance;
//            }
//        }
//        
//    }
    
    private void addDotNoOverlap(int x, int y, double diameter) {
        while (overLapsOther(x, y, diameter)) {
            x = randomGenerator.nextInt(OPTION_WIDTH - MAX_DIAMETER); 
            y = randomGenerator.nextInt(OPTION_HEIGHT - MAX_DIAMETER);
        }
        this.addDotAndDiameterAndArea(x, y, diameter);
    }

    private boolean overLapsOther(int x, int y, double diameter) {
        double radius = diameter / 2.0;
        double centerX = x + radius;
        double centerY = y + radius;

        for (int i = 0; i < positions.size(); i++)
        {
            Coordinate otherPosition = positions.get(i);
            double otherDiameter = diameters.get(i);
            double otherRadius = otherDiameter/2;
            double otherCenterX = otherPosition.x + otherRadius;
            double otherCenterY = otherPosition.y + otherRadius;

            double dx = centerX - otherCenterX;
            double dy = centerY - otherCenterY;
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
    public void addDotAndDiameterAndArea(int x, int y, double diameter) {
        this.positions.add(new Coordinate(x, y));
        this.diameters.add(diameter);
        this.totalArea += Math.PI * Math.pow((diameter / 2), 2);
    }
    
    public void matchArea(double otherTotalArea) {
        
        double resizeRatio = Math.sqrt(otherTotalArea / this.getTotalArea());
        System.out.println(resizeRatio);
        
        for (int diameterIndex = 0; diameterIndex < this.totalNumDots; diameterIndex++) {
            double diameterToScale = this.diameters.get(diameterIndex);
            double scaledDiameter = diameterToScale * resizeRatio;
            this.diameters.set(diameterIndex, scaledDiameter);
        }
    }

    public double getTotalArea() {
        return this.totalArea;
    }
    
    public int getTotalNumDots() {
        return this.totalNumDots;
    }

    public ArrayList<Double> getDiameters() {
        return diameters;
    }

    public void setDiameters(ArrayList<Double> diameters) {
        this.diameters = diameters;
    }

    public void setTotalNumDots(int totalNumDots) {
        this.totalNumDots = totalNumDots;
    }


}
