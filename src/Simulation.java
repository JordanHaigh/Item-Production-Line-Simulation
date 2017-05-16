/**
 * Created by Jordan on 15-May-17.
 */

import java.util.PriorityQueue;
import java.util.LinkedList;

public class Simulation
{
    private double m, n;
    private int qMax;

    private LinkedList<Stage> stages = new LinkedList<>();
    private Stage s0, s1;//s2,s3,s4,s5,s6;
    private InterStageStorage q01;//, q12, q23,q34,q45,q56;
    private InfiniteInboundStorage inboundItemGeneration;
    private InfiniteOutboundStorage outboundStorage;

    private double currentSimulationTime = 0; //Start at 0
    public final int MAX_SIMULATION_TIME = 10000000;
    private PriorityQueue<Double> priorityQueue = new PriorityQueue<>();

    public Simulation(double m, double n, int qMax)
    {
        this.m = m;
        this.n = n;
        this.qMax = qMax;

        //Create and link simulation
        s0 = new Stage(this.m, this.n, 1, 1, this);
        s1 = new Stage(this.m, this.n, 1, this);
        //s2, s3, s4, s5, s6;
        stages.addLast(s0);
        stages.addLast(s1);

        /*s2 = new Stage(this.m, this.n, 1);
        s3 = new Stage(this.m, this.n, 2);
        s4 = new Stage(this.m, this.n, 1);
        s5 = new Stage(this.m, this.n, 2);
        s6 = new Stage(this.m, this.n, 1);*/


        //Link Queue to stage
        q01 = new InterStageStorage(qMax, s1, s0);
        //todo implement interstagestorage constructor for LL
        /*q12 = new InterStageStorage(qMax, s2, s1);
        q23 = new InterStageStorage(qMax, s3, s2);
        q34 = new InterStageStorage(qMax, s4, s3);
        q45 = new InterStageStorage(qMax, s5, s4);
        q56 = new InterStageStorage(qMax, s6, s5);*/

        inboundItemGeneration = new InfiniteInboundStorage(null, s0, this);
        outboundStorage = new InfiniteOutboundStorage(s1, null);

        //Link Stages to Queues
        s0.setInboundStorage(inboundItemGeneration);
        s0.setOutboundStorage(q01);

        s1.setInboundStorage(q01);
        s1.setOutboundStorage(outboundStorage);
    }

    public double getCurrentSimulationTime() {
        return currentSimulationTime;
    }

    public void addToPriorityQueue(double time)
    {
        priorityQueue.add(time);
    }

    public void startProcessing()
    {
        while (currentSimulationTime <= MAX_SIMULATION_TIME)
        {
            if (s0.isEmpty())
            {
                Item item = s0.retrieveItemFromInboundStorage();
                s0.startProcessingItem(item);

                currentSimulationTime = priorityQueue.remove();

                //Iterate over each stage in the linked list
                for (Stage s : stages)
                {
                    //Determine whether a stage has finished processing
                    s.finishProcessingItem(item, currentSimulationTime);
                    //
                }
            }
        }
    }
}
