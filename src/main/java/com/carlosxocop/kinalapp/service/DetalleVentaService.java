package com.carlosxocop.kinalapp.service;

import com.carlosxocop.kinalapp.entity.DetalleVenta;
import com.carlosxocop.kinalapp.entity.Producto;
import com.carlosxocop.kinalapp.entity.Venta;
import com.carlosxocop.kinalapp.repository.DetalleVentaRepository;
import com.carlosxocop.kinalapp.repository.ProductoRepository;
import com.carlosxocop.kinalapp.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService{
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    public DetalleVentaService(DetalleVentaRepository detalleRepository, ProductoRepository productoRepository, VentaRepository ventaRepository) {
        this.detalleVentaRepository = detalleRepository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        Producto producto = productoRepository.findById(detalleVenta.getProducto().getCodigo_producto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        detalleVenta.setPrecio(producto.getPrecio());
        detalleVenta.setSubtotal(detalleVenta.getCantidad() *detalleVenta.getPrecio());

        DetalleVenta guardado = detalleVentaRepository.save(detalleVenta);
        Venta venta = guardado.getVenta();

        double total = 0;

        List<DetalleVenta> detalleVentas = detalleVentaRepository
                .findByVentaCodigoVenta(venta.getCodigo_venta());

        for (int i = 0; i < detalleVentas.size(); i++) {
            total =total+ detalleVentas.get(i).getSubtotal();
        }

        venta.setTotal(BigDecimal.valueOf(total));
        ventaRepository.save(venta);

        return guardado;

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(Long codigo) {
        return detalleVentaRepository.findById(codigo);
    }

    @Override
    public DetalleVenta actualizar(Long codigo, DetalleVenta detalleVenta) {
        if (!detalleVentaRepository.existsById(codigo)) {
            throw new RuntimeException("DetalleVenta no encontrado con codigo " + codigo);
        }
        detalleVenta.setCodigo_detalle(codigo);
        Producto producto = productoRepository.findById(detalleVenta.getProducto().getCodigo_producto()).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        detalleVenta.setPrecio(producto.getPrecio());
        detalleVenta.setSubtotal(detalleVenta.getCantidad()*detalleVenta.getPrecio());

        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(Long codigo) {
        DetalleVenta detalleVenta = detalleVentaRepository.findById(codigo).orElseThrow(()->new RuntimeException("DetalleVenta no encontrado"));
        Venta venta = detalleVenta.getVenta();

        detalleVentaRepository.deleteById(codigo);
        double total = 0;
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaCodigoVenta(venta.getCodigo_venta());
        for (int i = 0; i < detalles.size(); i++) {
            total = total + detalles.get(i).getSubtotal();
        }

        venta.setTotal(BigDecimal.valueOf(total));
        ventaRepository.save(venta);
    }

    @Override
    public boolean existePorCodigo(Long codigo) {
        return detalleVentaRepository.existsById(codigo);
    }
}
