import java.util.LinkedList;

/**
 * Created by Jordan on 15-May-17.
 */
public class InfiniteOutboundStorage extends InterStageStorage {
    LinkedList<Item> outboundList = new LinkedList<>();

    /**
     * InfiniteOutboundStorage outbound = new InfiniteOutboundStorage(s6, null)
     **/
    public InfiniteOutboundStorage(Stage inboundStage, Stage outboundStage) {
        super(inboundStage, outboundStage);
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
