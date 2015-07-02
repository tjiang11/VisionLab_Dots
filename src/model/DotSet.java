package model;

public class DotSet {
    
    static final int EXTRA_IMAGE_BOUNDARY = 2;
    
    private int totalNumDots;
    private int currentNumDots;
    private Coordinate[] positions;
    
    private int maxDotWidth;
    private int maxDotHeight;   
    
    public DotSet(int numDots) {
        this.totalNumDots = numDots;
        this.currentNumDots = 0;
        this.positions = new Coordinate[numDots];
        
        //temporary 
        this.maxDotHeight = 7;
        this.maxDotWidth = 7;
    }
    
    /**
     * Checks whether a dot placed at (x,y) with dimensions w, h would overlap
     * with a dot in this dot set
     * 
     * @param x
     *            the x coordinate of the top left corner of the dot to be
     *            checked
     * @param y
     *            the y coordinate of the top left corner of the dot to be
     *            checked
     * @param w
     *            the width of the dot to be checked
     * @param h
     *            the height of the dot to be checked
     * @return whether the dot with the given location and dimensions would
     *         overlap with a dot in this dot set
     */
    public boolean isOverlapping(int x, int y, int w, int h) {
        for (int i = 0; i < currentNumDots; i++) {
            if (areOverlapping(positions[i], x, y, w, h))
                return true;
        }
        return false;
    }
    
    /**
     * Checks whether a dot placed at (x,y) with dimensions w, h would overlap
     * with a the ith dot in this dot set
     * 
     * @param c
     *            the position of the dot to be checked for overlap with the new
     *            dot at (x,y) with dimensions w, h
     * @param x
     *            the x coordinate of the top left corner of the dot to be
     *            checked
     * @param y
     *            the y coordinate of the top left corner of the dot to be
     *            checked
     * @param w
     *            the width of the dot to be checked
     * @param h
     *            the height of the dot to be checked
     * @return whether the dot with the given location and dimensions would
     *         overlap with the ith dot in this dot set
     */
    private boolean areOverlapping(Coordinate c, int x, int y, int w, int h) {
        if ((c.x + this.maxDotWidth + 1 + 2 * EXTRA_IMAGE_BOUNDARY) < x
                || (c.x > (x + w + 1 + 2 * EXTRA_IMAGE_BOUNDARY)))
            return false;
        else if ((c.y + maxDotHeight + 1 + 2 * EXTRA_IMAGE_BOUNDARY) < y
                || (c.y > (y + h + 1 + 2 * EXTRA_IMAGE_BOUNDARY)))
            return false;
        return true;
    }
    
    /**
     * "Adds a dot" to the array of dots by storing the position of that dot
     * 
     * @param x
     *            x coordinate of the dot
     * @param y
     *            y coordinate of the dot
     */
    public void addDot(int x, int y) {
        if (currentNumDots < positions.length) {
            positions[currentNumDots] = new Coordinate(x, y);
            currentNumDots++;
        }   
    }
}
