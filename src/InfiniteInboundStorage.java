/**
 * Created by Jordan on 15-May-17.
 */
public class InfiniteInboundStorage extends InterStageStorage
{
    private double currentTime;
    private Simulation simulation;

    /**InfiniteInboundStorage inbound = new InfiniteInboundStorage(null, s0)**/
    public InfiniteInboundStorage(MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        super(inboundStage, outboundStage, name);
        this.simulation = simulation;
    }

    public Item dequeueWithStageID(int id)
    {
        return new Item(id, simulation.getCurrentSimulationTime());
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

}
