package com.carlosxocop.kinalapp.repository;

import com.carlosxocop.kinalapp.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Query("SELECT d FROM DetalleVenta d WHERE d.venta.codigo_venta = :codigo_venta")
    List<DetalleVenta> findByVentaCodigo(@Param("codigo_venta") Long codigo);

    @Query("SELECT d FROM DetalleVenta d WHERE d.producto.codigo_producto = :codigo_producto")
    List<DetalleVenta> findByProductoCodigo(@Param("codigo_producto") String codigoProducto);
}