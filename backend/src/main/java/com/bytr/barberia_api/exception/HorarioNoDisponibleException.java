package com.bytr.barberia_api.exception;

public class HorarioNoDisponibleException extends RuntimeException {
    public HorarioNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}