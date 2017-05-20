import java.util.Random;

/**
 * Created by Jordan on 15-May-17.
 */
public class Stage
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


    private Stage forwardStage;
    private Stage backwardStage;


    /*Passing simulation through to access currentSimulationTime*/
    private Simulation simulation;
    private double finishProcessingTime;

    public Stage(double m, double n, int multiplier, Simulation simulation, String name)
    {
        this.m = m;
        this.n = n;
        this.multiplier = multiplier;
        state = StageStates.STARVED;
        this.simulation = simulation;
        this.name = name;
    }

    public Stage(double m, double n, int multiplier, int stageID, Simulation simulation, String name)
    {
        //Since stag id is being passed through, we know that it will be one of the starting stages
        //Therefore the state should start at empty
        this(m, n, multiplier, simulation, name);
        this.stageID = stageID;
        state = StageStates.EMPTY;
    }

    /**
     * Getters and Setters
     */
    public InterStageStorage getInboundStorage() {
        return inboundStorage;
    }

    public void setInboundStorage(InterStageStorage inboundStorage) {
        this.inboundStorage = inboundStorage;
    }

    public InterStageStorage getOutboundStorage() {
        return outboundStorage;
    }

    public void setOutboundStorage(InterStageStorage outboundStorage) {
        this.outboundStorage = outboundStorage;
    }

    public int getStageID() {
        return stageID;
    }

    public double getFinishProcessingTime(){return finishProcessingTime; }

    public Stage getForwardStage() {return forwardStage; }

    public Stage getBackwardStage() {return backwardStage; }


    public void addStages(Stage backwardStage, Stage forwardStage)
    {
        this.backwardStage = backwardStage;
        this.forwardStage = forwardStage;
    }

    @Override
    public String toString() {
        return this.name + " [" + this.state + "]" + (isProcessing() ? " (Finishes at " + finishProcessingTime + ")": "");
    }


    /**
     * Calculation
     */
    private double calculatePValue()
    {
        Random r = new Random();
        double d = r.nextDouble();

        // TODO: Remove fake number and replace with actual function
        return multiplier;

        //return (m * multiplier) + ((n * multiplier) * (d - 0.5));
    }


    /**
     * States and Processing
     */
    public boolean isStarved()
    {
        return state.equals(StageStates.STARVED);
    }

    public boolean isBlocked()
    {
        return state.equals(StageStates.BLOCKED);
    }

    public boolean isProcessing() { return state.equals(StageStates.PROCESSING); }

    public boolean isEmpty() {return state.equals(StageStates.EMPTY); }

    public boolean isReady() {return state.equals(StageStates.READY); }

    public boolean isFinishedProcessing() {return state.equals(StageStates.FINISHEDPROCESSING); }

    public boolean stageIsFinished() { return simulation.getCurrentSimulationTime() >= finishProcessingTime; }

    public void startProcessingItem()
    {
        state = StageStates.PROCESSING;

        //calc p value for random - as per spec
        p = calculatePValue();
        finishProcessingTime = simulation.getCurrentSimulationTime() + p;
        simulation.notifyOfFinishProcessingTime(finishProcessingTime);
    }

    public void finishProcessingItem()
    {
        state = StageStates.FINISHEDPROCESSING;

    }

    public void starve()
    {
        //Inbound Queue is Empty = starve()
        if(isEmpty())
            state = StageStates.STARVED;
        else
            throw new IllegalStateException("Trying to starve when the state is not empty");
    }

    public void unstarve()
    {
        if(isStarved())
            state = StageStates.EMPTY;
        else
            throw new IllegalStateException("Trying to unstarve when the state is not starving");
    }

    public void retrieveItemFromInboundStorage()
    {
        if(!isEmpty())
        {
            throw new IllegalStateException("Trying to add item to a stage that is processing");
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
                this.item = inboundStorage.dequeue();

                //State ready for processing
                state = StageStates.READY;
            }
        }
    }

    public void block()
    {
        //Item cannot be passed to the next queue because it is full
        //Needs to stay in the current stage until room is available
        state = StageStates.BLOCKED;
    }

    public void unblock()
    {
        //Outbound Queue is not full
        if(isBlocked())
            state = StageStates.FINISHEDPROCESSING;
        else
            throw new IllegalStateException("Trying to unblock when the state is not blocked");

    }

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
                //Item can be enqueued in the following queue
                outboundStorage.enqueue(item);
                //Set state back to empty
                state = StageStates.EMPTY;
            }
        }
    }


}
