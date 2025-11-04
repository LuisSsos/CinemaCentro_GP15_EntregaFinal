package Modelo;

import java.util.Date;

/** 
    @author Grupo 15
    Luis Ezequiel Sosa
    Lucas Saidman
    Luca Rodrigaño
    Ignacio Rodriguez
**/

public class DetalleTicket {
    
    private int id_detalle;
    private int id_ticket;
    private int id_funcion;
    private int id_asiento;
    private String funcion;
    private Date fecha_funcion;
    private String lugares;
    private int cantidad;

    public DetalleTicket() {
    }

    public DetalleTicket(int id_detalle, int id_ticket, int id_funcion, int id_asiento, String funcion, Date fecha_funcion, String lugares, int cantidad) {
        this.id_detalle = id_detalle;
        this.id_ticket = id_ticket;
        this.id_funcion = id_funcion;
        this.id_asiento = id_asiento;
        this.funcion = funcion;
        this.fecha_funcion = fecha_funcion;
        this.lugares = lugares;
        this.cantidad = cantidad;
    }

    public int getIddetalle() {
        return id_detalle;
    }

    public void setIddetalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getIdticket() {
        return id_ticket;
    }

    public void setIdticket(int id_ticket) {
        this.id_ticket = id_ticket;
    }

    public int getIdfuncion() {
        return id_funcion;
    }

    public void setIdfuncion(int id_funcion) {
        this.id_funcion = id_funcion;
    }

    public int getIdasiento() {
        return id_asiento;
    }

    public void setIdasiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public Date getFechafuncion() {
        return fecha_funcion;
    }

    public void setFechafuncion(Date fecha_funcion) {
        this.fecha_funcion = fecha_funcion;
    }

    public String getLugares() {
        return lugares;
    }

    public void setLugares(String lugares) {
        this.lugares = lugares;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "DetalleTicket{" + "id_detalle=" + id_detalle + ", id_ticket=" + id_ticket + ", id_funcion=" + id_funcion + ", id_asiento=" + id_asiento + ", funcion=" + funcion + ", fecha_funcion=" + fecha_funcion + ", lugares=" + lugares + ", cantidad=" + cantidad + '}';
    }
    
    
}
