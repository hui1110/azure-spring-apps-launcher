package com.azure.spring.apps.launcher.service;

import com.azure.core.management.Region;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appplatform.models.RuntimeVersion;
import com.azure.resourcemanager.appplatform.models.SpringApp;
import com.azure.resourcemanager.appplatform.models.SpringService;
import com.azure.spring.apps.launcher.model.ASAInstance;
import com.azure.spring.apps.launcher.model.Subscription;
import com.azure.spring.apps.launcher.properties.ASAProperties;
import com.azure.spring.apps.launcher.utils.ResourceManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexService {
//    @Autowired
//    AzureResourceManager azureResourceManager;
//
//    @Autowired
//    ASAProperties properties;

    public boolean deploySpringCloudGateway(OAuth2AuthorizedClient management, String subscriptionId, String version, String rgName, String serviceName) {
//        final String rgName = properties.getResourceGroup();
//        final String serviceName = properties.getInstanceName();
        final String appName = "spring-cloud-gateway";
        final Region region = Region.US_EAST;
        final RuntimeVersion jdkVersion = RuntimeVersion.JAVA_17;
        final File springCloudGatewayJar = new File("jarFile/spring-cloud-gateway-"+ version +".jar");
        AzureResourceManager azureResourceManager = ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue(), subscriptionId);

        try {
            if (!azureResourceManager.resourceGroups().contain(rgName)) {
                createResourceGroup(azureResourceManager, rgName, region);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete Resource Group: " + rgName);
            azureResourceManager.resourceGroups().beginDeleteByName(rgName);
            return false;
        }

        try {
            if (azureResourceManager.springServices().checkNameAvailability(serviceName, region).nameAvailable()) {
                createASA(azureResourceManager, rgName, serviceName, region);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete Azure Spring App: " + serviceName);
            String id = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).id();
            azureResourceManager.springServices().deleteById(id);
            return false;
        }

        try {
            deploySpringApp(azureResourceManager, rgName, serviceName, appName, jdkVersion, springCloudGatewayJar);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete App: " + appName);
            azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).apps().deleteByName(appName);
            return false;
        }
        return true;
    }

    private void createResourceGroup(AzureResourceManager azureResourceManager, String rgName, Region region) {
        System.out.printf("Creating resource group %s ...%n", rgName);

        azureResourceManager.resourceGroups().define(rgName)
                            .withRegion(region)
                            .create();

        System.out.printf("Created resource group %s ...%n", rgName);
    }

    private String createASA(AzureResourceManager azureResourceManager, String rgName, String serviceName, Region region) {
        System.out.printf("Creating spring cloud service %s in resource group %s ...%n", serviceName, rgName);

        SpringService service = azureResourceManager.springServices().define(serviceName)
                                                    .withRegion(region)
                                                    .withExistingResourceGroup(rgName)
                                                    .create();

        System.out.printf("Created spring cloud service %s%n", service.name());
        ResourceManagerUtils.print(service);
        return service.id();
    }

    private void deploySpringApp(AzureResourceManager azureResourceManager, String rgName, String serviceName, String appName, RuntimeVersion jdkVersion,
                                 File JarFile) {
        System.out.printf("Creating spring cloud app %s in Azure Spring App %s ...%n", appName, serviceName);

        SpringService service = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName);
        SpringApp app = service.apps().define(appName)
                               .defineActiveDeployment("default")
                               .withJarFile(JarFile)
                               .withRuntime(jdkVersion)
                               .attach()
                               .withDefaultPublicEndpoint()
                               .withHttpsOnly()
                               .create();

        System.out.printf("Created spring cloud app %s %n", appName);
        ResourceManagerUtils.print(app);
    }

    public int queryProgress(OAuth2AuthorizedClient management, String rgName, String serviceName, String subscriptionId){
//        SpringService service =
//            azureResourceManager.springServices().getByResourceGroup(properties.getResourceGroup(),
//                properties.getInstanceName());
        AzureResourceManager azureResourceManager = ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue(), subscriptionId);

        SpringService service = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName);
        try {
            service.apps().getByName("spring-cloud-gateway");
            return 100;
        } catch (Exception e) {
            e.printStackTrace();
            return 101;
        }
    }

    public List<Subscription> subscriptionList(OAuth2AuthorizedClient management) {
        AzureResourceManager arm = ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue());
        return arm.subscriptions().list().stream()
                  .map(subscription -> new Subscription(subscription.subscriptionId(), subscription.displayName()))
                  .collect(Collectors.toList());
    }

    public List<ASAInstance> instanceList(OAuth2AuthorizedClient management, String subscriptionId) {
        AzureResourceManager arm = ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue(), subscriptionId);
        return arm.springServices().list().stream()
                  .map(springService -> new ASAInstance(springService.id(), springService.name(), springService.sku().tier()))
                  .collect(Collectors.toList());
    }

}

