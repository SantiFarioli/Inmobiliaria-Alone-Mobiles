package com.santisoft.inmobiliariaalone.model;

public class RestablecerContrasenaRequest {
    private String token;
    private String nuevaContrasena;

    public RestablecerContrasenaRequest(String token, String nuevaContrasena) {
        this.token = token;
        this.nuevaContrasena = nuevaContrasena;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNuevaContrasena() {
        return nuevaContrasena;
    }

    public void setNuevaContrasena(String nuevaContrasena) {
        this.nuevaContrasena = nuevaContrasena;
    }
}
