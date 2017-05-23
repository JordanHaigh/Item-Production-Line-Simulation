import java.util.LinkedList;

/**
 * InfiniteOutboundStorage Class - Extends from InterStageStorage and overrides some functionality
 */
public class InfiniteOutboundStorage extends InterStageStorage
{
    LinkedList<Item> outboundList = new LinkedList<>();

    /**
     * Overloaded Constructor
     * @param inboundStage - MasterStage InboundStage from the current Storage Object
     * @param outboundStage - MasterStage OutboundStage from the current Storage Object
     * @param simulation - Simulation class that runs the program
     * @param name - Name of the InfiniteInboundStorage
     */
    public InfiniteOutboundStorage(MasterStage inboundStage, MasterStage outboundStage, Simulation simulation, String name) {
        super(inboundStage, outboundStage, simulation, name);
    }

    /**
     * public void enqueue(Item item)
     * Overrides the parent method implementation to always enqueue the item to the outbound list
     * @param item - Type item to be enqueued into the list
     */
    @Override
    public void enqueue(Item item) {
        outboundList.addLast(item);
    }

    /**
     * public boolean isFull()
     * Overrides the parent method implementation so that this class will never be full
     * @return - Return false
     */
    @Override
    public boolean isFull() {
        return false;
    }
}
