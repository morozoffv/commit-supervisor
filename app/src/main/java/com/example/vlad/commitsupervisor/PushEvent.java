package com.example.vlad.commitsupervisor;

/**
 * Created by vlad on 13/10/2017.
 */

public class PushEvent extends Event {

    private int commitNumber;
    private String branch;
    private String repoName;
    private String creationTime;

    public int getCommitNumber() {
        return commitNumber;
    }

    public void setCommitNumber(int commitNumber) {
        this.commitNumber = commitNumber;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public void setEventData() {

    }
}
