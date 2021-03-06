package io.example.authorization.domain.partner.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class CreatePartner {

    @NotBlank(message = "아이디를 입력하세요.")
    @Size(min = 3, max = 10, message = "아이디는 3~15자 이내로 입력 가능합니다.")
    private String partnerId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 6, max = 25, message = "비밀번호는 6~25자 이내로 입력 가능합니다.")
    private String partnerPassword;

    @Email(message = "이메일 양식에 맞게 입력하세요")
    @NotBlank(message = "메일주소를 입력하세요.")
    private String partnerEmail;

    @NotBlank(message = "회사명을 입력하세요.")
    private String partnerCompanyName;
}