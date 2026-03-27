package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService implements IVentaService {

    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardar(Venta venta) {
        validarVenta(venta);
        venta.setFecha(LocalDate.now());
        venta.setTotal(null);
        if (venta.getEstado() == 0) {
            venta.setEstado(1);
        }
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(Long codigo) {
        return ventaRepository.findById(codigo);
    }


    @Override
    public Venta actualizar(Long codigo, Venta venta) {

        Venta actual = ventaRepository.findById(codigo).orElseThrow(()->new RuntimeException("Venta no encontrado con codigo " + codigo));
        venta.setCodigo_venta(codigo);
        venta.setTotal(actual.getTotal());
        validarVenta(venta);

        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(Long codigo) {
        if (!ventaRepository.existsById(codigo)) {
            throw new IllegalArgumentException("No existe ninguna venta con el codigo: " + codigo);

        }
        ventaRepository.deleteById(codigo);
    }

    @Override
    public boolean existePorCodigo(Long codigo) {
            return ventaRepository.existsById(codigo);
    }

    @Override
    public List<Venta> listarPorEstado(int estado) {
        return ventaRepository.findByEstado(estado);
    }

    private void validarVenta(Venta venta) {

        if (venta.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

    }
}