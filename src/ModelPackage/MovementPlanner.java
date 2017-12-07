package ModelPackage;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Uses a simplified version of Dijkstra's pathfinding algorithm to find the shortest path to a certain position.
 * Also provides information on where to navigate to.
 */
public class MovementPlanner {

    private IGrid simulationGrid;
    private ArrayList<MotionPoint> plannableGrid;

    private ArrayList<SimObject> obstacleList;
    private ArrayList<SimObject> plantList;
    private ArrayList<SimObject> creatureList;


    public MovementPlanner() {
    }

    /**
     * Initializes the planner with the correct data and triggers the generation of a complete plannable grid. Do not
     * use this function to update the plant and creature lists. Use setPlantList() and setCreatureList() instead!
     * @param obstacles ArrayList containing obstacles. Can't change once the simulation has started!
     * @param plants ArrayList of plants
     * @param creatures ArrayList of creatures
     * @param simulationGrid The simulation Grid used to generate a plannable grid
     * @return true if the generation of the plannable grid was successful, false otherwise.
     */
    public boolean initializePlanner(ArrayList<SimObject> obstacles, ArrayList<SimObject> plants, ArrayList<SimObject> creatures, IGrid simulationGrid){
        this.obstacleList = obstacles;
        this.creatureList = creatures;
        this.plantList = plants;
        this.simulationGrid = simulationGrid;

        try{
            generatePlannableGrid();
        }
        catch(Exception e){
            System.out.println ("Error while generating plannable grid.");
            return false;
        }
        return true;
    }

    /**
     * Update the plant list
     * @param plantList ArrayList of plants
     */
    public void setPlantList(ArrayList<SimObject> plantList) {
        this.plantList = plantList;
    }

    /**
     * Update the creature list
     * @param creatureList ArrayList of creatures
     */
    public void setCreatureList(ArrayList<SimObject> creatureList) {
        this.creatureList = creatureList;
    }

    /**
     * used to generate the plannable grid. If this fails the motionplanner can't properly function.
     */
    private void generatePlannableGrid(){
        if (simulationGrid != null){
            //create a MotionPoint for each GridPoint
            for (GridPoint gridPoint : simulationGrid.getPointList()){
                plannableGrid.add(new MotionPoint(gridPoint));
            }

            for (MotionPoint currentPoint : plannableGrid){
                getAdjacentPoints(currentPoint);
            }
        }
        else{
            throw new NullPointerException("SimulationGrid was not set!");
        }
    }

    /**
     * Find the points adjacent to the current point
     * @param currentPoint point to find the adjacent points of and store them in.
     */
    private void getAdjacentPoints(MotionPoint currentPoint) {
        int x = currentPoint.getX();
        int y = currentPoint.getY();

        int xPlusOne;
        int yPlusOne;
        int xMinusOne;
        int yMinusOne;

        //necessary for wrapping around the grid
        //revert to simple +1 or -1 usage to disable wrapping
        if (x == simulationGrid.getWidth() - 1){
            xPlusOne = 0;
        }
        else{
            xPlusOne = x++;
        }
        if (x == 0){
            xMinusOne = simulationGrid.getWidth()- 1;
        }
        else{
            xMinusOne = x--;
        }
        if (y == simulationGrid.getHeight() - 1){
            yPlusOne = 0;
        }
        else{
            yPlusOne = y++;
        }
        if (y == 0){
            yMinusOne = simulationGrid.getHeight() - 1;
        }
        else{
            yMinusOne = y--;
        }
    }

    public ArrayList<TargetCoordinate> findPath(int startX, int startY, int targetX, int targetY){
        return null;
    }

    /**
     * Inner class used to create a version of the grid suitable for use in motion planning.
     * These points are "aware" of their neighbouring points.
     */
    private class MotionPoint{

        private GridPoint gridPoint;
        private MotionPoint previousPoint;
        private ArrayList<MotionPoint> adjacentPoints;

        public MotionPoint(GridPoint gridPoint) {
            adjacentPoints = new ArrayList<>();
            this.gridPoint = gridPoint;
        }

        /**
         * Sets the previous point in the used path when generating a path
         * @param motionPoint A point in the plannable grid
         */
        public void setPreviousPoint(MotionPoint motionPoint){
            this.previousPoint = motionPoint;
        }

        /**
         * Gets the previous point in the used path when generating a path
         * @return Returns the previous point in the plannable grid
         */
        public MotionPoint getPreviousPoint(){
            return previousPoint;
        }

        /** Adds an adjacent point in the plannable grid
         * @param motionPoint a motionpoint instance in the plannable grid
         */
        public void addAdjacentPoint(MotionPoint motionPoint){
            adjacentPoints.add(motionPoint);
        }

        /** Fetch a list with all adjacent points in the plannable grid
         * @return ArrayList containing MotionPoints
         */
        public ArrayList<MotionPoint> getAdjacentPoints(){
            return adjacentPoints;
        }

        /** Gets the GridPoint representing this MotionPoint in the regular simulation grid
         * @return
         */
        public GridPoint getGridPoint(){
            return gridPoint;
        }

        /** Gets the X coordinate, based on the regular grid system
         * @return X coordinate
         */
        public int getX(){
            return gridPoint.getX();
        }

        /** Gets the Y coordinate, based on the regular grid system
         * @return Y coordinate
         */
        public int getY(){
            return gridPoint.getY();
        }
    }
}
