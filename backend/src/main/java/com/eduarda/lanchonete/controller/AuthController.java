package com.eduarda.lanchonete.controller;

import com.eduarda.lanchonete.dto.CadastroRequest;
import com.eduarda.lanchonete.dto.LoginRequest;
import com.eduarda.lanchonete.model.Usuario;
import com.eduarda.lanchonete.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody CadastroRequest req) {
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email é obrigatório");
        }
        if (req.getSenha() == null || req.getSenha().isBlank()) {
            return ResponseEntity.badRequest().body("Senha é obrigatória");
        }

        if (usuarioRepo.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        Usuario u = new Usuario();
        u.setNome(req.getNome());
        u.setEmail(req.getEmail());
        u.setSenhaHash(encoder.encode(req.getSenha())); 

        Usuario salvo = usuarioRepo.save(u);

        return ResponseEntity.ok("Usuário cadastrado com id: " + salvo.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var usuarioOpt = usuarioRepo.findByEmail(req.getEmail());
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }

        Usuario u = usuarioOpt.get();
        boolean ok = encoder.matches(req.getSenha(), u.getSenhaHash());
        if (!ok) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }

        return ResponseEntity.ok("Login OK! Bem-vindo(a), " + u.getNome());
    }
}
