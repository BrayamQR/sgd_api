package com.gob.sgd_demo.Dto.Usuario;

public record UsuarioMenuAccesoDTO(
        String codUser,
        String codOpc,
        String desOpc,
        Integer idGroupOpc,
        String deGroupOpc,
        String titleOpc,
        String pathOpc,
        String iconOpc,
        Integer orderOpc
) {
}
