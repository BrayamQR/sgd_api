package com.gob.sgd_demo.Dto.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioInfoDTO(
        String codUser,
        String codEmp,
        String apePat,
        String apeMat,
        String deNom,
        String nuDni
) {
}
