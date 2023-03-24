package com.azure.spring.apps.launcher.controller;

import com.azure.spring.apps.launcher.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    IndexService indexService;

    @GetMapping("/deploy/spring-cloud-gateway")
    public String deploy() {
        return indexService.deploySpringCloudGateway();
    }

}
