package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.DetalleVenta;
import com.carlosxocop.kinalapp.entity.Producto;
import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.service.DetalleVentaService;
import com.carlosxocop.kinalapp.service.ProductoService;
import com.carlosxocop.kinalapp.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;
    private final VentaService ventaService;
    private final ProductoService productoService;

    public DetalleVentaController(DetalleVentaService detalleVentaService, VentaService ventaService, ProductoService productoService) {
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @GetMapping("/lista")
    public String listarDetalleVentas(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesion primero");
            return "redirect:/login";
        }
        List<DetalleVenta> detalles = detalleVentaService.listarTodos();
        model.addAttribute("detalles", detalles);
        return "detalleVenta-lista";
    }

    @GetMapping("/nuevo/{codigoVenta}")
    public String formularioNuevoDetalle(@PathVariable Long codigoVenta, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesion primero");
            return "redirect:/login";
        }

        try {
            Venta venta = ventaService.buscarPorCodigo(codigoVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            List<Producto> productos = productoService.listarPorEstado(1);

            model.addAttribute("detalleVenta", new DetalleVenta());
            model.addAttribute("venta", venta);
            model.addAttribute("productos", productos);
            return "detalleVenta-formulario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Esta Venta no existe");
            return "redirect:/venta/lista";
        }
    }

    @PostMapping("/guardar")
    public String guardarDetalleVenta(@ModelAttribute DetalleVenta detalleVenta, @RequestParam Long ventaCodigo, @RequestParam Long productoCodigo, RedirectAttributes redirectAttributes) {
        try {
            Venta venta = ventaService.buscarPorCodigo(ventaCodigo)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            Producto producto = productoService.buscarPorCodigo(productoCodigo)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(producto);

            detalleVentaService.guardar(detalleVenta);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle de venta guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/detalleVenta/lista";
    }

    @GetMapping("/editar/{codigo}")
    public String formularioEditarDetalle(@PathVariable Long codigo, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("error", "Debe iniciar sesion primero");
            return "redirect:/login";
        }
        try {
            DetalleVenta detalle = detalleVentaService.buscarPorCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
            List<Producto> productos = productoService.listarPorEstado(1);

            model.addAttribute("detalleVenta", detalle);
            model.addAttribute("productos", productos);
            return "detalleVenta-editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Detalle no encontrado");
            return "redirect:/detalleVenta/lista";
        }
    }

    @PostMapping("/actualizar/{codigo}")
    public String actualizarDetalleVenta(@PathVariable Long codigo, @ModelAttribute DetalleVenta detalleVenta, @RequestParam Long productoCodigo, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorCodigo(productoCodigo)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            detalleVenta.setProducto(producto);
            detalleVentaService.actualizar(codigo, detalleVenta);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/detalleVenta/lista";
    }

    @PostMapping("/eliminar/{codigo}")
    public String eliminarDetalleVenta(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            detalleVentaService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/detalleVenta/lista";
    }
}