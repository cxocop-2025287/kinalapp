package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Producto;
import com.carlosxocop.kinalapp.service.IProductoService;
import com.carlosxocop.kinalapp.util.JwtIdEncryptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/editar/{id}")
    public String formularioEditarProducto(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Long codigo = JwtIdEncryptor.decryptId(id);
            Producto producto = productoService.buscarPorCodigo(codigo).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            model.addAttribute("producto", producto);
            return "editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/producto/lista";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable String id, @ModelAttribute Producto producto, RedirectAttributes redirectAttributes) {
        try {
            Long codigo = JwtIdEncryptor.decryptId(id);
            productoService.actualizar(codigo, producto);
            redirectAttributes.addFlashAttribute("mensaje", "¡Producto actualizado exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/producto/lista";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            Long codigo = JwtIdEncryptor.decryptId(id);
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