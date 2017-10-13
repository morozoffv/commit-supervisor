package com.example.vlad.commitsupervisor;

/**
 * Created by vlad on 11/10/2017.
 */

public class Commit {

    private String repoName;
    private String committerName;
    private String commitDate;
    private String message;
    private String commitUrl;

    Commit() {

    }

    Commit(String committerName, String commitDate, String message, String commitUrl) {
        this.committerName = committerName;
        this.commitDate = commitDate;
        this.message = message;
        this.commitUrl = commitUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommitUrl() {
        return commitUrl;
    }

    public void setCommitUrl(String commitUrl) {
        this.commitUrl = commitUrl;
    }
}
