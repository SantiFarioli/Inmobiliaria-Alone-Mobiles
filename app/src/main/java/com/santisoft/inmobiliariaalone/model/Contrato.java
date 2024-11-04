package com.santisoft.inmobiliariaalone.model;

import java.util.List;

public class Contrato {
    private int idContrato;
    private int idInmueble;
    private Inmueble inmueble;
    private int idInquilino;
    private Inquilino inquilino;
    private String fechaInicio;
    private String fechaFin;
    private double montoAlquiler;
    private List<Object> pagos;

    public Contrato(int idContrato, int idInmueble, Inmueble inmueble, int idInquilino, Inquilino inquilino, String fechaInicio, String fechaFin, double montoAlquiler, List<Object> pagos) {
        this.idContrato = idContrato;
        this.idInmueble = idInmueble;
        this.inmueble = inmueble;
        this.idInquilino = idInquilino;
        this.inquilino = inquilino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.montoAlquiler = montoAlquiler;
        this.pagos = pagos;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public int getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(int idInmueble) {
        this.idInmueble = idInmueble;
    }

    public Inmueble getInmueble() {
        return inmueble;
    }

    public void setInmueble(Inmueble inmueble) {
        this.inmueble = inmueble;
    }

    public int getIdInquilino() {
        return idInquilino;
    }

    public void setIdInquilino(int idInquilino) {
        this.idInquilino = idInquilino;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public double getMontoAlquiler() {
        return montoAlquiler;
    }

    public void setMontoAlquiler(double montoAlquiler) {
        this.montoAlquiler = montoAlquiler;
    }

    public List<Object> getPagos() {
        return pagos;
    }

    public void setPagos(List<Object> pagos) {
        this.pagos = pagos;
    }
}
