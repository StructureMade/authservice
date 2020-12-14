package de.structuremade.ms.authservice.api.error;

import org.springframework.http.HttpStatus;

public class ApiError {
    private HttpStatus status;
    private String error;

    public ApiError(HttpStatus status, String error) {
        super();
        this.status = status;
        this.error = error;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public void setHttpStatus(HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
