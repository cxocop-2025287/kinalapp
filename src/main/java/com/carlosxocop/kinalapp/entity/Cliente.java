package com.carlosxocop.kinalapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    @Column(name="dpi_cliente")
    private String dpi_Cliente;
    @Column
    private String nombreCliente;
    @Column
    private String apellidoCliente;
    @Column
    private String direccionCliente;
    @Column
    private int estado;


    public Cliente(String id_cliente){
    }

    public Cliente(){

    }

    public Cliente(String dpi_Cliente, String nombreCliente, String apellidoCliente, String direccionCliente, int estado) {
        this.dpi_Cliente = dpi_Cliente;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.direccionCliente = direccionCliente;
        this.estado = estado;
    }

    public String getDpi_Cliente() {
        return dpi_Cliente;
    }

    public void setDpi_Cliente(String dpi_Cliente) {
        this.dpi_Cliente = dpi_Cliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
