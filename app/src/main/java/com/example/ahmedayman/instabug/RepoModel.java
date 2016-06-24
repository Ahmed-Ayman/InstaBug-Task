package com.example.ahmedayman.instabug;


/**
 * Created by Ahmed on 20-Jun-16.
 */
public class RepoModel {

    private int id ;

    public RepoModel() {
    }

    public RepoModel(String repoName, String description, String username, String fork, String url, String ownerUrl) {
        this.repoName = repoName;
        this.description = description;
        this.username = username;
        this.fork = fork;
        this.url = url;
        this.ownerUrl = ownerUrl;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String repoName;
    private String description;
    private String username;
    private String fork;
    private String url;
    private String ownerUrl;

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFork() {
        return fork;
    }

    public void setFork(String fork) {
        this.fork = fork;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwnerUrl() {
        return ownerUrl;
    }

    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }


}
