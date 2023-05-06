package StepperEngine.FlowDetails.StepDetails.FlowIODetails;

/**
 * holds details of a single input/output in a certain flow.
 */
public class FlowIODetailsImpl {

    private final String dataName;

    private final String typeName;



    public FlowIODetailsImpl(String dataName, String typeName) {
        this.dataName = dataName;
        this.typeName = typeName;
    }


    public String getDataName() {
        return dataName;
    }



    public String getTypeName() {
        return typeName;
    }




}
