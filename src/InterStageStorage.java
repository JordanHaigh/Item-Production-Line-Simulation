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
    protected String name;

    //Used for InfiniteStorage Classes
    public InterStageStorage(Stage inboundStage, Stage outboundStage, String name)
    {
        this.inboundStage = inboundStage;
        this.outboundStage = outboundStage;
        this.name = name;
    }


    public InterStageStorage(int qMax, Stage inboundStage, Stage outboundStage, String name)
    {
        this(inboundStage, outboundStage, name);

        this.qMax = qMax;
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

    @Override
    public String toString()
    {
        return this.name +
            (isEmpty() ? " [Empty]" :
                    (isFull() ? " [Full]" :
                            " Holding " + size() + " items"));
    }
}
