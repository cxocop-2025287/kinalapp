package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    private final UsuarioRepository usuarioRepository;

    public InicioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping({"/", "/inicio"})
    public String mostrarInicio(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "inicio";
    }
}
