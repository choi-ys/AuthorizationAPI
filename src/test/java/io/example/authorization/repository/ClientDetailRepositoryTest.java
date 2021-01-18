package io.example.authorization.repository;

import io.example.authorization.common.BaseTest;
import io.example.authorization.domain.client.ClientDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class ClientDetailRepositoryTest extends BaseTest {

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

        ClientDetail clientDetail = ClientDetail.builder()
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
        ClientDetail savedClientDetail = this.clientDetailRepository.save(clientDetail);

        //then
        assertThat(savedClientDetail).isNotNull();
        assertThat(savedClientDetail.getClientId()).isEqualTo(clientId);
        assertThat(passwordEncoder.matches(clientSecret, savedClientDetail.getClientSecret()));
        assertThat(savedClientDetail.getScope()).isEqualTo(scope);
        assertThat(savedClientDetail.getAuthorizedGrantTypes()).isEqualTo(authorizedGrantTypes);
        assertThat(savedClientDetail.getWebServerRedirectUri()).isEqualTo(webServerRedirectUri);
        assertThat(savedClientDetail.getAuthorities()).isEqualTo(authorities);
        assertThat(savedClientDetail.getAccessTokenValidity()).isEqualTo(accessTokenValidity);
        assertThat(savedClientDetail.getRefreshTokenValidity()).isEqualTo(refreshTokenValidity);
    }
}