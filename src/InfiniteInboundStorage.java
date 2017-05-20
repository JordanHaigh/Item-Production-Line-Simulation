/**
 * Created by Jordan on 15-May-17.
 */
public class InfiniteInboundStorage extends InterStageStorage
{
    private double currentTime;
    private Simulation simulation;

    /**InfiniteInboundStorage inbound = new InfiniteInboundStorage(null, s0)**/
    public InfiniteInboundStorage(Stage inboundStage, Stage outboundStage, Simulation simulation, String name)
    {
        super(inboundStage, outboundStage, name);
        this.simulation = simulation;
    }

    @Override
    public Item dequeue()
    {
        return new Item(outboundStage.getStageID(), simulation.getCurrentSimulationTime());
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

}
