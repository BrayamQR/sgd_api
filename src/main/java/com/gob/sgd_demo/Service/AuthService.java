package com.gob.sgd_demo.Service;

import com.gob.sgd_demo.Dto.Login.LoginRequestDTO;
import com.gob.sgd_demo.Dto.Login.LoginResponseDTO;
import com.gob.sgd_demo.Dto.Usuario.UsuarioAuthDTO;
import com.gob.sgd_demo.Dto.Usuario.UsuarioInfoDTO;
import com.gob.sgd_demo.Repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {
    private final IUsuarioRepository usuarioRepository;
    private final CifradoService cifradoService;
    private final TokenService tokenService;

    @Value("${app.cifrado.key}")
    private String cifradoKey;

    public AuthService(IUsuarioRepository usuarioRepository, CifradoService cifradoService, TokenService tokenService) {
        this.usuarioRepository = usuarioRepository;
        this.cifradoService = cifradoService;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        UsuarioAuthDTO credenciales;

        try {
            credenciales = usuarioRepository.obtenerCredenciales(request.codUser())
                    .orElseThrow(
                            () -> new NoSuchElementException("Usuario no encontrado")
                    );

        } catch (NoSuchElementException e) {
            return new LoginResponseDTO(false, "Usuario o contraseña incorrectos", request.codUser(), null);
        }
        boolean passwordValida;

        try {
            String passwordCifrado = cifradoService.cifrar(request.password(), cifradoKey);
            passwordValida = passwordCifrado.equalsIgnoreCase(credenciales.dePassword());
        } catch (Exception e) {
            return new LoginResponseDTO(false, "Error al validar credenciales", request.codUser(), null);
        }

        if (!passwordValida) {
            return new LoginResponseDTO(false, "Usuario o contraseña incorrectos", request.codUser(), null);
        }

        String token = tokenService.generarToken(credenciales);
        return new LoginResponseDTO(true, "Login exitoso", credenciales.codUser(), token);
    }

}