package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.DetalleVenta;
import com.carlosxocop.kinalapp.entity.Producto;
import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.service.DetalleVentaService;
import com.carlosxocop.kinalapp.service.ProductoService;
import com.carlosxocop.kinalapp.service.VentaService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/detalleVenta")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;
    private final VentaService ventaService;
    private final ProductoService productoService;

    public DetalleVentaController(DetalleVentaService detalleVentaService,
                                  VentaService ventaService,
                                  ProductoService productoService) {
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;
        this.productoService = productoService;
    }

    @GetMapping
    public String index() {
        return "redirect:/detalleVenta/lista";
    }

    @GetMapping("/lista")
    public String listarDetalleVentas(Model model, Authentication authentication) {
        List<DetalleVenta> detalles;

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // ADMIN ve todos los detalles
            detalles = detalleVentaService.listarTodos();
        } else {
            // USER solo ve los detalles de sus propias ventas
            detalles = detalleVentaService.listarTodos().stream()
                    .filter(d -> d.getVenta().getUsuario().getUsername().equals(authentication.getName()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("detalles", detalles);
        return "detalleVenta-lista";
    }

    @GetMapping("/nuevo/{codigoVenta}")
    public String formularioNuevoDetalle(@PathVariable Long codigoVenta, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            Venta venta = ventaService.buscarPorCodigo(codigoVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = venta.getUsuario().getUsername().equals(authentication.getName());

            if (!isAdmin && !isOwner) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para agregar detalles a esta venta");
                return "redirect:/venta/lista";
            }

            List<Producto> productos = productoService.listarPorEstado(1);

            model.addAttribute("detalleVenta", new DetalleVenta());
            model.addAttribute("venta", venta);
            model.addAttribute("productos", productos);
            return "detalleVenta-formulario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Venta no encontrada");
            return "redirect:/venta/lista";
        }
    }

    @PostMapping("/guardar")
    public String guardarDetalleVenta(@ModelAttribute DetalleVenta detalleVenta, @RequestParam Long ventaCodigo, @RequestParam Long productoCodigo, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            Venta venta = ventaService.buscarPorCodigo(ventaCodigo)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = venta.getUsuario().getUsername().equals(authentication.getName());

            if (!isAdmin && !isOwner) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para agregar detalles a esta venta");
                return "redirect:/venta/lista";
            }

            Producto producto = productoService.buscarPorCodigo(productoCodigo).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

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
    public String formularioEditarDetalle(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            DetalleVenta detalle = detalleVentaService.buscarPorCodigo(codigo).orElseThrow(() -> new RuntimeException("Detalle no encontrado"));


            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = detalle.getVenta().getUsuario().getUsername().equals(authentication.getName());

            if (!isAdmin && !isOwner) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este detalle");
                return "redirect:/detalleVenta/lista";
            }

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
    public String actualizarDetalleVenta(@PathVariable Long codigo, @ModelAttribute DetalleVenta detalleVenta, @RequestParam Long productoCodigo, @RequestParam Long ventaCodigo, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            Venta venta = ventaService.buscarPorCodigo(ventaCodigo).orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = venta.getUsuario().getUsername().equals(authentication.getName());

            if (!isAdmin && !isOwner) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para actualizar este detalle");
                return "redirect:/detalleVenta/lista";
            }

            Producto producto = productoService.buscarPorCodigo(productoCodigo).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            detalleVenta.setVenta(venta);
            detalleVenta.setProducto(producto);
            detalleVentaService.actualizar(codigo, detalleVenta);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/detalleVenta/lista";
    }

    @PostMapping("/eliminar/{codigo}")
    public String eliminarDetalleVenta(@PathVariable Long codigo, RedirectAttributes redirectAttributes, Authentication authentication) {
        try {
            DetalleVenta detalle = detalleVentaService.buscarPorCodigo(codigo).orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isOwner = detalle.getVenta().getUsuario().getUsername().equals(authentication.getName());

            if (!isAdmin && !isOwner) {
                redirectAttributes.addFlashAttribute("error", "No tienes permiso para eliminar este detalle");
                return "redirect:/detalleVenta/lista";
            }

            detalleVentaService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Detalle eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/detalleVenta/lista";
    }
}