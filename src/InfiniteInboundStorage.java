/**
 * Created by Jordan on 15-May-17.
 */
public class InfiniteInboundStorage extends InterStageStorage
{
    private double currentTime;

    /**InfiniteInboundStorage inbound = new InfiniteInboundStorage(null, s0)**/
    public InfiniteInboundStorage(MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        super(inboundStage, outboundStage, simulation, name);
    }

    public Item dequeueWithStageID(int id)
    {
        Item newItem = new Item(id, simulation.getCurrentSimulationTime());
        System.out.println(String.format("Time %1$s: Generating a new Item with id %2$s",
                simulation.getCurrentSimulationTime(), newItem.getUniqueID()));
        return newItem;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

}
