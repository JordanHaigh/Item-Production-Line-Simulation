/**
 * Created by Jordan on 20-May-17.
 */

import java.util.Collections;
import java.util.LinkedList;
public class MasterStage
{
    private LinkedList<Stage> substages =  new LinkedList<>();
    private MasterStage forwardMasterStage, backwardMasterStage;
    private InterStageStorage inboundStorage, outboundStorage;
    private String name;

    public MasterStage(String name)
    {
        this.name = name;
    }

    public LinkedList<Stage> getSubstages() { return substages; }

    public LinkedList<Stage> getSubstagesInSortedOrder()
    {
        LinkedList<Stage> sortedStages  = new LinkedList<>(substages);
        Collections.sort(sortedStages,Stage.StageFinishTimeComparator);
        return sortedStages;
    }

    public void addSubStage(Stage stage)
    {
        stage.setParent(this);
        substages.addLast(stage);
    }

    public MasterStage getForwardMasterStage() {
        return forwardMasterStage;
    }

    public void setForwardMasterStage(MasterStage forwardMasterStage)
    {
        this.forwardMasterStage = forwardMasterStage;
    }

    public MasterStage getBackwardMasterStage() {
        return backwardMasterStage;
    }

    public void setBackwardMasterStage(MasterStage backwardMasterStage)
    {
        this.backwardMasterStage = backwardMasterStage;
    }

    public InterStageStorage getInboundStorage() {
        return inboundStorage;
    }

    public void setInboundStorage(InterStageStorage inboundStorage)
    {
        this.inboundStorage = inboundStorage;
        for(Stage s: this.getSubstages())
        {
            s.setInboundStorage(inboundStorage);
        }
    }

    public InterStageStorage getOutboundStorage() {
        return outboundStorage;
    }

    public void setOutboundStorage(InterStageStorage outboundStorage) {
        this.outboundStorage = outboundStorage;
        for(Stage s: this.getSubstages())
        {
            s.setOutboundStorage(outboundStorage);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" [");
        for(Stage s: this.getSubstages())
            sb.append(s.getName()).append(" ");
        sb.append("]");
        return sb.toString();
    }


}
