package com.gob.sgd_demo.Service;

import com.gob.sgd_demo.Dto.Global.GenericBasicListDTO;
import com.gob.sgd_demo.Dto.Global.GenericDescListDTO;
import com.gob.sgd_demo.Dto.Usuario.UsuarioConfigDTO;
import com.gob.sgd_demo.Dto.Usuario.UsuarioInfoDTO;
import com.gob.sgd_demo.Repository.IGenericListRepository;
import com.gob.sgd_demo.Repository.IUsuarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GenericListService {
    private final IGenericListRepository genericListRepository;
    private final IUsuarioRepository usuarioRepository;

    public List<GenericDescListDTO> obtenerDependenciasAcceso (String codEmp, String codUser){
        return genericListRepository.obtenerDependenciasAcceso(codEmp, codUser);
    }

    public GenericListService(IGenericListRepository genericListRepository, IUsuarioRepository usuarioRepository) {
        this.genericListRepository = genericListRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<GenericBasicListDTO> listarAnios(){
        return this.genericListRepository.listarAnios();
    }

    public List<GenericBasicListDTO> listarEstadoRemitos(){
        return this.genericListRepository.listarEstados("TDTV_REMITOS");
    }

    public List<GenericBasicListDTO> listarEstadoEnvio(){
        return this.genericListRepository.listarEstados(("EST_ENV_DOC_MSJ"));
    }

    public List<GenericBasicListDTO> listarPrioridad(){
        return this.genericListRepository.listarPrioridad();
    }

    public List<GenericBasicListDTO> listarTipodoc(String codDep){
        return this.genericListRepository.listarTipoDoc(codDep);
    }

    public List<GenericBasicListDTO> listarEmpFirma(String codDep){
        return this.genericListRepository.listarEmpFirma(codDep);
    }

    public List<GenericDescListDTO> listarDependencia(String codDep){
        return this.genericListRepository.listarDependencia(codDep);
    }

    public List<GenericDescListDTO> listarRefOrigen(String codUser, String codDep, LocalDate desde, LocalDate hasta){
        if(desde.isAfter(hasta)){
            throw new IllegalArgumentException("Rango de fechas invalido");
        }
        UsuarioInfoDTO usuarioInfo = usuarioRepository.obtenerInfoUsuario(codUser).orElseThrow(()->new IllegalStateException("Usuario no encontrado"));

        String codEmp = usuarioInfo.codEmp();

        UsuarioConfigDTO usuarioConfig = usuarioRepository.obtenerConfig(codDep, codEmp).orElseThrow(()->new IllegalStateException("Usuario Sin configuracion"));

        String anio = (desde.getYear() == hasta.getYear())? String.valueOf(desde.getYear()) : "0";

        return genericListRepository.listarRefOrigen(anio, codDep, codEmp, usuarioConfig.tiAcceso());
    }

    public List<GenericDescListDTO> listarDestinatario(String codUser, String codDep, LocalDate desde, LocalDate hasta){
        if(desde.isAfter(hasta)){
            throw new IllegalArgumentException("Rango de fechas invalido");
        }
        UsuarioInfoDTO usuarioInfo = usuarioRepository.obtenerInfoUsuario(codUser).orElseThrow(()->new IllegalStateException("Usuario no encontrado"));

        String codEmp = usuarioInfo.codEmp();

        UsuarioConfigDTO usuarioConfig = usuarioRepository.obtenerConfig(codDep, codEmp).orElseThrow(()->new IllegalStateException("Usuario Sin configuracion"));

        String anio = (desde.getYear() == hasta.getYear())? String.valueOf(desde.getYear()) : "0";
        return genericListRepository.listarDestinatario(anio,codDep,codEmp,usuarioConfig.tiConsulta());
    }

    public List<GenericBasicListDTO> listarEmpDestino(String codDep){
        return genericListRepository.listarEmpDestino(codDep);
    }

    public List<GenericBasicListDTO> listarEmpElabora(String codUser, String codDep){
        List<GenericBasicListDTO> list = null;
        UsuarioInfoDTO usuarioInfo = usuarioRepository.obtenerInfoUsuario(codUser).orElseThrow(()->new IllegalStateException("Usuario no encontrado"));
        String codEmp = usuarioInfo.codEmp();
        UsuarioConfigDTO usuarioConfig = usuarioRepository.obtenerConfig(codDep, codEmp).orElseThrow(()->new IllegalStateException("Usuario Sin configuracion"));
        if(usuarioConfig.tiAcceso() != null && (usuarioConfig.tiAcceso().equals("0") || usuarioConfig.tiAcceso().equals("2"))){
            list = genericListRepository.listarEmpElabora(codDep);
        }
        else{
            list = genericListRepository.listarEmpElaboraByEmp(codDep,codEmp);
        }
        return list;
    }

    public List<GenericBasicListDTO> listarTema(String codDep){
        return genericListRepository.listarTema(codDep);
    }
}
