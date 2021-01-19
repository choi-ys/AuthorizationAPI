package io.example.authorization.config;

import io.example.authorization.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // http://localhost:8080/oauth/authorize?client_id=clientId&response_type=code&scope=read&redirect_uri=http://localhost:8080/oauth/callback
    // http://localhost:8080/oauth/authorize?client_id=kstmClientId&response_type=code&scope=read&redirect_uri=http://localhost:8080/oauth/callback
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * Debugging StackTrace
         * #1 ClientDetailsServiceConfigurer.jdbc(datasource)
         * #2 ClientDetailsServiceBuilder.clients(ClientDetailsService clientDetailsService)
         * #3 [implement]JdbcClientDetailsService.loadClientByClientId(String clientId) -> [Interface]ClientDetailsService.loadClientByClientId(String clientId)
         *    : 결과적으로 Application에 설정된 DataSource에서 해당 쿼리가 수행되면서 DB의 client 데이터를 참조한다.
         *    -> this.selectClientDetailsSql =
         *       "select
         *          client_id,
         *          client_secret,
         *          resource_ids,
         *          scope,
         *          authorized_grant_types,
         *          web_server_redirect_uri,
         *          authorities,
         *          access_token_validity,
         *          refresh_token_validity,
         *          additional_information,
         *          autoapprove
         *          from
         *              oauth_client_details
         *          where
         *              client_id = ?";
         */
        clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PartnerService partnerService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) // Account 인증 정보를 소유한 Bean
                .userDetailsService(partnerService) // Account 인증 처리 Service Bean
                .tokenStore(new JdbcTokenStore(dataSource)) // application에 명시된 dataSource를 tokenStore로 설정
                .reuseRefreshTokens(false) //refresh token을 이용한 access_token 재 발급 시 기존 refresh token 소멸 처리
        ;
    }
}