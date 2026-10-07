package com.gob.sgd_demo.Controller;

import com.gob.sgd_demo.Dto.Login.LoginRequestDTO;
import com.gob.sgd_demo.Dto.Login.LoginResponseDTO;
import com.gob.sgd_demo.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody LoginRequestDTO request){
        LoginResponseDTO response = authService.login(request);

        if(!response.exito()){
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
