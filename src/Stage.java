import java.util.Comparator;
import java.util.Random;

/**
 * Stage Class - Implements core functionality for the processing of an item. Uses a stateStage enumeration to
 * determine what state the current Stage is in.
 */
public class Stage implements Comparable<Stage>
{
    private double m;
    private double n;
    private int multiplier;

    private String name;

    private int stageID;
    private double p;
    private StageStates state;
    private Item item;

    private InterStageStorage inboundStorage;
    private InterStageStorage outboundStorage;

    private MasterStage parent;


    private int itemCreationTally = 0;

    private double timeStartProcessing = 0;
    private double timeFinishedProcessing = 0;

    private double timeStartStarving = 0;
    private double timeFinishStarving = 0;

    private double timeStartBlocking = 0;
    private double timeFinishBlocking = 0;

    /*Passing simulation through to access currentSimulationTime*/
    private Simulation simulation;
    private double finishProcessingTime;

    /**
     * Overloaded Constructor
     * @param multiplier - Multiplier for the current stage
     * @param simulation - Current Simulation object running the simulation
     * @param name - Name of the Stage
     */
    public Stage(int multiplier, Simulation simulation, String name)
    {
        this.m = simulation.getM();
        this.n = simulation.getN();
        this.multiplier = multiplier;
        state = StageStates.STARVED;
        this.simulation = simulation;
        this.name = name;
    }

    /**
     * Overloaded Constructor
     * @param multiplier - Multiplier for the current stage
     * @param stageID - Stage ID for the current stage ID
     * @param simulation - Current Simulation Object running the simulation
     * @param name - Name of the Stage
     */
    public Stage(int multiplier, int stageID, Simulation simulation, String name)
    {
        //Since stag id is being passed through, we know that it will be one of the starting stages
        //Therefore the state should start at empty
        this(multiplier, simulation, name);
        this.stageID = stageID;
        state = StageStates.EMPTY;
    }

    /*****************************************GETTERS AND SETTERS*****************************************/

    //public InterStageStorage getInboundStorage() { return inboundStorage; }

    //public InterStageStorage getOutboundStorage() { return outboundStorage; }

    /**
     * public void setInboundStorage(InterStageStorage inboundStorage)
     * @param inboundStorage - Inbound Storage to be set
     */
    public void setInboundStorage(InterStageStorage inboundStorage) { this.inboundStorage = inboundStorage; }

    /**
     * public void setOutboundStorage(InterStageStorage outboundStorage)
     * @param outboundStorage - Outbound Storage to be set
     */
    public void setOutboundStorage(InterStageStorage outboundStorage) {
        this.outboundStorage = outboundStorage;
    }

    /**
     * public int getStageID()
     * @return - Returns Stage ID
     */
    public int getStageID() { return stageID; }

    /**
     * public double getFinishProcessingTime()
     * @return - Returns the Finish Processing Time
     */
    public double getFinishProcessingTime() { return finishProcessingTime; }

    /**
     * public MasterStage getParent()
     * @return - Returns the MasterStage Parent
     */
    public MasterStage getParent() { return parent; }

    /**
     * public void setParent(MasterStage parent)
     * @param parent - Sets the input parameter parent to the parent variable
     */
    public void setParent(MasterStage parent) { this.parent = parent; }

    /**
     * public String getName()
     * @return - Returns the name of the Stage
     */
    public String getName() { return name; }

    /**
     * public double getTimeFinishedProcessing()
     * @return - Returns the timeFinishedProcessing
     */
    public double getTimeFinishedProcessing() { return timeFinishedProcessing; }

    /**
     * public double getTimeFinishStarving()
     * @return - Returns the Time Finished Starving
     */
    public double getTimeFinishStarving() { return timeFinishStarving; }

    /**
     * public double getTimeFinishBlocking()
     * @return - Returns the Time Finished Blocking
     */
    public double getTimeFinishBlocking() { return timeFinishBlocking; }

    /**
     * public int getItemCreationTally()
     * @return - Returns the ItemCreationTally
     */
    public int getItemCreationTally() { return itemCreationTally; }

    /**
     * public String toString()
     * @return - Returns a String of the Stage name and whether it is processing
     */
    @Override
    public String toString() {
        return getName() + " [" + this.state + "]" + (isProcessing() ? " (Finishes at " + finishProcessingTime + ")": "");
    }

    /***************************************** CALCULATION *****************************************/

    /**
     * private double calculatePValue()
     * Calculates the P value for the current stage to determine how long an item will be processing for
     * @return - Returns the double calculation according the spec
     */
    private double calculatePValue()
    {
        Random r = simulation.getRandomSeed();
        double d = r.nextDouble();


        return (m * multiplier) + ((n * multiplier) * (d - 0.5));
    }


    /***************************************** QUERY *****************************************/

    /**
     * public boolean isStarved()
     * @return - Returns true or false whether the state is in the starved state
     */
    public boolean isStarved()
    {
        return state.equals(StageStates.STARVED);
    }

    /**
     * public boolean isBlocked()
     * @return - Returns true or false whether the state is in the blocked state
     */
    public boolean isBlocked()
    {
        return state.equals(StageStates.BLOCKED);
    }

    /**
     * public boolean isProcessing()
     * @return - Returns true or false whether the state is in the processing state
     */
    public boolean isProcessing() { return state.equals(StageStates.PROCESSING); }

    /**
     * public boolean isEmpty()
     * @return - Returns true or false whether the state is in the empty state
     */
    public boolean isEmpty() {return state.equals(StageStates.EMPTY); }

    /**
     * public boolean isReady()
     * @return - Returns true or false whether the state is the ready state
     */
    public boolean isReady() {return state.equals(StageStates.READY); }

    /**
     * public boolean isFinishedProcessing()
     * @return - Returns true or false whether the state is in the finishedProcessing state
     */
    public boolean isFinishedProcessing() {return state.equals(StageStates.FINISHEDPROCESSING); }

    /**
     * public boolean stageIsFinished()
     * @return - Returns true or false whether the current simulation time is greater than or equal to the finish processing time
     */
    public boolean stageIsFinished() { return simulation.getCurrentSimulationTime() >= finishProcessingTime; }

    /**
     * public void startProcessingItem()
     * State is updated to processing, p value is calculated and added to the priority queue in the simulation class
     * Time start processing is updated to the current time
     */

    /*****************************************STATE CHANGES*****************************************/

    public void startProcessingItem()
    {
        state = StageStates.PROCESSING;

        //calc p value for random - as per spec
        this.p = calculatePValue();
        finishProcessingTime = simulation.getCurrentSimulationTime() + p;

        if(finishProcessingTime < simulation.getCurrentSimulationTime())
        {
            throw new RuntimeException("Error. Finish Processing Time is less than the current simulation time. Review Input params."
                    +"[Current Simulation Time: " + simulation.getCurrentSimulationTime() + "] [Finish ProcessingTime " + finishProcessingTime + "] ");
        }

        simulation.notifyOfFinishProcessingTime(finishProcessingTime);

        /*System.out.println(String.format("Time %1$s: Stage %2$s starting processing. Will complete at %3$s",
                simulation.getCurrentSimulationTime(), this.name, finishProcessingTime));*/

        timeStartProcessing = simulation.getCurrentSimulationTime();
        //item.setCreationTime(timeStartProcessing);
    }

    /**
     * public void finishProcessingItem()
     * Checks whether the state is in the processing state as a precondition
     * Updates the state and updates relevant data statistics
     *
     * Throws an exception if not in the processing state
     */
    public void finishProcessingItem()
    {
        if(isProcessing())
        {
            state = StageStates.FINISHEDPROCESSING;
            double finishTime = simulation.getCurrentSimulationTime() - timeStartProcessing;
            timeFinishedProcessing += finishTime;
            //item.updateFinishedProductionLineTime(finishTime);
        }
        else
            throw new IllegalStateException("Trying to finish processing when stage is in  " + state.name() + " state");
    }

    /**
     * public void starve()
     * Checks whether the state is in the empty state as a precondition
     * Updates the state to starved and updates relevant data statistics
     *
     * Throws an exception if not in the empty state
     */
    public void starve()
    {
        //Inbound Queue is Empty = starve()
        if(isEmpty())
        {
            state = StageStates.STARVED;
            timeStartStarving = simulation.getCurrentSimulationTime();
        }
        else
            throw new IllegalStateException("Trying to starve when the state is not empty");
    }

    /**
     * public void unstarve()
     * Checks whether the state is in the starved state as a precondition
     * Updates the state to empty and updates relevant data statistics
     *
     * Throws an exception if not in the starved state
     */
    public void unstarve()
    {
        if(isStarved())
        {
            state = StageStates.EMPTY;
            timeFinishStarving += simulation.getCurrentSimulationTime() - timeStartStarving;
            //item.updateTotalStarveTime(timeFinishStarving);
        }
        else
            throw new IllegalStateException("Trying to unstarve when the state is not starving");
    }

    /**
     * public void retrieveItemFromInboundStorage()
     * Checks whether the state is in the empty state, throws an exception if not in the empty state
     * Checks whether the inbound storage is empty, if empty the stage starves
     * Checks whether the inbound storage is an instance of the infinite inbound storage
     *      If true, uses the dequeueWithStageID method
     * Dequeues an item from the inbound storage  and sets the state to ready
     */
    public void retrieveItemFromInboundStorage()
    {
        if(!isEmpty())
        {
            throw new IllegalStateException("Trying to add item to a stage that is in the " + state.name() + " state");
        }
        else
        {
            if (inboundStorage.isEmpty())
            {
                starve();
            }
            else
            {

                //Take item from inbound queue ready for processing
                if(inboundStorage instanceof InfiniteInboundStorage)
                {
                    //typecast to access the dequeue with stage id method
                    this.item = ((InfiniteInboundStorage) inboundStorage).dequeueWithStageID(this.stageID);
                    itemCreationTally++;
                }
                else
                    this.item = inboundStorage.dequeue();

                /*System.out.println(String.format("Time %1$s: Stage %2$s taking item %3$s from InterStage Storage Queue %4$s",
                        simulation.getCurrentSimulationTime(), this.name, item.getUniqueID(), inboundStorage.getName()));*/

                //State ready for processing
                state = StageStates.READY;
            }
        }
    }

    /**
     * public void block()
     * Checks whether the state is in the FinishedProcessed state
     * Updates the state to blocked and updates relevant statistics
     *
     * Throws an exception if not in the finishedProcessing state
     */
    public void block()
    {
        //Item cannot be passed to the next queue because it is full
        //Needs to stay in the current stage until room is available

        if(isFinishedProcessing())
        {
            state = StageStates.BLOCKED;
            timeStartBlocking = simulation.getCurrentSimulationTime();
        }
        else
            throw new IllegalStateException("Trying to block from the incorrect state");
    }

    /**
     * public void unblock()
     * Checks if the state is in the blocked state
     * Updates the state to finishedProcess and updates relevant statistics
     *
     * Throws an exception if not in the blocked state
     */
    public void unblock()
    {
        //Outbound Queue is not full
        if(isBlocked())
        {
            state = StageStates.FINISHEDPROCESSING;
            timeFinishBlocking += simulation.getCurrentSimulationTime() - timeStartBlocking;
            //item.updateTotalBlockTime(timeFinishBlocking);
            //item.updateIdleTime(timeFinishBlocking);
        }
        else
            throw new IllegalStateException("Trying to unblock when the state is not blocked");

    }

    /**
     * public void sendToOutboundStorage()
     * Checks if the stage is in the finishedProcessing state - Throws exception if false
     * Checks if the outbound storage is full - State updated to block if true
     * Enqueues an item to the outbound storage
     * State updated to empty
     */
    public void sendToOutboundStorage()
    {
        //Stage is Empty
        //Finished processing - move item to following queue
        if(!isFinishedProcessing())
        {
            throw new IllegalStateException("Item has not finished processing");
        }
        else
        {
            if (outboundStorage.isFull())
                block();
            else
            {

                /*System.out.println(String.format("Time %1$s: Stage %2$s sending item %3$s to InterStage Storage Queue %4$s",
                        simulation.getCurrentSimulationTime(), this.name, item.getUniqueID(), outboundStorage.getName()));*/

                //Item can be enqueued in the following queue
                outboundStorage.enqueue(item);
                //Set state back to empty
                state = StageStates.EMPTY;
            }
        }
    }

    /*****************************************COMPARATOR*****************************************/



    public static Comparator<Stage> StageFinishTimeComparator = new Comparator<Stage>()
    {
        /**
         * public int compare(Stage s1, Stage s2)
         * Compares the finishProcessing time of the two parameters and returns an integer whether its
         * Greater than (>0), Less than (<0) or Equal (0)
         * @param s1 - First Stage for comparison
         * @param s2 - Second Stage for comparison
         * @return - Integer determining whether less than, greater than or equal to 0
         */
        @Override
        public int compare(Stage s1, Stage s2)
        {
            return s1.compareTo(s2);
        }
    };

    /**
     * public int compareTo(Stage stage)
     * @param stage - Stage for comparison
     * @return - Returns -1,1,0 where the stage finish time is less than, equal to or greater than
     */
    @Override
    public int compareTo(Stage stage)
    {
        double compareFinishTime = stage.getFinishProcessingTime();

        //Since we are working with doubles for time, this needs to be typecasted for the overriden compareTo method
        int result = (int)(this.getFinishProcessingTime() - compareFinishTime);
        if(result < 0 || result > 0)
            return result;
        else
        {
            //If two stage times are equal (Result is zero), we sort by stageID's to determine which will come first
            int compareStageID = stage.getStageID();

            if(this.getStageID() < compareStageID)
                return -1;
            else if(this.getStageID() > compareStageID)
                return 1;
            else
                return 0;
        }
    }


}
