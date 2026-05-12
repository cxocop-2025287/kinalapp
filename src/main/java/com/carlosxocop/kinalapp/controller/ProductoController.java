package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Producto;
import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.service.IProductoService;
import com.carlosxocop.kinalapp.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/nuevo")
    public String formularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.guardar(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto guardado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/producto/lista";
    }

    @GetMapping("/editar/{codigo}")
    public String formularioEditarProducto(@PathVariable Long codigo, Model model, RedirectAttributes redirectAttributes) {
        try {
            Producto producto = productoService.buscarPorCodigo(codigo).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            model.addAttribute("producto", producto);
            return "editar";  // Vista diferente: editar.html
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/producto/lista";
        }
    }


    @PostMapping("/actualizar/{codigo}")
    public String actualizarProducto(@PathVariable Long codigo, @ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizar(codigo, producto);
            redirectAttributes.addFlashAttribute("mensaje", "¡Producto actualizado exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/producto/lista";
    }

    @PostMapping("/eliminar/{codigo}")
    public String eliminarProducto(@PathVariable Long codigo, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(codigo);
            redirectAttributes.addFlashAttribute("mensaje", "Producto desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/producto/lista";
    }


    @GetMapping("/lista")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        return "lista";
    }
}