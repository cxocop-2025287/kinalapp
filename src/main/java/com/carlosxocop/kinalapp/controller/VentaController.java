package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Cliente;
import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.repository.UsuarioRepository;
import com.carlosxocop.kinalapp.service.IClienteService;
import com.carlosxocop.kinalapp.service.IVentaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/venta")
public class VentaController {

    private final IVentaService ventaService;
    private final IClienteService clienteService;
    private final UsuarioRepository usuarioRepository;

    public VentaController(IVentaService ventaService, IClienteService clienteService, UsuarioRepository usuarioRepository) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/lista")
    public String listarVentas(Model model) {
        List<Venta> ventas = ventaService.listarTodos();
        model.addAttribute("ventas", ventas);
        return "venta-lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevaVenta(Model model, Authentication authentication) {
        List<Cliente> clientes = clienteService.listarPorEstado(1);
        Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElse(null);
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clientes);
        model.addAttribute("usuarioSesion", usuario);
        return "venta-formulario";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@RequestParam Long clienteDpi, @RequestParam String fecha, @RequestParam int estado, @RequestParam BigDecimal total, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = usuarioRepository.findByUsername(authentication.getName()).orElse(null);
            Cliente cliente = clienteService.buscarPorDPI(clienteDpi).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            Venta venta = new Venta();
            venta.setCliente(cliente);
            venta.setUsuario(usuario);
            venta.setFecha(LocalDate.parse(fecha));
            venta.setEstado(estado);
            venta.setTotal(total);

            Venta nuevaVenta = ventaService.guardar(venta);
            redirectAttributes.addFlashAttribute("mensaje", "Venta creada exitosamente");
            return "redirect:/detalleVenta/nuevo/" + nuevaVenta.getCodigo_venta();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear venta: " + e.getMessage());
            return "redirect:/venta/nuevo";
        }
    }

    @GetMapping("/editar/{codigo}")
    public String formularioEditarVenta(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            model.addAttribute("venta", venta);
            return "venta-editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Venta no encontrada");
            return "redirect:/venta/lista";
        }
    }

    @PostMapping("/actualizar/{codigo}")
    public String actualizarVenta(@PathVariable Long codigo, @RequestParam String fecha, @RequestParam int estado, @RequestParam BigDecimal total, RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            venta.setFecha(LocalDate.parse(fecha));
            venta.setEstado(estado);
            venta.setTotal(total);
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
