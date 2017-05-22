
import java.util.*;

/**
 * Item class used across Stages and InterStage Storage - Core part of the program
 * Includes various methods and attributes for querying
 */
public class Item
{
    private int uniqueID;
    private double creationTime = 0;

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



/* public double getThroughputTime() throws Exception
    {
        if(itemFinished)
            return finishTime - creationTime;
        else
            throw new Exception("Item needs to finalised first");
    }
*/

/*
    public double getTotalTimeSpentIdling()
    {
        return totalTimeSpentIdling;
    }
*/

    /*****************************************QUERY*****************************************/

/*  public void finishItem(double finishTime) throws Exception
    {
        if(finishTime < creationTime)
            throw new Exception("Finish time parameter is less than creation time");

        this.finishTime = finishTime;
        itemFinished = true;
    }
*/

/*
    public boolean isIdle() { return idle; }

    public void goIntoIdleState(double currentTime)
    {
        timeWhenGoingIntoIdle = currentTime;
        idle =  true;
    }

    public void comeOutOfIdleState(double currentTime) throws Exception
    {
        if(idle)
        {
            double timeSpentIdling = currentTime - timeWhenGoingIntoIdle;
            totalTimeSpentIdling += timeSpentIdling;
            idle = false;
        }

        else
            throw new Exception("Item needs to come out of idle state");
    }
*/
}














