import java.util.Collections;
import java.util.LinkedList;

/**
 *
 */
public class MasterStage
{
    private LinkedList<Stage> substages =  new LinkedList<>();
    private MasterStage forwardMasterStage, backwardMasterStage;
    private InterStageStorage inboundStorage, outboundStorage;
    private String name;

    /**
     * Overloaded Constructor
     * @param name - Name of the Master Stage
     */
    public MasterStage(String name) { this.name = name; }

    /**
     * public LinkedList<Stage> getSubstages()
     * @return - Returns the stages in the current list
     */
    public LinkedList<Stage> getSubstages() { return substages; }

    /**
     * public LinkedList<Stage> getSubstagesInSortedOrder()
     * Creates a copy of the substages LL and sorts the list - Eliminates updating the actual list of stages
     * Sorts the LL using the Collections library
     * @return - Returns a sorted List
     */
    public LinkedList<Stage> getSubstagesInSortedOrder()
    {
        LinkedList<Stage> sortedStages  = new LinkedList<>(substages);
        Collections.sort(sortedStages,Stage.StageFinishTimeComparator);
        return sortedStages;
    }

    /**
     * public void addSubstage(Stage stage)
     * Adds substage to the list
     * @param stage - Stage parameter
     */
    public void addSubStage(Stage stage)
    {
        stage.setParent(this);
        substages.addLast(stage);
    }

    /**
     * public MasterStage getForwardMasterStage()
     * @return - Returns the Forward Master Stage
     */
    public MasterStage getForwardMasterStage() { return forwardMasterStage; }

    /**
     * public void setForwardMasterStage(MasterStage forwardMasterStage)
     * Sets the input parameter forwardMasterStage to the class variable forwardMasterStage
     * @param forwardMasterStage - MasterStage to be assigned
     */
    public void setForwardMasterStage(MasterStage forwardMasterStage) { this.forwardMasterStage = forwardMasterStage; }

    /**
     * public MasterStage getBackwardMasterStage()
     * @return - Returns the Backward Master Stage
     */
    public MasterStage getBackwardMasterStage() { return backwardMasterStage; }

    /**
     * public void setBackwardMasterStage(MasterStage backwardMasterStage)
     * Sets the input parameter backwardMasterStage to the class variable backwardMasterStage
     * @param backwardMasterStage - MasterStage to be assigned
     */
    public void setBackwardMasterStage(MasterStage backwardMasterStage) { this.backwardMasterStage = backwardMasterStage; }

    //public InterStageStorage getInboundStorage() { return inboundStorage; }

    /**
     * public void setInboundStorage(InterStageStorage inboundStorage)
     * Sets the input parameter inboundStorage to the class variable inboundStorage
     * Updates the inboundstorage variable in every substage in the current list
     * @param inboundStorage - InterStageStorage to be set
     */
    public void setInboundStorage(InterStageStorage inboundStorage)
    {
        this.inboundStorage = inboundStorage;
        for(Stage s: this.getSubstages())
        {
            s.setInboundStorage(inboundStorage);
        }
    }

    //public InterStageStorage getOutboundStorage() { return outboundStorage; }

    /**
     * public void setOutboundStorage(InterStageStorage outboundStorage)
     * Sets the input parameter ouboundStorage to the class variable outboundStorage
     * Updates the outboundstorage variable in every substage in the current list
     * @param outboundStorage - InterstageStorage to be set
     */
    public void setOutboundStorage(InterStageStorage outboundStorage) {
        this.outboundStorage = outboundStorage;
        for(Stage s: this.getSubstages())
        {
            s.setOutboundStorage(outboundStorage);
        }
    }

    /**
     * public String toString()
     * @return - String containing name of the master stage and its substages
     */
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
