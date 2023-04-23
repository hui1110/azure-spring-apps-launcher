package com.azure.spring.apps.launcher.model;

public class ResourceGrooupInstance {

    public ResourceGrooupInstance(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
}
