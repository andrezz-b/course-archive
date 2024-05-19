package com.andrezzb.coursearchive.security.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  private final JwtEncoder encoder;
  private final JwtDecoder decoder;

  public TokenService(JwtEncoder encoder, JwtDecoder decoder) {
    this.encoder = encoder;
    this.decoder = decoder;
  }

  private enum TokenType {
    ACCESS, REFRESH
  }

  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    String scope = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(" "));
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plus(30, ChronoUnit.MINUTES))
        .subject(authentication.getName())
        .claim("scope", scope)
        .claim("type", TokenType.ACCESS.toString())
        .build();
    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  public String generateRefreshToken(Authentication authentication) {
    Instant now = Instant.now();
    JwtClaimsSet claims = JwtClaimsSet.builder()
        .issuer("self")
        .issuedAt(now)
        .expiresAt(now.plus(7, ChronoUnit.DAYS))
        .subject(authentication.getName())
        .claim("type", TokenType.REFRESH.toString())
        .build();
    return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  public Jwt validateRefreshToken(String token) {
    Jwt jwt = this.decoder.decode(token);
    if (!jwt.getClaimAsString("type").equals(TokenType.REFRESH.toString())) {
      throw new JwtException("Invalid token type, want: " + TokenType.REFRESH.toString());
    }
    if (jwt.getExpiresAt().isBefore(Instant.now())) {
      throw new JwtException("Token expired");
    }
    return jwt;
  }
}
