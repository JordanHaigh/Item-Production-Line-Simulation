/**
 * Created by Jordan on 15-May-17.
 */

import java.util.PriorityQueue;
import java.util.LinkedList;

public class Simulation
{
    private double m, n;
    private int qMax;

    private Stage s0a, s0b, s1a, s2a; //s3,s4,s5,s6;
    private MasterStage s0 = new MasterStage("s0"), s1 = new MasterStage("s1"), s2 = new MasterStage("s2");
    private LinkedList<MasterStage> masterStages = new LinkedList<>();

    private InterStageStorage q01, q12; //, q23,q34,q45,q56;
    private InfiniteInboundStorage inboundItemStorage;
    private InfiniteOutboundStorage outboundItemStorage;

    private double currentSimulationTime = 0; //Start at 0
    public final int MAX_SIMULATION_TIME = 10000000;
    private PriorityQueue<Double> timePriorityQueue = new PriorityQueue<>();

    public Simulation(double m, double n, int qMax)
    {
        this.m = m;
        this.n = n;
        this.qMax = qMax;

        //Create and link simulation
        s0a = new Stage(this.m, this.n, 1, 0, this, "S0a");
        s0b = new Stage(this.m, this.n, 10, 1, this, "S0b");
        s1a = new Stage(this.m, this.n, 100, this, "S1a");
        s2a = new Stage(this.m, this.n, 1000, this, "S2a");

        //Add substages to each master stage
        s0.addSubStage(s0a);
        s0.addSubStage(s0b);

        s1.addSubStage(s1a);

        s2.addSubStage(s2a);

        //Link master stages and its substages
        s0.setForwardMasterStage(s1);

        s1.setForwardMasterStage(s2);
        s1.setBackwardMasterStage(s0);

        s2.setBackwardMasterStage(s1);

        //Link Queue to stage
        q01 = new InterStageStorage(qMax, s0, s1, "q01");
        q12 = new InterStageStorage(qMax, s1, s2, "q12");

        inboundItemStorage = new InfiniteInboundStorage(null, s0, this, "Infinite Inbound");
        outboundItemStorage = new InfiniteOutboundStorage(s2, null, "Infinite Outbound");

        //Link Stages to Queues
        s0.setInboundStorage(inboundItemStorage);
        s0.setOutboundStorage(q01);

        s1.setInboundStorage(q01);
        s1.setOutboundStorage(q12);

        s2.setInboundStorage(q12);
        s2.setOutboundStorage(outboundItemStorage);

        masterStages.addLast(s0);
        masterStages.addLast(s1);
        masterStages.addLast(s2);
    }

    public double getCurrentSimulationTime() {
        return currentSimulationTime;
    }

    public void notifyOfFinishProcessingTime(double finishProcessingTime)
    {
        timePriorityQueue.add(finishProcessingTime);
    }


    public void startProcessing()
    {

        //todo fix the parallel stages to eliminate priority on the a-stages
        //todo when two parallel stages are blocked, stage a will take priority over stage b
        ///todo may need a priorituy queue of items when this happens??
        while (currentSimulationTime <= MAX_SIMULATION_TIME)
        {
            for(MasterStage m: masterStages)
            {
                for(Stage s: m.getSubstagesInOrder())
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
                    else
                    {
                        //Stage is not empty
                        checkStageContents(s);
                    }
                }

                // All stages updated. Get the next event time.
                // this will be the minimum value in the priority queue
                }

            currentSimulationTime = timePriorityQueue.remove(); //Update current time
        }
    }



    private void checkStageContents(Stage s)
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
           stageHasFinishedProcessing(s);
        }

        if(s.isBlocked())
        {
            //Stage is blocked, we can't add an item to the forward queue
            //Check stage in front to determine whether it is not holding an item
            checkForwardStage(s);
        }

    }

    private void stageHasFinishedProcessing(Stage s)
    {
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

    private void checkForwardStage(Stage s)
    {
        MasterStage forwardStage = s.getParent().getForwardMasterStage();
        if(forwardStage != null)
        {
            for(Stage substage: forwardStage.getSubstages())
            {
                //Accounting for the last stage in the production line
                if(substage.isEmpty() || substage.isStarved())
                {
                    forceUnblock(s, substage);
                }

                if (substage.stageIsFinished() && !substage.isBlocked()) // currentTime >= finishTime
                {
                    finishProcessingForwardStage(substage);
                }
            }

        }
    }

    private void forceUnblock(Stage s, Stage forwardStage)
    {
        //The forward stage can take an item from the intermediate queue and free a position
        // for the current stage to enqueue
        forwardStage.retrieveItemFromInboundStorage();

        //Now the current stage is able to pass the object
        s.unblock();
        s.sendToOutboundStorage(); //The current stage will now be set to empty

        //Now need to recursively move backwards in the from the current stage to determine whether
        //Previous stages can unblock if necessary
        unblockPreviousStages(s);
    }


    private void finishProcessingForwardStage(Stage forwardStage)
    {
        forwardStage.finishProcessingItem();

        // we can now move the item in the forward stage to the outbound storage, if it is empty
        forwardStage.sendToOutboundStorage();

        // forward stage is now either empty, or blocked
        if (!forwardStage.isBlocked())
            unblockPreviousStages(forwardStage);
    }



    private void unblockPreviousStages(Stage forwardStage)
    {
        // forward stage is now empty and can attempt to take from it's inbound storage
        // which must be full, because we are currently blocked
        forwardStage.retrieveItemFromInboundStorage();
        forwardStage.startProcessingItem();

        // there is now space in our outbound storage queue to send our item onwards
        MasterStage previousStage = forwardStage.getParent().getBackwardMasterStage();
        if(previousStage != null)
        {
            for(Stage substage: previousStage.getSubstages())
            {
                if(substage.isBlocked())
                {
                    substage.unblock();
                    substage.sendToOutboundStorage();

                    // if this was a single substage master stage, then we should be in the empty state
                    // but if we are in a multiple substage master stage, then we can still be blocked.
                    // There is no point in attempting to unblock any previous stages from here, if we are blocked
                    if (!substage.isBlocked())
                        unblockPreviousStages(substage);
                }
            }
        }
    }
}
