package com.pedidos360.productos_ms.config;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.regex.Pattern;

public class MultiTenantIssuerValidator implements OAuth2TokenValidator<Jwt> {

    private static final Pattern ISSUER_PATTERN =
            Pattern.compile("^https://login\\.microsoftonline\\.com/([a-zA-Z0-9-]+)/v2\\.0$");

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
        String tid = jwt.getClaimAsString("tid");

        if (issuer == null || tid == null) {
            return fallo("El token no tiene issuer o claim tid validos");
        }

        var matcher = ISSUER_PATTERN.matcher(issuer);
        if (!matcher.matches()) {
            return fallo("El issuer no tiene el formato esperado de Azure AD: " + issuer);
        }

        String tenantEnIssuer = matcher.group(1);
        if (!tenantEnIssuer.equals(tid)) {
            return fallo("El tenant del issuer no coincide con el claim tid del token");
        }

        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult fallo(String mensaje) {
        return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", mensaje, null));
    }
}
