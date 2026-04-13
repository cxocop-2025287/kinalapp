package com.carlosxocop.kinalapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name="Productos")
public class Producto{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="codigo_producto")
    private Long codigo_producto;
    @Column
    private String nombre_producto;
    @Column
    private double precio;
    @Column
    private int stock;
    @Column
    private int estado;

    public Producto() {
    }

    public Producto(Long codigo_producto, String nombre_producto, double precio, int stock, int estado) {
        this.codigo_producto = codigo_producto;
        this.nombre_producto = nombre_producto;
        this.precio = precio;
        this.stock = stock;
        this.estado = estado;
    }

    public Long getCodigo_producto() {
        return codigo_producto;
    }

    public void setCodigo_producto(Long codigo_producto) {
        this.codigo_producto = codigo_producto;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}