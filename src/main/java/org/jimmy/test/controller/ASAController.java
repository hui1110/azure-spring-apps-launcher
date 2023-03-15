package org.jimmy.test.controller;

import com.azure.resourcemanager.AzureResourceManager;
import org.jimmy.test.util.ResourceManagerUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/asa")
public class ASAController {

    @GetMapping("/asaquery")
    public String asaquery(
        @RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management
    ) {
        AzureResourceManager azureResourceManager = ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue());

        //        azureResourceManager.springServices().

        List<String> nameList = azureResourceManager.springServices().list().stream().map(spring -> spring.name()).collect(Collectors.toList());

        return "success";
    }

}
