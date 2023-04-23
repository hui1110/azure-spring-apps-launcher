package com.azure.spring.apps.launcher.service;

import com.azure.core.management.Region;
import com.azure.resourcemanager.AzureResourceManager;
import com.azure.resourcemanager.appplatform.models.RuntimeVersion;
import com.azure.resourcemanager.appplatform.models.SpringApp;
import com.azure.resourcemanager.appplatform.models.SpringApps;
import com.azure.resourcemanager.appplatform.models.SpringService;
import com.azure.spring.apps.launcher.model.ASAInstance;
import com.azure.spring.apps.launcher.model.AppInstance;
import com.azure.spring.apps.launcher.model.RegionInstance;
import com.azure.spring.apps.launcher.model.ResourceGrooupInstance;
import com.azure.spring.apps.launcher.model.Subscription;
import com.azure.spring.apps.launcher.utils.ResourceManagerUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;

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
            deploySpringApp("jar",azureResourceManager, rgName, serviceName, appName, jdkVersion, springCloudGatewayJar);
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

    private void deploySpringApp(String flag, AzureResourceManager azureResourceManager, String rgName, String serviceName, String appName, RuntimeVersion jdkVersion,
                                 File JarFile) {
        System.out.printf("Creating spring cloud app %s in Azure Spring App %s ...%n", appName, serviceName);

        SpringService service = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName);
        if ("jar".equals(flag)) {
            SpringApp app = service.apps().define(appName)
                                   .defineActiveDeployment("default")
                                   .withJarFile(JarFile)
                                   .withRuntime(jdkVersion)
                                   .attach()
                                   .withDefaultPublicEndpoint()
                                   .withHttpsOnly()
                                   .create();
            ResourceManagerUtils.print(app);
        } else if ("sourceCode".equals(flag)) {
            SpringApp app = service.apps().define(appName)
                                   .defineActiveDeployment("default")
                                   .withSourceCodeTarGzFile(JarFile)
                                   .withSingleModule()
                                   .withRuntime(jdkVersion)
                                   .attach()
                                   .withDefaultPublicEndpoint()
                                   .withHttpsOnly()
                                   .create();
            ResourceManagerUtils.print(app);

        }
        System.out.printf("Created spring cloud app %s %n", appName);
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
                  .map(springService -> new ASAInstance(springService.region(), springService.resourceGroupName(), springService.id(), springService.name(), springService.sku().tier()))
                  .collect(Collectors.toList());
    }

    public void deploySourceCodeToSpringApps(OAuth2AuthorizedClient management,String subscriptionId, Region region, String rgName, String serviceName, String appName, RuntimeVersion jdkVersion, String pathName) throws IOException {

        assert appName.length() < 4 || appName.length() > 32 : "App name must be between 4 and 32 characters long.";

        File file = new File(pathName);
        File sourceCode = createTarGzFile(file);

        AzureResourceManager azureResourceManager = ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue(), subscriptionId);

        try {
            if (!azureResourceManager.resourceGroups().contain(rgName)) {
                createResourceGroup(azureResourceManager, rgName, region);
                System.out.println("Created Resource Group: " + rgName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            azureResourceManager.resourceGroups().beginDeleteByName(rgName);
            System.out.println("Delete Resource Group: " + rgName);
        }

        try {
            if (azureResourceManager.springServices().checkNameAvailability(serviceName, region).nameAvailable()) {
                createASA(azureResourceManager, rgName, serviceName, region);
                System.out.println("Created Azure Spring Apps: " + serviceName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String id = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).id();
            azureResourceManager.springServices().deleteById(id);
            System.out.println("Delete Azure Spring Apps: " + serviceName);
        }

        try {
            deploySpringApp("sourceCode",azureResourceManager, rgName, serviceName, appName, jdkVersion, sourceCode);
        } catch (Exception e) {
            e.printStackTrace();
            azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).apps().deleteByName(appName);
            System.out.println("Delete App: " + appName);
        }
    }

    public void deleteRepositoryDirectory(File directory) {
        File tempGitDirectory;
        try {
            tempGitDirectory = new File(directory.toString());
            if(tempGitDirectory.exists()){
                FileUtils.deleteDirectory(tempGitDirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createTarGzFile(File sourceFolder) throws IOException {
        File compressFile = File.createTempFile("java_package", "tar.gz");
        compressFile.deleteOnExit();
        try (TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(
            new GZIPOutputStream(new FileOutputStream(compressFile)))) {
            tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            for (Path sourceFile : Files.walk(sourceFolder.toPath()).toList()) {
                String relativePath = sourceFolder.toPath().relativize(sourceFile).toString();
                TarArchiveEntry entry = new TarArchiveEntry(sourceFile.toFile(), relativePath);
                if (sourceFile.toFile().isFile()) {
                    try (InputStream inputStream = new FileInputStream(sourceFile.toFile())) {
                        tarArchiveOutputStream.putArchiveEntry(entry);
                        IOUtils.copy(inputStream, tarArchiveOutputStream);
                        tarArchiveOutputStream.closeArchiveEntry();
                    }
                } else {
                    tarArchiveOutputStream.putArchiveEntry(entry);
                    tarArchiveOutputStream.closeArchiveEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return compressFile;
    }

    public RuntimeVersion getJavaVersion(String javaVersion) {
        RuntimeVersion runtimeVersion = new RuntimeVersion();
        switch (javaVersion) {
            case "17" -> runtimeVersion = RuntimeVersion.JAVA_17;
            case "11" -> runtimeVersion = RuntimeVersion.JAVA_11;
            case "8" -> runtimeVersion = RuntimeVersion.JAVA_8;
        }
        return runtimeVersion;
    }

    public List<AppInstance> appList(OAuth2AuthorizedClient management, String rgName, String serviceName,
                                     String subscriptionId) {
        AzureResourceManager azureResourceManager =
            ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue(), subscriptionId);
        SpringApps spps = azureResourceManager.springServices().getByResourceGroup(rgName, serviceName).apps();
        return spps.list().stream()
                   .map(springApp -> new AppInstance(springApp.id(), springApp.name()))
                   .toList();
    }

    public List<RegionInstance> RegionList() {
        List<Region> regionArrayList = new ArrayList<>(Region.values());
        List<RegionInstance> resList = new ArrayList<>();
        if (!regionArrayList.isEmpty()) {
            for (Region region : regionArrayList) {
                resList.add(new RegionInstance(region.name(), region.label()));
            }
        }
        return resList;
    }

    public List<ResourceGrooupInstance> ResourceGroup(OAuth2AuthorizedClient management, String subscriptionId) {
        AzureResourceManager azureResourceManager =
            ResourceManagerUtils.getResourceManager(management.getAccessToken().getTokenValue(), subscriptionId);
        return azureResourceManager.resourceGroups().list().stream()
                                   .map(resourceGroup -> new ResourceGrooupInstance(resourceGroup.name()))
                                   .collect(Collectors.toList());
    }

}
