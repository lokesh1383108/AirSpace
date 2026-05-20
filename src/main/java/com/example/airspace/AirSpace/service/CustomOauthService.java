package com.example.airspace.AirSpace.service;

import com.example.airspace.AirSpace.models.CustomOAuth2User;
import com.example.airspace.AirSpace.models.user.User;
import com.example.airspace.AirSpace.models.user.UserType;
import com.example.airspace.AirSpace.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
@Service
public class CustomOauthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> registerNewUser(oAuth2User));

        return new CustomOAuth2User(user);
    }

    public OAuth2AccessToken getAccessToken(String authorizationCode, ClientRegistration clientRegistration) {
        if (clientRegistration == null || clientRegistration.getProviderDetails().getTokenUri() == null) {
            throw new IllegalArgumentException("Client registration or token URI cannot be null.");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizationCode);
        body.add("redirect_uri", clientRegistration.getRedirectUri());
        body.add("client_id", clientRegistration.getClientId());
        body.add("client_secret", clientRegistration.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<OAuth2AccessTokenResponse> response;

        try {
            response = restTemplate.postForEntity(
                    clientRegistration.getProviderDetails().getTokenUri(), request, OAuth2AccessTokenResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve access token: " + e.getMessage(), e);
        }

        if (response.getBody() == null) {
            throw new RuntimeException("Access token response is null");
        }

        return response.getBody().getAccessToken();
    }


    private User registerNewUser(OAuth2User oAuth2User) {
        User user = new User();
        user.setEmail(oAuth2User.getAttribute("email"));
        user.setRole(UserType.valueOf("Customer")); // Default role; adjust if needed
        return userRepository.save(user);
    }
}
