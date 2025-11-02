package com.santisoft.inmobiliariaalone.model;

import java.io.Serializable;
import java.util.List;

public class Inmueble implements Serializable {
    private int idInmueble;
    private String direccion;
    private String uso;
    private String tipo;
    private int ambientes;
    private double precio;
    private String estado;

    private int idPropietario;
    private Propietario propietario;
    private List<Contrato> contratos;

    private String foto;

    public Inmueble() { }

    public Inmueble(int idInmueble, String direccion, String uso, String tipo,
                    int ambientes, double precio, String estado,
                    int idPropietario, Propietario propietario,
                    List<Contrato> contratos, String foto) {
        this.idInmueble = idInmueble;
        this.direccion = direccion;
        this.uso = uso;
        this.tipo = tipo;
        this.ambientes = ambientes;
        this.precio = precio;
        this.estado = estado;
        this.idPropietario = idPropietario;
        this.propietario = propietario;
        this.contratos = contratos;
        this.foto = foto;
    }

    public Inmueble(String direccion, double precio, String imagen) {
        this.direccion = direccion;
        this.precio = precio;
        this.foto = imagen;
    }

    // Getters/Setters
    public int getIdInmueble() { return idInmueble; }
    public void setIdInmueble(int idInmueble) { this.idInmueble = idInmueble; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getAmbientes() { return ambientes; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getIdPropietario() { return idPropietario; }
    public void setIdPropietario(int idPropietario) { this.idPropietario = idPropietario; }

    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    public List<Contrato> getContratos() { return contratos; }
    public void setContratos(List<Contrato> contratos) { this.contratos = contratos; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getImagen() { return foto; }
    public void setImagen(String imagen) { this.foto = imagen; }
}
