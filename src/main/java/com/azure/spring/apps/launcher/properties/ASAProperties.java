package com.azure.spring.apps.launcher.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "asa")
public class ASAProperties {

    private String subscriptionId;
    private String resourceGroup;
    private String springCloudInstanceName;

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getResourceGroup() {
        return resourceGroup;
    }

    public void setResourceGroup(String resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    public String getSpringCloudInstanceName() {
        return springCloudInstanceName;
    }

    public void setSpringCloudInstanceName(String springCloudInstanceName) {
        this.springCloudInstanceName = springCloudInstanceName;
    }
}
