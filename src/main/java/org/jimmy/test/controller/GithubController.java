package org.jimmy.test.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/github")
public class GithubController {

    @GetMapping("/")
    public String hello(
        @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient github
    ) {
        return github.getAccessToken().getTokenValue();
    }

}
