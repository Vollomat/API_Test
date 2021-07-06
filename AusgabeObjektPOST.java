import com.google.gson.Gson;

public class AusgabeObjektPOST {

    private int runid;
    private Workflow workflow;
    private String workflowName;
    private String details;
    private String href;

    public AusgabeObjektPOST(Workflow workflow) {
        if (workflow != null) {
            this.runid = workflow.getRunid();
            this.workflowName = workflow.getWorkflowname();
            this.details = "Der Workflow wurde erfolgreich angestoßen!";
            this.href = "http://localhost:8080/ae/api/v1/executions";
        } else {
            this.details = "Der gewünschte Workflow existiert nicht!";
        }
    }

    public String jsonErzeugen() {
        Gson json = new Gson();
        return json.toJson(this) + ", ";
    }

}
