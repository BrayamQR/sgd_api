package com.gob.sgd_demo.Service;

import com.gob.sgd_demo.Dto.Usuario.*;
import com.gob.sgd_demo.Repository.IUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository ) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioInfoDTO obtenerInfoUsuario(String codUser) {
        return usuarioRepository.obtenerInfoUsuario(codUser)
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuario no encontrado: " + codUser));
    }

    public List<UsuarioPermisosDTO> obtenerPermisos(String codUser){
        return usuarioRepository.obtenerPermisos(codUser);
    }

    public List<UsuarioMenuNodoDTO> obtenerMenuAcceso(String codUser){
        List<UsuarioMenuAccesoDTO> planos = usuarioRepository.obtenerMenuAcceso(codUser);
        return construirArbol(planos);
    }

    public Optional<UsuarioConfigDTO> obtenerConfig (String codDep, String codEmp){
        return usuarioRepository.obtenerConfig(codDep,codEmp);
    }

    private List<UsuarioMenuNodoDTO> construirArbol(List<UsuarioMenuAccesoDTO> planos) {
        Map<Integer, String> nombrePorGrupo = new LinkedHashMap<>();
        Map<Integer, List<UsuarioMenuOpcionDTO>> opcionesPorGrupo = new HashMap<>();

        for (UsuarioMenuAccesoDTO item : planos) {
            nombrePorGrupo.putIfAbsent(item.idGroupOpc(), item.deGroupOpc());

            UsuarioMenuOpcionDTO opcion = new UsuarioMenuOpcionDTO(
                    item.codOpc(),
                    item.desOpc(),
                    item.titleOpc(),
                    item.pathOpc(),
                    item.iconOpc(),
                    item.orderOpc()
            );

            opcionesPorGrupo
                    .computeIfAbsent(item.idGroupOpc(), k -> new ArrayList<>())
                    .add(opcion);
        }

        return nombrePorGrupo.entrySet().stream()
                .map(entry -> {
                    List<UsuarioMenuOpcionDTO> opciones = opcionesPorGrupo.get(entry.getKey())
                            .stream()
                            .sorted(Comparator.comparing(UsuarioMenuOpcionDTO::orderOpc))
                            .collect(Collectors.toList());
                    return new UsuarioMenuNodoDTO(entry.getKey(), entry.getValue(), opciones);
                })
                .sorted(Comparator.comparing(grupo -> grupo.menuChildren().get(0).orderOpc()))
                .collect(Collectors.toList());
    }
}
