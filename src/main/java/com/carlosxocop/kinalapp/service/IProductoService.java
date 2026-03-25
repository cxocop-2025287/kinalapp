package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarTodos();

    Producto guardar (Producto producto);

    Optional<Producto> buscarPorCodigo(String codigo);

    Producto actualizar(String codigo, Producto producto);

    void eliminar(String codigo);

    boolean existePorCodigo(String codigo);

    List<Producto> listarPorEstado(int estado);
}