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

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    private final IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/")
    public String iniciarFormulario() {
        return "redirect:/productos/nuevo";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto, Model model) {
        productoService.guardar(producto);
        model.addAttribute("mensaje", "El producto se guardó de manera exitosa ");
        model.addAttribute("producto", new Producto()); // Limpiar formulario
        return "formulario";
    }

    @GetMapping("/lista")
    public String listarProductos(Model model) {
        List<Producto> productos = productoService.listarTodos();
        model.addAttribute("productos", productos);
        return "lista";
    }
}