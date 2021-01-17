package io.example.authorization.domain.partner.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class PartnerAccountEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long partnerNo; // Primary Key : Auto Increment

    @Column(unique = true)
    private String partnerId; // 제휴사 ID : Unique key

    @JsonIgnore
    private String partnerPassword; // 제휴사 비밀번호

    private String partnerEmail; // 제휴사 email
    private String partnerCompanyName; // 제휴사 명

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientId; // Token 발급을 위한 Client ID

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String clientSecret; // Token 발급을 위한 Client Secret

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<PartnerRole> roles; // 파트너에게 부여된 권한

    @Enumerated(EnumType.STRING)
    private PartnerStatus status; // 파트너 상태

    public void signUp(){
        this.roles = Collections.singleton(PartnerRole.FORBIDDEN);
        this.status = PartnerStatus.API_NOT_AVAILABLE;
    }
}
