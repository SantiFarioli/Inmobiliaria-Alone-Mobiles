package com.santisoft.inmobiliariaalone.model;

public class EventoMensaje {
    public enum Tipo { SUCCESS, WARNING, ERROR }

    private final String texto;
    private final Tipo tipo;

    public EventoMensaje(String texto, Tipo tipo) {
        this.texto = texto;
        this.tipo = tipo;
    }

    public String getTexto() { return texto; }
    public Tipo getTipo() { return tipo; }
}
