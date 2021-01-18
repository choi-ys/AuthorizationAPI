package io.example.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

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
}