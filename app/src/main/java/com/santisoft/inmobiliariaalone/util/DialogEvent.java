package com.santisoft.inmobiliariaalone.util;

/*
  Representa un evento de diálogo emitido por el ViewModel.
  Indica qué tipo de alerta debe mostrar la vista y qué mensaje acompañar.
 */
public class DialogEvent {
    public enum Type {
        SUCCESS,
        ERROR,
        WARNING,
        CONFIRM,
        LOADING,
        HIDE_LOADING
    }

    private final Type type;
    private final String title;
    private final String message;

    public DialogEvent(Type type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
