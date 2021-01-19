package io.example.authorization.repository;

import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.client.entity.ClientDetailsEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class ClientDetailsEntityRepositoryTest extends BaseTest {

    @Autowired
    ClientDetailRepository clientDetailRepository;

    @Test
    @DisplayName("Client 정보 DB 저장")
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

        ClientDetailsEntity clientDetailsEntity = ClientDetailsEntity.builder()
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
        ClientDetailsEntity savedClientDetailsEntity = this.clientDetailRepository.save(clientDetailsEntity);

        //then
        assertThat(savedClientDetailsEntity).isNotNull();
        assertThat(savedClientDetailsEntity.getClientId()).isEqualTo(clientId);
        assertThat(passwordEncoder.matches(clientSecret, savedClientDetailsEntity.getClientSecret()));
        assertThat(savedClientDetailsEntity.getScope()).isEqualTo(scope);
        assertThat(savedClientDetailsEntity.getAuthorizedGrantTypes()).isEqualTo(authorizedGrantTypes);
        assertThat(savedClientDetailsEntity.getWebServerRedirectUri()).isEqualTo(webServerRedirectUri);
        assertThat(savedClientDetailsEntity.getAuthorities()).isEqualTo(authorities);
        assertThat(savedClientDetailsEntity.getAccessTokenValidity()).isEqualTo(accessTokenValidity);
        assertThat(savedClientDetailsEntity.getRefreshTokenValidity()).isEqualTo(refreshTokenValidity);
    }
}