package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Usuario;
import com.carlosxocop.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        validarUsuario(usuario);
        if (usuario.getEstado() == 0) {
            usuario.setEstado(1);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(Long codigo) {
        return usuarioRepository.findById(codigo);
    }

    @Override
    public Usuario actualizar(Long codigo, Usuario usuario) {
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("Usuario no encontrado con codigo " + codigo);
        }
        usuario.setCodigo_usuario(codigo);
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Long codigo) {
        Usuario usuario = usuarioRepository.findById(codigo).orElseThrow(() -> new IllegalArgumentException("No existe ningun usuario con el codigo:  " + codigo));

        usuario.setEstado(0);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(Long codigo) {
        return usuarioRepository.existsById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorEstado(int estado) {
        return usuarioRepository.findByEstado(estado);
    }

    private void validarUsuario(Usuario usuario) {

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario es obligtorio.");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña del usuario es obligtorio.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del usuario es obligatorio.");
        }
        if (usuario.getRol() == null || usuario.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio.");
        }

    }
}