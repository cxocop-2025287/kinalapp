package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.service.IUsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final IUsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(IUsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/lista")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        return "usuario-lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario-formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            boolean existe = usuarioService.listarTodos().stream().anyMatch(u -> u.getUsername().equals(usuario.getUsername()));

            if (existe) {
                redirectAttributes.addFlashAttribute("error", "El usuario ya existe");
                return "redirect:/usuarios/nuevo";
            }

            usuario.setEstado(1);
            if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
                usuario.setRol("USER");
            }

            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuarioService.guardar(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
        }
        return "redirect:/usuarios/lista";
    }

    @PostMapping("/eliminar/{codigo}")
    public String eliminarUsuario(@PathVariable Long codigo,
                                  RedirectAttributes redirectAttributes,
                                  Authentication authentication,
                                  HttpServletRequest request) {
        try {
            String usernameActual = authentication.getName();
            Usuario usuarioActual = usuarioService.buscarPorUsername(usernameActual).orElse(null);

            boolean esMiCuenta = (usuarioActual != null && usuarioActual.getCodigo_usuario().equals(codigo));

            usuarioService.eliminar(codigo);

            if (esMiCuenta) {
                request.logout();
                redirectAttributes.addFlashAttribute("mensaje", "Tu cuenta ha sido desactivada. Sesión cerrada.");
                return "redirect:/login";
            }

            redirectAttributes.addFlashAttribute("mensaje", "Usuario desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar el usuario");
        }
        return "redirect:/usuarios/lista";
    }
}