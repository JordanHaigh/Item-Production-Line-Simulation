
/**
 * Item class used across Stages and InterStage Storage - Core part of the program
 * Includes various methods and attributes for querying
 */
public class Item
{
    private int uniqueID;
    private double creationTime = 0;
    private double finishTime = 0;
    private double finishedProductionLineTime = 0;
    private double totalBlockTime = 0;
    private double totalStarveTime = 0;
    private double idleTime = 0;

    public Item(int stageID, double creationTime)
    {
        createUniqueID(stageID);

        //Creation time allows for processing the throughput of the item
        this.creationTime = creationTime;
    }

    private void createUniqueID(int stageID)
    {
        int uniqueID = Singleton.getInstance().getNextID();

        uniqueID *= 10;
        uniqueID += stageID; //Allocates the stage id to the unique id field
        this.uniqueID = uniqueID;
    }

    /*****************************************GETTERS AND SETTERS*****************************************/
    /**
     * public int getUniqueID()
     * @return - Returns the uniqueID of the Item
     */
    public int getUniqueID()
    {
        return uniqueID;
    }

    /**
     * public double getCreationTime()
     * @return - Creation time of item
     */
    public double getCreationTime() { return creationTime; }

    /**
     * public void setCreationTime(double creationTime)
     * @param creationTime - Sets class variable creationTime to the input parameter
     */
    public void setCreationTime(double creationTime) { this.creationTime = creationTime; }

    /**
     * public double getFinishTime()
     * @return - Finish time of the item
     */
    public double getFinishTime() { return finishTime; }

    /**
     * public void updateFinishedProductionLineTime
     * Updates the totalTime throughout the production line to determine its throughput
     * @param time - Current end time of an item in its stage
     */
    public void updateFinishedProductionLineTime(double time) { this.finishedProductionLineTime += time; }

    /**
     * public fouble getFinishedProductionLineTime()
     * @return - Total time of the item throughout the production line
     */
    public double getFinishedProductionLineTime() { return finishedProductionLineTime; }

    /**
     * public getTotalBlockTime()
     * @return - Returns total blocking time of the item
     */
    public double getTotalBlockTime() { return totalBlockTime; }

    /**
     * public void updateTotalBlockTime(double blockTime)
     * @param blockTime - Amount of time the item was blocked for at its current position
     */
    public void updateTotalBlockTime(double blockTime) { this.totalBlockTime += blockTime; }

    /**
     * public double getTotalStarveTime()
     * @return - Total Starving time throughout the production line
     */
    public double getTotalStarveTime() { return totalStarveTime; }

    /**
     * public void updateTotalStarveTime(double starveTime)
     * @param starveTime - Amount of time the item was starved for at its current position
     */
    public void updateTotalStarveTime(double starveTime) { this.totalStarveTime += starveTime; }

    /**
     * public double getIdleTime()
     * @return - Idle time of the item
     */
    public double getIdleTime() { return idleTime; }

    /**
     * public void updateIdleTime(double time)
     * When an item is blocked or waiting in a queue
     * @param time - Updates the total idle time with the current time value
     */
    public void updateIdleTime(double time) { this.idleTime += time; }
}














