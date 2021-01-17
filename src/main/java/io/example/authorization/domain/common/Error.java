package io.example.authorization.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class Error {

    private int code;
    private String message;
    private String detail;
}
