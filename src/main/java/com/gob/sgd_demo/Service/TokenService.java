package com.gob.sgd_demo.Service;

import com.gob.sgd_demo.Dto.Usuario.UsuarioAuthDTO;
import com.gob.sgd_demo.Dto.Usuario.UsuarioInfoDTO;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generarToken(UsuarioAuthDTO credenciales){
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("sgd_demo")
                .issuedAt(Instant.now())
                .subject(credenciales.codUser())
                .claim("cemp_codemp", credenciales.cempCodemp())
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
