package com.azure.spring.apps.launcher.controller;

import com.azure.core.credential.TokenCredential;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appplatform.models.SpringService;
import com.azure.resourcemanager.resources.fluentcore.arm.ResourceId;
import com.azure.spring.apps.launcher.model.ASAInstance;
import com.azure.spring.apps.launcher.model.Subscription;
import com.azure.spring.apps.launcher.service.IndexService;
import com.azure.spring.apps.launcher.utils.ResourceManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class IndexController {

    @Autowired
    IndexService indexService;

//    @Autowired
//    AzureResourceManager azureResourceManager;
//
//    @Autowired
//    TokenCredential tokenCredential;
    @GetMapping("/subscriptionList")
    public List<Subscription> subscriptionList(@RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management) {
        return indexService.subscriptionList(management);
    }
//    @GetMapping("/subscriptionList")
//    public List<Subscription> subscriptionList() {
//        return azureResourceManager.subscriptions().list().stream()
//                  .map(subscription -> new Subscription(subscription.subscriptionId(), subscription.displayName()))
//                  .collect(Collectors.toList());
//    }
    @GetMapping("/instanceList/{subscriptionId}")
    public List<ASAInstance> instanceList(@RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management, @PathVariable String subscriptionId) {
        return indexService.instanceList(management, subscriptionId);
    }
//    @GetMapping("/instanceList/{subscriptionId}")
//    public List<ASAInstance> instanceList(@PathVariable String subscriptionId) {
//        AzureResourceManager arm = ResourceManagerUtils.getResourceManager(tokenCredential, subscriptionId);
//        return arm.springServices().list().stream()
//                  .map(springService -> new ASAInstance(springService.id(), springService.name(), springService.sku().tier()))
//                  .collect(Collectors.toList());
//    }


    @GetMapping("/deploy/spring-cloud-gateway/{version}/{rgName}/{asaName}/{subscriptionId}")
    public boolean deploy(@RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management, @PathVariable String subscriptionId, @PathVariable String version, @PathVariable String rgName, @PathVariable String asaName) {
        return indexService.deploySpringCloudGateway(management, subscriptionId, version, rgName, asaName);
    }

    @GetMapping("/queryProcess/{rgName}/{asaName}/{subscriptionId}")
    public int queryProgress(@RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management, @PathVariable String subscriptionId, @PathVariable String rgName, @PathVariable String asaName) {
        return indexService.queryProgress(management, rgName, asaName, subscriptionId);
    }

}
