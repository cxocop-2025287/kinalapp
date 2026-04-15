package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Cliente;
import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.service.IClienteService;
import com.carlosxocop.kinalapp.service.IUsuarioService;
import com.carlosxocop.kinalapp.service.IVentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final IUsuarioService usuarioService;

    public VentaController(IVentaService ventaService, IClienteService clienteService, IUsuarioService usuarioService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nuevo")
    public String formularioNuevaVenta(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        // Verificar si hay usuario en sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión primero");
            return "redirect:/login";
        }

        List<Cliente> clientes = clienteService.listarPorEstado(1);

        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clientes);
        model.addAttribute("usuario", usuario);

        return "venta-formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@RequestParam Long clienteDpi,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión primero");
                return "redirect:/login";
            }

            Cliente cliente = clienteService.buscarPorDPI(clienteDpi).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Venta venta = new Venta();
            venta.setUsuario(usuario);
            venta.setCliente(cliente);
            venta.setTotal(BigDecimal.ZERO);
            venta.setEstado(1);

            Venta nuevaVenta = ventaService.guardar(venta);

            redirectAttributes.addFlashAttribute("mensaje", "Venta creada exitosamente");
            // Redirigir a agregar productos a la venta
            return "redirect:/detalleVenta/nuevo/" + nuevaVenta.getCodigo_venta();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear venta: " + e.getMessage());
            return "redirect:/venta/nuevo";
        }
    }

    @GetMapping("/lista")
    public String listarVentas(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión primero");
            return "redirect:/login";
        }

        List<Venta> ventas = ventaService.listarTodos();
        model.addAttribute("ventas", ventas);
        model.addAttribute("usuario", usuario);
        return "venta-lista";
    }

    @GetMapping("/editar/{codigo}")
    public String formularioEditarVenta(@PathVariable Long codigo,
                                        Model model,
                                        HttpSession session,
                                        RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión primero");
            return "redirect:/login";
        }

        try {
            Venta venta = ventaService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            List<Cliente> clientes = clienteService.listarPorEstado(1);

            model.addAttribute("venta", venta);
            model.addAttribute("clientes", clientes);
            model.addAttribute("usuario", usuario);
            return "venta-editar";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Venta no encontrada");
            return "redirect:/venta/lista";
        }
    }

    @PostMapping("/actualizar/{codigo}")
    public String actualizarVenta(@PathVariable Long codigo,
                                  @RequestParam Long clienteDpi,
                                  @RequestParam int estado,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "Debe iniciar sesión primero");
                return "redirect:/login";
            }

            Cliente cliente = clienteService.buscarPorDPI(clienteDpi)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Venta venta = ventaService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            venta.setCliente(cliente);
            venta.setEstado(estado);
            venta.setUsuario(usuario);

            ventaService.actualizar(codigo, venta);

            redirectAttributes.addFlashAttribute("mensaje", "Venta actualizada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/venta/lista";
    }

    @PostMapping("/eliminar/{codigo}")
    public String eliminarVenta(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            ventaService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Venta desactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/venta/lista";
    }
}