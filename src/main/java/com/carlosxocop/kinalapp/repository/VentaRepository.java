package com.carlosxocop.kinalapp.repository;

import com.carlosxocop.kinalapp.entity.Venta;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByEstado(int estado);

    @Query("SELECT v FROM Venta v WHERE v.usuario.codigo_usuario = :codigo_usuario")
    List<Venta> findByUsuarioCodigo(@Param("codigo_usuario") String codigo);

    @Query("SELECT v FROM Venta v WHERE v.cliente.dpi_Cliente = :dpi_cliente")
    List<Venta> findByClienteDpi(@Param("dpi_cliente") String dpi);

    @Modifying
    @Transactional
    @Query("UPDATE Venta v SET v.total = :total WHERE v.codigo_venta = :codigo_venta")
    void actualizarTotal(@Param("codigo_venta") Long codigoVenta, @Param("total") BigDecimal total);

}