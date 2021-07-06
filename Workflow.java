public class Workflow {

    private String workflowname;
    private boolean active;
    private static int counter = 10000;
    private int runid;

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
        if (active) {
            this.runid = counter;
            counter++;
        } else {
            this.runid = 0;
        }
    }

    public int getRunid() {
        return runid;
    }

}
