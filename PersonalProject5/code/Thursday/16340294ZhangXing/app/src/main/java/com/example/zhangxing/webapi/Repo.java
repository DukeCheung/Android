package com.example.zhangxing.webapi;


import java.io.Serializable;

public class Repo implements Serializable {
    String name;
    String description;
    Integer id;
    Boolean has_issues;
    Integer open_issues;

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getHas_issues() {
        return has_issues;
    }

    public String getDescription() {
        return description;
    }

    public Integer getOpen_issues() {
        return open_issues;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHas_issues(Boolean has_issues) {
        this.has_issues = has_issues;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpen_issues(Integer open_issues) {
        this.open_issues = open_issues;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
