package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Producto;
import com.carlosxocop.kinalapp.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService implements IProductoService{

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardar(Producto producto) {
        validarProducto(producto);
        if (producto.getEstado()==0) {
            producto.setEstado(1);
        }
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return productoRepository.findById(codigo);
    }

    @Override
    public Producto actualizar(String codigo, Producto producto) {
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("Producto no encontrado con codigo " + codigo);
        }
        producto.setCodigo_producto(codigo);
        validarProducto(producto);

        return productoRepository.save(producto);
    }

    @Override
    public void eliminar(String codigo) {
        Producto producto = productoRepository.findById(codigo).orElseThrow(() -> new IllegalArgumentException("No existe ningun producto con el codigo:  " + codigo));

        producto.setEstado(0);
        productoRepository.save(producto);
    }

    @Override
    @Transactional (readOnly = true)
    public boolean existePorCodigo(String codigo) {
        return productoRepository.existsById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorEstado(int estado) {
        return productoRepository.findByEstado(estado);
    }

    private void validarProducto(Producto producto){

        if (producto.getCodigo_producto() == null || producto.getCodigo_producto().trim().isEmpty()){
            throw new IllegalArgumentException("El codigo del producto es un dato obligatorio.");
        }
        if (producto.getNombre_producto() == null || producto.getNombre_producto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligtorio.");
        }
    }

}