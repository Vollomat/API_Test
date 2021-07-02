public class Workflow {

    private String workflowname;
    private boolean active;

    public Workflow(String workflowname) {
        this.workflowname = workflowname;
        this.active = false;
    }

    public String getWorkflowname() {
        return workflowname;
    }

    public void setWorkflowname(String workflowname) {
        this.workflowname = workflowname;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
