package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.DetalleVenta;
import com.carlosxocop.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    List<DetalleVenta> listarTodos();

    DetalleVenta guardar(DetalleVenta detalleVenta);

    Optional<DetalleVenta> buscarPorCodigo(Long codigo);

    DetalleVenta actualizar(Long codigo, DetalleVenta detalleVenta);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);

}
