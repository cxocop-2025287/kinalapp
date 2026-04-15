package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model, HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return "redirect:/producto/lista";
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {

        Usuario usuario = usuarioService.listarTodos().stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().orElse(null);

        if (usuario != null && usuario.getEstado() == 1) {
            session.setAttribute("usuario", usuario);
            return "redirect:/producto/lista";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario o contraseña incorrectos");
            return "redirect:/login";
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            boolean existe = usuarioService.listarTodos().stream()
                    .anyMatch(u -> u.getUsername().equals(usuario.getUsername()));

            if (existe) {
                redirectAttributes.addFlashAttribute("error", "El usuario ya existe");
                return "redirect:/registro";
            }

            usuario.setEstado(1);
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/registro";
        }
    }
}