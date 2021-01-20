package io.example.authorization.repository;

import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.client.entity.ClientEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Repository:Client")
class ClientEntityRepositoryTest extends BaseTest {

    @Test
    @DisplayName("클라이언트 정보 INSERT")
    public void saveClientDetail(){
        //given
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String clientId = "kstmClientId";
        String clientSecret = "kstmClientSecret";
        String scope = "read";
        String authorizedGrantTypes = "authorization_code,refresh_token";
        String webServerRedirectUri = "http://localhost:8080/oauth/callback";
        String authorities = "ROLE_USER";
        int accessTokenValidity = 60 * 10;
        int refreshTokenValidity = 1200 * 10;

        ClientEntity clientEntity = ClientEntity.builder()
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .scope(scope)
                .authorizedGrantTypes(authorizedGrantTypes)
                .webServerRedirectUri(webServerRedirectUri)
                .authorities(authorities)
                .accessTokenValidity(accessTokenValidity)
                .refreshTokenValidity(refreshTokenValidity)
                .build();

        //when
        ClientEntity savedClientEntity = this.clientDetailRepository.save(clientEntity);

        //then
        assertThat(savedClientEntity).isNotNull();
        assertThat(savedClientEntity.getClientId()).isEqualTo(clientId);
        assertThat(passwordEncoder.matches(clientSecret, savedClientEntity.getClientSecret()));
        assertThat(savedClientEntity.getScope()).isEqualTo(scope);
        assertThat(savedClientEntity.getAuthorizedGrantTypes()).isEqualTo(authorizedGrantTypes);
        assertThat(savedClientEntity.getWebServerRedirectUri()).isEqualTo(webServerRedirectUri);
        assertThat(savedClientEntity.getAuthorities()).isEqualTo(authorities);
        assertThat(savedClientEntity.getAccessTokenValidity()).isEqualTo(accessTokenValidity);
        assertThat(savedClientEntity.getRefreshTokenValidity()).isEqualTo(refreshTokenValidity);
    }
}