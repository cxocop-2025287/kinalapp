package com.carlosxocop.kinalapp.repository;

import com.carlosxocop.kinalapp.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(int estado);

    List<Venta> findByUsuarioCodigo_usuario(String codigo_usuario);

    List<Venta> findByClienteCodigo_cliente(String codigo_cliente);


}