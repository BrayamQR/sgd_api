package com.gob.sgd_demo.Dto.Login;

public record LoginResponseDTO(
        boolean exito,
        String msg,
        String codUser,
        String token
) {
}
