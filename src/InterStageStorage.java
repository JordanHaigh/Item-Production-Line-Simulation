/**
 * Created by Jordan on 15-May-17.
 */

import java.util.LinkedList;
import java.util.Queue;

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

    //Used for InfiniteStorage Classes
    public InterStageStorage(MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        this.inboundStage = inboundStage;
        this.outboundStage = outboundStage;
        this.simulation = simulation;
        this.name = name;
    }


    public InterStageStorage(int qMax, MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        this(inboundStage, outboundStage, simulation, name);

        this.qMax = qMax;
    }

    public void enqueue(Item item)
    {
        list.add(item);
        queueStartTimes.add(totalNumItemsAddedToQueue, simulation.getCurrentSimulationTime());
        totalNumItemsAddedToQueue++;

        //totalStartTimeOfItemsInQueue += simulation.getCurrentSimulationTime();
    }

    public Item dequeue()
    {
        queueFinishTimes.add(totalNumItemsRemovedFromQueue, simulation.getCurrentSimulationTime());
        totalNumItemsRemovedFromQueue++;
        return list.remove();
    }

    public double calculateAverageTimeInQueue()
    {
        double totalTimeInQueue = 0;
        for(int i = 0; i < totalNumItemsRemovedFromQueue; i++)
        {
            queueDifference.add(i, queueFinishTimes.get(i)-queueStartTimes.get(i));
            totalTimeInQueue += queueDifference.get(i);
        }

        return totalTimeInQueue / totalNumItemsAddedToQueue;


    }

    public int getTotalNumItemsAddedToQueue() {return totalNumItemsAddedToQueue; }

    public int getTotalNumItemsRemovedFromQueue() {return totalNumItemsRemovedFromQueue; }

    public int size()
    {
        return list.size();
    }

    public boolean isFull()
    {
        return list.size() == qMax;
    }

    public boolean isEmpty()
    {
        return list.size() == 0;
    }


    public String getName() { return name; }

    @Override
    public String toString()
    {
        return this.name +
            (isEmpty() ? " [Empty]" :
                    (isFull() ? " [Full]" :
                            " Holding " + size() + " items"));
    }
}
