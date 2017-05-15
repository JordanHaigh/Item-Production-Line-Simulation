/**
 * Created by Jordan on 15-May-17.
 */

import java.util.LinkedList;

public class InterStageStorage
{
    protected LinkedList<Item> list = new LinkedList<>();
    protected int qMax;
    protected Stage inboundStage;
    protected Stage outboundStage;

    //Used for InfiniteStorage Classes
    public InterStageStorage(Stage inboundStage, Stage outboundStage)
    {
        super();
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
        list.addLast(item);
    }

    public Item dequeue()
    {
        return list.removeFirst();
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
