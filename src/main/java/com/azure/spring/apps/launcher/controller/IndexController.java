package com.azure.spring.apps.launcher.controller;

import com.azure.core.management.Region;
import com.azure.resourcemanager.appplatform.models.RuntimeVersion;
import com.azure.spring.apps.launcher.model.ASAInstance;
import com.azure.spring.apps.launcher.model.AppInstance;
import com.azure.spring.apps.launcher.model.RegionInstance;
import com.azure.spring.apps.launcher.model.ResourceGrooupInstance;
import com.azure.spring.apps.launcher.model.Subscription;
import com.azure.spring.apps.launcher.service.IndexService;
import jakarta.servlet.http.HttpServletRequest;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


    @GetMapping("/getRepo")
    public boolean deployGithubRepoToASA(HttpServletRequest request, @RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management) {

        String url = request.getParameter("url");
        String subscriptionId = request.getParameter("subscriptionId");
        String rgName = request.getParameter("rgName");
        String serviceName = request.getParameter("serviceName");
        String appName = request.getParameter("appName");
        String javaVersion = request.getParameter("javaVersion");
        String regionName = request.getParameter("region");
        String enableAction = request.getParameter("enableAction");

        Region region = Region.fromName(regionName);
        RuntimeVersion runtimeVersion = indexService.getJavaVersion(javaVersion);

        Git git = null;
        try {
            git = Git.cloneRepository()
                     .setURI(url)
                     .call();
            String pathName = git.getRepository().getWorkTree().toString();
            indexService.deploySourceCodeToSpringApps(management,subscriptionId, region, rgName, serviceName, appName, runtimeVersion, pathName);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (git != null) {
                git.getRepository().close();
                git.close();
                indexService.deleteRepositoryDirectory(git.getRepository().getWorkTree());
            }
        }
        return true;
    }

    @GetMapping("/getAppList")
    public List<AppInstance> getAppListFromASAInstance(@RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management,@RequestParam String subscriptionId, @RequestParam String rgName, @RequestParam String serviceName){
       return indexService.appList(management, rgName, serviceName, subscriptionId);
    }

    @GetMapping("/getRegionList")
    public List<RegionInstance> getRegionList() {
        return indexService.RegionList();
    }

    @GetMapping("/getResourceGroups/{subscriptionId}")
    public List<ResourceGrooupInstance> getResourceGroups(@RegisteredOAuth2AuthorizedClient("management") OAuth2AuthorizedClient management,@PathVariable String subscriptionId) {
        return indexService.ResourceGroup(management,subscriptionId);
    }

}
