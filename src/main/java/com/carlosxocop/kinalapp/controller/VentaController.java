package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {
    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping
    public ResponseEntity<List<Venta>> listar(){
        List<Venta> venta = ventaService.listarTodos();
        return ResponseEntity.ok(venta);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta) {
        try {
            Venta nuevaVenta = ventaService.guardar(venta);
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigo){
        try{
            if(!ventaService.existePorCodigo(codigo)){
                return  ResponseEntity.notFound().build();
            }
            ventaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable long codigo, @RequestBody Venta venta){
        try {
            if(!ventaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();

            }
            Venta ventaActualizada = ventaService.actualizar(codigo, venta);
            return ResponseEntity.ok(ventaActualizada);

        }catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Venta>> listarActivos() {
        List<Venta> activos = ventaService.listarPorEstado(1);
        return ResponseEntity.ok(activos);
    }

}
