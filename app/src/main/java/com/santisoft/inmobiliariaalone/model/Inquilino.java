package com.santisoft.inmobiliariaalone.model;

import java.util.List;

public class Inquilino {
    private int idInquilino;
    private String dni;
    private String nombreCompleto;
    private String lugarTrabajo;
    private String nombreGarante;
    private String dniGarante;
    private String telefono;
    private String email;
    private List<Object> contratos;

    public Inquilino(int idInquilino, String dni, String nombreCompleto, String lugarTrabajo, String nombreGarante, String dniGarante, String telefono, String email, List<Object> contratos) {
        this.idInquilino = idInquilino;
        this.dni = dni;
        this.nombreCompleto = nombreCompleto;
        this.lugarTrabajo = lugarTrabajo;
        this.nombreGarante = nombreGarante;
        this.dniGarante = dniGarante;
        this.telefono = telefono;
        this.email = email;
        this.contratos = contratos;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getLugarTrabajo() {
        return lugarTrabajo;
    }

    public void setLugarTrabajo(String lugarTrabajo) {
        this.lugarTrabajo = lugarTrabajo;
    }

    public String getNombreGarante() {
        return nombreGarante;
    }

    public void setNombreGarante(String nombreGarante) {
        this.nombreGarante = nombreGarante;
    }

    public String getDniGarante() {
        return dniGarante;
    }

    public void setDniGarante(String dniGarante) {
        this.dniGarante = dniGarante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Object> getContratos() {
        return contratos;
    }

    public void setContratos(List<Object> contratos) {
        this.contratos = contratos;
    }
}
