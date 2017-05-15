import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Jordan on 15-May-17.
 */
public class Stage implements IObservable
{
    private double m;
    private double n;
    private int multiplier;

    private int stageID;
    private double p;
    private StageStates state;

    private InterStageStorage inboundStorage;
    private InterStageStorage outboundStorage;

    public Stage(double m, double n, int multiplier)
    {
        this.m = m;
        this.n = n;
        this.multiplier = multiplier;
        state = StageStates.STARVED;
    }

    public Stage(double m, double n, int multiplier, int stageID)
    {
        //Since stag id is being passed through, we know that it will be one of the starting stages
        //Therefore the state should start at empty
        this(m, n, multiplier);
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

    /**
     * Calculation
     */
    private double calculatePValue()
    {
        Random r = new Random();
        double d = r.nextDouble();
        return (m * multiplier) + ((n * multiplier) * (d - 0.5));
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

    public void startProcessingItem(Item item)
    {
        state = StageStates.PROCESSING;

        //calc p value for random - as per spec
        p = calculatePValue();
        notifyAllObservers(); //Notify of updated p value to determine T2
    }

    public void finishProcessingItem(Item item)
    {
        //Where currentTime >= finishTime
        state = StageStates.FINISHEDPROCESSING;

        sendToOutboundStorage(item);
        retrieveItemFromInboundStorage();
    }

    public void starve()
    {
        //Inbound Queue is Empty = starve()
        state = StageStates.STARVED;
    }

    public void unstarve()
    {
        state = StageStates.EMPTY;

        retrieveItemFromInboundStorage();
    }

    public void retrieveItemFromInboundStorage()
    {
        if (inboundStorage.isEmpty())
        {
            starve();
        }
        else
        {
            //Take item from inbound queue ready for processing
            Item newItem = inboundStorage.dequeue();

            //State ready for processing
            state = StageStates.READY;
            startProcessingItem(newItem);
        }
    }

    public void block()
    {
        //Item cannot be passed to the next queue because it is full
        //Needs to stay in the current stage until room is available
        state = StageStates.BLOCKED;
    }

    public void unblock(Item item)
    {
        //Outbound Queue is not full
        state = StageStates.FINISHEDPROCESSING;

        sendToOutboundStorage(item);
    }

    public void sendToOutboundStorage(Item item)
    {
        //Stage is Empty
        //Finished processing - move item to following queue

        if (outboundStorage.isFull())
            block();
        else {
            //Item can be enqueued in the following queue
            outboundStorage.enqueue(item);
            //Set state back to empty
            state = StageStates.EMPTY;
        }
    }


    /**
     * Observer pattern implementation
     * SUBJECT OF SIMULATION OBSERVER
     * https://www.tutorialspoint.com/design_pattern/observer_pattern.htm
     */
    private List<IObserver> observers = new ArrayList<>();

    public void attach(IObserver observer)
    {
        observers.add(observer);
    }

    public void notifyAllObservers()
    {
        ObservableMessage pValueMessage = new ObservableMessage(this.p);
        for (IObserver observer : observers)
        {
            observer.update(pValueMessage);
        }
    }

}
