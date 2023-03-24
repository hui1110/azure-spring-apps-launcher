package com.azure.spring.apps.launcher.service;

import com.azure.core.management.Region;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appplatform.models.RuntimeVersion;
import com.azure.resourcemanager.appplatform.models.SpringApp;
import com.azure.resourcemanager.appplatform.models.SpringService;
import com.azure.spring.apps.launcher.properties.ASAProperties;
import com.azure.spring.apps.launcher.utils.ResourceManagerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class IndexService {
    @Autowired
    AzureResourceManager azureResourceManager;

    @Autowired
    ASAProperties properties;

    public String deploySpringCloudGateway() {
        final String rgName = properties.getResourceGroup();
        final String serviceName = properties.getInstanceName();
        final String appName = "spring-cloud-gateway";
        final Region region = Region.US_EAST;
        final RuntimeVersion jdkVersion = RuntimeVersion.JAVA_17;
        final File springCloudGatewayJar = new File("jarFile/spring-cloud-gateway-1.0-SNAPSHOT.jar");

        try {
            if (!azureResourceManager.resourceGroups().contain(rgName)) {
                createResourceGroup(rgName, region);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete Resource Group: " + rgName);
            azureResourceManager.resourceGroups().beginDeleteByName(rgName);
        }

        try {
            if (azureResourceManager.springServices().checkNameAvailability(serviceName, region).nameAvailable()) {
                createASA(rgName, serviceName, region);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete Azure Spring App: " + serviceName);
            String id = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).id();
            azureResourceManager.springServices().deleteById(id);
        }

        try {
            deploySpringApp(rgName, serviceName, appName, jdkVersion, springCloudGatewayJar);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Delete App: " + appName);
            azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).apps().deleteByName(appName);
        }
        return "Successfully created app: spring-cloud-gateway in " + serviceName + "<br/> Go to <a href='https://ms"
            + ".portal.azure.com'>Azure Portal</a>";
    }

    private void createResourceGroup(String rgName, Region region) {
        System.out.printf("Creating resource group %s ...%n", rgName);

        azureResourceManager.resourceGroups().define(rgName)
                            .withRegion(region)
                            .create();

        System.out.printf("Created resource group %s ...%n", rgName);
    }

    private String createASA(String rgName, String serviceName, Region region) {
        System.out.printf("Creating spring cloud service %s in resource group %s ...%n", serviceName, rgName);

        SpringService service = azureResourceManager.springServices().define(serviceName)
                                                    .withRegion(region)
                                                    .withExistingResourceGroup(rgName)
                                                    .create();

        System.out.printf("Created spring cloud service %s%n", service.name());
        ResourceManagerUtils.print(service);
        return service.id();
    }

    private void deploySpringApp(String rgName, String serviceName, String appName, RuntimeVersion jdkVersion,
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
}

