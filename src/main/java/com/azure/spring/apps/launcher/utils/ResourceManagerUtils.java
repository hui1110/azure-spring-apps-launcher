package com.azure.spring.apps.launcher.utils;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.management.AzureEnvironment;
import com.azure.core.management.profile.AzureProfile;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appplatform.models.ConfigServerProperties;
import com.azure.resourcemanager.appplatform.models.ConfigServerState;
import com.azure.resourcemanager.appplatform.models.MonitoringSettingProperties;
import com.azure.resourcemanager.appplatform.models.MonitoringSettingState;
import com.azure.resourcemanager.appplatform.models.SpringApp;
import com.azure.resourcemanager.appplatform.models.SpringService;
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
            .withDefaultSubscription();
//            .withSubscription("8c81302b-c67a-494e-ac1b-17e93b7e2e5a");
    }

//    public static AzureResourceManager getResourceManager(TokenCredential token, String subscription) {
    public static AzureResourceManager getResourceManager(String token, String subscription) {
        final AzureProfile profile = new AzureProfile(AzureEnvironment.AZURE);
        final TokenCredential credential = toTokenCredential(token);
        return AzureResourceManager
            .configure()
            .withLogLevel(HttpLogDetailLevel.BASIC)
//            .authenticate(token, profile)
            .authenticate(credential, profile)
            .withSubscription(subscription);
    }

    private static TokenCredential toTokenCredential(String accessToken) {
        return request -> Mono.just(new AccessToken(accessToken, OffsetDateTime.MAX));
    }

    /**
     * Print spring service settings.
     *
     * @param springService spring service instance
     */
    public static void print(SpringService springService) {
        StringBuilder info = new StringBuilder("Spring Service: ")
            .append("\n\tId: ").append(springService.id())
            .append("\n\tName: ").append(springService.name())
            .append("\n\tResource Group: ").append(springService.resourceGroupName())
            .append("\n\tRegion: ").append(springService.region())
            .append("\n\tTags: ").append(springService.tags());

        ConfigServerProperties serverProperties = springService.getServerProperties();
        if (serverProperties != null && serverProperties.provisioningState() != null
            && serverProperties.provisioningState().equals(ConfigServerState.SUCCEEDED) && serverProperties.configServer() != null) {
            info.append("\n\tProperties: ");
            if (serverProperties.configServer().gitProperty() != null) {
                info.append("\n\t\tGit: ").append(serverProperties.configServer().gitProperty().uri());
            }
        }

        if (springService.sku() != null) {
            info.append("\n\tSku: ")
                .append("\n\t\tName: ").append(springService.sku().name())
                .append("\n\t\tTier: ").append(springService.sku().tier())
                .append("\n\t\tCapacity: ").append(springService.sku().capacity());
        }

        MonitoringSettingProperties monitoringSettingProperties = springService.getMonitoringSetting();
        if (monitoringSettingProperties != null && monitoringSettingProperties.provisioningState() != null
            && monitoringSettingProperties.provisioningState().equals(MonitoringSettingState.SUCCEEDED)) {
            info.append("\n\tTrace: ")
                .append("\n\t\tEnabled: ").append(monitoringSettingProperties.traceEnabled())
                .append("\n\t\tApp Insight Instrumentation Key: ").append(monitoringSettingProperties.appInsightsInstrumentationKey());
        }

        System.out.println(info.toString());
    }

    /**
     * Print spring app settings.
     *
     * @param springApp spring app instance
     */
    public static void print(SpringApp springApp) {
        StringBuilder info = new StringBuilder("Spring Service: ")
            .append("\n\tId: ").append(springApp.id())
            .append("\n\tName: ").append(springApp.name())
            .append("\n\tPublic Endpoint: ").append(springApp.isPublic())
            .append("\n\tUrl: ").append(springApp.url())
            .append("\n\tHttps Only: ").append(springApp.isHttpsOnly())
            .append("\n\tFully Qualified Domain Name: ").append(springApp.fqdn())
            .append("\n\tActive Deployment Name: ").append(springApp.activeDeploymentName());

        if (springApp.temporaryDisk() != null) {
            info.append("\n\tTemporary Disk:")
                .append("\n\t\tSize In GB: ").append(springApp.temporaryDisk().sizeInGB())
                .append("\n\t\tMount Path: ").append(springApp.temporaryDisk().mountPath());
        }

        if (springApp.persistentDisk() != null) {
            info.append("\n\tPersistent Disk:")
                .append("\n\t\tSize In GB: ").append(springApp.persistentDisk().sizeInGB())
                .append("\n\t\tMount Path: ").append(springApp.persistentDisk().mountPath());
        }

        if (springApp.identity() != null) {
            info.append("\n\tIdentity:")
                .append("\n\t\tType: ").append(springApp.identity().type())
                .append("\n\t\tPrincipal Id: ").append(springApp.identity().principalId())
                .append("\n\t\tTenant Id: ").append(springApp.identity().tenantId());
        }

        System.out.println(info.toString());
    }
}
