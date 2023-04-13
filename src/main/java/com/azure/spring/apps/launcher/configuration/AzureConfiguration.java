package com.azure.spring.apps.launcher.configuration;

import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.spring.apps.launcher.properties.ASAProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AzureConfiguration {

    private final ASAProperties properties;

    public AzureConfiguration(ASAProperties properties) {
        this.properties = properties;
    }

//    @Bean
//    public TokenCredential tokenCredential() {
//        return new DefaultAzureCredentialBuilder().build();
//    }

//    @Bean
//    public AzureResourceManager azureResourceManager(TokenCredential credential) {
//        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
//        return AzureResourceManager
//            .configure()
//            .withLogLevel(HttpLogDetailLevel.NONE)
//            .authenticate(credential, profile)
//            .withSubscription(properties.getSubscriptionId())
//            ;
//    }

    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
                               OAuth2AuthorizedClientRepository authorizedClientRepository) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction function =
            new ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository,
                authorizedClientRepository);
        return WebClient.builder()
                        .apply(function.oauth2Configuration())
                        .build();
    }

}
