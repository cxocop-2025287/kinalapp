package com.carlosxocop.kinalapp.repository;

import com.carlosxocop.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEstado(int estado);
    Optional<Usuario> findByUsername(String username);
}