package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    // intefaz es un contrato que dice que debe tener cualquier servicio de clientes,
    // no tiene implementacion , solo la definicion de los metodos

    // Metodo que devuelve una lista de todos los clientes

    List<Cliente> ListarTodos();
    // La parte de listarTodo lo que hace es devolver una lista de objetos de la entidad clientes

    // Metodo que guarda cliente en la base de datos
    Cliente guardar (Cliente cliente);

    // Parametros : recibe un objeto de tipo cliente con los datos a guardar
    // Evita el nullPonterException
    Optional<Cliente> buscarPorDPI(Long dpi);

    // Metodo que actualiza un cliente
    Cliente actualizar(Long dpi, Cliente cliente);
    // Parametros: dpi del cliente a actualizar
    // cliente: objeto con los datos nuevos
    // este retorna un objeto de tipo cliente ya actualizado

    //Metodo de tipo void para eliminar a un cliente
    void eliminar(Long dpi);

    //boolean retorna veradero si existe y falso si no existe
    boolean existePorDPI(Long dpi);

    // muestra los dependiendo de su estado
    List<Cliente> listarPorEstado(int estado);


}
