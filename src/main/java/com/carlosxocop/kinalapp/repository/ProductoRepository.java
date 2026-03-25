package com.carlosxocop.kinalapp.repository;

import com.carlosxocop.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByEstado(int estado);
}