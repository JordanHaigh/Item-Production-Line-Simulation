import java.util.LinkedList;

/**
 * Created by Jordan on 15-May-17.
 */
public class InfiniteOutboundStorage extends InterStageStorage
{
    LinkedList<Item> outboundList = new LinkedList<>();

    /**
     * InfiniteOutboundStorage outbound = new InfiniteOutboundStorage(s6, null)
     **/
    public InfiniteOutboundStorage(MasterStage inboundStage, MasterStage outboundStage, String name) {
        super(inboundStage, outboundStage, name);
    }

    @Override
    public void enqueue(Item item) {
        outboundList.addLast(item);
    }

    @Override
    public boolean isFull() {
        return false;
    }
}
