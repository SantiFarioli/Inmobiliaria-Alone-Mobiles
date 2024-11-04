package com.santisoft.inmobiliariaalone.model;

import java.util.Date;

public class Pago {
    private int idPago;
    private int idContrato;
    private Contract contrato;
    private int numeroPago;
    private Date fechaPago;
    private double importe;

    public Pago(int idPago, int idContrato, Contract contrato, int numeroPago, Date fechaPago, double importe) {
        this.idPago = idPago;
        this.idContrato = idContrato;
        this.contrato = contrato;
        this.numeroPago = numeroPago;
        this.fechaPago = fechaPago;
        this.importe = importe;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public Contract getContrato() {
        return contrato;
    }

    public void setContrato(Contract contrato) {
        this.contrato = contrato;
    }

    public int getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(int numeroPago) {
        this.numeroPago = numeroPago;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}
