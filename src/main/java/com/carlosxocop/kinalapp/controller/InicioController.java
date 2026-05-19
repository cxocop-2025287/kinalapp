package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InicioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public InicioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"/", "/inicio"})
    public String mostrarInicio(Authentication authentication, Model model) {
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorUsername(username).orElse(null);
        model.addAttribute("usuario", usuario);
        return "inicio";
    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "logout", required = false) String logout,
                               Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Sesión cerrada correctamente");
        }
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            boolean existe = usuarioService.listarTodos().stream().anyMatch(u -> u.getUsername().equals(usuario.getUsername()));

            if (existe) {
                redirectAttributes.addFlashAttribute("error", "El usuario ya existe");
                return "redirect:/registro";
            }

            usuario.setEstado(1);
            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("USER");
            }

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/registro";
        }
    }
}