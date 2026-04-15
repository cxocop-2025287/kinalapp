package com.carlosxocop.kinalapp.repository;

import com.carlosxocop.kinalapp.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByEstado(int estado);
}
