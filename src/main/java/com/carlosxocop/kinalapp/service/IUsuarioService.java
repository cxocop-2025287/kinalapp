package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Cliente;
import com.carlosxocop.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();

    Usuario guardar (Usuario usuario);

    Optional<Usuario> buscarPorCodigo(String codigo);

    Usuario actualizar(String codigo, Usuario usuario);

    void eliminar(String codigo);

    boolean existePorCodigo(String codigo);

    List<Usuario> listarPorEstado(int estado);

}