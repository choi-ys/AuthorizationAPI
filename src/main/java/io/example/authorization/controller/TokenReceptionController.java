package io.example.authorization.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.example.authorization.domain.authorization.AuthToken;
import io.example.authorization.domain.client.entity.ClientEntity;
import io.example.authorization.repository.ClientDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
@Slf4j
public class TokenReceptionController {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ClientDetailRepository clientDetailRepository;

    // http://localhost:8080/oauth/callback?code={autorization_code}&clientId={clientId}
    @GetMapping(value = "/callback")
    public AuthToken callbackSocial(@RequestParam String code, @RequestParam String clientId) throws JsonProcessingException {
        log.info("code : {}, cliendId : {}", code, clientId);
        Optional<ClientEntity> optionalClientDetailsEntity = clientDetailRepository.findByClientId(clientId);
        if(optionalClientDetailsEntity == null){
            return null;
        }

        ClientEntity clientEntity = optionalClientDetailsEntity.get();
        String credentials = clientEntity.getClientId() + ":" + clientEntity.getClientSecretOrigin();

        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", clientEntity.getWebServerRedirectUri());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/token", request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            AuthToken authToken = this.objectMapper.readValue(response.getBody(), AuthToken.class);
            log.info(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(authToken));
            return authToken;
        }
        return null;
    }

    // http://localhost:8080/oauth/token/refresh?refreshToken={refresh_token}&clientId={clientId}
    // http://localhost:8080/oauth/token/refresh?refreshToken=03874a1a-6cd3-4ae9-ab11-b9945b4178d4&clientId=a352d947-82f6-43fb-a289-21698d0bd97f
    @GetMapping(value = "/token/refresh")
    public AuthToken refreshToken(@RequestParam String refreshToken, @RequestParam String clientId) throws JsonProcessingException {
        log.info("refreshToken : {}, cliendId : {}", refreshToken, clientId);

        Optional<ClientEntity> optionalClientDetailsEntity = clientDetailRepository.findByClientId(clientId);
        if(optionalClientDetailsEntity == null){
            return null;
        }
        ClientEntity clientEntity = optionalClientDetailsEntity.get();
        String credentials = clientEntity.getClientId() + ":" + clientEntity.getClientSecretOrigin();

        String encodedCredentials = new String(Base64.encodeBase64(credentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "Basic " + encodedCredentials);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/oauth/token", request, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            AuthToken authToken = this.objectMapper.readValue(response.getBody(), AuthToken.class);
            log.info(this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(authToken));
            return authToken;
        }
        return null;
    }
}