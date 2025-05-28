package org.mukulphougat.userindexerservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/error")
public class FallbackController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @GetMapping
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Map<String, Object> defaultErrorAttributes = errorAttributes.getErrorAttributes(
                (WebRequest) request,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS)
        );

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", defaultErrorAttributes.get("status"));
        body.put("error", defaultErrorAttributes.get("error"));
        body.put("message", defaultErrorAttributes.get("message"));
        body.put("path", defaultErrorAttributes.get("path"));

        HttpStatus status = HttpStatus.resolve((Integer) defaultErrorAttributes.get("status"));
        return new ResponseEntity<>(body, status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
