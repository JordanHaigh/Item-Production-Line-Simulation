/**
 * Created by Jordan on 20-May-17.
 */

import java.util.LinkedList;
public class MasterStage
{
    private LinkedList<Stage> substages =  new LinkedList<>();
    private MasterStage forwardMasterStage, backwardMasterStage;
    private InterStageStorage inboundStorage, outboundStorage;


    public LinkedList<Stage> getSubstages() {
        return substages;
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
}
