package com.santisoft.inmobiliariaalone.model;

public class CambioPasswordRequest {
    private String passwordActual;
    private String nuevaPassword;

    public CambioPasswordRequest(String passwordActual, String nuevaPassword) {
        this.passwordActual = passwordActual;
        this.nuevaPassword = nuevaPassword;
    }
}
