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
    private int id_asiento;

    public DetalleTicket() {
    }

    public DetalleTicket(int id_detalle, int id_ticket, int id_asiento) {
        this.id_detalle = id_detalle;
        this.id_ticket = id_ticket;
        this.id_asiento = id_asiento;
    }

    public int getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(int id_detalle) {
        this.id_detalle = id_detalle;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(int id_ticket) {
        this.id_ticket = id_ticket;
    }

    public int getId_asiento() {
        return id_asiento;
    }

    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    @Override
    public String toString() {
        return "DetalleTicket{" + "id_detalle=" + id_detalle + ", id_ticket=" + id_ticket + ", id_asiento=" + id_asiento + '}';
    }
 
}
