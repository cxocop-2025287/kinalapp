package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.DetalleVenta;
import com.carlosxocop.kinalapp.service.DetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalleVenta")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    public DetalleVentaController(DetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }


    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar() {
        List<DetalleVenta> detalleVenta = detalleVentaService.listarTodos();
        return ResponseEntity.ok(detalleVenta);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta) {
        try {
            DetalleVenta nuevoDetalleVenta = detalleVentaService.guardar(detalleVenta);
            return new ResponseEntity<>(nuevoDetalleVenta, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable long codigo, @RequestBody DetalleVenta detalleVenta){
        try {
            if(!detalleVentaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();

            }
            DetalleVenta detalleVentaActualizada = detalleVentaService.actualizar(codigo, detalleVenta);
            return ResponseEntity.ok(detalleVentaActualizada);

        }catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable Long codigo){
        try{
            if(!detalleVentaService.existePorCodigo(codigo)){
                return  ResponseEntity.notFound().build();
            }
            detalleVentaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}