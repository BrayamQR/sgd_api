package com.gob.sgd_demo.Dto.Usuario;

import java.util.List;

public record UsuarioMenuNodoDTO(
        Integer idGroupOpc,
        String deGroupOpc,
        List<UsuarioMenuOpcionDTO> menuChildren
) {
}
