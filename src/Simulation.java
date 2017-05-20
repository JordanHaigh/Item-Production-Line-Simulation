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
        s0 = new Stage(this.m, this.n, 1, 1, this, s1, null);
        s1 = new Stage(this.m, this.n, 1, this, null, s0);
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
            for(Stage s: stages)
            {
                //If the stage is empty
                if(s.isEmpty() || s.isStarved())
                {
                    // unstare to get back into empty state. otherwise, we can't take from inbound queue
                    // due to precondition checks
                    if (s.isStarved())
                        s.unstarve();

                    s.retrieveItemFromInboundStorage();
                    //Depending on the queue setup it will generate a new item
                    // for the first stage or pass through an item from a queue

                    if (s.isReady())
                        s.startProcessingItem();
                }
                //Stage is not empty
                else
                {
                    //Something is inside the stage
                    //Could be processing, finished processing, could be blocked
                    if(s.isProcessing())
                    {
                        //Check if the current time is greater than the finish item time
                        if(s.stageIsFinished())
                        {
                            //Update the state - If Statements will chain downwards when state is updated
                            s.finishProcessingItem();
                        }
                    }

                    if(s.isFinishedProcessing())
                    {
                        //Item finished processing, send to outbound storage if there is a position available
                        s.sendToOutboundStorage(); //Stage may be set to BLOCKED if there is no availability

                        // s is now empty and could take an item from it's inbound queue immediately
                        // and start reprocessing
                        // if the inbound queue is empty, s will starve
                        if (!s.isBlocked())
                        {
                            s.retrieveItemFromInboundStorage();

                            if (s.isReady())
                                s.startProcessingItem();
                        }
                    }

                    if(s.isBlocked())
                    {
                        //Stage is blocked, we can't add an item to the forward queue
                        //Check stage in front to determine whether it is not holding an item
                        Stage forwardStage = s.getForwardStage();

                        //Accounting for the last stage in the production line
                        if(forwardStage.isEmpty() || forwardStage.isStarved())
                        {
                            //The forward stage can take an item from the intermediate queue and free a position
                            // for the current stage to enqueue
                            forwardStage.retrieveItemFromInboundStorage();

                            //Now the current stage is able to pass the object
                            s.unblock();
                            s.sendToOutboundStorage(); //The current stage will now be set to empty

                            //Now need to recursively move backwards in the from the current stage to determine whether
                            //Previous stages can unblock if necessary
                        }
                        if (forwardStage.stageIsFinished() && !forwardStage.isBlocked()) // currentTime >= finishTime
                        {
                            forwardStage.finishProcessingItem();

                            // we can now move the item in the forward stage to the outbound storage, if it is empty
                            forwardStage.sendToOutboundStorage();

                            // forward stage is now either empty, or blocked
                            if (!forwardStage.isBlocked())
                            {
                                unblockPreviousStages(forwardStage);
                            }
                        }

                    }

                }
            }

            currentSimulationTime = priorityQueue.remove(); //Update current time
        }
    }

    private void unblockPreviousStages(Stage forwardStage)
    {
        // forward stage is now empty and can attempt to take from it's inbound storage
        // which must be full, because we are currently blocked
        forwardStage.retrieveItemFromInboundStorage();
        forwardStage.startProcessingItem();

        // there is now space in our outbound storage queue to send our item onwards
        Stage previousStage = forwardStage.getBackwardStage();
        if(previousStage != null && previousStage.isBlocked())
        {
            previousStage.unblock();
            previousStage.sendToOutboundStorage();

            // we should now be in the empty state
            unblockPreviousStages(previousStage);

        }
    }
}
