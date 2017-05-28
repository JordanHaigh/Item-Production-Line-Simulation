
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

  /*private double timeWhenGoingIntoIdle;
    private boolean idle;
    private double totalTimeSpentIdling = 0;
    private double finishTime = 0;
    private boolean itemFinished;*/

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

    public double getCreationTime() { return creationTime; }

    public void setCreationTime(double creationTime) { this.creationTime = creationTime; }

    public double getFinishTime() { return finishTime; }

    public void updateFinishedProductionLineTime(double time) { this.finishedProductionLineTime += time; }

    public double getFinishedProductionLineTime() { return finishedProductionLineTime; }

    public double getTotalBlockTime() { return totalBlockTime; }

    public void updateTotalBlockTime(double blockTime) { this.totalBlockTime += blockTime; }

    public double getTotalStarveTime() { return totalStarveTime; }

    public void updateTotalStarveTime(double starveTime) { this.totalStarveTime += starveTime; }


}














