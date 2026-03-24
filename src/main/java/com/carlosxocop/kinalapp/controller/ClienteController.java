package com.carlosxocop.kinalapp.controller;

import com.carlosxocop.kinalapp.entity.Cliente;
import com.carlosxocop.kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @ResponseBody
@RequestMapping("/clientes")
// Todas las rutas en este controlador deben empezar con /clientes
public class ClienteController {

    // Inyectamos el SERVICIO y no el reposotorio
    // El controller solo debe tener conexion con el servicio
    private final IClienteService clienteService;

    // Como Buena practica la inyeccion de dependencias debe hacerce por el constructor
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }
    //Reponde a peticiones GET
    @GetMapping
    public ResponseEntity <List<Cliente>> listar(){
        List<Cliente> clientes = clienteService.ListarTodos();
        // Delegamos al servicio
        return ResponseEntity.ok(clientes);
        //200 ok con la lista de clientes
    }
    /*
    {DPI}es una variable de ruta
     */
    // {dpi} es una variable de ruta (valor a buscar)
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi){
        //@PahVariable Toma el valor de la URL y lo asigna al dpi
        return clienteService.buscarPorDPI(dpi)
                //Si el opcional tiene valor devuelve 200 OK con el Cliente
                .map(ResponseEntity::ok)
                //Si opcional esta vacio devuelve 404 NOT FUND
                .orElse(ResponseEntity.notFound().build());
    }

    //Post crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente) {
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a unobjeto de tipo Cliente
        //<?> significa "tipo generico puede ser un CLiente o un String"
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Intentamos guardar el cliente pero puede lanzar un excepcion
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 created(mucho mas especifico que el 2200 para la cracion de un cliente)
        } catch (IllegalArgumentException e) {
            //Si hay error de validacion
            return ResponseEntity.badRequest().body(e.getMessage());
            //400 BAD REQUES con el mensaje de error
        }
    }

    // DELETE elimina un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        // no devuelve cuerpo en la respuesta
        try{
            if(!clienteService.existePorDPI(dpi)){
                return  ResponseEntity.notFound().build();
                //404 si no existe
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
        }catch(RuntimeException e) {
            return ResponseEntity.notFound().build();
            // 404 del not found
        }
    }

    // Actualizar
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try {
            if(!clienteService.existePorDPI(dpi)){
                // Verificar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
                // 303 Not Found
            }
            // Actualizamos el cliente pero esto puede lanzar una excepcion
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
            // 200 ok con el cliente ya actualizado
        }catch(IllegalArgumentException e){
            // Error cuando los datos sean incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(RuntimeException e) {
            // Posiblemente cualquier otro error como: cliente no encontrado, etc.
            // 404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> listarActivos() {
        List<Cliente> activos = clienteService.listarPorEstado(1);
        return ResponseEntity.ok(activos);
    }

}
