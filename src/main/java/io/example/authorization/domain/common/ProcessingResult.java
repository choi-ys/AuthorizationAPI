package io.example.authorization.domain.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProcessingResult<T> {

    private boolean success = true;
    private T data;
    private Error error;

    public ProcessingResult(T data) {
        this.data = data;
    }

    public ProcessingResult(Error error) {
        this.success = false;
        this.error = error;
    }
}
