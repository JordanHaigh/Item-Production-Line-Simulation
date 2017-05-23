/**
 * InfiniteInboundStorage Class - Extends from InterStageStorage and overrides some functionality
 */
public class InfiniteInboundStorage extends InterStageStorage
{
    /**
     * Overloaded Constructor
     * @param inboundStage - MasterStage InboundStage from the current Storage Object
     * @param outboundStage - MasterStage OutboundStage from the current Storage Object
     * @param simulation - Simulation class that runs the program
     * @param name - Name of the InfiniteInboundStorage
     */
    public InfiniteInboundStorage(MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name)
    {
        super(inboundStage, outboundStage, simulation, name);
    }

    /**
     * public Item dequeueWithStageID(int id)
     * @param id - Identification of the Stage that is dequeuing the item
     * @return - Returns a new item with the current simulation time and the stage id to be appended to the uniqueID
     */
    public Item dequeueWithStageID(int id)
    {
        Item newItem = new Item(id, simulation.getCurrentSimulationTime());
        System.out.println(String.format("Time %1$s: Generating a new Item with id %2$s",
                simulation.getCurrentSimulationTime(), newItem.getUniqueID()));
        return newItem;
    }

    /**
     * public boolean isEmpty()
     * Overridden function that will always return false (Since the inbound is always going to be able to generate an item)
     * @return - Return false
     */
    @Override
    public boolean isEmpty()
    {
        return false;
    }

}
