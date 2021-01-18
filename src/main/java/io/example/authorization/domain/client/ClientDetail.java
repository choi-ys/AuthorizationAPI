package io.example.authorization.domain.client;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "clinet_id")
@Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "oauth_client_details")
public class ClientDetail {

    @Id
    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "client_secret")
    private String clientSecret;

    private String scope;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    private String authorities;

    @Column(name = "access_token_validity")
    private int accessTokenValidity;

    @Column(name = "refresh_token_validity")
    private int refreshTokenValidity;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "autoapprove")
    private String autoApprove;
}