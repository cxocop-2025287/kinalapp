
package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping
    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuario = usuarioService.listarTodos();
        // Delegamos al servicio
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable String codigo){
        try{
            if(!usuarioService.existePorCodigo(Long.valueOf(codigo))){
                return  ResponseEntity.notFound().build();
            }
            usuarioService.eliminar(Long.valueOf(codigo));
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable String codigo, @RequestBody Usuario usuario){
        try {
            if(!usuarioService.existePorCodigo(Long.valueOf(codigo))){
                return ResponseEntity.notFound().build();

            }
            Usuario usuarioActualizado = usuarioService.actualizar(Long.valueOf(codigo), usuario);
            return ResponseEntity.ok(usuarioActualizado);

        }catch(IllegalArgumentException e){

            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos() {
        List<Usuario> activos = usuarioService.listarPorEstado(1);
        return ResponseEntity.ok(activos);
    }
}
