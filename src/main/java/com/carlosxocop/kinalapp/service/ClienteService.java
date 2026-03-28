package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Cliente;
import com.carlosxocop.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

//Anotacion que registra un bean de modelo como un bean de spring
//La clase contiene la logica del negocio @Service
@Service
// Por defecto todos los metodos de esta clase seran transaccionales
// Una transaccion es que puede o no ocurrir algo, una trasferencia bancaria
@Transactional
public class ClienteService implements IClienteService{
    /*
        private: es accesible solo en esta clase
        ClienteRepositiry es el repositoruo para acceder
        inyeccion de Dependencias de Spring nos da el repositorio
     */
    private final ClienteRepository clienteRepository;

    /*
     Constructor : Este ejecuta al crear el objeto
     Asignamos el repositorio a nuestra variable de clase

     */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /*
     * @Override : Indica que estamos implementando un metodo de la interfaz
     *
     */
    @Override
    /*
    readOnly = true lo que hace es optimizar la consulata , no bloquea la  BD
     */
    @Transactional(readOnly = true)
    public List<Cliente> ListarTodos() {
        return clienteRepository.findAll();
        /**
         * este metodo llama al findAll del repositorio de JPA
         * este metodo hace las consultas de select * from y la tabla
         */
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        /*
        El metodo de guardar crea un cliente
        Aca es donde colocamos la logica del negocio Antes de guardar
        Primero validamos el dato
         */
        validarCliente(cliente);
        if (cliente.getEstado()==0) {
            cliente.setEstado(1);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        /*
        Buscar un cliente con dpi
         */
        return clienteRepository.findById(dpi);
        //Optional nos evita el NullPointerException
    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Actualiza un cliente existente
        if (!clienteRepository.existsById(dpi)) {
            throw new RuntimeException("Cliente no encontrado con DPI "+ dpi );
            // si no existe se lanza una excepcion (Error controlado)
        }
        /*
        1. Asegurar que el dpi del objeto coincida con el de la url
        @. por seguridad usamos el dpi de la url y no el que viene en json
         */
        cliente.setDpi_Cliente(dpi);
        validarCliente(cliente);

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(String dpi) {
        Cliente cliente = clienteRepository.findById(dpi).orElseThrow(() -> new IllegalArgumentException("No existe ningun cliente con el dpi : " + dpi));

        cliente.setEstado(0);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional (readOnly = true)
    public boolean existePorDPI(String dpi) {
        // Verificar si existe el cliente

        return clienteRepository.existsById(dpi);
        // retorna true o false
    }
    // Meotodo privado (Solo pueden utilizarce dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
        *Vlidaciones del negocio: Este metodo se hara privado
        * porque es algo interno del servicio
        * */
        if (cliente.getDpi_Cliente() == null || cliente.getDpi_Cliente().trim().isEmpty()){
            // SI el dpi es null o esta vacio despues de quitar espacios
            // lanza una exception con un mensaje
            throw new IllegalArgumentException("El DPI es un dato obligatorio.");

        }
        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligtorio.");
        }
        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido del cliente es obligtorio.");
        }


    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarPorEstado(int estado) {
        return clienteRepository.findByEstado(estado);
    }
}
