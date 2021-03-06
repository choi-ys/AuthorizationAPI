package io.example.authorization.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessingResult<T> {

    private boolean success = true;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    public ProcessingResult(T data) {
        this.data = data;
    }

    public ProcessingResult(Error error) {
        this.success = false;
        this.error = error;
    }
}
