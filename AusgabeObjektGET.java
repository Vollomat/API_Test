import com.google.gson.Gson;

public class AusgabeObjektGET {
    private Workflow workflow;
    private String workflowName;
    private boolean isActive;

    public AusgabeObjektGET(Workflow workflow) {
        this.workflowName = workflow.getWorkflowname(); //eindeutige ID
        this.isActive = workflow.isActive();
    }

    public String jsonErzeugen() {
        Gson json = new Gson();
        return json.toJson(this) + ", ";
    }

}
