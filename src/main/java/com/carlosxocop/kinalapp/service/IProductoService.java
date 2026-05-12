package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarTodos();

    Producto guardar (Producto producto);

    Optional<Producto> buscarPorCodigo(Long codigo);

    Producto actualizar(Long codigo, Producto producto);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);

    List<Producto> listarPorEstado(int estado);
}