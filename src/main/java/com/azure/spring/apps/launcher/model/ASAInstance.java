package com.azure.spring.apps.launcher.model;

public class ASAInstance {

    private String id;

    private String name;

    private String sku;

    public ASAInstance() {
    }

    public ASAInstance(String id, String name, String sku) {
        this.id = id;
        this.name = name;
        this.sku = sku;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
