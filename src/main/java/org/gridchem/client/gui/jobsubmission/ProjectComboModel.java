package org.gridchem.client.gui.jobsubmission;

import org.apache.oodt.cas.filemgr.structs.Product;

public class ProjectComboModel {
    private String projectName;
    private String projectId;

    public ProjectComboModel(String name, String projectId){
        this.projectName = name;
        this.projectId = projectId;
    }

    @Override
    public String toString(){
        return projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
