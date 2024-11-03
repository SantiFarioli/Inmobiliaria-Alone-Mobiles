package com.santisoft.inmobiliariaalone.model;

import java.util.List;

public class Propietario {
    private int idPropietario;
    private String dni;
    private String apellido;
    private String nombre;
    private String telefono;
    private String email;
    private String password;
    private String fotoPerfil;
    private List<Inmueble> inmuebles;
    private String resetToken;
    private String resetTokenExpiry;

    public Propietario(int idPropietario, String dni, String apellido, String nombre, String telefono, String email, String password, String fotoPerfil, List<Inmueble> inmuebles, String resetToken, String resetTokenExpiry) {
        this.idPropietario = idPropietario;
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.fotoPerfil = fotoPerfil;
        this.inmuebles = inmuebles;
        this.resetToken = resetToken;
        this.resetTokenExpiry = resetTokenExpiry;
    }

    // Getters y setters
    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public List<Inmueble> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(List<Inmueble> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetTokenExpiry() {
        return resetTokenExpiry;
    }

    public void setResetTokenExpiry(String resetTokenExpiry) {
        this.resetTokenExpiry = resetTokenExpiry;
    }
}
