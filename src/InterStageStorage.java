/**
 * Created by Jordan on 15-May-17.
 */

import java.util.LinkedList;
import java.util.Queue;

public class InterStageStorage
{
    protected Queue<Item> list = new LinkedList<>();
    protected int qMax;
    protected Stage inboundStage;
    protected Stage outboundStage;

    //Used for InfiniteStorage Classes
    public InterStageStorage(Stage inboundStage, Stage outboundStage)
    {
        this.inboundStage = inboundStage;
        this.outboundStage = outboundStage;
    }


    public InterStageStorage(int qMax, Stage inboundStage, Stage outboundStage)
    {
        this.qMax = qMax;
        this.inboundStage = inboundStage;
        this.outboundStage = outboundStage;
    }

    public void enqueue(Item item)
    {
        list.add(item);
    }

    public Item dequeue()
    {
        return list.remove();
    }

    public int size()
    {
        return list.size();
    }

    public boolean isFull()
    {
        return list.size() == qMax;
    }

    public boolean isEmpty()
    {
        return list.size() == 0;
    }
}
