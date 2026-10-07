package com.gob.sgd_demo.Controller;

import com.gob.sgd_demo.Dto.Usuario.*;
import com.gob.sgd_demo.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/info")
    public ResponseEntity<UsuarioInfoDTO> obtenerInfo(@AuthenticationPrincipal Jwt jwt) {
        String codUser = jwt.getSubject();
        return ResponseEntity.ok(usuarioService.obtenerInfoUsuario(codUser));
    }

    @GetMapping("/permission")
    public ResponseEntity<List<UsuarioPermisosDTO>> obtenerPermisos(@AuthenticationPrincipal Jwt jwt){
        String codUser = jwt.getSubject();
        return ResponseEntity.ok(usuarioService.obtenerPermisos(codUser));
    }

    @GetMapping("/menu")
    public ResponseEntity<List<UsuarioMenuNodoDTO>> obtenerMenuAcceso(@AuthenticationPrincipal Jwt jwt){
        String codUser = jwt.getSubject();
        return ResponseEntity.ok(usuarioService.obtenerMenuAcceso(codUser));
    }

    @GetMapping("/config/{codDep}/{codEmp}")
    public ResponseEntity<UsuarioConfigDTO> config(@PathVariable("codDep") String codDep,
                                                   @PathVariable("codEmp") String codEmp) {
        return usuarioService.obtenerConfig(codDep, codEmp)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
