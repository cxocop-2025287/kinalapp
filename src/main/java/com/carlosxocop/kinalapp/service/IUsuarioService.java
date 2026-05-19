package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorCodigo(Long codigo);

    Optional<Usuario> buscarPorUsername(String username);

    Usuario actualizar(Long codigo, Usuario usuario);

    void eliminar(Long codigo);

    boolean existePorCodigo(Long codigo);

    List<Usuario> listarPorEstado(int estado);
}