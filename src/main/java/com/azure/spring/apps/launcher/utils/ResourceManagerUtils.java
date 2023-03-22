package com.azure.spring.apps.launcher.utils;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.AzureResourceManager;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

public class ResourceManagerUtils {

    public static AzureResourceManager getResourceManager(String token) {
        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        final TokenCredential credential = toTokenCredential(token);
        return AzureResourceManager
            .configure()
            .withLogLevel(HttpLogDetailLevel.BASIC)
            .authenticate(credential, profile)
            .withSubscription("8c81302b-c67a-494e-ac1b-17e93b7e2e5a");
    }

    private static TokenCredential toTokenCredential(String accessToken) {
        return request -> Mono.just(new AccessToken(accessToken, OffsetDateTime.MAX));
    }

}
