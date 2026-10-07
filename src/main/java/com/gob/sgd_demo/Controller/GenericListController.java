package com.gob.sgd_demo.Controller;

import com.gob.sgd_demo.Dto.Global.GenericBasicListDTO;
import com.gob.sgd_demo.Dto.Global.GenericDescListDTO;
import com.gob.sgd_demo.Dto.Usuario.UsuarioInfoDTO;
import com.gob.sgd_demo.Service.GenericListService;
import com.gob.sgd_demo.Service.UsuarioService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/gelist")
public class GenericListController {
    private final GenericListService genericListService;
    private final UsuarioService usuarioService;

    public GenericListController(GenericListService genericListService, UsuarioService usuarioService) {
        this.genericListService = genericListService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/dependencies")
    public ResponseEntity<List<GenericDescListDTO>> obtenerDependenciasAccesoAutenticado(@AuthenticationPrincipal Jwt jwt) {
        String codUser = jwt.getSubject();
        return obtenerDependencias(codUser);
    }

    @GetMapping("/dependencies/{codUser}")
    public ResponseEntity<List<GenericDescListDTO>> obtenerDependenciasAccesoPublico(@PathVariable String codUser) {
        return obtenerDependencias(codUser);
    }

    @GetMapping("/anios")
    public ResponseEntity<List<GenericBasicListDTO>> listarAnios(){
        return ResponseEntity.ok(genericListService.listarAnios());
    }

    @GetMapping("/stremi")
    public ResponseEntity<List<GenericBasicListDTO>> listarEstadoRemitos(){
        return ResponseEntity.ok(genericListService.listarEstadoRemitos());
    }

    @GetMapping("/stenv")
    public  ResponseEntity<List<GenericBasicListDTO>> listarEstadoEnvio(){
        return ResponseEntity.ok(genericListService.listarEstadoEnvio());
    }

    @GetMapping("/priority")
    public ResponseEntity<List<GenericBasicListDTO>> listarPrioridad(){
        return ResponseEntity.ok(genericListService.listarPrioridad());
    }
    @GetMapping("/depdoc/{codDep}")
    public ResponseEntity<List<GenericBasicListDTO>> listarTipoDoc(@PathVariable String codDep){
        return ResponseEntity.ok(genericListService.listarTipodoc(codDep));
    }

    @GetMapping("/empfirma/{codDep}")
    public ResponseEntity<List<GenericBasicListDTO>> listarEmpFirma(@PathVariable String codDep){
        return ResponseEntity.ok(genericListService.listarEmpFirma(codDep));
    }

    @GetMapping("/lstdepen/{codDep}")
    public ResponseEntity<List<GenericDescListDTO>> listarDependencia(@PathVariable String codDep) {
        return ResponseEntity.ok(genericListService.listarDependencia(codDep));
    }

    @GetMapping("/reforigen/{codDep}")
    public ResponseEntity<List<GenericDescListDTO>> listarRefOrigen(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable  String codDep,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
        String codUser = jwt.getSubject();
        return ResponseEntity.ok(genericListService.listarRefOrigen(codUser,codDep,desde,hasta));
    }

    @GetMapping("/destinatario/{codDep}")
    public ResponseEntity<List<GenericDescListDTO>> listarDestinatario(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable  String codDep,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
        String codUser = jwt.getSubject();
        return ResponseEntity.ok(genericListService.listarDestinatario(codUser,codDep,desde,hasta));
    }

    @GetMapping("/empdestino/{codDep}")
    public ResponseEntity<List<GenericBasicListDTO>> listarEmpDestino(
            @PathVariable  String codDep){
        return ResponseEntity.ok(genericListService.listarEmpDestino(codDep));
    }

    @GetMapping("/empelabora/{codDep}")
    public ResponseEntity<List<GenericBasicListDTO>> listarEmpElabora(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable  String codDep){
        String codUser = jwt.getSubject();
        return ResponseEntity.ok(genericListService.listarEmpElabora(codUser,codDep));
    }

    @GetMapping("/tema/{codDep}")
    public ResponseEntity<List<GenericBasicListDTO>> listarTema(@PathVariable  String codDep){
        return ResponseEntity.ok(genericListService.listarTema(codDep));
    }

    private ResponseEntity<List<GenericDescListDTO>> obtenerDependencias(String codUser) {
        try {
            UsuarioInfoDTO info = usuarioService.obtenerInfoUsuario(codUser);
            List<GenericDescListDTO> dependencias = genericListService.obtenerDependenciasAcceso(info.codEmp(), codUser);
            return ResponseEntity.ok(dependencias);
        } catch (NoSuchElementException e) {
            return ResponseEntity.ok(List.of());
        }
    }
}
