package io.example.authorization.domain.client.dto;

import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder @NoArgsConstructor
@AllArgsConstructor
public class ClientPublish {

    @NotNull
    private long partnerNo;

    @NotBlank(message = "서비스 이름을 입력하세요")
    @Size(min = 1, max = 25, message = "서비스 이름은 1 ~ 25자 이내로 입력 가능합니다.")
    private String resourceIds;

    @NotBlank
    @URL(message = "발급된 인증정보를 수신할 URL을 입력하세요.")
    private String webServerRedirectUri;
}