/**
 * Created by Jordan on 15-May-17.
 */

import java.util.PriorityQueue;
import java.util.LinkedList;

public class Simulation
{
    private double m, n;
    private int qMax;

    private Stage s0a, s0b, s1a, s2a, s3a, s3b, s4a, s5a, s5b, s6a;
    private MasterStage s0 = new MasterStage("s0M");
    private MasterStage s1= new MasterStage("s1M");
    private MasterStage s2 = new MasterStage("s2M");
    private MasterStage s3 = new MasterStage("s3M");
    private MasterStage s4 = new MasterStage("s4M");
    private MasterStage s5 = new MasterStage("s5M");
    private MasterStage s6 = new MasterStage("s6M");
    private LinkedList<MasterStage> masterStages = new LinkedList<>();

    private InterStageStorage q01, q12, q23,q34,q45,q56;
    private LinkedList<InterStageStorage> queues = new LinkedList<>();
    private InfiniteInboundStorage inboundItemStorage;
    private InfiniteOutboundStorage outboundItemStorage;

    private double currentSimulationTime = 0; //Start at 0
    public final int MAX_SIMULATION_TIME = 10000000;
    private PriorityQueue<Double> timePriorityQueue = new PriorityQueue<>();

    /**
     * Overloaded Constructor
     * Creates the simulation according to the assignment specification and creates all necessary links
     * @param m - Mean value passed through from command line
     * @param n - Range value passed through from command line
     * @param qMax - QMax Size passed through from the command line
     */
    public Simulation(double m, double n, int qMax)
    {
        this.m = m;
        this.n = n;
        this.qMax = qMax;

        //Create and link simulation
        initialiseStages();

        //Add substages to each master stage
        addSubstagesToMasterStages();

        //Link master stages and its substages
        linkMasterStages();

        //Link Queue to stage
       linkQueuesToStages();

        //Link Stages to Queues
        linkStagesToQueues();

        //Populate MasterStages LL with the current MasterStages
        masterStages.addLast(s0);
        masterStages.addLast(s1);
        masterStages.addLast(s2);
        masterStages.addLast(s3);
        masterStages.addLast(s4);
        masterStages.addLast(s5);
        masterStages.addLast(s6);

        //Populate Queues LL with the current InterStageStorages
        queues.addLast(q01);
        queues.addLast(q12);
        queues.addLast(q23);
        queues.addLast(q34);
        queues.addLast(q45);
        queues.addLast(q56);
    }

    /************************************CONSTRUCTOR PRIVATE CREATION AND LINKS************************************/

    /**
     * private void initialiseStages()
     * Initialises all stages necessary according to the specification
     * Constructors include the multiplier for each stage and the current simulation to determine the M and N values
     */
    private void initialiseStages()
    {
        s0a = new Stage(2, 0, this, "S0a");
        s0b = new Stage(1, 1, this, "S0b");
        s1a = new Stage(1, this, "S1");
        s2a = new Stage(1, this, "S2");
        s3a = new Stage(2, this, "s3a");
        s3b = new Stage(2, this, "s3b");
        s4a = new Stage(1, this, "s4");
        s5a = new Stage(2, this, "s5a");
        s5b = new Stage(2, this, "s5b");
        s6a = new Stage(1, this, "s6");
    }

    /**
     * private void addSubstagesToMasterStages()
     * Adds each relevant Stage to its MasterStage
     */
    private void addSubstagesToMasterStages()
    {
        s0.addSubStage(s0a);
        s0.addSubStage(s0b);

        s1.addSubStage(s1a);

        s2.addSubStage(s2a);

        s3.addSubStage(s3a);
        s3.addSubStage(s3b);

        s4.addSubStage(s4a);

        s5.addSubStage(s5a);
        s5.addSubStage(s5b);

        s6.addSubStage(s6a);
    }

    /**
     * private void linkMasterStages()
     * Sets the forward and backward masterStage relevant to the assignment specification
     */
    private void linkMasterStages()
    {
        s0.setForwardMasterStage(s1);

        s1.setForwardMasterStage(s2);
        s1.setBackwardMasterStage(s0);

        s2.setForwardMasterStage(s3);
        s2.setBackwardMasterStage(s1);

        s3.setForwardMasterStage(s4);
        s3.setBackwardMasterStage(s2);

        s4.setForwardMasterStage(s5);
        s4.setBackwardMasterStage(s3);

        s5.setForwardMasterStage(s6);
        s5.setBackwardMasterStage(s4);

        s6.setBackwardMasterStage(s5);
    }

    /**
     * private void linkQueuesToStage()
     * Initialises and sets each queues to its relevant forward and backward masterstages
     */
    private void linkQueuesToStages()
    {
        q01 = new InterStageStorage(qMax, s0, s1, this,"q01");
        q12 = new InterStageStorage(qMax, s1, s2, this, "q12");
        q23 = new InterStageStorage(qMax, s2, s3, this, "q23");
        q34 = new InterStageStorage(qMax, s3, s4, this, "q34");
        q45 = new InterStageStorage(qMax, s4, s5, this, "q45");
        q56 = new InterStageStorage(qMax, s5, s6, this,"q56");

        inboundItemStorage = new InfiniteInboundStorage(null, s0, this, "Infinite Inbound");
        outboundItemStorage = new InfiniteOutboundStorage(s6, null, this,  "Infinite Outbound");
    }

    /**
     * private void linkStagesToQueues()
     * Sets each Masterstage's storages to its relevant forward and backward InterStageStorage's
     */
    private void linkStagesToQueues()
    {
        s0.setInboundStorage(inboundItemStorage);
        s0.setOutboundStorage(q01);

        s1.setInboundStorage(q01);
        s1.setOutboundStorage(q12);

        s2.setInboundStorage(q12);
        s2.setOutboundStorage(q23);

        s3.setInboundStorage(q23);
        s3.setOutboundStorage(q34);

        s4.setInboundStorage(q34);
        s4.setOutboundStorage(q45);

        s5.setInboundStorage(q45);
        s5.setOutboundStorage(q56);

        s6.setInboundStorage(q56);
        s6.setOutboundStorage(outboundItemStorage);
    }

    /*****************************************GETTERS AND SETTERS*****************************************/

    /**
     * public double getCurrentSimulationTime()
     * @return - Returns the current simulation time
     */
    public double getCurrentSimulationTime() { return currentSimulationTime; }

    /**
     * public double getM()
     * @return - Returns mean value
     */
    public double getM() {return m;}

    /**
     * public double getN()
     * @return - Returns the range value
     */
    public double getN() {return n;}

    /**
     * public void notifyOfFinishProcessingTime(double finishProcessingTime)
     * Sends the finishProcessingTime parameter to a priority queue for discrete event simulation
     * @param finishProcessingTime - FinishProcessingTime to be added to the priority queue
     */
    public void notifyOfFinishProcessingTime(double finishProcessingTime)
    {
        timePriorityQueue.add(finishProcessingTime);
    }

    /*****************************************CALCULATION*****************************************/

    /**
     * private double calculateStageProcessingTimePercentage(Stage s, double totalFinishTime)
     * @param s - Current Stage to determine statistic for processing
     * @param totalFinishTime - Total finish time of the program
     * @return - Percentage value of total time processing
     */
    private double calculateStageProcessingTimePercentage(Stage s, double totalFinishTime)
    {
        return (s.getTimeFinishedProcessing()/totalFinishTime) * 100;
    }

    /**
     * private double calculateStageStarvingTimePercentage(Stage s, double totalFinishTime)
     * @param s - Current stage to determine statistic for starving
     * @param totalFinishTime - Total finish time of the program
     * @return - Percentage value of total time starving
     */
    private double calculateStageStarvingTimePercentage(Stage s, double totalFinishTime)
    {
        return (s.getTimeFinishStarving()/totalFinishTime) * 100;
    }

    /**
     * private double calculateStageBlocking(Stage s, double totalFinishTime)
     * @param s - Current Stage to determine statistic for blocking
     * @param totalFinishTime - Total finish time of the program
     * @return - Percentage value of total time blocking
     */
    private double calculateStageBlockingTimePercentage(Stage s, double totalFinishTime)
    {
        return (s.getTimeFinishBlocking()/totalFinishTime) * 100;
    }

    /*****************************************SIMULATING*****************************************/

    /**
     * public void startProcessing()
     * Starts the processing of the simulation
     * Uses a while loop for to iterate up to the max simulation time using discrete event simulation
     */
    public void startProcessing()
    {
        double tempRemoval;
        while (currentSimulationTime < MAX_SIMULATION_TIME)
        {
            for(MasterStage m: masterStages)
            {
                for(Stage s: m.getSubstagesInSortedOrder())
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
            tempRemoval = timePriorityQueue.remove(); //Update current time
            if(tempRemoval >=  MAX_SIMULATION_TIME)
                break;
            else
            {
                currentSimulationTime = tempRemoval;

                System.out.println(String.format("=== Updating Time to %1$s ===", currentSimulationTime));

            }
        }
    }

    /**
     * private void checkStageContents(Stage s)
     * Checks the contents of a current stage, whether the stage is processing, finished processing or blocked
     * @param s - Current stage to check
     */
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

    /**
     * private void stageHasFinishedProcessing(Stage s)
     * Called when the stage has finished processing an item and attempts to send to the outbound storage
     * Retrieves a new item if the current state is not blocked
     * @param s - Current stage processing the item
     */
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

    /**
     * private void checkForwardStage(Stage s)
     * Checks the forward masterstage to determine if a substage is empty or finished processing
     * Calls another private method depending on the state of the current stage
     * @param s - Current stage checking if we can unblock from the forward master stage
     */
    private void checkForwardStage(Stage s)
    {
        MasterStage forwardStage = s.getParent().getForwardMasterStage();
        if(forwardStage != null)
        {
            for(Stage substage: forwardStage.getSubstagesInSortedOrder())
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

    /**
     * private void forceUnblock(Stage s, Stage forwardStage)
     * Unblocks from the current stage by passing an item through to the forwardStage
     * Starts to recursively move backwards to unblock as many stages as it can (In order of what stage was blocked first)
     * @param s - Current Stage to attempt to unblock
     * @param forwardStage - Forward Stage to unblock from
     */
    private void forceUnblock(Stage s, Stage forwardStage)
    {
        //The forward stage can take an item from the intermediate queue and free a position
        // for the current stage to enqueue
        if (forwardStage.isStarved())
            forwardStage.unstarve();

        forwardStage.retrieveItemFromInboundStorage();

        //Now the current stage is able to pass the object
        s.unblock();
        s.sendToOutboundStorage(); //The current stage will now be set to empty

        //Now need to recursively move backwards in the from the current stage to determine whether
        //Previous stages can unblock if necessary
        if (!s.isBlocked())
            unblockPreviousStages(s);
    }

    /**
     * private void finishProcessingForwardStage(Stage forwardStage)
     * Updates the forwardStage state and attempts to send the item to the outbound storage
     * If that forward stage state becomes empty (Or not blocked) it will attempt to unblock previous stages
     * @param forwardStage
     */
    private void finishProcessingForwardStage(Stage forwardStage)
    {
        forwardStage.finishProcessingItem();

        // we can now move the item in the forward stage to the outbound storage, if it is empty
        forwardStage.sendToOutboundStorage();

        // forward stage is now either empty, or blocked
        if (!forwardStage.isBlocked())
            unblockPreviousStages(forwardStage);
    }

    /**
     * private void unblockPreviousStages(Stage forwardStage)
     * Recursively attempts to unblock the forward stage and move an item from the current stage to its InterStageStorage
     * @param forwardStage - Stage to retrieve and start processing an item
     */
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
            for(Stage substage: previousStage.getSubstagesInSortedOrder())
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


    /**
     * public void runDataStatistics()
     * Creates the table for all statistics required in the assignment specification
     * Determines Stage Statistics, Queue Statistics and the number of items processed
     */
    public void runDataStatistics()
    {
        double totalFinishTime = getCurrentSimulationTime();

        System.out.println("Station | %Processing \t | %Starving \t | %Blocked \t");

        for(MasterStage m: masterStages)
        {
            for(Stage s: m.getSubstages())
            {
                System.out.println(
                        s.getName() +
                        "\t \t| \t " +
                        String.format("%.5f",calculateStageProcessingTimePercentage(s, totalFinishTime)) +
                        "\t | \t " +
                        String.format("%.5f",calculateStageStarvingTimePercentage(s, totalFinishTime)) +
                        "\t | \t " +
                        String.format("%.5f",calculateStageBlockingTimePercentage(s, totalFinishTime))
                );
            }
        }

        System.out.println();

        //Queue output
        System.out.println("Queue | TimeAverage \t | ItemAverage");
        for(InterStageStorage q: queues)
        {
            System.out.println(
                    q.getName() +
                    "\t  | \t " +
                    String.format("%.5f", q.calculateAverageTimeInQueue()) +
                    "\t | \t " +
                    String.format("%.5f", q.calculateAverageNumberOfItemsInQueue())
            );
        }

        System.out.println();

        //S0 output
        for(Stage s: s0.getSubstages())
            System.out.println("Items created in " + s.getName() + ": " + s.getItemCreationTally());
    }
}
