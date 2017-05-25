import java.util.LinkedList;
import java.util.Queue;

/**
 * InterStageStorage Class - Utilises Java Data Structures such as LinkedLists and Queues to hold a certain number of items
 */
public class InterStageStorage
{
    protected Queue<Item> list = new LinkedList<>();
    protected int qMax;
    protected MasterStage inboundStage;
    protected MasterStage outboundStage;
    protected String name;
    protected Simulation simulation;

    private int totalNumItemsAddedToQueue = 0;
    private int totalNumItemsRemovedFromQueue = 0;
    private LinkedList<Double> queueStartTimes = new LinkedList<>();
    private LinkedList<Double> queueFinishTimes = new LinkedList<>();
    private LinkedList<Double> queueDifference = new LinkedList<>();

    private double totalTimeInQueue = 0;
    private double lastAddRemoveTime = 0;

    /**
     * Overloaded Constructor
     * @param inboundStage - MasterStage to the left of the current InterStageStorage
     * @param outboundStage - MasterStage to the right of the current InterStageStorage
     * @param simulation - Simulation that runs the entire program
     * @param name - Name of the InterStateStorage - Useful in debugging
     */
    public InterStageStorage(MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        this.inboundStage = inboundStage;
        this.outboundStage = outboundStage;
        this.simulation = simulation;
        this.name = name;
    }


    /**
     * Overloadaed Constructor
     * @param qMax - Size of the InterStageStorage Queues
     * @param inboundStage - MasterStage to the left of the current InterStageStorage
     * @param outboundStage - MasterStage to the right of the current InterStageStorage
     * @param simulation - Simulation that runs the entire program
     * @param name - Name of the InterstageStorage - Useful in Debugging
     */
    public InterStageStorage(int qMax, MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        this(inboundStage, outboundStage, simulation, name);

        this.qMax = qMax;
    }

    /*****************************************GETTERS AND SETTERS*****************************************/

    //public int getTotalNumItemsAddedToQueue() {return totalNumItemsAddedToQueue; }

    //public int getTotalNumItemsRemovedFromQueue() {return totalNumItemsRemovedFromQueue; }

    /**
     * public String getName()
     * @return - Returns name of the InterStageStorage
     */
    public String getName() { return name; }


    /*****************************************QUERY*****************************************/

    /**
     * public int size()
     * @return - Returns size of the list
     */
    public int size()
    {
        return list.size();
    }

    /**
     * public boolean isFull()
     * @return - Returns true is the list is full, false if not
     */
    public boolean isFull()
    {
        return list.size() == qMax;
    }

    /**
     * public boolean isEmpty()
     * @return - Returns true if the list is empty, false if not
     */
    public boolean isEmpty()
    {
        return list.size() == 0;
    }

    /**
     * public double calculateAverageTimeInQueue()
     * Calculates the difference between each starting time and each finishing time.
     * Adds a total of this difference and divides by the total number
     * @return - Total Average time per stage
     */
    public double calculateAverageTimeInQueue()
    {
        double totalTimeInQueue = 0;
        for(int i = 0; i < totalNumItemsRemovedFromQueue; i++)
        {
            queueDifference.add(i, queueFinishTimes.get(i)-queueStartTimes.get(i));
            totalTimeInQueue += queueDifference.get(i);
        }

        return totalTimeInQueue / totalNumItemsAddedToQueue; //todo double check
        //return totalTimeInQueue / totalNumItemsRemovedFromQueue
    }

    /**
     * public double calculateAverageNumberOfItemsInQueue()
     * @return - Returns the total time in the queue divided by the max simulation time
     */
    public double calculateAverageNumberOfItemsInQueue()
    {
        return totalTimeInQueue / simulation.MAX_SIMULATION_TIME;
    }

    /*****************************************OTHER*****************************************/

    /**
     * public void enqueue(Item item)
     * Enqueues the item to the list parameter (Type LinkedList)
     * Updates the total time an item spends in the queue and relevant data statistics
     * @param item - Type item to be enqueued into the list
     */
    public void enqueue(Item item)
    {
        updateTotalTimeInQueue();

        queueStartTimes.add(totalNumItemsAddedToQueue, simulation.getCurrentSimulationTime());
        totalNumItemsAddedToQueue++;

        list.add(item);

    }

    /**
     * public Item dequeue()
     * Dequeues an item from the list (LinkedList)
     * Updates the total time an item spends in the queue and relevant data statistics
     * @return - Item removed from the LinkedList
     */
    public Item dequeue()
    {
        updateTotalTimeInQueue();

        queueFinishTimes.add(totalNumItemsRemovedFromQueue, simulation.getCurrentSimulationTime());
        totalNumItemsRemovedFromQueue++;

        return list.remove();
    }

    /**
     * private void updateTotalTimeInQueue()
     * Updates the total time an item spends in the queue
     * The LastAddRemoveTime is updated to the current simulation time when a modification occurs
     */
    private void updateTotalTimeInQueue()
    {
        //Update the total time with the current time minus the lastRemovalTime, all multiplied the size
        //If an item is moved straight from one stage to another, the totalTime will remain the same
        //In the event that an item is dequeued from the queue and another item is enqueued to the same queue, the totaltime
        //  will remain the same since the lastAddRemoveTime had already been updated
        totalTimeInQueue += (simulation.getCurrentSimulationTime() - lastAddRemoveTime) * size();
        //Update the lastAddRemovalTime to the current simulation time
        lastAddRemoveTime = simulation.getCurrentSimulationTime();
    }

    /**
     * public String toString()
     * @return - Returns Storage class in String format (Useful for debugging)
     */
    @Override
    public String toString()
    {
        return this.name +
            (isEmpty() ? " [Empty]" :
                    (isFull() ? " [Full]" :
                            " Holding " + size() + " items"));
    }
}
