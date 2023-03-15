package org.jimmy.test.controller;

import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.authorization.models.ActiveDirectoryUsers;
import com.azure.resourcemanager.authorization.models.ServicePrincipal;
import org.jimmy.test.util.ResourceManagerUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/graph")
    public String test(
        @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph
    ) {
        AzureResourceManager azureResourceManager = ResourceManagerUtils.getResourceManager(graph.getAccessToken().getTokenValue());

        ActiveDirectoryUsers activeDirectoryUsers = azureResourceManager.accessManagement().activeDirectoryUsers();

        List<String> names = activeDirectoryUsers.list().stream().map(user -> user.userPrincipalName()).collect(Collectors.toList());

        return names.toString();
    }

    @GetMapping("/createsp")
    public String createsp(
        @RegisteredOAuth2AuthorizedClient("graph") OAuth2AuthorizedClient graph
    ) {
        AzureResourceManager azureResourceManager = ResourceManagerUtils.getResourceManager(graph.getAccessToken().getTokenValue());

        AtomicReference<String> clientSecret = new AtomicReference<>();
        ServicePrincipal principal = azureResourceManager
            .accessManagement()
            .servicePrincipals()
            .define("jimmy-test-" + System.currentTimeMillis())
            .withNewApplication()
            .definePasswordCredential(UUID.randomUUID().toString())
            .withPasswordConsumer(passwordCredential -> clientSecret.set(passwordCredential.value()))
            .attach()
            .create();

        return clientSecret.get();
    }


}
