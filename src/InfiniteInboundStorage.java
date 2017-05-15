/**
 * Created by Jordan on 15-May-17.
 */
public class InfiniteInboundStorage extends InterStageStorage implements IObserver
{
    private Stage inboundStage;
    private Stage outboundStage;
    private double currentTime;
    private Simulation simulation;

    /**InfiniteInboundStorage inbound = new InfiniteInboundStorage(null, s0)**/
    public InfiniteInboundStorage(Stage inboundStage, Stage outboundStage, Simulation simulation)
    {
        super(inboundStage, outboundStage);
        this.simulation = simulation;
        initialiseObserver();
    }

    @Override
    public Item dequeue()
    {
        return new Item(outboundStage.getStageID(), currentTime);
    }

    /**
     * Observer implementation
     * OBSERVING SIMULATION FOR CURRENT TIME VALUE
     * https://www.tutorialspoint.com/design_pattern/observer_pattern.htm
     */

    @Override
    public void update(ObservableMessage currentTimeMessage)
    {
        this.currentTime = currentTimeMessage.getValue();
    }

    public void initialiseObserver()
    {
        simulation.attach(this);
    }
}
