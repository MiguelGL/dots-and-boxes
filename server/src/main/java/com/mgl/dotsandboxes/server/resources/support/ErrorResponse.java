package com.mgl.dotsandboxes.server.resources.support;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    // This will be the same as the HTTP response status code for simplicity.
    private final int code;

    private final @NonNull String message;

    public static ErrorResponse forHttpStatusCode(HttpStatus status, String message) {
        return new ErrorResponse(status.value(),
                String.format("%s: %s", status.getReasonPhrase(), message));
    }

}
