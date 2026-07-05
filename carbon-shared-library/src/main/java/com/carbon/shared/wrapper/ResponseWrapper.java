package com.carbon.shared.wrapper;

import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

@ControllerAdvice
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    private final Environment environment;

    public ResponseWrapper(Environment environment) {
        this.environment = Objects.requireNonNull(environment, "Environment cannot be null.");
    }

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return !returnType.getDeclaringClass().getPackageName().contains("org.springdoc");
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(
            @Nullable Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        return ServerResponse.builder()
                .serviceId(environment.getProperty("spring.application.name", "unknown-service"))
                .timestamp(System.currentTimeMillis())
                .response(body)
                .build();
    }
}
