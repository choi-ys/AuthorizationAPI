package io.example.authorization.domain.client.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.example.authorization.domain.partner.entity.PartnerEntity;
import io.example.authorization.domain.partner.entity.PartnerRole;
import lombok.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "clinet_id")
@Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "oauth_client_details")
public class ClientEntity {

    @Id
    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column(name = "resource_ids")
    private String resourceIds;

    @Column(name = "client_secret_origin")
    private String clientSecretOrigin;

    @Column(name = "client_secret")
    private String clientSecret;

    private String scope;

    @JsonIgnore
    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "web_server_redirect_uri")
    private String webServerRedirectUri;

    private String authorities;

    @JsonIgnore
    @Column(name = "access_token_validity")
    private int accessTokenValidity;

    @JsonIgnore
    @Column(name = "refresh_token_validity")
    private int refreshTokenValidity;

    @JsonIgnore
    @Column(name = "additional_information")
    private String additionalInformation;

    @JsonIgnore
    @Column(name = "autoapprove")
    private String autoApprove;

    public void setPublishedClientInfo(PartnerEntity partnerEntity){
        this.clientId = partnerEntity.getClientId();

        this.clientSecretOrigin = partnerEntity.getPartnerNo() + ":" + partnerEntity.getPartnerId() + ":" + clientId;
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.clientSecret = passwordEncoder.encode(clientSecretOrigin);

        this.scope = "read";
        this.authorizedGrantTypes = "authorization_code,refresh_token";
        this.authorities = PartnerRole.TRIAL.name();
        this.accessTokenValidity = 43200; // 12시간
        this.refreshTokenValidity = 86400; // 24시간
    }
}