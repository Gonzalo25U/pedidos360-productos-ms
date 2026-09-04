package com.pedidos360.productos_ms.config;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final Collection<String> expectedAudiences;

    public AudienceValidator(Collection<String> expectedAudiences) {
        this.expectedAudiences = expectedAudiences;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (jwt.getAudience() != null && jwt.getAudience().stream().anyMatch(expectedAudiences::contains)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error(
                "invalid_token",
                "El token no contiene ninguna audience esperada: " + expectedAudiences,
                null
        );
        return OAuth2TokenValidatorResult.failure(error);
    }
}
