import com.google.gson.Gson;

public class AusgabeObjektGET {
    private Workflow workflow;
    private String workflowName;
    private boolean isActive;
    private String href;

    public AusgabeObjektGET(Workflow workflow) {
        this.workflowName = workflow.getWorkflowname(); //eindeutige ID
        this.isActive = workflow.isActive();
        this.href = "http://localhost:8080/ae/api/v1/executions";
    }

    public String jsonErzeugen() {
        Gson json = new Gson();
        return json.toJson(this) + ", ";
    }

}
