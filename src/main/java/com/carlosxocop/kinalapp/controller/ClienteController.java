package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Cliente;
import com.carlosxocop.kinalapp.service.IClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final IClienteService clienteService;

    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/nuevo")
    public String formularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-formulario";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteService.guardar(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }
        return "redirect:/cliente/lista";
    }

    @GetMapping("/editar/{dpi}")
    public String formularioEditarCliente(@PathVariable Long dpi, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.buscarPorDPI(dpi).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            model.addAttribute("cliente", cliente);
            return "cliente-editar";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
            return "redirect:/cliente/lista";
        }
    }

    @PostMapping("/actualizar/{dpi}")
    public String actualizarCliente(@PathVariable Long dpi, @ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteService.actualizar(dpi, cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:/cliente/lista";
    }

    @PostMapping("/eliminar/{dpi}")
    public String eliminarCliente(@PathVariable Long dpi, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(dpi);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:/cliente/lista";
    }

    @GetMapping("/lista")
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.ListarTodos();
        model.addAttribute("clientes", clientes);
        return "cliente-lista";
    }
}